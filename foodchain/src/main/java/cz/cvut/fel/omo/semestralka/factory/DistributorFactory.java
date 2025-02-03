package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;

import java.time.LocalDate;
import java.util.List;

public class DistributorFactory extends AbstractFactory{

    private final Distributor distributor;

    public DistributorFactory(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, getPerson(), Place.WAREHOUSE, Place.VAN, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        return null;
    }
}
