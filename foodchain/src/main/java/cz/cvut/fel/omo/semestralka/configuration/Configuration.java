package cz.cvut.fel.omo.semestralka.configuration;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.report.*;
import cz.cvut.fel.omo.semestralka.transaction.StorageMoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.StorageSecurityTransaction;

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
    }

    protected abstract void initialize();

    protected void generateProductReport() {
        for (ProductInterface product : products) {
            System.out.println("-----" + product.getName() + "-----");
            jsonTextReport.generateTransaction(product.getTransactionHistory());
            System.out.println(" ");
        }
    }

    protected void saveProductReport() {
        for (ProductInterface product : products) {
            ReportSaver.saveReportToJson(product.getTransactionHistory(), product.getDescription().toLowerCase() + "Report.json");
            product.generatePartiesReport(product.getDescription().toLowerCase() + "PartiesReport.json");
        }
    }

    protected void generateMoneyReport() {
        System.out.println("-----" + "Money report" + "-----");
        jsonTextReport.generateMoneyTransaction(StorageMoneyTransaction.transactionHistory);
        System.out.println(" ");
    }

    protected void saveMoneyReport() {
        ReportSaver.saveMoneyReportToJson(StorageMoneyTransaction.transactionHistory, "MoneyReport.json");
    }

    protected void saveSecurityReport() {
        ReportSaver.saveSecurityReportToJson(StorageSecurityTransaction.securityTransactions, "SecurityReport.json");
    }
}
