package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.model.Message;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getPriceByName;

public class ShopOwnerFactory extends AbstractFactory{

    private final ShopOwner shopOwner;

    @Getter
    private final List<Customer> subscribedCustomers;

    public ShopOwnerFactory(ShopOwner shopOwner) {
        this.shopOwner = shopOwner;
        this.subscribedCustomers = new ArrayList<>();
    }

    @Override
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP);
    }

    @Override
    public void returnProduct(String productName, Person distributor, Person salesman) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot return product " + productName);
        }
        createNewTransaction(product, Place.SHOP, Place.VAN);
        transportProduct(product, distributor, salesman);
    }

    /**
     * Removes product from persons storage and puts it on sale
     * @param product product to be sold
     * @return sold product
     */
    public Product sellProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null.");
        }
        boolean found = getStorage().findProduct(product.getName());
        if (found) {
            if (getStorage().getProductByName(product.getName()).checkExpirationDateForSale()) {
                throw new IllegalArgumentException("Product " + product.getName() + " is expired. Cannot sell product " + product.getName());
            }
            getStorage().removeProductFromStorage(product, getPerson(), Place.WAREHOUSE, Place.ON_SALE);
            createNewTransaction(product, product.getPrice());
            informCustomers(product);
            return product;
        }
        throw new IllegalArgumentException("Product " + product.getName() + " not found. Cannot sell product " + product.getName());
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
    protected List<String> getProductOrigin(String productName) {
        return null;
    }

    public void addSubscribedCustomer(Customer customer) {
        subscribedCustomers.add(customer);
    }

    public void removeSubscribedCustomer(Customer customer) {
        subscribedCustomers.remove(customer);
    }
}
