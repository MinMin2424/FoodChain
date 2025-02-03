package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {

    private ProductInterface product;
    private ProductInterface product2;
    private ShopOwner shopOwner;
    private final List<Place> places = new ArrayList<>();

    @BeforeEach
    void setUp() {
        places.add(Place.SHOP);
        product = new Product(
                ProductsCatalogue.WHEAT.name(),
                LocalDate.of(2024, 10, 1));
        product2 = new Product(
                ProductsCatalogue.CHEESE.name(),
                LocalDate.now().minusDays(2));
        shopOwner = new ShopOwner(
                "Shop owner",
                "123 123 123",
                100,
                places,
                Address.generateRandomAddress());
    }

    @Test
    void testAddProductToStorage_Success() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        assertTrue(shopOwner.getStorage().findProduct(product));
        assertEquals(1, shopOwner.getStorage().countFullness());
    }

    @Test
    void testRemoveProductFromStorage_Success() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        boolean removed = shopOwner.getStorage().removeProductFromStorage(product, shopOwner, Place.SHOP, Place.BACKPACK, LocalDate.now());
        assertTrue(removed);
        assertFalse(shopOwner.getStorage().findProduct(product));
        assertEquals(0, shopOwner.getStorage().countFullness());
    }

    @Test
    void testRemoveProductFromStorage_Fail() {
        boolean removed = shopOwner.getStorage().removeProductFromStorage(product, shopOwner, Place.SHOP, Place.BACKPACK, LocalDate.now());
        assertFalse(removed);
    }

    @Test
    void testFindProduct_ByCatalogue() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        assertTrue(shopOwner.getStorage().findProduct(ProductsCatalogue.WHEAT));
    }

    @Test
    void testFindProduct_ByProduct() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        assertTrue(shopOwner.getStorage().findProduct(product));
    }

    @Test
    void testGetAllProducts() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        shopOwner.getStorage().addProductToStorage(product2, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        List<ProductInterface> products = shopOwner.getStorage().getProductList();
        assertEquals(2, products.size());
        assertTrue(products.contains(product));
        assertTrue(products.contains(product2));
        assertEquals(products.getLast(), product2);
    }

    @Test
    void testCountFullness() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        shopOwner.getStorage().addProductToStorage(product2, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        assertEquals(2, shopOwner.getStorage().countFullness());
    }

    @Test
    void testGetProduct_ByCatalogue() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        shopOwner.getStorage().addProductToStorage(product2, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        assertEquals(product, shopOwner.getStorage().getProduct(ProductsCatalogue.WHEAT));
        assertEquals(product2, shopOwner.getStorage().getProduct(ProductsCatalogue.CHEESE));
    }

    @Test
    void testGetProduct_ByProduct() {
        shopOwner.getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        shopOwner.getStorage().addProductToStorage(product2, shopOwner, Place.VAN, Place.SHOP, LocalDate.now());
        assertEquals(product, shopOwner.getStorage().getProduct(product));
        assertEquals(product2, shopOwner.getStorage().getProduct(product2));
    }
}
