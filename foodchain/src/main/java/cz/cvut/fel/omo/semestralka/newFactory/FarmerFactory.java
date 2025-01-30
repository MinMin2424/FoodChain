package cz.cvut.fel.omo.semestralka.newFactory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;

import java.util.ArrayList;
import java.util.List;

public class FarmerFactory extends AbstractFactory{

    private final Farmer farmer;

    public FarmerFactory(Farmer farmer) {
        this.farmer = farmer;
    }

    @Override
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, getPerson(), Place.MANUFACTORY, Place.WAREHOUSE_FARMER);
    }

    @Override
    protected Person getPerson() {
        return farmer;
    }

    @Override
    protected Storage getStorage() {
        return farmer.getStorage();
    }

    @Override
    protected boolean canTransportProduct() {
        return false;
    }

    @Override
    protected boolean canReturnProduct() {
        return false;
    }

    @Override
    protected boolean canPurchaseProduct() {
        return false;
    }

    @Override
    protected List<String> getProductOrigin(String productName) {
        List<String> origins = new ArrayList<>();
        switch (productName) {
            case "BEEF", "MILK":
                origins.add(ProductsCatalogue.COW.name());
                break;
            case "CHICKEN_MEAT", "EGG", "FEATHER":
                origins.add(ProductsCatalogue.CHICKEN.name());
                break;
            case "FISH_FILET":
                origins.add(ProductsCatalogue.FISH.name());
                break;
            case "LAMB", "WOOL":
                origins.add(ProductsCatalogue.SHEEP.name());
                break;
            default:
                return null;
        }
        return origins;
    }
}
