package cz.cvut.fel.omo.semestralka.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
import cz.cvut.fel.omo.semestralka.model.Person;
import lombok.Getter;

@Getter
public class ModificationSecurityTransaction {

    @JsonManagedReference
    private final ProductInterface product;
    private final OperationType operationType;
    private final Person person;
    private final String dataType;

    public ModificationSecurityTransaction(ProductInterface product,
                                           Person person, String dataType) {
        this.product = product;
        this.operationType = OperationType.MODIFICATION_DATA_TRANSACTION;
        this.person = person;
        this.dataType = dataType;
    }
}
