package cz.cvut.fel.omo.semestralka.transaction;

import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Transaction {

    private final Product product;
    private final Person personFrom;
    private final Person personTo;
    private final Place movedFrom;
    private final Place movedTo;
    private final OperationType operationType;
    private final LocalDate transactionDate;
    private final double price;
    private final Transaction previousTransaction;

    // Transaction for STORE, REMOVE, RETURN
    public Transaction(Product product, Person personFrom, Place movedFrom, Place movedTo, OperationType operationType, LocalDate transactionDate, double price, Transaction previousTransaction) {
        this.product = product;
        this.personFrom = personFrom;
        this.personTo = null;
        this.movedFrom = movedFrom;
        this.movedTo = movedTo;
        this.operationType = operationType;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }

    // Transaction for PURCHASE AND TRANSPORT
    public Transaction(Product product, Person personFrom, Person personTo, OperationType operationType, LocalDate transactionDate, double price, Transaction previousTransaction) {
        this.product = product;
        this.personFrom = personFrom;
        this.personTo = personTo;
        this.movedFrom = null;
        this.movedTo = null;
        this.operationType = operationType;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }

    // Transaction for CREATE, SELL
    public Transaction(Product product, Person person, OperationType operationType, LocalDate transactionDate, double price, Transaction previousTransaction) {
        this.product = product;
        this.personFrom = person;
        this.personTo = null;
        this.movedFrom = null;
        this.movedTo = null;
        this.operationType = operationType;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }
}
