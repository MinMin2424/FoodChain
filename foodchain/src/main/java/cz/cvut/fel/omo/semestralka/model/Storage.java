package cz.cvut.fel.omo.semestralka.model;

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
    private List<Product> productList;

    public Storage(int temperature) {
        this.temperature = temperature;
        this.productList = new ArrayList<>();
    }

    /**
     * Adds product to persons storage and saves information about the transaction
     * @param product product
     * @param person person executing operation
     * @param movedFrom where we are moving product from
     * @param movedTo where we are moving product to
     */
    public void addProductToStorage(Product product, Person person, Place movedFrom, Place movedTo) {
        addProduct(product);
        Transaction transaction = new Transaction(
                product,
                person,
                movedFrom,
                movedTo,
                OperationType.STORE,
                LocalDate.now(),
                0,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    /**
     * Removes product from persons storage and saves information about the transaction
     * @param product Product
     * @param person Person executing operation
     * @param movedFrom Place where we are moving product from
     * @param movedTo Place where we are moving product to
     */
    public void removeProductFromStorage(Product product, Person person, Place movedFrom, Place movedTo) {
        removeProduct(product);
        Transaction transaction = new Transaction(
                product,
                person,
                movedFrom,
                movedTo,
                OperationType.REMOVE,
                LocalDate.now(),
                0,
                product.getLastTransaction()
        );
        product.addTransaction(transaction);
    }

    /**
     * Adds product to the warehouse
     * @param product product to be added
     */
    private void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (checkTemperature(product.getTemperature(), getTemperature())) {
            productList.add(product);
            System.out.println("NEW PRODUCT " + product.getName() + " has been added to the list");
        } else {
            System.out.println("NOPE");
            throw new IllegalArgumentException("Product cannot be added to the list because of temperature.");
        }
    }

    /**
     * Removes product from the warehouse
     * @param product product to be removed
     */
    private void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        for (int i = 0; i < productList.size(); i++) {
            Product currentProduct = productList.get(i);
            if (currentProduct.getName().equals(product.getName())) {
                productList.remove(i);
                break;
            }
        }
    }

    /**
     * Compares warehouse temperature with products storing temperature
     * @param productTemperature payment information separated by semicolon
     * @param warehouseTemperature IP address of the sender
     * @return if product temperature is higher than temperature in warehouse
     */
    private boolean checkTemperature(int productTemperature, int warehouseTemperature) {
        return productTemperature >= warehouseTemperature;
    }

    /**
     * Checks for product in the warehouse
     * @param product the searched product
     * @return whether is the product in the warehouse
     */
    public boolean findProduct(ProductsCatalogue product) {
        boolean found = false;
        for (Product productInStorage : productList) {
            if (productInStorage.getName().equals(product.name())) {
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean findProduct(Product product) {
        boolean found = false;
        for (Product productInStorage : productList) {
            if (productInStorage.equals(product)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public List<Product> getAllProducts(String productName) {
        List<Product> products = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().equals(productName)) {
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Counts and adds number of quantities of each product in warehouse
     * @return the number of all items in warehouse
     */
    public int countFullness() {
        int fullness = 0;
        for (Product product : productList) {
            fullness += 1;
        }
        return fullness;
    }

    /**
     * Writes out each product and its quantity in warehouse
     */
    public void getStorageInventory(){
        StringBuilder warehouseInventory = new StringBuilder();
        warehouseInventory.append("This storage contains: \n");
        for (Product product : productList) {
            warehouseInventory.append(product.getName()).append("\n");

        }
        System.out.println(warehouseInventory);
    }

    /**
     * Finds product by name
     * @param product the searched product
     * @return Product
     */
    public Product getProduct(ProductsCatalogue product) {
        for (Product productInStorage : productList) {
            if (productInStorage.getName().equals(product.name())) {
                return productInStorage;
            }
        }
        return null;
    }

}
