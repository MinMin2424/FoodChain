package cz.cvut.fel.omo.semestralka.strategy.producerStrategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;

import java.util.ArrayList;
import java.util.List;

public class CheeseOriginStrategy implements ProductOriginStrategy {
    @Override
    public List<ProductsCatalogue> getListProductOrigin() {
        List<ProductsCatalogue> origins = new ArrayList<>();
        origins.add(ProductsCatalogue.MILK);
        return origins;
    }
}
