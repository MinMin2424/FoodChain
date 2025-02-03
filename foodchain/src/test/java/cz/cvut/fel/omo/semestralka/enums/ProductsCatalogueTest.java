package cz.cvut.fel.omo.semestralka.enums;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.model.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProductsCatalogueTest {

    @Test
    void testGetDurationByName_ValidProduct() {
        ProductInterface HEAVY_CREAM = new Product(ProductsCatalogue.HEAVY_CREAM.name(), LocalDate.now());
        assertEquals(16, ProductsCatalogue.getDurationByName(HEAVY_CREAM.getName()));
        assertNotEquals(0, ProductsCatalogue.getDurationByName(HEAVY_CREAM.getName()));
    }

    @Test
    void testGetDurationByName_InvalidProduct() {
        assertEquals(-1, ProductsCatalogue.getDurationByName("INVALID"));
    }

    @Test
    void testGetTemperatureByName_ValidProduct() {
        ProductInterface BURGER = new Product(ProductsCatalogue.BURGER.name(), LocalDate.now());
        assertEquals(5, ProductsCatalogue.getTemperatureByName(BURGER.getName()));
        assertNotEquals(10, ProductsCatalogue.getTemperatureByName(BURGER.getName()));
    }

    @Test
    void testGetTemperatureByName_InvalidProduct() {
        assertEquals(-1, ProductsCatalogue.getTemperatureByName("INVALID"));
    }

    @Test
    void testGetPriceByName_ValidProduct() {
        ProductInterface WALNUT = new Product(ProductsCatalogue.WALNUT.name(), LocalDate.now());
        assertEquals(120, ProductsCatalogue.getPriceByName(WALNUT.getName()));
        assertNotEquals(250, ProductsCatalogue.getPriceByName(WALNUT.getName()));
    }

    @Test
    void testGetPriceByName_InvalidProduct() {
        assertEquals(-1, ProductsCatalogue.getPriceByName("INVALID"));
    }

    @Test
    void testGetMethodsCaseInsensitive_ValidProduct() {
        assertEquals(5, ProductsCatalogue.getDurationByName("Pie"));
        assertEquals(5, ProductsCatalogue.getTemperatureByName("pie"));
        assertEquals(130, ProductsCatalogue.getPriceByName("pIe"));
    }
}
