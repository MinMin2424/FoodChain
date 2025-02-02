package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;

import java.time.LocalDate;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getPriceByName;

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
    protected boolean canCreateProduct() {
        return false;
    }

    @Override
    protected boolean canSellProduct() {
        return false;
    }

    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        getStorage().addProductToStorage(product, getPerson(), Place.SHOP, Place.BACKPACK, date);
    }

    @Override
    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.SHOP, Place.VAN, date);
        getStorage().removeProduct(product);
    }

    @Override
    protected void addTransportTransaction(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        // no transport
        salesman.getStorage().addProductToStorage(product, salesman, Place.BACKPACK, Place.SHOP, date);
    }

    @Override
    public void purchaseProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (!checkWallet(product.getPrice())) {
            throw new IllegalArgumentException("Wallet has not enough money to purchase product.");
        }
        salesman.setWallet(salesman.getWallet() + product.getPrice());
        getPerson().setWallet(getPerson().getWallet() - product.getPrice());
        createNewTransaction(product, salesman, product.getPrice(), date);
        salesman.getStorage().removeProductFromStorage(product, salesman, Place.WAREHOUSE, Place.ON_SALE, date);
        storeProduct(product, date);
        createMoneyTransaction(product, salesman, product.getPrice(), date);
    }

    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        return null;
    }
}
