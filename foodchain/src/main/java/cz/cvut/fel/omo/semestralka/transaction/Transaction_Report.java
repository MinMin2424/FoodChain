package cz.cvut.fel.omo.semestralka.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Transaction_Report {

    public static List<MoneyTransaction> transactionHistory = new ArrayList<>();

    public static void generateTransactionReport() {
        if (transactionHistory == null || transactionHistory.isEmpty()) {
            throw new IllegalArgumentException("Transaction history is empty");
        }

        for (MoneyTransaction transaction : transactionHistory) {
            System.out.println("Transaction Report for Product: " + transaction.getProduct().getName());
            System.out.println("Product's price: " + transaction.getProductPrice());
            System.out.println("Transaction Type: " + transaction.getOperationType());
            System.out.println("Person from: " + transaction.getPersonFrom().getName());
            System.out.println("Person to: " + transaction.getPersonTo().getName());
            System.out.println("Before Transaction:");
            System.out.println("Person from - Money: " + transaction.getWallet_PersonFrom_BeforeTransaction());
            System.out.println("Person to - Money: " + transaction.getWallet_PersonTo_BeforeTransaction());
            System.out.println("After Transaction:");
            System.out.println("Person from - Money: " + transaction.getWallet_PersonFrom_AfterTransaction());
            System.out.println("Person to - Money: " + transaction.getWallet_PersonTo_AfterTransaction());
            System.out.println("-----------------------------------");
        }
    }
}
