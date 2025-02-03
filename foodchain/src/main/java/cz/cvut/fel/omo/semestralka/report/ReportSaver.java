package cz.cvut.fel.omo.semestralka.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.cvut.fel.omo.semestralka.transaction.ModificationSecurityTransaction;
import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.SecurityTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReportSaver {

    /**
     * Saves a list of Transaction objects to a JSON file.
     * The file is written in a pretty-printed format with Java date/time serialization handled appropriately.
     * @param transactions A list of Transaction objects to be saved to the JSON file.
     * @param fileName The name of the file to save the transactions in.
     */
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

    /**
     * Saves a list of MoneyTransaction objects to a JSON file.
     * The file is written in a pretty-printed format with Java date/time serialization handled appropriately.
     * @param moneyTransactions A list of MoneyTransaction objects to be saved to the JSON file.
     * @param fileName The name of the file to save the transactions in.
     */
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

    /**
     * Saves a list of SecurityTransaction objects to a JSON file.
     * The file is written in a pretty-printed format with Java date/time serialization handled appropriately.
     * @param securityTransactions A list of SecurityTransaction objects to be saved to the JSON file.
     * @param fileName The name of the file to save the transactions in.
     */
    public static void saveSecurityReportToJson(List<SecurityTransaction> securityTransactions, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), securityTransactions);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Saves a list of ModificationSecurityTransaction objects to a JSON file.
     * The file is written in a pretty-printed format with Java date/time serialization handled appropriately.
     * @param securityTransactions A list of ModificationSecurityTransaction objects to be saved to the JSON file.
     * @param fileName The name of the file to save the transactions in.
     */
    public static void saveModificationSecurityToJson(List<ModificationSecurityTransaction> securityTransactions, String fileName) {
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
