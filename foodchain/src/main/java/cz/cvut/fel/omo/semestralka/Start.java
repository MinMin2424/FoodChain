package cz.cvut.fel.omo.semestralka;

import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;

import java.time.LocalDate;

public class Start {

    public static LocalDate createExpirationDays(LocalDate producedOnDays, int durationDays) {
        return producedOnDays.plusDays(durationDays);
    }

    public static void main(String[] args) {
//        System.out.println("Hello world!");
//
//        System.out.println(ProductsCatalogue.WHEAT.name().toLowerCase());

        LocalDate producedOnDays = LocalDate.of(2024, 10, 30);
        int durationDays = 30;

        LocalDate expirationDate = createExpirationDays(producedOnDays, durationDays);
        System.out.println("Expiration Date: " + expirationDate);


    }
}
