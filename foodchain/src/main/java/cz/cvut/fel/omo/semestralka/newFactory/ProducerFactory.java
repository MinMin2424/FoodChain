package cz.cvut.fel.omo.semestralka.newFactory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;

import java.util.ArrayList;
import java.util.List;

public class ProducerFactory extends AbstractFactory{

    private final Producer producer;

    public ProducerFactory(Producer producer) {
        this.producer = producer;
    }

    @Override
    protected Person getPerson() {
        return producer;
    }

    @Override
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, producer, Place.VAN, Place.WAREHOUSE_PRODUCER);
    }

    @Override
    protected Storage getStorage() {
        return producer.getStorage();
    }

    @Override
    public void returnProduct(String productName, Person distributor, Person salesman) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot return product " + productName);
        }
        createNewTransaction(product, Place.WAREHOUSE_PRODUCER, Place.VAN);
        transportProduct(product, distributor, salesman);
    }

    @Override
    protected List<String> getProductOrigin(String productName) {
        List<String> origins = new ArrayList<>();
        switch (productName) {
            case "FLOUR":
                origins.add(ProductsCatalogue.WHEAT.name());
            case "YOGHURT", "HEAVY_CREAM", "CHEESE":
                origins.add(ProductsCatalogue.MILK.name());
                break;
            case "PIE":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.APPLE.name());
                break;
            case "CHEESE_CAKE":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.HEAVY_CREAM.name());
                break;
            case "PASTA":
                origins.add(ProductsCatalogue.FLOUR.name());
                break;
            case "PASTA_FRESH":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                break;
            case "FRIES":
                origins.add(ProductsCatalogue.POTATO.name());
                break;
            case "BURGER":
                origins.add(ProductsCatalogue.BUN.name());
                origins.add(ProductsCatalogue.BEEF.name());
                origins.add(ProductsCatalogue.TOMATO.name());
                break;
            case "CHICKEN_STRIPS":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.CHICKEN_MEAT.name());
                break;
            case "BREAD", "BUN":
                origins.add(ProductsCatalogue.FLOUR.name());
                origins.add(ProductsCatalogue.EGG.name());
                origins.add(ProductsCatalogue.MILK.name());
                break;
            default:
                return null;
        }
        return origins;
    }
}
