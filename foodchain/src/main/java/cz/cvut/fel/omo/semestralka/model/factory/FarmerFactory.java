package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.model.transaction.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.model.enums.Place.*;
import static cz.cvut.fel.omo.semestralka.model.enums.OperationType.*;

public class FarmerFactory implements Factory {

    private final Farmer farmer;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
    }

    @Override
    public void executeOperation(String productName, int sellQuantity, OperationType operationType) {
        switch (operationType) {
            case CREATE:
                createProduct(productName);
                break;
            case STORE:
                storeProduct(productName);
            case SELL:
                // TODO
                break;
        }
    }

    /**
     * Creates and adds new product out of already existing products in warehouse
     * @param productName name of the product
     * @return Product
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
            farmer.getStorage().removeProductFromStorage(productOrigin, WAREHOUSE, MANUFACTORY, 1);
        }
        Product newProduct = new Product(productName, 1, LocalDate.now());
        Transaction transaction = new Transaction(newProduct, MUSHROOM_LAND, MUSHROOM_LAND, CREATE, LocalDate.now(), 0, null);
        farmer.getStorage().addProductToStorage(newProduct, MANUFACTORY, WAREHOUSE);
    }

    @Override
    public void storeProduct(String productName) {
        Product product = farmer.getStorage().getProductByName(productName);
        farmer.getStorage().addProductToStorage(product, FARM, WAREHOUSE);
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

