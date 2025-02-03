package cz.cvut.fel.omo.semestralka.state;

public interface ProductState {

    /**
     * Checks if the product is eatable.
     * This method should be implemented to return true if the product is safe to consume.
     * @return true if the product is eatable, false otherwise.
     */
    boolean eatable();

    /**
     * Checks if the product has expired.
     * This method should be implemented to return true if the product is no longer valid or has passed its expiration date.
     * @return true if the product has expired, false otherwise.
     */
    boolean expired();
}
