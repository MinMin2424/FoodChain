package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Storage {

    private int temperature;
    private List<ProductInterface> productList;

    public Storage(int temperature) {
        this.temperature = temperature;
        this.productList = new ArrayList<>();
    }

    /**
     * Adds a product to the storage and records the transaction history.
     * @param product   Product to be added
     * @param person    Person executing the operation
     * @param movedFrom Place from where the product is moved
     * @param movedTo   Place where the product is being stored
     * @param date      Date of the transaction
     */
    public void addProductToStorage(ProductInterface product, Person person, Place movedFrom, Place movedTo, LocalDate date) {
        boolean isProductAdded = addProduct(product);
        if (isProductAdded) {
            Transaction transaction = new Transaction(
                    product,
                    person,
                    movedFrom,
                    movedTo,
                    OperationType.STORE,
                    date,
                    0,
                    product.getLastTransaction()
            );
            product.addTransaction(transaction);
        }
    }

    /**
     * Removes a product from the storage and records the transaction history.
     * @param product   Product to be removed
     * @param person    Person executing the operation
     * @param movedFrom Place from where the product is removed
     * @param movedTo   Place where the product is moved to
     * @param date      Date of the transaction
     * @return True if the product was successfully removed, false otherwise
     */
    public boolean removeProductFromStorage(ProductInterface product, Person person, Place movedFrom, Place movedTo, LocalDate date) {
        boolean isProductRemoved = removeProduct(product);
        if (isProductRemoved) {
            Transaction transaction = new Transaction(
                    product,
                    person,
                    movedFrom,
                    movedTo,
                    OperationType.REMOVE,
                    date,
                    0,
                    product.getLastTransaction()
            );
            product.addTransaction(transaction);
        }
        return isProductRemoved;
    }

    /**
     * Adds a product to the storage if the temperature requirements are met.
     * @param product Product to be added
     * @return True if the product was added successfully, otherwise throws an exception
     */
    private boolean addProduct(ProductInterface product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        boolean isProductAdded = false;
        if (checkTemperature(product.getTemperature(), getTemperature())) {
            productList.add(product);
            isProductAdded = true;
        } else {
            throw new IllegalArgumentException("Product cannot be added to the list because of temperature.");
        }
        return isProductAdded;
    }

    /**
     * Removes a product from the storage if it exists.
     * @param product Product to be removed
     * @return True if the product was removed successfully, false otherwise
     */
    public boolean removeProduct(ProductInterface product) {
        boolean isRemoved = false;
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        for (int i = 0; i < productList.size(); i++) {
            ProductInterface currentProduct = productList.get(i);
            if (currentProduct.equals(product)) {
                productList.remove(i);
                isRemoved = true;
                break;
            }
        }
        return isRemoved;
    }

    /**
     * Checks if the product's required temperature is compatible with the storage temperature.
     * @param productTemperature    Product's required storage temperature
     * @param warehouseTemperature  Storage's current temperature
     * @return True if the product temperature is greater than or equal to storage temperature
     */
    private boolean checkTemperature(int productTemperature, int warehouseTemperature) {
        return productTemperature >= warehouseTemperature;
    }

    /**
     * Finds a product in the storage by its catalogue name.
     * @param product Product to find
     * @return True if the product exists in the storage, false otherwise
     */
    public boolean findProduct(ProductsCatalogue product) {
        boolean found = false;
        for (ProductInterface productInStorage : productList) {
            if (productInStorage.getName().equals(product.name())) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Finds a product in the storage by its object reference.
     * @param product Product to find
     * @return True if the product exists in the storage, false otherwise
     */
    public boolean findProduct(ProductInterface product) {
        boolean found = false;
        for (ProductInterface productInStorage : productList) {
            if (productInStorage.equals(product)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Counts the total number of products currently stored.
     * @return Total number of products in the storage
     */
    public int countFullness() {
        int fullness = 0;
        for (ProductInterface product : productList) {
            fullness += 1;
        }
        return fullness;
    }

    /**
     * Retrieves a product from the storage based on its catalogue name.
     * @param product Product catalogue reference
     * @return The product if found, otherwise null
     */
    public ProductInterface getProduct(ProductsCatalogue product) {
        for (ProductInterface productInStorage : productList) {
            if (productInStorage.getName().equals(product.name())) {
                return productInStorage;
            }
        }
        return null;
    }

    /**
     * Retrieves a product from the storage based on its object reference.
     * @param product Product object reference
     * @return The product if found, otherwise null
     */
    public ProductInterface getProduct(ProductInterface product) {
        for (ProductInterface productInStorage : productList) {
            if (productInStorage.equals(product)) {
                return productInStorage;
            }
        }
        return null;
    }

}
