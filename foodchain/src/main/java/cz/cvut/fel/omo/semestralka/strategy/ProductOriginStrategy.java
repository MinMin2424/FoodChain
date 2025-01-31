package cz.cvut.fel.omo.semestralka.strategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;

import java.util.List;

public interface ProductOriginStrategy {
    List<ProductsCatalogue> getListProductOrigin();
}
