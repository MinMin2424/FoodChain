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

    /**
     * Stores a product in the farmer's storage.
     * The product is transferred from the manufactory to the warehouse specific to the farmer.
     * @param product The product to be stored.
     * @param date    The date when the product is stored.
     */
    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, getPerson(), Place.MANUFACTORY, Place.WAREHOUSE_FARMER, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the associated farmer as a Person.
     * @return the farmer as a Person object
     */
    @Override
    protected Person getPerson() {
        return farmer;
    }

    /**
     * Returns the storage associated with the farmer.
     * @return the farmer's storage
     */
    @Override
    protected Storage getStorage() {
        return farmer.getStorage();
    }

    /**
     * Determines whether the farmer can return products.
     * @return false, as farmer cannot return products
     */
    @Override
    protected boolean canReturnProduct() {
        return false;
    }

    /**
     * Determines whether the farmer can purchase products.
     * @return false, as farmer cannot purchase products
     */
    @Override
    protected boolean canPurchaseProduct() {
        return false;
    }

    /**
     * Retrieves the origin of a specific product using a defined strategy.
     * @param product The product whose origin is to be retrieved.
     * @return A list of products representing the origin, or null if no strategy exists.
     */
    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        ProductOriginStrategy strategy = originStrategies.get(product);
        if (strategy != null) {
            return strategy.getListProductOrigin();
        }
        return null;
    }

    /**
     * Initializes the product origin strategies for specific products.
     * Each product in the catalogue is mapped to its corresponding strategy.
     */
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
