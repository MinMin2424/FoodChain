
package cz.cvut.fel.omo.semestralka.model.enums;

import lombok.Getter;

@Getter
public enum OperationType {
    CREATE,
    STORE,
    SELL,
    TRANSPORT,
    REMOVE,
    RETURN,
    PURCHASE;
}