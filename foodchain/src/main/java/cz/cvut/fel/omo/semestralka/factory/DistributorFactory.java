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

    /**
     * Stores a product in the storage system by adding it to the distributor's storage.
     * The product is stored from the warehouse to the van at the specified date.
     * @param product the product to be stored
     * @param date the date when the product is stored
     */
    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, getPerson(), Place.WAREHOUSE, Place.VAN, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the associated distributor as a Person.
     * @return the customer as a Person object
     */
    @Override
    protected Person getPerson() {
        return distributor;
    }

    /**
     * Returns the storage associated with the distributor.
     * @return the distributor's storage
     */
    @Override
    protected Storage getStorage() {
        return distributor.getStorage();
    }

    /**
     * Determines whether the distributor can create products.
     * @return false, as distributors cannot create products
     */
    @Override
    protected boolean canCreateProduct() {
        return false;
    }

    /**
     * Determines whether the distributor can sell products.
     * @return false, as distributors cannot sell products
     */
    @Override
    protected boolean canSellProduct() {
        return false;
    }

    /**
     * Determines whether the distributor can return products.
     * @return false, as distributors cannot return products
     */
    @Override
    protected boolean canReturnProduct() {
        return false;
    }

    /**
     * Determines whether the distributor can purchase products.
     * @return false, as distributors cannot purchase products
     */
    @Override
    protected boolean canPurchaseProduct() {
        return false;
    }

    /**
     * Returns the origin of the product for this distributor.
     * Since distributors do not have a product origin, this method returns null.
     * @param product the product to check the origin for
     * @return null, as distributors do not create products
     */
    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        return null;
    }
}
