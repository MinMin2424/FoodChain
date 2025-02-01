package cz.cvut.fel.omo.semestralka.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.enums.Place;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonPropertyOrder({
        "product",
        "operationType",
        "transactionDate",
        "price",
        "personFrom",
        "personTo",
        "movedFrom",
        "movedTo"
})
public class Transaction {

    @JsonManagedReference
    private final ProductInterface product;
    private final OperationType operationType;
    @JsonManagedReference
    private final Person personFrom;
    @JsonManagedReference
    private final Person personTo;
    private final Place movedFrom;
    private final Place movedTo;
    private final LocalDate transactionDate;
    private final double price;
    @JsonIgnore
    private final Transaction previousTransaction;

    // Transaction for STORE, REMOVE, RETURN
    public Transaction(ProductInterface product,
                       Person personFrom,
                       Place movedFrom,
                       Place movedTo,
                       OperationType operationType,
                       LocalDate transactionDate,
                       double price,
                       Transaction previousTransaction) {

        this.product = product;
        this.operationType = operationType;
        this.personFrom = personFrom;
        this.personTo = null;
        this.movedFrom = movedFrom;
        this.movedTo = movedTo;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }

    // Transaction for PURCHASE AND TRANSPORT
    public Transaction(ProductInterface product,
                       Person personFrom,
                       Person personTo,
                       OperationType operationType,
                       LocalDate transactionDate,
                       double price,
                       Transaction previousTransaction) {

        this.product = product;
        this.operationType = operationType;
        this.personFrom = personFrom;
        this.personTo = personTo;
        this.movedFrom = null;
        this.movedTo = null;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }

    // Transaction for CREATE, SELL
    public Transaction(ProductInterface product,
                       Person person,
                       OperationType operationType,
                       LocalDate transactionDate,
                       double price,
                       Transaction previousTransaction) {

        this.product = product;
        this.operationType = operationType;
        this.personFrom = person;
        this.personTo = null;
        this.movedFrom = null;
        this.movedTo = null;
        this.transactionDate = transactionDate;
        this.price = price;
        this.previousTransaction = previousTransaction;
    }
}
