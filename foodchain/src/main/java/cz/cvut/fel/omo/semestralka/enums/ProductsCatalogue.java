package cz.cvut.fel.omo.semestralka.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ProductsCatalogue {

    // FARMER PRODUCTS
    COW (90000, 0, 25),
    SHEEP (3000, 0, 25),
    CHICKEN (250, 0, 25),
    FISH (500, 0, 15),
    WHEAT (20, 900, 10), //obili
    APPLE (10, 7, 12),
    WALNUT (120, 100, 12),
    POTATO (10, 14, 12),
    CARROT (10, 14, 12),
    STRAWBERRY (90, 12, 12),
    TOMATO(30, 8, 5),

    MILK (15, 7, 5),
    EGG (25, 90,15),
    WOOL (10, 0, 0), // vlna
    FEATHER (90, 0, 0),

    BEEF (250, 7, 5),
    CHICKEN_MEAT (150, 7, 5),
    FISH_FILET (350, 7, 5),
    LAMB (350, 7, 5),

    // PRODUCER PRODUCTS
    FLOUR (12, 1000, 0),
    YOGHURT (20, 16, 5),
    HEAVY_CREAM (100, 16, 5),
    CHEESE (100, 31, 5),
    PIE (130, 5, 5),
    CHEESECAKE (230, 9, 5),
    PASTA (30, 500, 0),
    PASTA_FRESH (40, 12, 5),
    FRIES (50, 1000, -15),
    BURGER (220, 1, 5),
    CHICKEN_STRIPS (80, 1, 5),
    BREAD (25, 4, 0),
    BUN (10, 4, 0);


    private final double price;
    private final int durationDays;
    private final int keepInTemperature;

    /**
     * Finds product in Product catalogue by its name and returns its duration
     * @param name of the product
     * @return Duration of product
     */
    public static int getDurationByName(String name) {
        for (ProductsCatalogue productsCatalogue : ProductsCatalogue.values()) {
            if (productsCatalogue.name().equalsIgnoreCase(name)) {
                return productsCatalogue.durationDays;
            }
        }
        return -1;
    }

    /**
     * Finds product in ProductCatalogue by its name and
     * @param name name of the product
     * @return Highest temperature the product can be kept in
     */
    public static int getTemperatureByName(String name) {
        for (ProductsCatalogue productsCatalogue : ProductsCatalogue.values()) {
            if (productsCatalogue.name().equalsIgnoreCase(name)) {
                return productsCatalogue.keepInTemperature;
            }
        }
        return -1;
    }

    /**
     * Finds product in Product catalogue by its name and returns its price
     * @param name of the product
     * @return Price of product
     */
    public static double getPriceByName(String name) {
        for (ProductsCatalogue productsCatalogue : ProductsCatalogue.values()) {
            if (productsCatalogue.name().equalsIgnoreCase(name)) {
                return productsCatalogue.price;
            }
        }
        return -1;
    }

}
