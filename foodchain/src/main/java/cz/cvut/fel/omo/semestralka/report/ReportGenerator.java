package cz.cvut.fel.omo.semestralka.report;

import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ReportGenerator {
    private PlainTextFormatterAdapter formatter;

    /**
     * Generates and prints reports for a list of Transaction objects.
     * @param transactions A list of Transaction objects to be processed.
     * Each transaction is formatted and printed to the standard output.
     */
    public void generateTransaction(List<Transaction> transactions) {
        try {
            for (Transaction transaction : transactions) {
                System.out.println(formatter.formatReport(transaction));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Generates and prints reports for a list of MoneyTransaction objects.
     * @param transactions A list of MoneyTransaction objects to be processed.
     * Each transaction is formatted and printed to the standard output.
     */
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
