package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import cz.cvut.fel.omo.semestralka.model.transaction.Transaction;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.model.enums.Place.*;
import static cz.cvut.fel.omo.semestralka.model.enums.OperationType.*;
import static cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue.getPriceByName;

public class ProducerFactory implements Factory {
    private final Producer producer;
    public ProducerFactory(Producer producer){this.producer = producer;}

    /**
     * Executes specific function
     * @param productName name of the product
     * @param sellQuantity quantity of the product
     * @param operationType name of the function to be executed
     */
    @Override
    public void executeOperation(String productName, int sellQuantity, OperationType operationType) {
        switch (operationType) {
            case CREATE:
                createProduct(productName);
                break;
            case STORE:
                storeProduct(productName);
                break;
            case SELL:
                sellProduct(productName, sellQuantity);
                break;
            case RETURN:
                //to do
                break;
        }
    }

    /**
     * Creates and adds new product out of already existing products in warehouse
     * @param productName name of the product
     */
    private void createProduct(String productName) {
        List<String> origins = getProductOrigin(productName);
        if (origins == null) {
            return;
        }
        for (String origin : origins) {
            boolean found = producer.getStorage().findProduct(origin);
            if (!found) {
                return;
            }
        }
        for (String origin : origins) {
            Product productOrigin = producer.getStorage().getProductByName(origin);
            producer.getStorage().removeProductFromStorage(productOrigin, producer, WAREHOUSE, MANUFACTORY, 1);
        }
        Product newProduct = new Product(productName, 1, LocalDate.now());
        Transaction transaction = new Transaction(newProduct, producer, MUSHROOM_LAND, MUSHROOM_LAND, CREATE, LocalDate.now(), CREATE.getPrice(), null);
        producer.getStorage().addProductToStorage(newProduct, producer, MANUFACTORY, WAREHOUSE);
    }

    /**
     * Adds product to producers storage
     * @param productName name of the product
     */
    @Override
    public void storeProduct(String productName) {
        Product product = producer.getStorage().getProductByName(productName);
        producer.getStorage().addProductToStorage(product, producer, VAN, WAREHOUSE);
    }

    /**
     * Subtracts quantity of said product from producers storage and transfers it to van and then to shop oners warehouse
     * @param productName name of the product
     * @param sellQuantity quantity of the product to be sold
     */
    private void sellProduct(String productName, int sellQuantity) {
        Product product = producer.getStorage().getProductByName(productName);
        if (product == null) {
            return;
        }
        producer.getStorage().removeProductFromStorage(product, producer, WAREHOUSE, VAN, sellQuantity);
        Transaction sellTransaction = new Transaction(product, producer, PLACE_OF_SOLD, PLACE_OF_SOLD, SELL, LocalDate.now(), getPriceByName(productName), product.getLastTransaction());
        product.addTransaction(sellTransaction);
        Transaction transportTransaction = new Transaction(product, producer, VAN, WAREHOUSE, TRANSPORT, LocalDate.now(), TRANSPORT.getPrice(), product.getLastTransaction());
        product.addTransaction(transportTransaction);
    }

    private void returnProduct(String productName) {
        Product product = producer.getStorage().getProductByName(productName);
        if (product == null) {
            return;
        }
        producer.getStorage().removeProductFromStorage(product, producer, WAREHOUSE, VAN, product.getQuantity());
//        Transaction returnTransaction = new Transaction(product, , , RETURN, LocalDate.now(), 0, product.getLastTransaction());
//        product.addTransaction(returnTransaction);
        Transaction transportTransaction = new Transaction(product, producer, VAN, WAREHOUSE, TRANSPORT, LocalDate.now(), TRANSPORT.getPrice(), product.getLastTransaction());
    }

    /**
     * Assignes source products to the give product
     * @param productName name of the products
     * @return String listing out source products
     */
    public List<String> getProductOrigin(String productName) {

        List<String> origins = new ArrayList<>();
        switch (productName){
            case "FLOUR":
                origins.add(ProductsCatalogue.WHEAT.name());
            case "YOGHURT", "HEAVY_CREAM", "CHEESE":
                origins.add(ProductsCatalogue.MILK.name());
                break;
            case "PIE":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.APPLE.name());
                break;
            case "CHEESE_CAKE":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.HEAVY_CREAM.name());
                break;
            case "PASTA":
                origins.add(ProductsCatalogue.FLOUR.name());
                break;
            case "PASTA_FRESH":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                break;
            case "FRIES":
                origins.add(ProductsCatalogue.POTATO.name());
                break;
            case "BURGER":
                origins.add(ProductsCatalogue.BUN.name());
                origins.add(ProductsCatalogue.BEEF.name());
                origins.add(ProductsCatalogue.TOMATO.name());
                break;
            case "CHICKEN_STRIPS":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.CHICKEN_MEAT.name());
                break;
            case "BREAD", "BUN":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.MILK.name());
                break;
            default: return null;
        }

        return origins;
    }


}
