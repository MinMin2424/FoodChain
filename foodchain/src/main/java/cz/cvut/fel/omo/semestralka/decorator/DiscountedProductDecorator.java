package cz.cvut.fel.omo.semestralka.decorator;

public class DiscountedProductDecorator extends ProductDecorator{
    private final double discount;

    public DiscountedProductDecorator(ProductInterface decoratedProduct, double discount) {
        super(decoratedProduct);
        this.discount = discount;
    }

    /**
     * Adds the discount percentage to the product's description to indicate the applied discount.
     * @return the updated description of the product with the discount information.
     */
    @Override
    public String getDescription() {
        return decoratedProduct.getDescription() + "(Discount_" + discount + "%)";
    }

    /**
     * Reduces the price of the product based on the specified discount percentage.
     * @return the discounted price of the product.
     */
    @Override
    public double getPrice() {
        return decoratedProduct.getPrice() * (1 - discount / 100);
    }

}
