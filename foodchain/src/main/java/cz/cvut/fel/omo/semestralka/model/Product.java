package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.state.EatableProductState;
import cz.cvut.fel.omo.semestralka.state.ExpiredProductState;
import cz.cvut.fel.omo.semestralka.state.ProductState;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getPriceByName;
import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getTemperatureByName;

@Getter
@Setter
public class Product implements ProductInterface {

    private String name;
    private LocalDate producedOnDate;
    private List<Transaction> transactionHistory;
    private LocalDate expirationDate;
    private ProductState currentState;
    private double price;
    private int temperature;

    public Product(String name, LocalDate producedOnDate) {
        this.name = name;
        this.producedOnDate = producedOnDate;
        this.transactionHistory = new ArrayList<>();
        this.price = getPriceByName(name);
        this.temperature = getTemperatureByName(name);
        calcExpirationDate();
        updateState();
    }

    /**
     * Counts the expiration date
     */
    public void calcExpirationDate() {
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
    public Transaction getLastTransaction() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            return null;
        }
        return transactionHistory.getLast();
    }

    public void updateState() {
        if (LocalDate.now().isAfter(expirationDate)) {
            currentState = new ExpiredProductState();
        } else {
            currentState = new EatableProductState();
        }
    }

    public boolean eatable() {
        return currentState.eatable();
    }

    public boolean expired() {
        return currentState.expired();
    }

    public boolean checkExpirationDateForSale() {
        updateState();
        return expired();
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Prints out history of products transactions
     */
    public void generateFoodChainReport() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            throw new IllegalArgumentException("No transactions for product: " + name);
        }

        for (Transaction transaction : transactionHistory) {
            if (transaction.getOperationType() == OperationType.PURCHASE || transaction.getOperationType() == OperationType.TRANSPORT) {
                System.out.println("Product: " + name);
                System.out.println("Person: " + transaction.getPersonTo().getName());
            } else {
                System.out.println("Product: " + name);
                System.out.println("Person: " + transaction.getPersonFrom().getName());
            }
            System.out.println("Transaction Type: " + transaction.getOperationType());
            System.out.println("Moved from: " + transaction.getMovedFrom());
            System.out.println("Moved to: " + transaction.getMovedTo());
            System.out.println("Transaction Date: " + transaction.getTransactionDate());
            System.out.println("Price: " + transaction.getPrice());
            System.out.println("-----------------------------------");

        }
    }

    /**
     * Prints out history of persons transactions
     */
    public void generatePartiesReport() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            throw new IllegalArgumentException("No transactions for product: " + name);
        }

        Person currentPerson = null;
        LocalDate startDate = null;

        for (Transaction transaction : transactionHistory) {
            if (currentPerson == null) {
                currentPerson = transaction.getPersonFrom();
                //TODO
                startDate = transaction.getTransactionDate();
            }
            if (!currentPerson.equals(transaction.getPersonFrom())) {
                long duration = ChronoUnit.DAYS.between(startDate, transaction.getTransactionDate());
                System.out.println("Product: " + name);
                System.out.println("Person: " + currentPerson.getName());
                System.out.println("Transaction Type: " + duration + " days");
                System.out.println("Margin applied: ");
                System.out.println("-----------------------------------");

                currentPerson = transaction.getPersonFrom();
                startDate = transaction.getTransactionDate();
            }
        }

        if (currentPerson != null && startDate != null) {
            long duration = ChronoUnit.DAYS.between(startDate, LocalDate.now());
            System.out.println("Product: " + name);
            System.out.println("Person: " + currentPerson.getName());
            System.out.println("Transaction Type: " + duration + " days");
            System.out.println("Margin applied: ");
            System.out.println("-----------------------------------");
        }
    }

}
