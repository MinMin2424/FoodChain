package cz.cvut.fel.omo.semestralka.strategy.producerStrategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;

import java.util.ArrayList;
import java.util.List;

public class CheeseCakeOriginStrategy implements ProductOriginStrategy {
    @Override
    public List<ProductsCatalogue> getListProductOrigin() {
        List<ProductsCatalogue> origins = new ArrayList<>();
        origins.add(ProductsCatalogue.FLOUR);
        origins.add(ProductsCatalogue.EGG);
        origins.add(ProductsCatalogue.HEAVY_CREAM);
        return origins;
    }
}
