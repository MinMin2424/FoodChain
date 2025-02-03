package cz.cvut.fel.omo.semestralka.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationType {
    CREATE(0),
    STORE(0),
    SELL(0),
    TRANSPORT(100),
    REMOVE(0),
    RETURN(0),
    PURCHASE(0),
    MODIFICATION_DATA_TRANSACTION(0);

    private final double price;
}
