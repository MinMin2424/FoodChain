package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;
import cz.cvut.fel.omo.semestralka.strategy.farmerStrategy.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmerFactory extends AbstractFactory{

    private final Farmer farmer;
    private final Map<ProductsCatalogue, ProductOriginStrategy> originStrategies;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
        this.originStrategies = new HashMap<>();
        putOriginsToStrategy();
    }

    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, getPerson(), Place.MANUFACTORY, Place.WAREHOUSE_FARMER, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected Person getPerson() {
        return farmer;
    }

    @Override
    protected Storage getStorage() {
        return farmer.getStorage();
    }

    @Override
    protected boolean canReturnProduct() {
        return false;
    }

    @Override
    protected boolean canPurchaseProduct() {
        return false;
    }

    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        ProductOriginStrategy strategy = originStrategies.get(product);
        if (strategy != null) {
            return strategy.getListProductOrigin();
        }
        return null;
    }

    private void putOriginsToStrategy() {
        originStrategies.put(ProductsCatalogue.BEEF, new BeefOriginStrategy());
        originStrategies.put(ProductsCatalogue.MILK, new MilkOriginStrategy());
        originStrategies.put(ProductsCatalogue.CHICKEN_MEAT, new ChickenOriginStrategy());
        originStrategies.put(ProductsCatalogue.EGG, new ChickenOriginStrategy());
        originStrategies.put(ProductsCatalogue.FEATHER, new ChickenOriginStrategy());
        originStrategies.put(ProductsCatalogue.FISH_FILET, new FishOriginStrategy());
        originStrategies.put(ProductsCatalogue.LAMB, new LambOriginStrategy());
        originStrategies.put(ProductsCatalogue.WOOL, new WoolOriginStrategy());
    }
}
