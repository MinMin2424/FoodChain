package cz.cvut.fel.omo.semestralka.newFactory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;

import java.util.List;

import static cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue.getPriceByName;

public class CustomerFactory extends AbstractFactory {

    private final Customer customer;

    public CustomerFactory(Customer customer) {
        this.customer = customer;
    }

    @Override
    protected Person getPerson() {
        return customer;
    }

    @Override
    protected Storage getStorage() {
        return customer.getStorage();
    }

    @Override
    protected boolean canTransportProduct() {
        return false;
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
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, getPerson(), Place.SHOP, Place.BACKPACK);
    }

    @Override
    public void returnProduct(String productName, int returnQuantity) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot return product " + productName);
        }
        createNewTransaction(product, Place.BACKPACK, Place.SHOP, OperationType.RETURN, returnQuantity);
    }

    @Override
    protected List<String> getProductOrigin(String productName) {
        return null;
    }
}
