package cz.cvut.fel.omo.semestralka.newFactory;

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

    public final void createProduct(String productName) {
        if (!canCreateProduct()) {
            throw new UnsupportedOperationException("Cannot create product " + productName);
        }
        List<String> origins = getProductOrigin(productName);
        if (origins == null) {
            throw new IllegalArgumentException("Origins is null. Cannot create product " + productName);
        }
        for (String origin : origins) {
            boolean found = getStorage().findProduct(origin);
            if (!found) {
                throw new IllegalArgumentException("Origin " + origin + " not found. Cannot create product " + productName);
            }
        }
        for (String origin : origins) {
            Product productOrigin = getStorage().getProductByName(origin);
            getStorage().removeProductFromStorage(productOrigin, getPerson(), Place.WAREHOUSE, Place.MANUFACTORY);
        }
        Product newProduct = new Product(productName, LocalDate.now());
        createNewTransaction(newProduct);
        storeProduct(newProduct);
    }

    public final Product sellProduct(Product product) {
        if (!canSellProduct()) {
            throw new UnsupportedOperationException("Cannot sell product " + product.getName());
        }
        if (product == null) {
            throw new IllegalArgumentException("Product " + product.getName() + " not found. Cannot create product " + product.getName());
        }
        getStorage().removeProductFromStorage(product, getPerson(), Place.WAREHOUSE, Place.ON_SALE);
        createNewTransaction(product, getPriceByName(product.getName()));
        return product;
    }

    public void transportProduct(Product product, Person distributor, Person salesman) {
        if (distributor instanceof Distributor) {
            distributor.setWallet(distributor.getWallet() + OperationType.TRANSPORT.getPrice());
            createNewTransaction(product, salesman, distributor);
        } else {
            throw new UnsupportedOperationException("Cannot transport product " + product.getName());
        }
    }

    public void returnProduct(String productName, Person distributor, Person salesman) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot return product " + productName);
        }
    }

    public void purchaseProduct(Product product, Person distributor, Person salesman) {
        if (!canPurchaseProduct()) {
            throw new UnsupportedOperationException("Cannot purchase product " + product.getName());
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (!checkWallet(getPriceByName(product.getName()))) {
            throw new IllegalArgumentException("Wallet has not enough money to purchase product.");
        }
        salesman.setWallet(salesman.getWallet() + getPriceByName(product.getName()));
        getPerson().setWallet(getPerson().getWallet() - getPriceByName(product.getName()));
        createNewTransaction(product, salesman, getPriceByName(product.getName()));
        transportProduct(product, distributor, salesman);
        storeProduct(product);
        createMoneyTransaction(product, salesman, getPriceByName(product.getName()));
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
    protected abstract List<String> getProductOrigin(String productName);

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

    protected boolean checkWallet(double productPrice) {
        return getPerson().getWallet() >= productPrice;
    }
}
