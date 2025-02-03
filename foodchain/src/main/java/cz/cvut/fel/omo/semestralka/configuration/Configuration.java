package cz.cvut.fel.omo.semestralka.configuration;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.report.PlainTextFormatterAdapter;
import cz.cvut.fel.omo.semestralka.report.JsonFormatter;
import cz.cvut.fel.omo.semestralka.report.ReportGenerator;
import cz.cvut.fel.omo.semestralka.report.ReportSaver;
import cz.cvut.fel.omo.semestralka.transaction.StorageMoneyTransaction;

import java.util.ArrayList;
import java.util.List;

public abstract class Configuration {

    protected final List<ProductInterface> products;
    PlainTextFormatterAdapter textFormatter = new PlainTextFormatterAdapter(new JsonFormatter());
    ReportGenerator jsonTextReport = new ReportGenerator(textFormatter);

    public Configuration() {
        this.products = new ArrayList<>();
    }

    public void run() {
        initialize();
        generateProductReport();
        generateMoneyReport();
        saveProductReport();
        saveMoneyReport();
        saveSecurityReport();
        saveModificationSecurityReport();
    }

    /**
     * Initializes the configuration.
     */
    protected abstract void initialize();

    /**
     * Generates a report for each product in the list by processing their transaction
     * history and printing the results to the console.
     */
    protected void generateProductReport() {
        for (ProductInterface product : products) {
            System.out.println("-----" + product.getName() + "-----");
            jsonTextReport.generateTransaction(product.getTransactionHistory());
            System.out.println(" ");
        }
    }

    /**
     * Saves transaction reports and parties reports for each product in JSON format.
     * Each report file is named based on the product's description.
     */
    protected abstract void saveProductReport();

    /**
     * Generates a money report.
     */
    protected void generateMoneyReport() {
        System.out.println("-----" + "Money report" + "-----");
        jsonTextReport.generateMoneyTransaction(StorageMoneyTransaction.transactionHistory);
        System.out.println(" ");
    }

    /**
     * Saves the money transaction report to a JSON file.
     */
    protected void saveMoneyReport() {
        ReportSaver.saveMoneyReportToJson(StorageMoneyTransaction.transactionHistory, "MoneyReport.json");
    }

    /**
     * Saves a security report.
     */
    protected abstract void saveSecurityReport();

    /**
     * Saves a modification security report.
     */
    protected abstract void saveModificationSecurityReport();
}
