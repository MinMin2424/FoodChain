package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.state.ProductState;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class ProductDecorator implements ProductInterface {
    protected ProductInterface decoratedProduct;

    @Override
    public String getName() {
        return decoratedProduct.getName();
    }

    @Override
    public LocalDate getProducedOnDate() {
        return decoratedProduct.getProducedOnDate();
    }

    @Override
    public List<Transaction> getTransactionHistory() {
        return decoratedProduct.getTransactionHistory();
    }

    @Override
    public LocalDate getExpirationDate() {
        return decoratedProduct.getExpirationDate();
    }

    @Override
    public ProductState getCurrentState() {
        return decoratedProduct.getCurrentState();
    }

    @Override
    public double getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    public int getTemperature() {
        return decoratedProduct.getTemperature();
    }

    @Override
    public ProductStatus getProductStatus() {
        return decoratedProduct.getProductStatus();
    }

    @Override
    public void setProductStatus(ProductStatus productStatus) {
        decoratedProduct.setProductStatus(productStatus);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        decoratedProduct.addTransaction(transaction);
    }

    @Override
    public Transaction getLastTransaction() {
        return decoratedProduct.getLastTransaction();
    }

    @Override
    public boolean checkExpirationDateForSale() {
        return decoratedProduct.checkExpirationDateForSale();
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription();
    }

    @Override
    public void generatePartiesReport(String filePath) {
        decoratedProduct.generatePartiesReport(filePath);
    }
}
