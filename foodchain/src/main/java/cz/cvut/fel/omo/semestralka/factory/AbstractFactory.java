package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;
import cz.cvut.fel.omo.semestralka.transaction.*;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractFactory {

    public abstract void storeProduct(ProductInterface product, LocalDate date);

    /**
     * Creates specified product
     * @param product the product to be created
     */
    public final ProductInterface createProduct(ProductsCatalogue product, LocalDate date) {
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
            ProductInterface productOrigin = getStorage().getProduct(origin);
            getStorage().removeProductFromStorage(productOrigin, getPerson(), Place.WAREHOUSE, Place.MANUFACTORY, date);
        }
        ProductInterface newProduct = new Product(product.name(), LocalDate.now());
        createNewTransaction(newProduct, date);
        storeProduct(newProduct, date);
        return newProduct;
    }

    /**
     * Removes product from persons storage and puts it on sale
     * @param product product to be sold
     * @return sold product
     */
    public void sellProduct(ProductInterface product) {
        if (!canSellProduct()) {
            throw new UnsupportedOperationException("Cannot sell product " + product.getName());
        }
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
    }

    /**
     * moves product from one place to another
     * @param product Product to be moved
     * @param distributor Person who moves the product
     * @param salesman Person who sells product
     */
    public void transportProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        if (distributor instanceof Distributor) {
            distributor.setWallet(distributor.getWallet() + OperationType.TRANSPORT.getPrice());
            createNewTransaction(product, salesman, distributor, date);
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
    public void returnProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        checkIfPersonCanReturnProduct(product);
        checkIfProductInNotNull(product);
        addReturnTransaction(product, date);
        addTransportTransaction(product, distributor, salesman, date);
    }

    protected void checkIfPersonCanReturnProduct(ProductInterface product) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + product.getName());
        }
    }

    protected void checkIfProductInNotNull(ProductInterface product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null. Cannot return product.");
        }
    }

    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.WAREHOUSE_PRODUCER, Place.VAN, date);
        getStorage().removeProduct(product);
    }

    protected void addTransportTransaction(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        transportProduct(product, distributor, salesman, date);
        salesman.getStorage().addProductToStorage(product, salesman, Place.VAN, Place.WAREHOUSE_FARMER, date);
    }

    /**
     * Product gets moved to buyers storage and seller gets paid
     * @param product Product to be bought
     * @param distributor Person who moves the product
     * @param salesman Person who sells the product
     */
    public void purchaseProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        checkIfPersonCanPurchaseProduct(product);
        checkIfProductInNotNull(product);
        checkProductStatusForSale(product, salesman, date);
        checkIfPersonHasEnoughMoney(product);
        if (salesman.getStorage().removeProductFromStorage(product, salesman, Place.WAREHOUSE, Place.ON_SALE, date)) {
            salesman.setWallet(salesman.getWallet() + product.getPrice());
            getPerson().setWallet(getPerson().getWallet() - product.getPrice());
            createNewTransaction(product, salesman, product.getPrice(), date);
            transportProduct(product, distributor, salesman, date);
            storeProduct(product, date);
            createMoneyTransaction(product, salesman, product.getPrice(), date);
            product.setProductStatus(ProductStatus.IS_ALREADY_PURCHASED);
        }
    }

    private void checkIfPersonCanPurchaseProduct(ProductInterface product) {
        if (!canPurchaseProduct()) {
            throw new UnsupportedOperationException("Cannot purchase product " + product.getName());
        }
    }

    private void checkProductStatusForSale(ProductInterface product, Person salesman, LocalDate date) {
        if (product.getProductStatus() == ProductStatus.NOT_ON_SALE) {
            throw new IllegalArgumentException("Product " + product.getName() + " is not on sale.");
        }
        if (product.getProductStatus() == ProductStatus.IS_ALREADY_PURCHASED) {
            SecurityTransaction securityTransaction = findSecurityTransaction(product, salesman);
            if (securityTransaction == null) {
                securityTransaction = new SecurityTransaction(product, getPerson(), salesman);
                StorageSecurityTransaction.securityTransactions.add(securityTransaction);
            } else {
                securityTransaction.increaseAttemptCount(date);
            }
//            throw new IllegalArgumentException("Product " + product.getName() + " is already purchased.");
        }
    }

    private SecurityTransaction findSecurityTransaction(ProductInterface product, Person salesman) {
        for (SecurityTransaction securityTransaction : StorageSecurityTransaction.securityTransactions) {
            if (securityTransaction.getProduct().equals(product) &&
                    securityTransaction.getAttemptedBuyer().equals(getPerson()) &&
                    securityTransaction.getOriginOwner().equals(salesman)) {
                return securityTransaction;
            }
        }
        return null;
    }

    private void checkIfPersonHasEnoughMoney(ProductInterface product) {
        if (!checkWallet(product.getPrice())) {
            throw new IllegalArgumentException("Wallet has not enough money to purchase product.");
        }
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
    protected final void createMoneyTransaction(ProductInterface product, Person personFrom, double price, LocalDate date) {
        MoneyTransaction transaction = new MoneyTransaction(
                product,
                price,
                personFrom,
                getPerson(),
                OperationType.PURCHASE,
                date,
                personFrom.getWallet() - price,
                getPerson().getWallet() + price,
                personFrom.getWallet(),
                getPerson().getWallet()
        );
        StorageMoneyTransaction.transactionHistory.add(transaction);
    }

    protected final void createNewTransaction(ProductInterface product, Place movedFrom, Place moveTo, LocalDate date) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                movedFrom,
                moveTo,
                OperationType.RETURN,
                date,
                0,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(ProductInterface product, double price, LocalDate date) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                OperationType.SELL,
                date,
                price,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(ProductInterface product, Person personFrom, double price, LocalDate date) {
        Transaction transaction = new Transaction(
                product,
                personFrom,
                getPerson(),
                OperationType.PURCHASE,
                date,
                price,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(ProductInterface product, Person personFrom, Person personTo, LocalDate date) {
        Transaction transaction = new Transaction(
                product,
                personFrom,
                personTo,
                OperationType.TRANSPORT,
                date,
                OperationType.TRANSPORT.getPrice(),
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    protected final void createNewTransaction(ProductInterface product, LocalDate date) {
        Transaction transaction = new Transaction(
                product,
                getPerson(),
                OperationType.CREATE,
                date,
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
