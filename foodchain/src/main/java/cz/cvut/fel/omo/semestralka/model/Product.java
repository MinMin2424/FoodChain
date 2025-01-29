package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Product {

    private String name;
    private int quantity;
    private LocalDate producedOnDate;
    private List<Transaction> transactionHistory;

    public Product(String name, int quantity, LocalDate producedOnDate) {
        this.name = name;
        this.quantity = quantity;
        this.producedOnDate = producedOnDate;
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
            System.out.println("No transactions for product: " + name);
            return;
        }

        for (Transaction transaction : transactionHistory) {
            System.out.println("Product: " + name);
            System.out.println("Transaction Type: " + transaction.getOperationType());
            System.out.println("Moved from: " + transaction.getMovedFrom());
            System.out.println("Moved to: " + transaction.getMovedTo());
            System.out.println("Transaction Date: " + transaction.getTransactionDate());
            System.out.println("-----------------------------------");

        }
    }


}

