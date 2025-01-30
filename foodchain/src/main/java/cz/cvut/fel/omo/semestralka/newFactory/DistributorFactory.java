package cz.cvut.fel.omo.semestralka.newFactory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;

import java.util.List;

public class DistributorFactory extends AbstractFactory{

    private final Distributor distributor;

    public DistributorFactory(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, getPerson(), Place.WAREHOUSE, Place.VAN);
    }

    @Override
    protected Person getPerson() {
        return distributor;
    }

    @Override
    protected Storage getStorage() {
        return distributor.getStorage();
    }

    @Override
    protected boolean canCreateProduct() {
        return false;
    }

    @Override
    protected boolean canSellProduct() {
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
        return null;
    }
}
