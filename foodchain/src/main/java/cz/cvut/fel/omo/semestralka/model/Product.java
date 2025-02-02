package cz.cvut.fel.omo.semestralka.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.state.EatableProductState;
import cz.cvut.fel.omo.semestralka.state.ExpiredProductState;
import cz.cvut.fel.omo.semestralka.state.ProductState;
import cz.cvut.fel.omo.semestralka.transaction.PartiesTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getPriceByName;
import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getTemperatureByName;

@Getter
@Setter
@JsonIgnoreProperties({
        "name",
        "producedOnDate",
        "expirationDate",
        "currentState",
        "price",
        "temperature",
        "productStatus",
        "foodChainTransactionHistory"
})
public class Product implements ProductInterface {

    private String name;
    private LocalDate producedOnDate;
    @JsonBackReference
    private List<Transaction> transactionHistory;
    private LocalDate expirationDate;
    private boolean currentState;
    private double price;
    private int temperature;
    private ProductStatus productStatus;

    public Product(String name, LocalDate producedOnDate) {
        this.name = name;
        this.producedOnDate = producedOnDate;
        this.transactionHistory = new ArrayList<>();
        this.price = getPriceByName(name);
        this.temperature = getTemperatureByName(name);
        productStatus = ProductStatus.NOT_ON_SALE;
        calcExpirationDate();
        updateState();
    }

    /**
     * Counts the expiration date
     */
    private void calcExpirationDate() {
        int durationDays = ProductsCatalogue.getDurationByName(name);

        if (durationDays > 0) {
            this.expirationDate = producedOnDate.plusDays(durationDays);
        } else {
            this.expirationDate = producedOnDate.plusDays(5*365);
        }
    }

    /**
     * Adds executed transaction to the history of transactions
     * @param transaction type of executed transaction
     */
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     *
     * @return latest transaction in the history of executed transactions
     */
    @JsonIgnore
    public Transaction getLastTransaction() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            return null;
        }
        return transactionHistory.getLast();
    }

    private void updateState() {
        if (LocalDate.now().isAfter(expirationDate)) {
            currentState = new ExpiredProductState().expired();
        } else {
            currentState = new EatableProductState().expired();
        }
    }

//    private boolean eatable() {
//        return currentState;
//    }
//
//    private boolean expired() {
//        return currentState;
//    }

    public boolean checkExpirationDateForSale() {
        updateState();
        return currentState;
    }

    @Override
//    @JsonIgnore
    public String getDescription() {
        return name;
    }

    @Override
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Prints out history of persons transactions
     */
    public void generatePartiesReport(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), createListPartiesTransaction());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<PartiesTransaction> createListPartiesTransaction() {

        if (transactionHistory == null || transactionHistory.isEmpty()) {
            throw new IllegalArgumentException("No transactions for product: " + name);
        }

        List<PartiesTransaction> transactions = new ArrayList<>();

        Person currentPerson = null;
        LocalDate startDate = null;

        for (Transaction transaction : transactionHistory) {
            if (currentPerson == null) {
                currentPerson = transaction.getPersonFrom();
                startDate = transaction.getTransactionDate();
            }
            if (!currentPerson.equals(transaction.getPersonFrom())) {
                long duration = ChronoUnit.DAYS.between(startDate, transaction.getTransactionDate());
                transactions.add(new PartiesTransaction(name, currentPerson.getName(), duration));

                currentPerson = transaction.getPersonFrom();
                startDate = transaction.getTransactionDate();
            }
        }

        if (currentPerson != null && startDate != null) {
            long duration = ChronoUnit.DAYS.between(startDate, LocalDate.now());
            transactions.add(new PartiesTransaction(name, currentPerson.getName(), duration));
        }

        return transactions;
    }

}
