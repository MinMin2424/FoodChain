package cz.cvut.fel.omo.semestralka.decorator;

public class DiscountedProductDecorator extends ProductDecorator{
    private final double discount;

    public DiscountedProductDecorator(ProductInterface decoratedProduct, double discount) {
        super(decoratedProduct);
        this.discount = discount;
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription() + "(Discount_" + discount + "%)";
    }

    @Override
    public double getPrice() {
        return decoratedProduct.getPrice() * (1 - discount / 100);
    }

}
