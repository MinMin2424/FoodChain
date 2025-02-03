package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ProducerFactoryTest {

    private Producer producer;
    private ProducerFactory producerFactory;
    private final List<Place> places = new ArrayList<>();
    List<ProductsCatalogue> origins;

    @BeforeEach
    void setUp() {
        places.add(Place.WAREHOUSE_PRODUCER);
        places.add(Place.MANUFACTORY);
        producer = new Producer(
                "Producer",
                "123 123 123",
                100,
                places,
                Address.generateRandomAddress());
        producerFactory = new ProducerFactory(producer);
    }

    @Test
    void testGetProductOrigin_Flour() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.FLOUR);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.WHEAT));
    }

    @Test
    void testGetProductOrigin_Yoghurt() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.YOGHURT);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.MILK));
    }

    @Test
    void testGetProductOrigin_HeavyCream() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.HEAVY_CREAM);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.MILK));
    }

    @Test
    void testGetProductOrigin_Cheese() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.CHEESE);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.MILK));
    }

    @Test
    void testGetProductOrigin_Pie() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.PIE);
        assertNotNull(origins);
        assertEquals(origins.size(), 3);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
        assertTrue(origins.contains(ProductsCatalogue.EGG));
        assertTrue(origins.contains(ProductsCatalogue.APPLE));
    }

    @Test
    void testGetProductOrigin_CheeseCake() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.CHEESECAKE);
        assertNotNull(origins);
        assertEquals(origins.size(), 3);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
        assertTrue(origins.contains(ProductsCatalogue.EGG));
        assertTrue(origins.contains(ProductsCatalogue.HEAVY_CREAM));
    }

    @Test
    void testGetProductOrigin_Pasta() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.PASTA);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
    }

    @Test
    void testGetProductOrigin_PastaFresh() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.PASTA_FRESH);
        assertNotNull(origins);
        assertEquals(origins.size(), 2);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
        assertTrue(origins.contains(ProductsCatalogue.EGG));
    }

    @Test
    void testGetProductOrigin_Fries() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.FRIES);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.POTATO));
    }

    @Test
    void testGetProductOrigin_Burger() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.BURGER);
        assertNotNull(origins);
        assertEquals(origins.size(), 3);
        assertTrue(origins.contains(ProductsCatalogue.BUN));
        assertTrue(origins.contains(ProductsCatalogue.BEEF));
        assertTrue(origins.contains(ProductsCatalogue.TOMATO));
    }

    @Test
    void testGetProductOrigin_ChickenStrips() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.CHICKEN_STRIPS);
        assertNotNull(origins);
        assertEquals(origins.size(), 3);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
        assertTrue(origins.contains(ProductsCatalogue.EGG));
        assertTrue(origins.contains(ProductsCatalogue.CHICKEN_MEAT));
    }

    @Test
    void testGetProductOrigin_Bread() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.BREAD);
        assertNotNull(origins);
        assertEquals(origins.size(), 3);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
        assertTrue(origins.contains(ProductsCatalogue.EGG));
        assertTrue(origins.contains(ProductsCatalogue.MILK));
    }

    @Test
    void testGetProductOrigin_Bun() {
        origins = producerFactory.getProductOrigin(ProductsCatalogue.BUN);
        assertNotNull(origins);
        assertTrue(origins.contains(ProductsCatalogue.FLOUR));
        assertTrue(origins.contains(ProductsCatalogue.EGG));
        assertTrue(origins.contains(ProductsCatalogue.MILK));
    }

}
