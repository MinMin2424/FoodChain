package cz.cvut.fel.omo.semestralka.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonPropertyOrder({
        "product",
        "productPrice",
        "personFrom",
        "personTo",
        "operationType",
        "transactionDate",
        "wallet_PersonFrom_BeforeTransaction",
        "wallet_PersonTo_BeforeTransaction",
        "wallet_PersonFrom_AfterTransaction",
        "wallet_PersonTo_AfterTransaction"
})
public class MoneyTransaction {

    @JsonManagedReference
    private final ProductInterface product;
    private final OperationType operationType;
    private final double productPrice;
    private final Person personFrom;
    private final Person personTo;
    private final LocalDate transactionDate;
    private final double wallet_PersonFrom_BeforeTransaction;
    private final double wallet_PersonTo_BeforeTransaction;
    private final double wallet_PersonFrom_AfterTransaction;
    private final double wallet_PersonTo_AfterTransaction;


    public MoneyTransaction(ProductInterface product,
                            double productPrice,
                            Person personFrom,
                            Person personTo,
                            OperationType operationType,
                            LocalDate transactionDate,
                            double walletPersonFromBeforeTransaction,
                            double walletPersonToBeforeTransaction,
                            double walletPersonFromAfterTransaction,
                            double walletPersonToAfterTransaction) {

        this.product = product;
        this.operationType = operationType;
        this.productPrice = productPrice;
        this.personFrom = personFrom;
        this.personTo = personTo;
        this.transactionDate = transactionDate;
        wallet_PersonFrom_BeforeTransaction = walletPersonFromBeforeTransaction;
        wallet_PersonTo_BeforeTransaction = walletPersonToBeforeTransaction;
        wallet_PersonFrom_AfterTransaction = walletPersonFromAfterTransaction;
        wallet_PersonTo_AfterTransaction = walletPersonToAfterTransaction;
    }
}
