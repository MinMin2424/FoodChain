package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.factory.CustomerFactory;
import cz.cvut.fel.omo.semestralka.factory.FarmerFactory;
import cz.cvut.fel.omo.semestralka.factory.ShopOwnerFactory;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductDecoratorTest {

    private Farmer farmer;
    private FarmerFactory farmerFactory;
    private ShopOwner shopOwner;
    private ShopOwnerFactory shopOwnerFactory;
    private Distributor distributor;
    private List<Place> places_farmer = new ArrayList<>();
    private List<Place> places_shopOwner = new ArrayList<>();
    private List<Place> places_distributor = new ArrayList<>();
    private ProductInterface product;
    private ProductInterface product2;

    @BeforeEach
    void setUp() {
        places_farmer.add(Place.WAREHOUSE_FARMER); places_farmer.add(Place.MANUFACTORY);
        places_shopOwner.add(Place.SHOP); places_shopOwner.add(Place.WAREHOUSE);
        places_distributor.add(Place.VAN);
        farmer = new Farmer(
                "Farmer",
                "123 123 123",
                100,
                places_farmer,
                Address.generateRandomAddress());
        farmerFactory = new FarmerFactory(farmer);
        shopOwner = new ShopOwner(
                "Shop owner",
                "123 123 123",
                200,
                places_shopOwner,
                Address.generateRandomAddress());
        shopOwnerFactory = new ShopOwnerFactory(shopOwner);
        distributor = new Distributor(
                "Distributor",
                "123 123 123",
                0,
                places_distributor,
                Address.generateRandomAddress());
        product = new Product(
                ProductsCatalogue.BURGER.name(),
                LocalDate.of(2024, 10, 1));
        product2 = new Product(
                ProductsCatalogue.CHEESE.name(),
                LocalDate.now().minusDays(2));
    }

    @Test
    void testBioProductDecorator() {
        farmerFactory.storeProduct(product2, LocalDate.now());
        ProductInterface product2_bio = farmerFactory.createProduct(new BioProductDecorator(product2), LocalDate.now());
        farmerFactory.sellProduct(product2_bio);
        shopOwnerFactory.purchaseProduct(product2_bio, distributor, farmer, LocalDate.now());
        assertEquals(250, farmer.getWallet());
        assertEquals(50, shopOwner.getWallet());
    }

    @Test
    void testDiscountedProductDecorator() {
        farmerFactory.storeProduct(product2, LocalDate.now());
        ProductInterface product2_discounted = farmerFactory.createProduct(new DiscountedProductDecorator(product2, 30), LocalDate.now());
        farmerFactory.sellProduct(product2_discounted);
        shopOwnerFactory.purchaseProduct(product2_discounted, distributor, farmer, LocalDate.now());
        assertEquals(170, farmer.getWallet());
        assertEquals(130, shopOwner.getWallet());
    }

}
