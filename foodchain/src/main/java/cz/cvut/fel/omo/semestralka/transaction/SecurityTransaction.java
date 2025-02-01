package cz.cvut.fel.omo.semestralka.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.Person;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SecurityTransaction {

    @JsonManagedReference
    private final ProductInterface product;
    private final OperationType operationType;
    private final Person attemptedBuyer;
    private final Person originOwner;
    private int attemptCount;
    private final List<LocalDate> transactionDates;

    public SecurityTransaction(ProductInterface product,
                               Person attemptedBuyer,
                               Person originOwner) {
        this.product = product;
        this.operationType = OperationType.PURCHASE;
        this.attemptedBuyer = attemptedBuyer;
        this.originOwner = originOwner;
        this.attemptCount = 1;
        this.transactionDates = new ArrayList<>();
        this.transactionDates.add(LocalDate.now());
    }

    public void increaseAttemptCount(LocalDate transactionDate) {
        this.attemptCount++;
        this.transactionDates.add(transactionDate);
    }
}
