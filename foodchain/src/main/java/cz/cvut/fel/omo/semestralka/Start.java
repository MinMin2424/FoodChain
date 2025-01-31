package cz.cvut.fel.omo.semestralka;

import cz.cvut.fel.omo.semestralka.decorator.BioProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.DiscountedProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.ProductDecorator;
import cz.cvut.fel.omo.semestralka.factory.CustomerFactory;
import cz.cvut.fel.omo.semestralka.factory.ShopOwnerFactory;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.*;
import cz.cvut.fel.omo.semestralka.factory.FarmerFactory;
import cz.cvut.fel.omo.semestralka.factory.ProducerFactory;
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
        farmerFactory_VERCA.createProduct(ProductsCatalogue.MILK);
        farmerFactory_VERCA.createProduct(ProductsCatalogue.BEEF);
        farmerFactory_VERCA.createProduct(ProductsCatalogue.EGG);

        // CHECKING IF CERTAIN PRODUCTS ARE IN STORAGE
        farmer_VERCA.getStorage().getStorageInventory();

        // CREATE NEW PRODUCER
        List<Place> places1 = new ArrayList<>();
        places1.add(Place.MANUFACTORY);
        Producer producer_MINA  = new Producer("Mina", "123456789", 10_000, places1, address);
        ProducerFactory producerFactory_MINA = new ProducerFactory(producer_MINA);

        Product MILK = farmer_VERCA.getStorage().getProduct(ProductsCatalogue.MILK);
        Product BEEF = farmer_VERCA.getStorage().getProduct(ProductsCatalogue.BEEF);

        // FARMER SELLS SOME PRODUCTS
        farmerFactory_VERCA.sellProduct(MILK);
        farmerFactory_VERCA.sellProduct(BEEF);

        // CREATE NEW DISTRIBUTOR
        List<Place> places2 = new ArrayList<>();
        places1.add(Place.MANUFACTORY);
        Distributor distributor_TOM  = new Distributor("Tom", "123456789", 0, places2, address);

        // PRODUCER BUYS PRODUCTS
        producerFactory_MINA.purchaseProduct(MILK, distributor_TOM, farmer_VERCA);
        producerFactory_MINA.purchaseProduct(BEEF, distributor_TOM, farmer_VERCA);

        farmer_VERCA.getStorage().getStorageInventory();
        producer_MINA.getStorage().getStorageInventory();

        producerFactory_MINA.sellProduct(BEEF);

        ShopOwner shopOwner_Kaufland = new ShopOwner("Kaufland", "123456789", 800, places2, address);
        ShopOwnerFactory shopOwnerFactory_Kaufland = new ShopOwnerFactory(shopOwner_Kaufland);

        shopOwnerFactory_Kaufland.purchaseProduct(BEEF, distributor_TOM, producer_MINA);
//        DiscountedProductDecorator discount = new DiscountedProductDecorator(BEEF, 20);

        Customer customer_Roxy = new Customer("Roxy", "123456789", 100_000, places2, address);
        CustomerFactory customerFactory_Roxy = new CustomerFactory(customer_Roxy);

        shopOwnerFactory_Kaufland.addSubscribedCustomer(customer_Roxy);

        shopOwnerFactory_Kaufland.sellProduct(BEEF);
        customerFactory_Roxy.purchaseProduct(BEEF, distributor_TOM, shopOwner_Kaufland);

        BEEF.generateFoodChainReport();
        Transaction_Report.generateTransactionReport();


    }
}