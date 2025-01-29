package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;

import java.time.LocalDate;

public class FarmerFactory implements Factory{

    private final Farmer farmer;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
    }

    /**
     * Creates and adds new product out of already existing products in warehouse
     * @param productName name of the product
     * @return Product
     */
    @Override
    public Product createProduct(String productName) {
        if (farmer.getWarehouse().findProduct(getProductOrigin(productName))) {
            Product newProduct = new Product(productName, 1, LocalDate.now());
            farmer.getWarehouse().addProduct(newProduct);
            return newProduct;
        }
        return null;
    }

    /**
     * Assignes source products to the give product
     * @param productName name of the products
     * @return String listing out source products
     */
    @Override
    public String getProductOrigin(String productName) {
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
    }

}

