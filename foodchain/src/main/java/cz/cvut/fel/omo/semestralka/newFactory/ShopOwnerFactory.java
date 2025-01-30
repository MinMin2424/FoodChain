package cz.cvut.fel.omo.semestralka.newFactory;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;

import java.util.List;

import static cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue.getPriceByName;

public class ShopOwnerFactory extends AbstractFactory{

    private final ShopOwner shopOwner;

    public ShopOwnerFactory(ShopOwner shopOwner) {
        this.shopOwner = shopOwner;
    }

    @Override
    public void storeProduct(Product product) {
        getStorage().addProductToStorage(product, shopOwner, Place.VAN, Place.SHOP);
    }

    @Override
    protected boolean canTransportProduct() {
        return false;
    }

    @Override
    public void returnProduct(String productName, int returnQuantity) {
        if (!canReturnProduct()) {
            throw new UnsupportedOperationException("Cannot return product " + productName);
        }
        Product product = getStorage().getProductByName(productName);
        if (product == null) {
            throw new IllegalArgumentException("Product " + productName + " not found. Cannot return product " + productName);
        }
        createNewTransaction(product, Place.SHOP, Place.VAN, OperationType.RETURN, returnQuantity);
        createNewTransaction(product, Place.VAN, Place.WAREHOUSE_PRODUCER, OperationType.TRANSPORT, returnQuantity);
    }

    @Override
    protected Person getPerson() {
        return shopOwner;
    }

    @Override
    protected Storage getStorage() {
        return shopOwner.getStorage();
    }

    @Override
    protected boolean canCreateProduct() {
        return false;
    }

    @Override
    protected List<String> getProductOrigin(String productName) {
        return null;
    }
}
