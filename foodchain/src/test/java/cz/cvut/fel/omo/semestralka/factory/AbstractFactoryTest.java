package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
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


public class AbstractFactoryTest {

    private Farmer farmer;
    private FarmerFactory farmerFactory;
    private ShopOwner shopOwner;
    private ShopOwnerFactory shopOwnerFactory;
    private Customer customer;
    private CustomerFactory customerFactory;
    private Distributor distributor;
    private List<Place> places_farmer = new ArrayList<>();
    private List<Place> places_shopOwner = new ArrayList<>();
    private List<Place> places_customer = new ArrayList<>();
    private List<Place> places_distributor = new ArrayList<>();
    private ProductInterface product;
    private ProductInterface product2;

    @BeforeEach
    void setUp() {
        places_farmer.add(Place.WAREHOUSE_FARMER); places_farmer.add(Place.MANUFACTORY);
        places_shopOwner.add(Place.SHOP); places_shopOwner.add(Place.WAREHOUSE);
        places_distributor.add(Place.VAN);
        places_customer.add(Place.BACKPACK);
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
                100,
                places_shopOwner,
                Address.generateRandomAddress());
        shopOwnerFactory = new ShopOwnerFactory(shopOwner);
        distributor = new Distributor(
                "Distributor",
                "123 123 123",
                0,
                places_distributor,
                Address.generateRandomAddress());
        customer = new Customer(
                "Customer",
                "123 123 123",
                1000,
                places_customer,
                Address.generateRandomAddress());
        customerFactory = new CustomerFactory(customer);
        product = new Product(
                ProductsCatalogue.COW.name(),
                LocalDate.of(2024, 10, 1));
        product2 = new Product(
                ProductsCatalogue.CHEESE.name(),
                LocalDate.now().minusDays(2));
    }

    @Test
    void testCreateProduct_CannotCreateProduct() {
        // Customer cannot create product
        ProductInterface product = customerFactory.createProduct(ProductsCatalogue.BEEF, LocalDate.now());
        assertNull(product);
    }

    @Test
    void testCreateProduct_DoNotHaveOrigins() {
        // Farmer doesn't have all origins to create certain product
        ProductInterface product = farmerFactory.createProduct(ProductsCatalogue.BEEF, LocalDate.now());
        assertNull(product);
    }

    @Test
    void testCreateProduct_Success() {
        // Farmer has origin cow and can create beef
        farmerFactory.storeProduct(product, LocalDate.now());
        ProductInterface beef = farmerFactory.createProduct(ProductsCatalogue.BEEF, LocalDate.now());
        assertNotNull(beef);
        assertTrue(farmer.getStorage().findProduct(beef));
        assertEquals(beef, farmer.getStorage().getProduct(beef));
        assertEquals(1, farmer.getStorage().countFullness());
    }

    @Test
    void testSellProduct_CannotSellProduct() {
        // customer cannot sell product
        customerFactory.sellProduct(product2);
        assertEquals(ProductStatus.NOT_ON_SALE, product2.getProductStatus());
    }

    @Test
    void testSellProduct_DoNotHaveProduct() {
        // farmer doesn't have beef, cannot sell it
        farmerFactory.sellProduct(product2);
        assertEquals(0, farmer.getStorage().countFullness());
        assertEquals(ProductStatus.NOT_ON_SALE, product2.getProductStatus());
    }

    @Test
    void testSellProduct_Success() {
        // farmer has beef in storage
        farmerFactory.storeProduct(product, LocalDate.now());
        assertEquals(1, farmer.getStorage().countFullness());
        farmerFactory.sellProduct(product);
        assertEquals(ProductStatus.ON_SALE, product.getProductStatus());
    }

    @Test
    void testReturnProduct_CannotReturnProduct() {
        // farmer cannot return product
        farmerFactory.storeProduct(product2, LocalDate.now());
        assertEquals(1, farmer.getStorage().countFullness());
        farmerFactory.returnProduct(product2, distributor, customer, LocalDate.now());
        assertEquals(1, farmer.getStorage().countFullness());
    }

    @Test
    void testReturnProduct_DoNotHaveProduct() {
        // customer doesn't have product, cannot return it
        customerFactory.returnProduct(product2, distributor, farmer, LocalDate.now());
        assertEquals(0, customer.getStorage().countFullness());
    }

    @Test
    void testReturnProduct_FarmerIsNotADistributor() {
        // farmer is not a distributor
        shopOwnerFactory.storeProduct(product, LocalDate.now());
        shopOwnerFactory.returnProduct(product, farmer, farmer, LocalDate.now());
        assertEquals(1, shopOwner.getStorage().countFullness());
    }

    @Test
    void testReturnProduct_Success() {
        // ok
        shopOwnerFactory.storeProduct(product, LocalDate.now());
        shopOwnerFactory.storeProduct(product2, LocalDate.now());
        assertEquals(2, shopOwner.getStorage().countFullness());
        shopOwnerFactory.returnProduct(product2, distributor, farmer, LocalDate.now());
        assertEquals(1, shopOwner.getStorage().countFullness());
    }

    @Test
    void testPurchaseProduct_CannotPurchaseProduct() {
        // farmer cannot purchase product
        shopOwnerFactory.storeProduct(product, LocalDate.now());
        shopOwnerFactory.sellProduct(product);
        farmerFactory.purchaseProduct(product, distributor, shopOwner, LocalDate.now());
        assertEquals(1, shopOwner.getStorage().countFullness());
        assertEquals(0, farmer.getStorage().countFullness());
        assertEquals(ProductStatus.ON_SALE, product.getProductStatus());
    }

    @Test
    void testPurchaseProduct_ProductIsNotForSale() {
        // product is not for sale, shop owner didn't sell it
        shopOwnerFactory.storeProduct(product, LocalDate.now());
        customerFactory.purchaseProduct(product, distributor, shopOwner, LocalDate.now());
        assertEquals(1, shopOwner.getStorage().countFullness());
        assertEquals(0, farmer.getStorage().countFullness());
        assertEquals(ProductStatus.NOT_ON_SALE, product.getProductStatus());
    }

    @Test
    void testPurchaseProduct_NotEnoughMoneyToPurchase() {
        // customer has only 10,-
        customer.setWallet(10);
        shopOwnerFactory.storeProduct(product, LocalDate.now());
        shopOwnerFactory.sellProduct(product);
        customerFactory.purchaseProduct(product, distributor, shopOwner, LocalDate.now());
        assertEquals(1, shopOwner.getStorage().countFullness());
        assertEquals(0, customer.getStorage().countFullness());
        assertEquals(ProductStatus.ON_SALE, product.getProductStatus());
    }

    @Test
    void testPurchaseProduct_Success() {
        // ok
        customer.setWallet(1000);
        shopOwnerFactory.storeProduct(product2, LocalDate.now());
        shopOwnerFactory.sellProduct(product2);
        customerFactory.purchaseProduct(product2, distributor, shopOwner, LocalDate.now());
        assertEquals(0, shopOwner.getStorage().countFullness());
        assertEquals(1, customer.getStorage().countFullness());
        assertEquals(ProductStatus.IS_ALREADY_PURCHASED, product2.getProductStatus());
    }

    @Test
    void testPurchaseProduct_DoubleSpending() {
        farmerFactory.storeProduct(product2, LocalDate.now());
        farmerFactory.sellProduct(product2);
        customerFactory.purchaseProduct(product2, distributor, farmer, LocalDate.now());
        shopOwnerFactory.purchaseProduct(product2, distributor, farmer, LocalDate.now());
        assertEquals(0, farmer.getStorage().countFullness());
        assertEquals(1, customer.getStorage().countFullness());
        assertEquals(0, shopOwner.getStorage().countFullness());
        assertEquals(ProductStatus.IS_ALREADY_PURCHASED, product2.getProductStatus());
    }
}
