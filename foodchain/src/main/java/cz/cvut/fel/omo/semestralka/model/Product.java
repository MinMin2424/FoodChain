package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Product {

    private String name;
    //    private int quantity;
    private LocalDate producedOnDate;
    private List<Transaction> transactionHistory;

//    public Product(String name, int quantity, LocalDate producedOnDate) {
//        this.name = name;
//        this.quantity = quantity;
//        this.producedOnDate = producedOnDate;
//        this.transactionHistory = new ArrayList<>();
//    }

    public Product(String name, LocalDate producedOnDate) {
        this.name = name;
        this.producedOnDate = producedOnDate;
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Counts the expiration date
     * @return Products date of expiration
     */
    public LocalDate getExpirationDate() {
        int durationDays = ProductsCatalogue.getDurationByName(name);

        if (durationDays > 0) {
            return producedOnDate.plusDays(durationDays);
        } else {
            return null;
        }
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public Transaction getLastTransaction() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            return null;
        }
        return transactionHistory.getLast();
    }

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
