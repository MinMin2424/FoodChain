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
     * Calculates the expiration date for the product based on its catalog duration.
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
     * Adds a transaction to the history of executed transactions.
     * @param transaction The transaction to be added.
     */
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     * Retrieves the latest transaction in the product's transaction history.
     * @return The most recent Transaction, or null if no transactions exist.
     */
    @JsonIgnore
    public Transaction getLastTransaction() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            return null;
        }
        return transactionHistory.getLast();
    }

    /**
     * Updates the state of the product based on the expiration date.
     * If the product is expired, the state is set to expired; otherwise, it's eatable.
     */
    private void updateState() {
        if (LocalDate.now().isAfter(expirationDate)) {
            currentState = new ExpiredProductState().expired();
        } else {
            currentState = new EatableProductState().expired();
        }
    }

    /**
     * Checks if the product is still valid for sale based on its state.
     * @return True if the product is eatable (not expired), false otherwise.
     */
    public boolean checkExpirationDateForSale() {
        updateState();
        return currentState;
    }

    /**
     * Returns the product's description, which is its name.
     * @return The name of the product.
     */
    @Override
    public String getDescription() {
        return name;
    }

    /**
     * Retrieves the transaction history for the product.
     * @return A list of Transaction objects representing the transaction history.
     */
    @Override
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Retrieves the price of the product.
     * @return The price of the product.
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Generates a report of transaction parties related to this product and writes it to a file.
     * @param filePath The path to the file where the report will be saved.
     */
    public void generatePartiesReport(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), createListPartiesTransaction());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Creates a list of transactions for the report, including durations each person held the product.
     * @return A list of PartiesTransaction objects representing transaction data.
     */
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
