package cz.cvut.fel.omo.semestralka.report;

import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

public class PlainTextFormatter implements ReportFormatter{
    @Override
    public String formatReport(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        StringBuilder report = new StringBuilder();
        if (transaction.getOperationType() == OperationType.PURCHASE ||
                transaction.getOperationType() == OperationType.TRANSPORT) {
            report.append("Person: ").append(transaction.getPersonTo().getName()).append("\n");
        } else {
            report.append("Person: ").append(transaction.getPersonFrom().getName()).append("\n");
        }
        report.append("Transaction Type: ").append(transaction.getOperationType()).append("\n")
                .append("Moved from: ").append(transaction.getMovedFrom()).append("\n")
                .append("Moved to: ").append(transaction.getMovedTo()).append("\n")
                .append("Transaction Date: ").append(transaction.getTransactionDate()).append("\n")
                .append("Price: ").append(transaction.getPrice()).append("\n")
                .append("-----------------------------------");
        return report.toString();
    }

    @Override
    public String formatReport(MoneyTransaction moneyTransaction) {
        if (moneyTransaction == null) {
            throw new IllegalArgumentException("Transaction history is empty");
        }
        StringBuilder report = new StringBuilder();
        report.append("Transaction Report for Product: ").append(moneyTransaction.getProduct().getName()).append("\n")
                .append("Product's price: ").append(moneyTransaction.getProductPrice()).append("\n")
                .append("Transaction Type: ").append(moneyTransaction.getOperationType()).append("\n")
                .append("Person from: ").append(moneyTransaction.getPersonFrom().getName()).append("\n")
                .append("Person to: ").append(moneyTransaction.getPersonTo().getName()).append("\n")
                .append("BEFORE TRANSACTION: ")
                .append("Person from - Money: ").append(moneyTransaction.getWallet_PersonFrom_BeforeTransaction()).append("\n")
                .append("Person to - Money: ").append(moneyTransaction.getWallet_PersonTo_BeforeTransaction()).append("\n")
                .append("AFTER TRANSACTION: ")
                .append("Person from - Money: ").append(moneyTransaction.getWallet_PersonFrom_AfterTransaction()).append("\n")
                .append("Person to - Money: ").append(moneyTransaction.getWallet_PersonTo_AfterTransaction()).append("\n")
                .append("-----------------------------------");
        return report.toString();
    }

}
