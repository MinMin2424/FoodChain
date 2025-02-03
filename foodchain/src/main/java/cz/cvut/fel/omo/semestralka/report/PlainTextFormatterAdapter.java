package cz.cvut.fel.omo.semestralka.report;

import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PlainTextFormatterAdapter extends PlainTextFormatter {
    JsonFormatter jsonFormatter;

    /**
     * Formats a Transaction report using the JsonFormatter.
     * @param transaction The transaction object to be formatted.
     * @return A JSON-formatted report as a String.
     */
    @Override
    public String formatReport(Transaction transaction) {
        return jsonFormatter.jsonFormatReport(transaction);
    }

    /**
     * Formats a MoneyTransaction report using the JsonFormatter.
     * @param moneyTransaction The money transaction object to be formatted.
     * @return A JSON-formatted report as a String.
     */
    @Override
    public String formatReport(MoneyTransaction moneyTransaction) {
        return jsonFormatter.jsonFormatReport(moneyTransaction);
    }
}
