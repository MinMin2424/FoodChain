package cz.cvut.fel.omo.semestralka.model.factory;

import cz.cvut.fel.omo.semestralka.model.enums.OperationType;

public interface Factory {
    void executeOperation(String productName, int sellQuantity, OperationType operationType);
    void storeProduct(String productName);
}

