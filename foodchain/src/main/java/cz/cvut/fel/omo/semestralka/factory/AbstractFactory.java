package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;
import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import cz.cvut.fel.omo.semestralka.transaction.Transaction_Report;

import java.time.LocalDate;
import java.util.List;
import static cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue.getPriceByName;

public abstract class AbstractFactory {

    public abstract void storeProduct(Product product);

    /**
     * Creates specified product
     * @param product the product to be created
     */
    public final void createProduct(ProductsCatalogue product) {
        if (!canCreateProduct()) {
            throw new UnsupportedOperationException("Cannot create product " + product.name());
        }
        List<ProductsCatalogue> origins = getProductOrigin(product);
        if (origins == null) {
            throw new IllegalArgumentException("Origins is null. Cannot create product " + product.name());
        }
        for (ProductsCatalogue origin : origins) {
            boolean found = getStorage().findProduct(origin);
            if (!found) {
                throw new IllegalArgumentException("Origin " + origin + " not found. Cannot create product " + product.name());
            }
        }
        for (ProductsCatalogue origin : origins) {
            Product productOrigin = getStorage().getProduct(origin);
            getStorage().removeProductFromStorage(productOrigin, getPerson(), Place.WAREHOUSE, Place.MANUFACTORY);
        }
        Product newProduct = new Product(product.name(), LocalDate.now());
        createNewTransaction(newProduct);
        storeProduct(newProduct);
    }

    /**
     * Removes product from persons storage and puts it on sale
     * @param product product to be sold
     * @return sold product
     */
    public Product sellProduct(Product product) {
        if (!canSellProduct()) {
            throw new UnsupportedOperationException("Cannot sell product " + product.getName());
        }
        if (product == null) {
            throw new IllegalArgumentException("Product is null.");
        }
        boolean found = getStorage().findProduct(product);
        if (found) {
            if (product.checkExpirationDateForSale()) {
                throw new IllegalArgumentException("Product " + product.getName() + " is expired. Cannot sell product " + product.getName());
            }
            getStorage().removeProductFromStorage(product, getPerson(), Place.WAREHOUSE, Place.ON_SALE);
            createNewTransaction(product, product.getPrice());
            return product;
        }
        throw new IllegalArgumentException("Product " + product.getName() + " not found. Cannot sell product " + product.getName());
    }

    /**
     * moves product from one place to another
     * @param product Product to be moved
     * @param distributor Person who moves the product
     * @param salesman Person who sells product
     */
    public void transportProduct(Product product, Person distributor, Person salesman) {
        if (distributor instanceof Distributor) {
            distributor.setWallet(distributor.getWallet() + OperationType.TRANSPORT.getPrice());
            createNewTransaction(product, salesman, distributor);
        } else {
            throw new UnsupportedOperationException("Cannot transport product " + product.getName());
        }
    }

    /**
     * Returns bought product back to seller
     * @param product the product
     * @param distributor Person who moves the product
     * @param salesman Person who sold the product
     */
    public void returnProduct(Product product, Person distributor, Person salesman) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + product.getName());
        }
    }

    /**
     * Product gets moved to buyers storage and seller gets paid
     * @param product Product to be bought
     * @param distributor Person who moves the product
     * @param salesman Person who sells the product
     */
    public void purchaseProduct(Product product, Person distributor, Person salesman) {
        if (!canPurchaseProduct()) {
            throw new UnsupportedOperationException("Cannot purchase product " + product.getName());
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (!checkWallet(product.getPrice())) {
            throw new IllegalArgumentException("Wallet has not enough money to purchase product.");
        }
        salesman.setWallet(salesman.getWallet() + product.getPrice());
        getPerson().setWallet(getPerson().getWallet() - product.getPrice());
        createNewTransaction(product, salesman, product.getPrice());
        transportProduct(product, distributor, salesman);
        storeProduct(product);
        createMoneyTransaction(product, salesman, product.getPrice());
    }


    protected boolean canCreateProduct() {
        return true;
    }

    protected boolean canSellProduct() {
        return true;
    }

    protected boolean canReturnProduct() {
        return true;
    }

    protected boolean canPurchaseProduct() {
        return true;
    }

    protected abstract Person getPerson();
    protected abstract Storage getStorage();
    protected abstract List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product);

    /**
     * Transfers money from one person to another
     * @param product Product
     * @param personFrom Person who pays
     * @param price amount of money to be paid
     */
    protected final void createMoneyTransaction(Product product, Person personFrom, double price) {
        MoneyTransaction transaction = new MoneyTransaction(
                product,
                price,
                personFrom,
                getPerson(),
                OperationType.PURCHASE,
                LocalDate.now(),
                personFrom.getWallet() - price,
                getPerson().getWallet() + price,
                personFrom.getWallet(),
                getPerson().getWallet()
        );
        Transaction_Report.transactionHistory.add(transaction);
    }

    protected final void createNewTransaction(Product product, Place movedFrom, Place moveTo) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                movedFrom,
                moveTo,
                OperationType.RETURN,
                LocalDate.now(),
                0,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(Product product, double price) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                OperationType.SELL,
                LocalDate.now(),
                price,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(Product product, Person personFrom, double price) {
        Transaction transaction = new Transaction(
                product,
                personFrom,
                getPerson(),
                OperationType.PURCHASE,
                LocalDate.now(),
                price,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(Product product, Person personFrom, Person personTo) {
        Transaction transaction = new Transaction(
                product,
                personFrom,
                personTo,
                OperationType.TRANSPORT,
                LocalDate.now(),
                OperationType.TRANSPORT.getPrice(),
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(Product product) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                OperationType.CREATE,
                LocalDate.now(),
                0,
                null
        );
        product.addTransaction(transaction);
    }

    /**
     * Checks if person has enough money for the payment
     * @param productPrice price of the product
     * @return Boolean Whether there is enough money for the payment
     */
    protected boolean checkWallet(double productPrice) {
        return getPerson().getWallet() >= productPrice;
    }
}
