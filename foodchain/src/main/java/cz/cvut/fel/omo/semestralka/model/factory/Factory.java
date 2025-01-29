package cz.cvut.fel.omo.semestralka.model.factory;
import cz.cvut.fel.omo.semestralka.model.Product;

public interface Factory {
    void executeOperation(String productName, OperationType type);
}

