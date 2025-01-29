package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Product {

    private String name;
    private int quantity;
    private LocalDate producedOnDate;
    private List<Transaction> transactionHistory;

    public Product(String name, int quantity, LocalDate producedOnDate) {
        this.name = name;
        this.quantity = quantity;
        this.producedOnDate = producedOnDate;
    }

    /**
     * Counts the expiration date
     * @return Products date of expiration
     */
    public LocalDate getExpirationDate() {
        int durationDays = ProductsCatalogue.getDurationByName(name);

        if (durationDays > 0) {
            return producedOnDate.plusDays(durationDays);
        } else {
            return null;
        }
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

}

