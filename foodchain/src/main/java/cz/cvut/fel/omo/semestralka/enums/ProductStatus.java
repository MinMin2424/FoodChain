package cz.cvut.fel.omo.semestralka.enums;

import lombok.Getter;

/**
 * Enum representing the various statuses of a product in the system.
 * The status indicates whether the product is available for sale, has been purchased, or is not on sale.
 */
@Getter
public enum ProductStatus {
    NOT_ON_SALE,
    ON_SALE,
    IS_ALREADY_PURCHASED;
}
