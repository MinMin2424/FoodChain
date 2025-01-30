package cz.cvut.fel.omo.semestralka.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Transaction_Report {

    private static List<Transaction> transactionHistory;

    public Transaction_Report() {
        transactionHistory = new ArrayList<Transaction>();
    }

    public void generateTransactionReport() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            throw new IllegalArgumentException("Transaction history is empty");
        }

        for (Transaction transaction : transactionHistory) {
            System.out.println("Transaction Report for Product: " + transaction.getProduct().getName());
            System.out.println("Transaction Type: " + transaction.getOperationType());
            System.out.println("Person from: " + transaction.getPersonFrom());
            System.out.println("Person to: " + transaction.getPersonTo());
            System.out.println("Before Transaction:");
            System.out.println("Person from - Money: " + (transaction.getPersonFrom().getWallet() - transaction.getPrice()));
            System.out.println("Person to - Money: " + (transaction.getPersonTo().getWallet() + transaction.getPrice()));
            System.out.println("After Transaction:");
            System.out.println("Person from - Money: " + transaction.getPersonFrom().getWallet());
            System.out.println("Person to - Money: " + transaction.getPersonTo().getWallet());
            System.out.println("----------------------------------------------------------------");
        }
    }
}
