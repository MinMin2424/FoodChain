package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import cz.cvut.fel.omo.semestralka.model.transaction.Transaction;

import java.time.LocalDate;

import static cz.cvut.fel.omo.semestralka.model.enums.OperationType.SELL;
import static cz.cvut.fel.omo.semestralka.model.enums.OperationType.TRANSPORT;
import static cz.cvut.fel.omo.semestralka.model.enums.Place.*;
import static cz.cvut.fel.omo.semestralka.model.enums.Place.WAREHOUSE;
import static cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue.getPriceByName;

public class ShopOwnerFactory implements Factory{
    private final ShopOwner shopOwner;
    public ShopOwnerFactory(ShopOwner shopOwner){this.shopOwner = shopOwner;}

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
            case SELL:
                sellProduct(productName, sellQuantity);
                break;
            case REMOVE:
                //to do
                break;
            case RETURN:
                //to do
                break;
        }
    }

    /**
     * Adds product to shop owners storage
     * @param productName name of the product
     */
    @Override
    public void storeProduct(String productName) {
        Product product = shopOwner.getStorage().getProductByName(productName);
        shopOwner.getStorage().addProductToStorage(product, shopOwner, WAREHOUSE, SHOP);
    }

    /**
     * Subtracts quantity of said product from producers storage and transfers it to van and then to shop oners warehouse
     * @param productName name of the product
     * @param sellQuantity quantity of the product to be sold
     */
    private void sellProduct(String productName, int sellQuantity) {
        Product product = shopOwner.getStorage().getProductByName(productName);
        if (product == null) {
            return;
        }
        shopOwner.getStorage().removeProductFromStorage(product, shopOwner, WAREHOUSE, VAN, sellQuantity);
        Transaction sellTransaction = new Transaction(product, shopOwner, PLACE_OF_SOLD, PLACE_OF_SOLD, SELL, LocalDate.now(), getPriceByName(productName), product.getLastTransaction());
        product.addTransaction(sellTransaction);
        Transaction transportTransaction = new Transaction(product, shopOwner, VAN, WAREHOUSE, TRANSPORT, LocalDate.now(), TRANSPORT.getPrice(), product.getLastTransaction());
        product.addTransaction(transportTransaction);
    }
}
