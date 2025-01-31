package cz.cvut.fel.omo.semestralka.strategy.farmerStrategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;

import java.util.ArrayList;
import java.util.List;

public class WoolOriginStrategy implements ProductOriginStrategy {
    @Override
    public List<String> getListProductOrigin() {
        List<String> origins = new ArrayList<>();
        origins.add(ProductsCatalogue.SHEEP.name());
        return origins;
    }
}
