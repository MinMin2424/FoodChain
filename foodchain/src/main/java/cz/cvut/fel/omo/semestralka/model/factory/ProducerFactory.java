package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;

import java.time.LocalDate;

public class ProducerFactory implements Factory{

    private final Producer producer;

    public ProducerFactory(Producer producer){
        this.producer = producer;
    }

    // function creates new product out of already existing product in farmers warehouse
    @Override
    public Product createProduct(String productName) {
        if (producer.getWarehouse().findProduct(getProductOrigin(productName))) {
            Product newProduct = new Product(productName, 1, LocalDate.now());
            producer.getWarehouse().addProduct(newProduct);
            return newProduct;
        }
        return null;
    }

    @Override
    public String getProductOrigin(String productName) {
        switch (productName){
            case "FLOUR":
                return ProductsCatalogue.WHEAT.name();
            case "YOGHURT", "HEAVY?CREAM", "CHEESE":
                return ProductsCatalogue.MILK.name();
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
    }
}

