package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Message;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShopOwnerFactory extends AbstractFactory{

    private final ShopOwner shopOwner;

    @Getter
    private final List<Customer> subscribedCustomers;

    public ShopOwnerFactory(ShopOwner shopOwner) {
        this.shopOwner = shopOwner;
        this.subscribedCustomers = new ArrayList<>();
    }

    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.SHOP, Place.VAN, date);
//        getStorage().removeProduct(product);
    }

    @Override
    protected void addTransportTransaction(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        transportProduct(product, distributor, salesman, date);
        salesman.getStorage().addProductToStorage(product, salesman, Place.VAN, Place.WAREHOUSE_PRODUCER, date);
    }

    /**
     * Removes product from persons storage and puts it on sale
     * @param product product to be sold
     * @return sold product
     */
    @Override
    public void sellProduct(ProductInterface product) {
        try {
            checkIfProductInNotNull(product);
            boolean found = getStorage().findProduct(product);
            if (found) {
                if (product.checkExpirationDateForSale()) {
                    throw new IllegalArgumentException("Product " + product.getName() + " is expired. Cannot sell product " + product.getName());
                }
                product.setProductStatus(ProductStatus.ON_SALE);
            } else {
                throw new IllegalArgumentException("Product " + product.getName() + " not found. Cannot sell product " + product.getName());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private void informCustomers(Product product) {
        Message message = new Message(
                this.shopOwner.getName(),
                String.format("Dear customer! We would like to inform you that %s is now on sale for %f.",
                        product.getName(), product.getPrice()));
        for (Customer customer : subscribedCustomers) {
            customer.receive(message);
        }
    }

    @Override
    protected Person getPerson() {
        return shopOwner;
    }

    @Override
    protected Storage getStorage() {
        return shopOwner.getStorage();
    }

    @Override
    protected boolean canCreateProduct() {
        return false;
    }

    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        return null;
    }

    public void addSubscribedCustomer(Customer customer) {
        subscribedCustomers.add(customer);
    }

    public void removeSubscribedCustomer(Customer customer) {
        subscribedCustomers.remove(customer);
    }
}
