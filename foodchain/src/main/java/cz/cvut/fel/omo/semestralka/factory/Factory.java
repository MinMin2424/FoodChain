package cz.cvut.fel.omo.semestralka.factory;

import cz.cvut.fel.omo.semestralka.enums.OperationType;

public interface Factory {
    void executeOperation(String productName, int sellQuantity, OperationType operationType);
    void storeProduct(String productName);
}

