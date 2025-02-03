package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.decorator.BioProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.DiscountedProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.ProductDecorator;
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

public abstract class AbstractFactory {

    public abstract void storeProduct(ProductInterface product, LocalDate date);

    /**
     * Creates specified product
     * @param product the product to be created
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
     * Removes product from persons storage and puts it on sale
     * @param product product to be sold
     * @return sold product
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
     * moves product from one place to another
     * @param product Product to be moved
     * @param distributor Person who moves the product
     * @param salesman Person who sells product
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
     * Returns bought product back to seller
     * @param product the product
     * @param distributor Person who moves the product
     * @param salesman Person who sold the product
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

    protected void checkIfPersonCanReturnProduct(ProductInterface product) {
        if (!canReturnProduct()) {
            throw new IllegalArgumentException(getPerson().getName() + " cannot return product " + product.getName());
        }
    }

    protected void checkIfProductInNotNull(ProductInterface product) {
        if (product == null) {
            throw new IllegalArgumentException("Product is null.");
        }
    }

    protected void checkIfPersonHasProduct(ProductInterface product) {
        if (!getStorage().findProduct(product)) {
            throw new IllegalArgumentException("Product " + product.getName() + " not found. " + getPerson().getName() + " doesn't have " + product.getName() + ".");
        }
    }

    protected void addReturnTransaction(ProductInterface product, LocalDate date) {
        createNewTransaction(product, Place.WAREHOUSE_PRODUCER, Place.VAN, date);
    }

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
     * Product gets moved to buyers storage and seller gets paid
     * @param product Product to be bought
     * @param distributor Person who moves the product
     * @param salesman Person who sells the product
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

    private void checkIfPersonCanPurchaseProduct(ProductInterface product) {
        if (!canPurchaseProduct()) {
            throw new UnsupportedOperationException("Cannot purchase product " + product.getName());
        }
    }

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

    protected void checkIfPersonHasEnoughMoney(ProductInterface product) {
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
