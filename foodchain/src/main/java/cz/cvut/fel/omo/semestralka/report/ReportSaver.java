package cz.cvut.fel.omo.semestralka.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.SecurityTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReportSaver {

    public static void saveReportToJson(List<Transaction> transactions, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), transactions);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void saveMoneyReportToJson(List<MoneyTransaction> moneyTransactions, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), moneyTransactions);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void saveSaveSecurityToJson(List<SecurityTransaction> securityTransactions, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), securityTransactions);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
