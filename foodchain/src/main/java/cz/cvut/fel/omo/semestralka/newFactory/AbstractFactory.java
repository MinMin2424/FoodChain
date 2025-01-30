package cz.cvut.fel.omo.semestralka.newFactory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;

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
            getStorage().removeProductFromStorage(productOrigin, getPerson(), Place.WAREHOUSE, Place.MANUFACTORY, 1);
        }
        Product newProduct = new Product(productName, 1, LocalDate.now());
        createNewTransaction(newProduct);
        storeProduct(newProduct);
    }

    public Product sellProduct(String productName, int sellQuantity) {
        if (!canSellProduct()) {
            throw new UnsupportedOperationException("Cannot sell product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot create product " + productName);
        }
        getStorage().removeProductFromStorage(product, getPerson(), Place.WAREHOUSE, Place.VAN, sellQuantity);
        createNewTransaction(product, OperationType.SELL, getPriceByName(productName));
        //TODO
        createNewTransaction(product, Place.VAN, Place.WAREHOUSE, OperationType.TRANSPORT, OperationType.TRANSPORT.getPrice());
        // Není lepší mi TransportTransaction až v metodě purchaseProduct???
        return product;
    }

    public void transportProduct(Product product) {
        //TODO
        if (!canTransportProduct()) {
            throw new UnsupportedOperationException("Cannot transport product " + product.getName());
        }

    }

    public void returnProduct(String productName, int returnQuantity) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot return product " + productName);
        }
    }

    public void purchaseProduct(Product product) {
        if (!canPurchaseProduct()) {
            throw new UnsupportedOperationException("Cannot purchase product " + product.getName());
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (!checkWallet(getPriceByName(product.getName()))) {
            throw new IllegalArgumentException("Wallet has not enough money to purchase product.");
        }
        getPerson().setWallet(getPerson().getWallet() - getPriceByName(product.getName()));
        createNewTransaction(product, OperationType.PURCHASE, getPriceByName(product.getName()));
        storeProduct(product);
    }

    protected boolean canCreateProduct() {
        return true;
    }

    protected boolean canSellProduct() {
        return true;
    }

    protected boolean canTransportProduct() {
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

    protected void createNewTransaction(Product product, Place movedFrom, Place moveTo, OperationType operationType, double price) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                movedFrom,
                moveTo,
                operationType,
                LocalDate.now(),
                price,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected void createNewTransaction(Product product, OperationType operationType, double price) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                operationType,
                LocalDate.now(),
                price,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected void createNewTransaction(Product product) {
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
