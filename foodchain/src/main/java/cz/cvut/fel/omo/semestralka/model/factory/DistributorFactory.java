package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;

import static cz.cvut.fel.omo.semestralka.model.enums.Place.*;

public class DistributorFactory implements Factory {
    private final Distributor distributor;
    public DistributorFactory(Distributor distributor){this.distributor = distributor;}

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
            case TRANSPORT:
                //TO DO
                break;
        }
    }

    /**
     * Adds product to distributors storage
     * @param productName name of the product
     */
    @Override
    public void storeProduct(String productName) {
        Product product = distributor.getStorage().getProductByName(productName);
        distributor.getStorage().addProductToStorage(product, distributor, WAREHOUSE, VAN);
        //JDE TU DEFINOVAT PRESNE ODKOD KAM? NEMA DISTRIBUTOR RUYNA MISTA ODKUD?
    }

}
