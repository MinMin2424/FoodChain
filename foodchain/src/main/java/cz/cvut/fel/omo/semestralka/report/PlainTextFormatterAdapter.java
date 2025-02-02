package cz.cvut.fel.omo.semestralka.report;

import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PlainTextFormatterAdapter extends PlainTextFormatter {
    JsonFormatter jsonFormatter;

    @Override
    public String formatReport(Transaction transaction) {
        return jsonFormatter.jsonFormatReport(transaction);
    }

    @Override
    public String formatReport(MoneyTransaction moneyTransaction) {
        return jsonFormatter.jsonFormatReport(moneyTransaction);
    }
}
