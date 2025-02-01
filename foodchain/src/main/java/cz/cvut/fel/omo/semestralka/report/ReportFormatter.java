package cz.cvut.fel.omo.semestralka.report;

import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

public interface ReportFormatter {
    String formatReport(Transaction transaction);
    String formatReport(MoneyTransaction moneyTransaction);
}
