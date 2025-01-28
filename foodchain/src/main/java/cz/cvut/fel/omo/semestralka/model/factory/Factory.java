package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;

public interface Factory {
    Product createProduct(String productName);
    String getProductOrigin(String productName);
}

