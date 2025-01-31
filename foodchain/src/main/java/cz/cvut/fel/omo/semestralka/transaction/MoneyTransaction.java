package cz.cvut.fel.omo.semestralka.transaction;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class MoneyTransaction {

    private final ProductInterface product;
    private final double productPrice;
    private final Person personFrom;
    private final Person personTo;
    private final OperationType operationType;
    private final LocalDate transactionDate;
    private final double Wallet_PersonFrom_BeforeTransaction;
    private final double Wallet_PersonTo_BeforeTransaction;
    private final double Wallet_PersonFrom_AfterTransaction;
    private final double Wallet_PersonTo_AfterTransaction;

}
