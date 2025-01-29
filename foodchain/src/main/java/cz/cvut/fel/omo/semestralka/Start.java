package cz.cvut.fel.omo.semestralka;

import java.time.LocalDate;

public class Start {

    public static LocalDate createExpirationDays(LocalDate producedOnDays, int durationDays) {
        return producedOnDays.plusDays(durationDays);
    }

    public static void main(String[] args) {
//        List<Place> places = new ArrayList<>();
//        places.add(Place.FARM);
//        Address address = new Address("Dejvicka", "Prague", "169 00", "Czech");
//        Farmer farmer = new Farmer("Verca", "123456789", 100_000_000, places, address);
//
//        Product cow = new Product(ProductsCatalogue.COW.name(), 1, LocalDate.now());
//        Product cow1 = new Product(ProductsCatalogue.COW.name(), 1, LocalDate.now());
//        farmer.getWarehouse().addProduct(cow);
//        farmer.getWarehouse().addProduct(cow1);
//
//        System.out.println(farmer.getWarehouse().toString());
//        FarmerFactory farmerFactory = new FarmerFactory(farmer);
//        farmerFactory.createProduct(ProductsCatalogue.BEEF.name());
//        farmer.getWarehouse().getWarehouseInventory();

    }
}
