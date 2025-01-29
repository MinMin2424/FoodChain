package cz.cvut.fel.omo.semestralka.model.transaction;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class Transaction {

    private final Product product;
    private final Place movedFrom;
    private final Place movedTo;
    private final OperationType operationType;
    private final LocalDate transactionDate;
    private final double price;

    private Transaction previousTransaction;

}
