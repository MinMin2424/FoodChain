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

//        for (Product p : productList) {
//            if (p.getName().equals(product.getName())) {
//
//                if (checkTemperature(ProductsCatalogue.getTemperatureByName(product.getName()), getTemperature())) {
//                    p.setQuantity(p.getQuantity() + product.getQuantity());
//                    found = true;
//                    System.out.println("ALREADY EXISTING PRODUCT: " + product.getName() + " has been added to the list");
//                } else {
//                    System.out.println("NOPE");
//                    throw new IllegalArgumentException("Product cannot be added to the list because of temperature.");
//                }
//                break;
//            }
//        }

        if (checkTemperature(ProductsCatalogue.getTemperatureByName(product.getName()), getTemperature())) {
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
//                    if (currentProduct.getQuantity() > count) {
//                        currentProduct.setQuantity(currentProduct.getQuantity() - count);
//                        count = 0;
//                    } else if (currentProduct.getQuantity() == count) {
//                        productList.remove(i);
//                        count = 0;
//                    } else {
//                        count -= currentProduct.getQuantity();
//                        productList.remove(i);
//                    }
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
     * @param productName name of the searched product
     * @return whether is the product in the warehouse
     */
    public boolean findProduct(String productName) {
        boolean found = false;
        for (Product product : productList) {
            if (product.getName().equals(productName)) {
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

    public Product getProductByName(String productName) {
        for (Product product : productList) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

}
