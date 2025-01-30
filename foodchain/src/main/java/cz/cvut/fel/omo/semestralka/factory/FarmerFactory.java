package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.enums.OperationType.*;
import static cz.cvut.fel.omo.semestralka.enums.Place.*;
import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getPriceByName;

public class FarmerFactory implements Factory {

    private final Farmer farmer;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
    }

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
        }
    }

    //should not function also remove products used for creating the new product out of the warehouse?
    //right now you can get infinetly meat from one cow
    //on the other hand you can get infinite ammount of milk from one cow
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
            boolean found = farmer.getStorage().findProduct(origin);
            if (!found) {
                return;
            }
        }
        for (String origin : origins) {
            Product productOrigin = farmer.getStorage().getProductByName(origin);
            farmer.getStorage().removeProductFromStorage(productOrigin, farmer, WAREHOUSE, MANUFACTORY);
        }
        Product newProduct = new Product(productName, LocalDate.now());
        Transaction transaction = new Transaction(newProduct, farmer,MUSHROOM_LAND, MUSHROOM_LAND, CREATE, LocalDate.now(), 0, null);
        farmer.getStorage().addProductToStorage(newProduct, farmer, MANUFACTORY, WAREHOUSE);
    }

    /**
     * Adds product to customers storage
     * @param productName name of the product
     */
    @Override
    public void storeProduct(String productName) {
        Product product = farmer.getStorage().getProductByName(productName);
        farmer.getStorage().addProductToStorage(product, farmer, MUSHROOM_LAND, FARM);
    }

    /**
     * Subtracts quantity of said product from farmers storage and transfers it to van and then to warehouse
     * @param productName name of the product
     * @param sellQuantity quantity of the product to be sold
     */
    private void sellProduct(String productName, int sellQuantity) {
        Product product = farmer.getStorage().getProductByName(productName);
        if (product == null) {
            return;
        }
        farmer.getStorage().removeProductFromStorage(product, farmer, WAREHOUSE, VAN);
        Transaction sellTransaction = new Transaction(product, farmer, ON_SALE, ON_SALE, SELL, LocalDate.now(), getPriceByName(productName), product.getLastTransaction());
        product.addTransaction(sellTransaction);
        Transaction transportTransaction = new Transaction(product, farmer,VAN, WAREHOUSE, TRANSPORT, LocalDate.now(), TRANSPORT.getPrice(), product.getLastTransaction());
        product.addTransaction(transportTransaction);
    }

    /**
     * Assignes source products to the give product
     * @param productName name of the products
     * @return String listing out source products
     */
    private List<String> getProductOrigin(String productName) {
        List<String> origins = new ArrayList<>();
        switch (productName) {
            case "BEEF", "MILK":
                origins.add(ProductsCatalogue.COW.name());
                break;
            case "CHICKEN_MEAT", "EGG", "FEATHER":
                origins.add(ProductsCatalogue.CHICKEN.name());
                break;
            case "FISH_FILET":
                origins.add(ProductsCatalogue.FISH.name());
                break;
            case "LAMB", "WOOL":
                origins.add(ProductsCatalogue.SHEEP.name());
                break;
            default:
                return null;
        }
        return origins;
    }

}
