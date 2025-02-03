package cz.cvut.fel.omo.semestralka.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing various places where operations or transactions can occur.
 * These places are used to define locations for storing, producing, selling, or transporting products.
 */
@AllArgsConstructor
@Getter
public enum Place {

    MUSHROOM_LAND,
    FARM,
    WAREHOUSE,
    WAREHOUSE_FARMER,
    WAREHOUSE_PRODUCER,
    ON_SALE,
    VAN,
    SHOP,
    MANUFACTORY,
    BACKPACK;

}
