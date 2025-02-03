package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;

import java.time.LocalDate;
import java.util.List;

public class CustomerFactory extends AbstractFactory {

    private final Customer customer;

    public CustomerFactory(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the associated customer as a Person.
     * @return the customer as a Person object
     */
    @Override
    protected Person getPerson() {
        return customer;
    }

    /**
     * Returns the storage associated with the customer.
     * @return the customer's storage
     */
    @Override
    protected Storage getStorage() {
        return customer.getStorage();
    }

    /**
     * Determines whether a product can be created by this customer.
     * @return false, as customers cannot create products
     */
    @Override
    protected boolean canCreateProduct() {
        return false;
    }

    /**
     * Determines whether a product can be sold by this customer.
     * @return false, as customers cannot sell products
     */
    @Override
    protected boolean canSellProduct() {
        return false;
    }

    /**
     * Stores the given product in the customer's storage on the specified date.
     * @param product the product to be stored
     * @param date the date the product is stored
     */
    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, getPerson(), Place.SHOP, Place.BACKPACK, date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Adds a return transaction for the given product, moving it from the shop to the van.
     * @param product the product to be returned
     * @param date the date of the return transaction
     */
    @Override
    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.BACKPACK, Place.SHOP, date);
        getStorage().removeProduct(product);
    }

    /**
     * Adds a transport transaction for the given product, moving it from the salesperson's backpack to the shop.
     * @param product the product to be transported
     * @param distributor the distributor performing the transport
     * @param salesman the salesperson receiving the product
     * @param date the date of the transport transaction
     */
    @Override
    protected void addTransportTransaction(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        // no transport
        salesman.getStorage().addProductToStorage(product, salesman, Place.BACKPACK, Place.SHOP, date);
    }

    /**
     * Allows a customer to purchase a product from a salesman, given sufficient funds.
     * It checks if the product is available, if the customer has enough money, and processes the transaction.
     * @param product the product being purchased
     * @param distributor the distributor selling the product
     * @param salesman the salesperson facilitating the transaction
     * @param date the date of the purchase
     */
    @Override
    public void purchaseProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        try {
            checkIfProductInNotNull(product);
            checkProductStatusForSale(product, salesman, date);
            checkIfPersonHasEnoughMoney(product);

            if (salesman.getStorage().removeProductFromStorage(product, salesman, Place.WAREHOUSE, Place.ON_SALE, date)) {
                createNewTransaction(product, salesman, product.getPrice(), date);
                salesman.setWallet(salesman.getWallet() + product.getPrice());
                getPerson().setWallet(getPerson().getWallet() - product.getPrice());
                storeProduct(product, date);
                createMoneyTransaction(product, salesman, product.getPrice(), date);
                product.setProductStatus(ProductStatus.IS_ALREADY_PURCHASED);
                System.out.println(getPerson().getName() + " successfully purchased product " + product.getName() + ".");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the origin of a product based on its category.
     * (Not applicable in the context of a customer)
     * @param product the product whose origin is to be retrieved
     * @return null, as customers do not have a product origin
     */
    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        return null;
    }
}
