package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;
import cz.cvut.fel.omo.semestralka.strategy.producerStrategy.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProducerFactory extends AbstractFactory{

    private final Producer producer;
    private final Map<ProductsCatalogue, ProductOriginStrategy> originStrategies;

    public ProducerFactory(Producer producer) {
        this.producer = producer;
        this.originStrategies = new HashMap<>();
        putOriginsToStrategy();
    }

    /**
     * Returns the associated producer as a Person.
     * @return the producer as a Person object
     */
    @Override
    protected Person getPerson() {
        return producer;
    }

    /**
     * Stores a product in the producer's storage.
     * The product is transferred from the van to the warehouse specific to the producer.
     * @param product The product to be stored.
     * @param date    The date when the product is stored.
     */
    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, producer, Place.VAN, Place.WAREHOUSE_PRODUCER, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the storage associated with the producer.
     * @return the producer's storage
     */
    @Override
    protected Storage getStorage() {
        return producer.getStorage();
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
        originStrategies.put(ProductsCatalogue.FLOUR, new FlourOriginStrategy());
        originStrategies.put(ProductsCatalogue.YOGHURT, new YoghurtOriginStrategy());
        originStrategies.put(ProductsCatalogue.HEAVY_CREAM, new HeavyCreamOriginStrategy());
        originStrategies.put(ProductsCatalogue.CHEESE, new CheeseOriginStrategy());
        originStrategies.put(ProductsCatalogue.PIE, new PieOriginStrategy());
        originStrategies.put(ProductsCatalogue.CHEESECAKE, new CheeseCakeOriginStrategy());
        originStrategies.put(ProductsCatalogue.PASTA, new PastaOriginStrategy());
        originStrategies.put(ProductsCatalogue.PASTA_FRESH, new PastaFreshOriginStrategy());
        originStrategies.put(ProductsCatalogue.FRIES, new FriesOriginStrategy());
        originStrategies.put(ProductsCatalogue.BURGER, new BurgerOriginStrategy());
        originStrategies.put(ProductsCatalogue.CHICKEN_STRIPS, new ChickenStripsOriginStrategy());
        originStrategies.put(ProductsCatalogue.BREAD, new BreadOriginStrategy());
        originStrategies.put(ProductsCatalogue.BUN, new BunOriginStrategy());
    }
}
