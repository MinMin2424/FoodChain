package cz.cvut.fel.omo.semestralka.strategy.farmerStrategy;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;

import java.util.ArrayList;
import java.util.List;

public class FishOriginStrategy implements ProductOriginStrategy {
    @Override
    public List<String> getListProductOrigin() {
        List<String> origins = new ArrayList<>();
        origins.add(ProductsCatalogue.FISH.name());
        return origins;
    }
}
