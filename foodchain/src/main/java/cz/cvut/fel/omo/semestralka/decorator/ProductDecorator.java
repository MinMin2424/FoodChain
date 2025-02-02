package cz.cvut.fel.omo.semestralka.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnore
    public String getName() {
        return decoratedProduct.getName();
    }

    @Override
    @JsonIgnore
    public LocalDate getProducedOnDate() {
        return decoratedProduct.getProducedOnDate();
    }

    @Override
    @JsonIgnore
    public List<Transaction> getTransactionHistory() {
        return decoratedProduct.getTransactionHistory();
    }

    @Override
    @JsonIgnore
    public LocalDate getExpirationDate() {
        return decoratedProduct.getExpirationDate();
    }

//    @Override
//    public boolean getCurrentState() {
//        return decoratedProduct.getCurrentState();
//    }

    @Override
    public double getPrice() {
        return decoratedProduct.getPrice();
    }

    @Override
    @JsonIgnore
    public int getTemperature() {
        return decoratedProduct.getTemperature();
    }

    @Override
    @JsonIgnore
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
    @JsonIgnore
    public Transaction getLastTransaction() {
        return decoratedProduct.getLastTransaction();
    }

    @Override
    public boolean checkExpirationDateForSale() {
        return decoratedProduct.checkExpirationDateForSale();
    }

    @Override
//    @JsonIgnore
    public String getDescription() {
        return decoratedProduct.getDescription();
    }

    @Override
    public void generatePartiesReport(String filePath) {
        decoratedProduct.generatePartiesReport(filePath);
    }
}
