package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.state.ProductState;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ProductInterface {

    String getName();
    LocalDate getProducedOnDate();
    List<Transaction> getTransactionHistory();
    LocalDate getExpirationDate();
    ProductState getCurrentState();
    double getPrice();
    int getTemperature();
    void addTransaction(Transaction transaction);
    Transaction getLastTransaction();
    boolean checkExpirationDateForSale();
    String getDescription();
    void generateFoodChainReport();
    void generatePartiesReport();

}
