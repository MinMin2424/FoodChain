package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;

import static cz.cvut.fel.omo.semestralka.model.enums.Place.*;

public class CustomerFactory implements Factory{
    private final Customer customer;
    public CustomerFactory(Customer customer){this.customer = customer;}

    /**
     * Executes specific function
     * @param productName name of the product
     * @param sellQuantity quantity of the product
     * @param operationType name of the function to be executed
     */
    @Override
    public void executeOperation(String productName, int sellQuantity, OperationType operationType) {
        switch (operationType) {
            case STORE:
                storeProduct(productName);
                break;
            case PURCHASE:
                //to do
                break;
            case RETURN:
                //to do
                break;
        }
    }

    /**
     * Adds product to customers storage
     * @param productName name of the product
     */
    @Override
    public void storeProduct(String productName) {
        Product product = customer.getStorage().getProductByName(productName);
        customer.getStorage().addProductToStorage(product, customer, SHOP, BACKPACK);
    }
}
