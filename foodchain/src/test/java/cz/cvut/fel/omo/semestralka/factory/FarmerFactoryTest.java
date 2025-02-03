package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FarmerFactoryTest {

    private Farmer farmer;
    private FarmerFactory farmerFactory;
    private final List<Place> places = new ArrayList<>();
    List<ProductsCatalogue> origins;

    @BeforeEach
    void setUp() {
        places.add(Place.WAREHOUSE_FARMER);
        places.add(Place.MANUFACTORY);
        farmer = new Farmer(
                "Farmer",
                "123 123 123",
                100,
                places,
                Address.generateRandomAddress());
        farmerFactory = new FarmerFactory(farmer);
    }

    @Test
    void testGetProductOrigin_Beef() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.BEEF);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.COW));
    }

    @Test
    void testGetProductOrigin_Milk() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.MILK);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.COW));
    }

    @Test
    void testGetProductOrigin_ChickenMeat() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.CHICKEN_MEAT);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.CHICKEN));
    }

    @Test
    void testGetProductOrigin_Egg() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.EGG);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.CHICKEN));
    }

    @Test
    void testGetProductOrigin_Feather() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.FEATHER);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.CHICKEN));
    }

    @Test
    void testGetProductOrigin_FishFilet() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.FISH_FILET);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.FISH));
    }

    @Test
    void testGetProductOrigin_Lamb() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.LAMB);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.SHEEP));
    }

    @Test
    void testGetProductOrigin_Wool() {
        origins = farmerFactory.getProductOrigin(ProductsCatalogue.WOOL);
        assertNotNull(origins);
        assertEquals(origins.size(), 1);
        assertTrue(origins.contains(ProductsCatalogue.SHEEP));
    }
}
