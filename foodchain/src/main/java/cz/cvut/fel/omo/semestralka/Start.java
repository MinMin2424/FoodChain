package cz.cvut.fel.omo.semestralka;

import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import cz.cvut.fel.omo.semestralka.newFactory.DistributorFactory;
import cz.cvut.fel.omo.semestralka.newFactory.FarmerFactory;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.newFactory.ProducerFactory;
import cz.cvut.fel.omo.semestralka.transaction.Transaction_Report;

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
        Farmer farmer_VERCA = new Farmer("Verca", "123456789", 500_000, places, address);
        FarmerFactory farmerFactory_VERCA = new FarmerFactory(farmer_VERCA);

        // CREATE NEW PRODUCTS AND ADD IN FARMER STORAGE
        Product chicken = new Product(ProductsCatalogue.CHICKEN.name(), LocalDate.now());
        Product cow = new Product(ProductsCatalogue.COW.name(), LocalDate.now());
        Product cow1 = new Product(ProductsCatalogue.COW.name(), LocalDate.now());
        farmerFactory_VERCA.storeProduct(chicken);
        farmerFactory_VERCA.storeProduct(cow);
        farmerFactory_VERCA.storeProduct(cow1);


        // CREATE NEW PRODUCTS BY EXISTING PRODUCTS IN STORAGE
        farmerFactory_VERCA.createProduct(ProductsCatalogue.MILK.name());
        farmerFactory_VERCA.createProduct(ProductsCatalogue.BEEF.name());
//        farmerFactory_VERCA.createProduct(ProductsCatalogue.BEEF.name());
        farmerFactory_VERCA.createProduct(ProductsCatalogue.EGG.name());

        // CHECKING IF CERTAIN PRODUCTS ARE IN STORAGE
        farmer_VERCA.getStorage().getStorageInventory();

        // CREATE NEW PRODUCER
        List<Place> places1 = new ArrayList<>();
        places1.add(Place.MANUFACTORY);
        Producer producer_MINA  = new Producer("Mina", "123456789", 10_000, places1, address);
        ProducerFactory producerFactory_MINA = new ProducerFactory(producer_MINA);

        // FARMER SELLS SOME PRODUCTS
        Product MILK_SELL = farmerFactory_VERCA.sellProduct(farmer_VERCA.getStorage().getProductByName(ProductsCatalogue.MILK.name()));
        Product BEEF_SELL = farmerFactory_VERCA.sellProduct(farmer_VERCA.getStorage().getProductByName(ProductsCatalogue.BEEF.name()));

        // CREATE NEW DISTRIBUTOR
        List<Place> places2 = new ArrayList<>();
        places1.add(Place.MANUFACTORY);
        Distributor distributor_TOM  = new Distributor("Tom", "123456789", 0, places2, address);

        // PRODUCER BUYS PRODUCTS
        producerFactory_MINA.purchaseProduct(MILK_SELL, distributor_TOM, farmer_VERCA);
        producerFactory_MINA.purchaseProduct(BEEF_SELL, distributor_TOM, farmer_VERCA);

        farmer_VERCA.getStorage().getStorageInventory();
        producer_MINA.getStorage().getStorageInventory();

        // PRODUCER RETURNS BEEF
        producerFactory_MINA.returnProduct(BEEF_SELL.getName(), distributor_TOM, farmer_VERCA);

//        MILK_SELL.generateFoodChainReport();
        BEEF_SELL.generateFoodChainReport();
        BEEF_SELL.generatePartiesReport();

//        System.out.println("Farmer's wallet: " + farmer_VERCA.getWallet());
//        System.out.println("Producer's wallet: " + producer_MINA.getWallet());
//        System.out.println("Distributor's wallet: " + distributor_TOM.getWallet());

        Transaction_Report.generateTransactionReport();

    }
}