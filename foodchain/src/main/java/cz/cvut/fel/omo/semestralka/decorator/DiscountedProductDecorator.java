package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.model.Product;

public class DiscountedProductDecorator extends ProductDecorator{
    private final double discount;

    public DiscountedProductDecorator(Product decoratedProduct, double discount) {
        super(decoratedProduct);
        this.discount = discount;
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription() + "(Discount " + discount + "%)";
    }

    @Override
    public double getPrice() {
        return decoratedProduct.getPrice() * (1 - discount / 100);
    }

}
