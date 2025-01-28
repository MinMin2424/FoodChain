package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class Product {

    private String name;
    private int quantity;
    private LocalDate producedOnDate;

    public Product(String name, int quantity, LocalDate producedOnDate) {
        this.name = name;
        this.quantity = quantity;
        this.producedOnDate = producedOnDate;
    }

    public LocalDate getExpirationDate() {
        int durationDays = ProductsCatalogue.getDurationByName(name);

        if (durationDays > 0) {
            return producedOnDate.plusDays(durationDays);
        } else {
            return null;
        }
    }


}

