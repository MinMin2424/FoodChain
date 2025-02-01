package cz.cvut.fel.omo.semestralka.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReportSaver {

    public static void saveReportToJson(List<Transaction> transactions, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), transactions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
