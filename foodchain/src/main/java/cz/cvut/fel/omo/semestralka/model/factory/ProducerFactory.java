package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProducerFactory implements Factory{

    private final Producer producer;

    public ProducerFactory(Producer producer){
        this.producer = producer;
    }

    // function creates new product out of already existing product in farmers warehouse
    public Product createProduct(String productName) {
        List<String> origins = getProductOrigin(productName);
        for (String origin : origins) {
            boolean found = producer.getWarehouse().findProduct(origin);
            if (!found) {
                return null;
            }
        }
        for (String origin : origins) {
            Product productOrigin = producer.getWarehouse().getProductByName(origin);
            producer.getWarehouse().removeProduct(productOrigin, 1);
        }
        return new Product(productName, 1, LocalDate.now());
    }

    public List<String> getProductOrigin(String productName) {
        List<String> origins = new ArrayList<>();
        switch (productName){
            case "FLOUR":
                origins.add(ProductsCatalogue.WHEAT.name());
            case "YOGHURT", "HEAVY_CREAM", "CHEESE":
                ProductsCatalogue.FLOUR.name() + " " + ProductsCatalogue.EGG.name() + " " + ProductsCatalogue.APPLE.name();
            case "PIE":
                return ProductsCatalogue.FLOUR.name() + " " + ProductsCatalogue.EGG.name() + " " + ProductsCatalogue.APPLE.name();
            case "CHEESE_CAKE":
                return ProductsCatalogue.FLOUR.name() + " " + ProductsCatalogue.EGG.name() + " " + ProductsCatalogue.HEAVY_CREAM.name();
            case "PASTA":
                return ProductsCatalogue.FLOUR.name();
            case "PASTA_FRESH":
                return ProductsCatalogue.FLOUR.name() + " " + ProductsCatalogue.EGG.name();
            case "FRIES":
                return ProductsCatalogue.POTATO.name();
            case "BURGER":
                return ProductsCatalogue.BUN.name() + " " + ProductsCatalogue.BEEF.name() + " " + ProductsCatalogue.TOMATO.name();
            case "CHICKEN_STRIPS":
                return ProductsCatalogue.FLOUR.name() + " " + ProductsCatalogue.EGG.name() + " " + ProductsCatalogue.CHICKEN_MEAT.name();
            case "BREAD", "BUN":
                return ProductsCatalogue.FLOUR.name() + " " + ProductsCatalogue.EGG.name() + " " + ProductsCatalogue.MILK.name();
            default: return null;
        }
        return origins;
    }
}

