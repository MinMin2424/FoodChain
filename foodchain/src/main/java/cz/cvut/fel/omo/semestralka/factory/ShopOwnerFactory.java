package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Message;
import cz.cvut.fel.omo.semestralka.model.Person;
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

    /**
     * Stores a product in the shop owner's storage, moving it to the shop location.
     * @param product The product to be stored.
     * @param date    The date of storage.
     */
    @Override
    public void storeProduct(ProductInterface product, LocalDate date) {
        try {
            getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP, date);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles a return transaction, moving the product from the shop back to the van.
     * @param product The product to be returned.
     * @param date    The date of the return transaction.
     */
    @Override
    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.SHOP, Place.VAN, date);
//        getStorage().removeProduct(product);
    }

    /**
     * Handles a transport transaction, moving a product between distributor and salesman,
     * and storing it back in the warehouse.
     * @param product     The product to transport.
     * @param distributor The distributor responsible for transportation.
     * @param salesman    The salesman handling the product.
     * @param date        The date of the transport transaction.
     */
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
                System.out.println(getPerson().getName() + " successfully sold product " + product.getName() + ".");
                informCustomers(product);
            } else {
                throw new IllegalArgumentException("Product " + product.getName() + " not found. Cannot sell product " + product.getName());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Notifies all subscribed customers about a new product on sale.
     * @param product The product that is now on sale.
     */
    private void informCustomers(ProductInterface product) {
        Message message = new Message(
                this.shopOwner.getName(),
                String.format("Dear customer! We would like to inform you that %s is now on sale for %f.",
                        product.getName(), product.getPrice()));
        for (Customer customer : subscribedCustomers) {
            customer.receive(message);
        }
    }

    /**
     * Returns the associated shopOwner as a Person.
     * @return the shopOwner as a Person object
     */
    @Override
    protected Person getPerson() {
        return shopOwner;
    }

    /**
     * Returns the storage associated with the shopOwner.
     * @return the shopOwner's storage
     */
    @Override
    protected Storage getStorage() {
        return shopOwner.getStorage();
    }

    /**
     * Determines whether the shopOwner can create products.
     * @return false, as shopOwner cannot create products
     */
    @Override
    protected boolean canCreateProduct() {
        return false;
    }

    /**
     * Returns the origin of the product for this shopOwner.
     * Since shopOwners do not have a product origin, this method returns null.
     * @param product the product to check the origin for
     * @return null, as shopOwners do not create products
     */
    @Override
    protected List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product) {
        return null;
    }

    /**
     * Adds a customer to the list of subscribed customers for notifications.
     * @param customer The customer to be added.
     */
    public void addSubscribedCustomer(Customer customer) {
        subscribedCustomers.add(customer);
    }

    /**
     * Removes a customer from the list of subscribed customers.
     * @param customer The customer to be removed.
     */
    public void removeSubscribedCustomer(Customer customer) {
        subscribedCustomers.remove(customer);
    }
}
