package cz.cvut.fel.omo.semestralka.strategy.producerStrategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;

import java.util.ArrayList;
import java.util.List;

public class ChickenStripsOriginStrategy implements ProductOriginStrategy {
    @Override
    public List<ProductsCatalogue> getListProductOrigin() {
        List<ProductsCatalogue> origins = new ArrayList<>();
        origins.add(ProductsCatalogue.FLOUR);
        origins.add(ProductsCatalogue.CHICKEN_MEAT);
        origins.add(ProductsCatalogue.EGG);
        return origins;
    }
}
