package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ProductInterface {

    String getName();
    LocalDate getProducedOnDate();
    List<Transaction> getTransactionHistory();
    LocalDate getExpirationDate();
    //    boolean getCurrentState();
    double getPrice();
    int getTemperature();
    ProductStatus getProductStatus();
    void setProductStatus(ProductStatus productStatus);
    void addTransaction(Transaction transaction);
    Transaction getLastTransaction();
    boolean checkExpirationDateForSale();
    String getDescription();
    void generatePartiesReport(String filePath);

}
