package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.BioProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.DiscountedProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.ProductStatus;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import cz.cvut.fel.omo.semestralka.transaction.SecurityTransaction;
import cz.cvut.fel.omo.semestralka.transaction.StorageSecurityTransaction;
import cz.cvut.fel.omo.semestralka.transaction.MoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.StorageMoneyTransaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Abstract factory class that defines methods for creating, storing, and managing products.
 * It supports operations like creating, transporting, selling, purchasing, and returning products.
 */
public abstract class AbstractFactory {

    /**
     * Stores the specified product in the storage at the given date.
     * @param product the product to be stored
     * @param date the date when the product is stored
     */
    public abstract void storeProduct(ProductInterface product, LocalDate date);

    /**
     * Creates a new product based on the provided product catalog and date.
     * @param product the product to be created
     * @param date the date of creation
     * @return the created product
     */
    public final ProductInterface createProduct(ProductsCatalogue product, LocalDate date) {
        try {
            if (!canCreateProduct()) {
                throw new UnsupportedOperationException(getPerson().getName() + " cannot create product " + product.name() + ".");
            }
            List<ProductsCatalogue> origins = getProductOrigin(product);
            if (origins == null) {
                throw new IllegalArgumentException("Origins is null. " + getPerson().getName() + " cannot create product " + product.name() + ".");
            }
            for (ProductsCatalogue origin : origins) {
                boolean found = getStorage().findProduct(origin);
                if (!found) {
                    throw new IllegalArgumentException("Origin " + origin + " not found. " + getPerson().getName() + " cannot create product " + product.name());
                }
            }
            for (ProductsCatalogue origin : origins) {
                ProductInterface productOrigin = getStorage().getProduct(origin);
                getStorage().removeProductFromStorage(productOrigin, getPerson(), Place.WAREHOUSE, Place.MANUFACTORY, date);
            }
            ProductInterface newProduct = new Product(product.name(), LocalDate.now());
            createNewTransaction(newProduct, date);
            storeProduct(newProduct, date);
            System.out.println(getPerson().getName() + " successfully created product " + newProduct.getName() + ".");
            return newProduct;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Creates a bio_product with the specified date.
     * @param product the bio_product to be created
     * @param date the date of creation
     * @return the created bio_product
     */
    public ProductInterface createProduct(BioProductDecorator product, LocalDate date) {
        try {
            if (!canCreateProduct()) {
                throw new UnsupportedOperationException(getPerson().getName() + " cannot create product " + product.getName() + ".");
            }
            if (product == null) {
                throw new IllegalArgumentException(getPerson().getName() + " cannot create product " + product.getName() + ", because product is null");
            }
            createNewTransaction(product, date);
            storeProduct(product, date);
            System.out.println(getPerson().getName() + " successfully created bio product " + product.getName() + ".");
            return product;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Creates a discounted product with the specified date.
     * @param product the discounted product to be created
     * @param date the date of creation
     * @return the created discounted product
     */
    public ProductInterface createProduct(DiscountedProductDecorator product, LocalDate date) {
        try {
            if (!canCreateProduct()) {
                throw new UnsupportedOperationException(getPerson().getName() + " cannot create product " + product.getName() + ".");
            }
            if (product == null) {
                throw new IllegalArgumentException(getPerson().getName() + " cannot create product " + product.getName() + ", because product is null");
            }
            createNewTransaction(product, date);
            storeProduct(product, date);
            System.out.println(getPerson().getName() + " successfully created bio product " + product.getName() + ".");
            return product;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Sells the specified product, setting its status to "ON SALE".
     * @param product the product to be sold
     */
    public void sellProduct(ProductInterface product) {
        try {
            if (!canSellProduct()) {
                throw new UnsupportedOperationException(getPerson().getName() + " cannot sell product " + product.getName() + ".");
            }
            checkIfProductInNotNull(product);
            boolean found = getStorage().findProduct(product);
            if (found) {
                if (product.checkExpirationDateForSale()) {
                    throw new IllegalArgumentException("Product " + product.getName() + " is expired. " + getPerson().getName() + " cannot sell product " + product.getName() + ".");
                }
                product.setProductStatus(ProductStatus.ON_SALE);
                System.out.println(getPerson().getName() + " successfully sold product " + product.getName() + ".");
            } else {
                throw new IllegalArgumentException("Product " + product.getName() + " not found. " + getPerson().getName() + " cannot sell product " + product.getName() + ".");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Transports the product from one person to another at the specified date.
     * @param product the product to be moved
     * @param distributor the person who transports the product
     * @param salesman the person who sells the product
     * @param date the date of transport
     */
    public void transportProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        if (distributor instanceof Distributor) {
            distributor.setWallet(distributor.getWallet() + OperationType.TRANSPORT.getPrice());
            getStorage().removeProduct(product);
            createNewTransaction(product, salesman, distributor, date);
        } else {
            throw new IllegalArgumentException(distributor.getName() + " cannot transport product " + product.getName());
        }
    }

    /**
     * Returns the purchased product back to the seller.
     * @param product the product to be returned
     * @param distributor the person returning the product
     * @param salesman the person who sold the product
     * @param date the date of return
     */
    public void returnProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        try {
            checkIfPersonCanReturnProduct(product);
            checkIfProductInNotNull(product);
            checkIfPersonHasProduct(product);
            addReturnTransaction(product, date);
            addTransportTransaction(product, distributor, salesman, date);
            System.out.println(getPerson().getName() + " successfully returned product " + product.getName() + ".");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Checks if the person is allowed to return the specified product.
     * @param product the product to be checked
     */
    protected void checkIfPersonCanReturnProduct(ProductInterface product) {
        if (!canReturnProduct()) {
            throw new IllegalArgumentException(getPerson().getName() + " cannot return product " + product.getName());
        }
    }

    /**
     * Checks if the product is null and throws an exception if it is.
     * @param product the product to be checked
     */
    protected void checkIfProductInNotNull(ProductInterface product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null.");
        }
    }

    /**
     * Checks if the person already has the product in their storage.
     * @param product the product to be checked
     */
    protected void checkIfPersonHasProduct(ProductInterface product) {
        if (!getStorage().findProduct(product)) {
            throw new IllegalArgumentException("Product " + product.getName() + " not found. " + getPerson().getName() + " doesn't have " + product.getName() + ".");
        }
    }

    /**
     * Adds a return transaction for the specified product.
     * @param product the product to be returned
     * @param date the date of return
     */
    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.WAREHOUSE_PRODUCER, Place.VAN, date);
    }

    /**
     * Adds a transport transaction for the specified product.
     * @param product the product to be transported
     * @param distributor the person transporting the product
     * @param salesman the person selling the product
     * @param date the date of transport
     */
    protected void addTransportTransaction(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        try {
            transportProduct(product, distributor, salesman, date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
        salesman.getStorage().addProductToStorage(product, salesman, Place.VAN, Place.WAREHOUSE_FARMER, date);
    }

    /**
     * Purchases the specified product and moves it to the buyer's storage.
     * @param product the product to be purchased
     * @param distributor the person who transports the product
     * @param salesman the person who sells the product
     * @param date the date of purchase
     */
    public void purchaseProduct(ProductInterface product, Person distributor, Person salesman, LocalDate date) {
        try {
            checkIfPersonCanPurchaseProduct(product);
            checkIfProductInNotNull(product);
            checkProductStatusForSale(product, salesman, date);
            checkIfPersonHasEnoughMoney(product);

            if (salesman.getStorage().removeProductFromStorage(product, salesman, Place.WAREHOUSE, Place.ON_SALE, date)) {
                createNewTransaction(product, salesman, product.getPrice(), date);
                transportProduct(product, distributor, salesman, date);
                storeProduct(product, date);
                salesman.setWallet(salesman.getWallet() + product.getPrice());
                getPerson().setWallet(getPerson().getWallet() - product.getPrice());
                createMoneyTransaction(product, salesman, product.getPrice(), date);
                product.setProductStatus(ProductStatus.IS_ALREADY_PURCHASED);
                System.out.println(getPerson().getName() + " successfully purchased product " + product.getName() + ".");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks if the person can purchase the specified product.
     * @param product the product to be checked
     */
    private void checkIfPersonCanPurchaseProduct(ProductInterface product) {
        if (!canPurchaseProduct()) {
            throw new UnsupportedOperationException("Cannot purchase product " + product.getName());
        }
    }

    /**
     * Checks the product's status to determine if it can be sold.
     * @param product the product to be checked
     * @param salesman the person selling the product
     * @param date the date of purchase
     */
    protected void checkProductStatusForSale(ProductInterface product, Person salesman, LocalDate date) {
        if (product.getProductStatus() == ProductStatus.NOT_ON_SALE) {
            throw new IllegalArgumentException("Product " + product.getName() + " is not on sale.");
        }
        if (product.getProductStatus() == ProductStatus.IS_ALREADY_PURCHASED) {
            SecurityTransaction securityTransaction = findSecurityTransaction(product, salesman);
            if (securityTransaction == null) {
                securityTransaction = new SecurityTransaction(product, getPerson(), salesman);
                securityTransaction.getTransactionDates().add(date);
                StorageSecurityTransaction.securityTransactions.add(securityTransaction);
            } else {
                securityTransaction.increaseAttemptCount(date);
            }
            throw new IllegalArgumentException("Product " + product.getName() + " is already purchased. " + getPerson().getName() + " cannot purchase " + product.getName() + ".");
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

    /**
     * Checks if the person has enough money to purchase the product.
     * @param product the product to be checked
     */
    protected void checkIfPersonHasEnoughMoney(ProductInterface product) {
        if (!checkWallet(product.getPrice())) {
            throw new IllegalArgumentException("Wallet has not enough money to purchase product.");
        }
    }

    /**
     * Checks if a product can be created by the current person.
     * @return true if the product can be created, false otherwise
     */
    protected boolean canCreateProduct() {
        return true;
    }

    /**
     * Checks if the current person can sell a product.
     * @return true if the person can sell the product, false otherwise
     */
    protected boolean canSellProduct() {
        return true;
    }

    /**
     * Checks if the current person can return a product.
     * @return true if the person can return the product, false otherwise
     */
    protected boolean canReturnProduct() {
        return true;
    }

    /**
     * Checks if the current person can return a product.
     * @return true if the person can return the product, false otherwise
     */
    protected boolean canPurchaseProduct() {
        return true;
    }

    /**
     * Gets the person currently working with the factory.
     * @return the person working with the factory
     */
    protected abstract Person getPerson();

    /**
     * Gets the storage associated with the current factory.
     * @return the storage associated with the factory
     */
    protected abstract Storage getStorage();

    /**
     * Gets the product origins for the specified product.
     * @param product the product to get origins for
     * @return a list of product origins
     */
    protected abstract List<ProductsCatalogue> getProductOrigin(ProductsCatalogue product);

    /**
     * Creates a money transaction for the specified product and purchase details.
     * @param product the product involved in the transaction
     * @param personFrom the person who pays for the product
     * @param price the price of the product
     * @param date the date of the transaction
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

    /**
     * Creates a new transaction for the specified product, moving it between places.
     * @param product the product involved in the transaction
     * @param movedFrom the place the product is moved from
     * @param moveTo the place the product is moved to
     * @param date the date of the transaction
     */
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
