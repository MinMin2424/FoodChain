package cz.cvut.fel.omo.semestralka;

import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import cz.cvut.fel.omo.semestralka.newFactory.FarmerFactory;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.newFactory.ProducerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Start {

    public static LocalDate createExpirationDays(LocalDate producedOnDays, int durationDays) {
        return producedOnDays.plusDays(durationDays);
    }

    public static void main(String[] args) {

        // CREATE NEW ADDRESS AND NEW FARMER
        List<Place> places = new ArrayList<>();
        places.add(Place.FARM);
        Address address = new Address("Dejvicka", "Prague", "169 00", "Czech");
        Farmer farmer_VERCA = new Farmer("Verca", "123456789", 100_000_000, places, address);
        FarmerFactory farmerFactory_VERCA = new FarmerFactory(farmer_VERCA);

        // CREATE NEW PRODUCTS AND ADD IN FARMER STORAGE
        Product cow = new Product(ProductsCatalogue.COW.name(), 1, LocalDate.now());
        Product cow1 = new Product(ProductsCatalogue.COW.name(), 3, LocalDate.now());
        farmerFactory_VERCA.storeProduct(cow);
        farmerFactory_VERCA.storeProduct(cow1);
        Product chicken = new Product(ProductsCatalogue.CHICKEN.name(), 10, LocalDate.now());
        farmerFactory_VERCA.storeProduct(chicken);

        // CREATE NEW PRODUCTS BY EXISTING PRODUCTS IN STORAGE
        farmerFactory_VERCA.createProduct(ProductsCatalogue.MILK.name());
        farmerFactory_VERCA.createProduct(ProductsCatalogue.BEEF.name());
        farmerFactory_VERCA.createProduct(ProductsCatalogue.BEEF.name());
//        farmerFactory_VERCA.createProduct(ProductsCatalogue.EGG.name());

        // CHECKING IF CERTAIN PRODUCTS ARE IN STORAGE
        farmer_VERCA.getStorage().getStorageInventory();

        // CREATE NEW PRODUCER
        List<Place> places1 = new ArrayList<>();
        places1.add(Place.MANUFACTORY);
        Producer producer_MINA  = new Producer("Mina", "123456789", 500_000, places1, address);
        ProducerFactory producerFactory_MINA = new ProducerFactory(producer_MINA);

        // FARMER SELLS SOME PRODUCTS AND PRODUCER WILL BUY THEM
        Product MILK_SELL = farmerFactory_VERCA.sellProduct(farmer_VERCA.getStorage().getProductByName(ProductsCatalogue.MILK.name()).getName(), 1);
        Product BEEF_SELL = farmerFactory_VERCA.sellProduct(farmer_VERCA.getStorage().getProductByName(ProductsCatalogue.BEEF.name()).getName(), 2);

        producerFactory_MINA.purchaseProduct(MILK_SELL);
        producerFactory_MINA.purchaseProduct(BEEF_SELL);

        farmer_VERCA.getStorage().getStorageInventory();
        producer_MINA.getStorage().getStorageInventory();

        // PRODUCER RETURNS BEEF
        producerFactory_MINA.returnProduct(BEEF_SELL.getName(), BEEF_SELL.getQuantity());

        BEEF_SELL.generateFoodChainReport();
    }
}