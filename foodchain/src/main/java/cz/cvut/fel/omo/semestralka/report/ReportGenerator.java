package cz.cvut.fel.omo.semestralka.report;

import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ReportGenerator {
    private PlainTextFormatterAdapter formatter;

    public void generateTransaction(List<Transaction> transactions) {
        try {
            for (Transaction transaction : transactions) {
                System.out.println(formatter.formatReport(transaction));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void generateMoneyTransaction(List<MoneyTransaction> transactions) {
        try {
            for (MoneyTransaction transaction : transactions) {
                System.out.println(formatter.formatReport(transaction));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
