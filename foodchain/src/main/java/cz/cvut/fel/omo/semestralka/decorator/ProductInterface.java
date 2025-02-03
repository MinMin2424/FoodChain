package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ProductInterface {

    /**
     * @return the name of the product.
     */
    String getName();

    /**
     * @return the production date of the product.
     */
    LocalDate getProducedOnDate();

    /**
     * @return a list of transactions for the product.
     */
    List<Transaction> getTransactionHistory();

    /**
     * @return the expiration date of the product.
     */
    LocalDate getExpirationDate();

    /**
     * @return the price of the product.
     */
    double getPrice();

    /**
     * @return the temperature of the product.
     */
    int getTemperature();

    /**
     * @return the current status of the product.
     */
    ProductStatus getProductStatus();

    /**
     * @param productStatus the new status of the product.
     */
    void setProductStatus(ProductStatus productStatus);

    /**
     * @param transaction the transaction to be added.
     */
    void addTransaction(Transaction transaction);

    /**
     * @return the last transaction for the product.
     */
    Transaction getLastTransaction();

    /**
     * @return true if the product can be sold, false otherwise.
     */
    boolean checkExpirationDateForSale();

    /**
     * @return the description of the product.
     */
    String getDescription();

    /**
     * @param filePath the path to save the report.
     */
    void generatePartiesReport(String filePath);

}
