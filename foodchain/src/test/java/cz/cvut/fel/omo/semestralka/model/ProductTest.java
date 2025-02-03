package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ProductTest {

    private ProductInterface product;
    private ProductInterface product2;
    private ShopOwner shopOwner;
    private final List<Place> places = new ArrayList<>();
    Transaction transaction1;
    Transaction transaction2;


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
        transaction1 = new Transaction(
                product,
                shopOwner,
                Place.SHOP,
                Place.WAREHOUSE,
                OperationType.STORE,
                LocalDate.of(2024, 10, 25),
                0,
                product.getLastTransaction());
        transaction2 = new Transaction(
                product,
                shopOwner,
                Place.WAREHOUSE,
                Place.SHOP,
                OperationType.REMOVE,
                LocalDate.of(2024, 8, 24),
                0,
                product.getLastTransaction());
    }

    @Test
    void testProductConstructor() {
        assertEquals("WHEAT", product.getName());
        assertEquals(LocalDate.of(2024, 10, 1), product.getProducedOnDate());
        assertEquals(ProductStatus.NOT_ON_SALE, product.getProductStatus());
        assertNotNull(product.getTransactionHistory());
        assertTrue(product.getTransactionHistory().isEmpty());
        assertEquals(20, product.getPrice());
        assertEquals(10, product.getTemperature());
        assertEquals(LocalDate.of(2024, 10, 11), product.getExpirationDate());
    }

    @Test
    void testAddTransaction() {
        product.addTransaction(transaction1);
        assertEquals(1, product.getTransactionHistory().size());
        assertEquals(transaction1, product.getTransactionHistory().getFirst());
    }

    @Test
    void testGetLastTransaction_NoTransaction() {
        assertNull(product.getLastTransaction());
    }

    @Test
    void testGetLastTransaction_WithTransactions() {
        product.addTransaction(transaction1);
        product.addTransaction(transaction2);
        assertEquals(2, product.getTransactionHistory().size());
        assertEquals(transaction2, product.getLastTransaction());
    }

    @Test
    void testCheckExpirationDateForSale_NotExpired() {
        assertFalse(product2.checkExpirationDateForSale());
    }

    @Test
    void testCheckExpirationDateForSale_Expired() {
        assertTrue(product.checkExpirationDateForSale());
    }
}
