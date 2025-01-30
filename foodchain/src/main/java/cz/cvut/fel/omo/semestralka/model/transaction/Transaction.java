package cz.cvut.fel.omo.semestralka.model.transaction;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Transaction {

    private final Product product;
    private final Person personFrom;
    private final Place movedFrom;
    private final Place movedTo;
    private final OperationType operationType;
    private final LocalDate transactionDate;
    private final double price;
    private final Transaction previousTransaction;


    public Transaction(Product product, Person personFrom, Place movedFrom, Place movedTo, OperationType operationType, LocalDate transactionDate, double price, Transaction previousTransaction) {
        this.product = product;
        this.personFrom = personFrom;
        this.movedFrom = movedFrom;
        this.movedTo = movedTo;
        this.operationType = operationType;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }

    public Transaction(Product product, Person person, Place movedTo, OperationType operationType, LocalDate transactionDate, double price, Transaction previousTransaction) {
        this.product = product;
        this.personFrom = person;
        this.movedFrom = null;
        this.movedTo = movedTo;
        this.operationType = operationType;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }

    public Transaction(Product product, Person person, OperationType operationType, LocalDate transactionDate, double price, Transaction previousTransaction) {
        this.product = product;
        this.personFrom = person;
        this.movedFrom = null;
        this.movedTo = null;
        this.operationType = operationType;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }
}
