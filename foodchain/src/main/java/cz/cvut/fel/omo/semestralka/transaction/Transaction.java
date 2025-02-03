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
        "movedTo",
        "previousTransaction"
})
public class Transaction {

    @JsonManagedReference
    private ProductInterface product;
    private OperationType operationType;
    @JsonManagedReference
    private Person personFrom;
    @JsonManagedReference
    private Person personTo;
    private Place movedFrom;
    private Place movedTo;
    private LocalDate transactionDate;
    private double price;
    @JsonIgnore
    private Transaction previousTransaction;

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

    public void setProduct(ProductInterface product) {
        System.out.println("Modification of product is not allowed.");
        addModificationSecurityInStorage("Set product");
    }

    public void setOperationType(OperationType operationType) {
        System.out.println("Modification of operation type is not allowed.");
        addModificationSecurityInStorage("Set operation type");
    }

    public void setPersonFrom(Person personFrom) {
        System.out.println("Modification of person from is not allowed.");
        addModificationSecurityInStorage("Set person from");
    }

    public void setPersonTo(Person personTo) {
        System.out.println("Modification of person to is not allowed.");
        addModificationSecurityInStorage("Set person to");
    }

    public void setMovedFrom(Place movedFrom) {
        System.out.println("Modification of moved from is not allowed.");
        addModificationSecurityInStorage("Set moved from");
    }

    public void setMovedTo(Place movedTo) {
        System.out.println("Modification of moved to is not allowed.");
        addModificationSecurityInStorage("Set moved from");
    }

    public void setTransactionDate(LocalDate transactionDate) {
        System.out.println("Modification of transaction date is not allowed.");
        addModificationSecurityInStorage("Set transaction date");
    }

    public void setPrice(double price) {
        System.out.println("Modification of price is not allowed.");
        addModificationSecurityInStorage("Set price");
    }

    public void setPreviousTransaction(Transaction previousTransaction) {
        System.out.println("Modification of previous transaction is not allowed.");
        addModificationSecurityInStorage("Set previous transaction");
    }

    private void addModificationSecurityInStorage(String dataType) {
        StorageModificationSecurityTransaction.securityTransactions.add(
                new ModificationSecurityTransaction(
                        this.product,
                        this.personFrom,
                        dataType
                )
        );
    }
}
