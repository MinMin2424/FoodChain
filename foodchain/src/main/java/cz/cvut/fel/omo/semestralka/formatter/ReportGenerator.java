package cz.cvut.fel.omo.semestralka.formatter;

import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ReportGenerator {
    private ReportFormatter formatter;

    public void generateTransaction(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(formatter.formatReport(transaction));
        }
    }

    public void generateMoneyTransaction(List<MoneyTransaction> transactions) {
        for (MoneyTransaction transaction : transactions) {
            System.out.println(formatter.formatReport(transaction));
        }
    }
}
