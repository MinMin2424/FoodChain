package cz.cvut.fel.omo.semestralka.decorator;

public class BioProductDecorator extends ProductDecorator {

    public BioProductDecorator(ProductInterface decoratedProduct) {
        super(decoratedProduct);
    }

    @Override
    public String getDescription() {
        return decoratedProduct.getDescription() + "(Bio)";
    }

    @Override
    public double getPrice() {
        return decoratedProduct.getPrice() * 1.5;
    }
}
