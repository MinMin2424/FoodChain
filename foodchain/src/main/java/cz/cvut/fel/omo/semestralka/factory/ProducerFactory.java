package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import cz.cvut.fel.omo.semestralka.strategy.ProductOriginStrategy;
import cz.cvut.fel.omo.semestralka.strategy.producerStrategy.*;

import java.util.ArrayList;
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

    @Override
    protected Person getPerson() {
        return producer;
    }

    @Override
    public void storeProduct(ProductInterface product) {
        getStorage().addProductToStorage(product, producer, Place.VAN, Place.WAREHOUSE_PRODUCER);
    }

    @Override
    protected Storage getStorage() {
        return producer.getStorage();
    }

    @Override
    public void returnProduct(ProductInterface product, Person distributor, Person salesman) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null. Cannot return product.");
        }
        createNewTransaction(product, Place.WAREHOUSE_PRODUCER, Place.VAN);
        transportProduct(product, distributor, salesman);
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
