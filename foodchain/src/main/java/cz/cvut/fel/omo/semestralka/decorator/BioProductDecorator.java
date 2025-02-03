package cz.cvut.fel.omo.semestralka.decorator;

public class BioProductDecorator extends ProductDecorator {

    public BioProductDecorator(ProductInterface decoratedProduct) {
        super(decoratedProduct);
    }

    /**
     * Adds 'Bio' to the product's description to indicate its bio nature.
     * @return the updated description of the product
     */
    @Override
    public String getDescription() {
        return decoratedProduct.getDescription() + "(Bio)";
    }

    /**
     * Increases the price of the product by 50% to reflect its bio quality.
     * @return the adjusted price of the product.
     */
    @Override
    public double getPrice() {
        return decoratedProduct.getPrice() * 1.5;
    }
}
