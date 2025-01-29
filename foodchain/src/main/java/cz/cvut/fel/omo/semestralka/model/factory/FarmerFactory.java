package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FarmerFactory implements Factory{

    private final Farmer farmer;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
    }

    public void executeOperation(String productName, OperationType operationType) {
        switch (operationType) {
            case CREATE:
                createProduct(productName);
                break;
            case SELL:

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
        for (String origin : origins) {
            boolean found = farmer.getWarehouse().findProduct(origin);
            if (!found) {
                return;
            }
        }
        for (String origin : origins) {
            Product productOrigin = farmer.getWarehouse().getProductByName(origin);
            farmer.getWarehouse().removeProduct(productOrigin, 1);
        }
        farmer.getWarehouse().addProduct(new Product(productName, 1, LocalDate.now()));
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
                return ProductsCatalogue.COW.name();
            case "CHICKEN_MEAT", "EGG", "FEATHER":
                return ProductsCatalogue.CHICKEN.name();
            case "FISH_FILET":
                return ProductsCatalogue.FISH.name();
            case "LAMB", "WOOL":
                return ProductsCatalogue.SHEEP.name();
            default:
                return null;
        }
        return origins;
    }

}

