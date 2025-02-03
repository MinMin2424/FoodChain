package cz.cvut.fel.omo.semestralka.strategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;

import java.util.List;

public interface ProductOriginStrategy {

    /**
     * Retrieves a list of product origins from the product catalog.
     * @return a List of ProductsCatalogue representing the origins of products.
     */
    List<ProductsCatalogue> getListProductOrigin();
}
