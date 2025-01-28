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


    // function creates new product out of already existing product in farmers warehouse
    @Override
    public Product createProduct(String productName) {
        if (farmer.getWarehouse().findProduct(getProductOrigin(productName))) {
            Product newProduct = new Product(productName, 1, LocalDate.now());
            farmer.getWarehouse().addProduct(newProduct);
            return newProduct;
        }
        return null;
    }


    @Override
    public String getProductOrigin(String productName) {
        switch (productName) {
            case "BEEF":
                return ProductsCatalogue.COW.name();
            default:
                return null;
        }
    }

}

