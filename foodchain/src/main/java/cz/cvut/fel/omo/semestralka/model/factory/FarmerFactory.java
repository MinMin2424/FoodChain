package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;

public class FarmerFactory implements Factory{


    @Override
    public Product createProduct(String productName) {
        return null;
    }

    @Override
    public boolean isProductOfProduct(String productName) {
        return false;
    }
}

