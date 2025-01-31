package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarmerFactory extends AbstractFactory{

    private final Farmer farmer;
    private final Map<String, ProductOriginStrategy> originStrategies;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
        this.originStrategies = new HashMap<>();
        originStrategies.put("BEEF", new BeefOriginStrategy());
        originStrategies.put("MILK", new MilkOriginStrategy());
        originStrategies.put("CHICKEN_MEAT", new ChickenOriginStrategy());
        originStrategies.put("EGG", new ChickenOriginStrategy());
        originStrategies.put("FEATHER", new ChickenOriginStrategy());
        originStrategies.put("FISH_FILET", new FishOriginStrategy());
        originStrategies.put("LAMB", new LambOriginStrategy());
        originStrategies.put("WOOL", new WoolOriginStrategy());
    }

    @Override
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, getPerson(), Place.MANUFACTORY, Place.WAREHOUSE_FARMER);
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
    protected List<String> getProductOrigin(String productName) {
        ProductOriginStrategy strategy = originStrategies.get(productName);
        if (strategy != null) {
            return strategy.getListProductOrigin();
        }
        return null;
    }
}
