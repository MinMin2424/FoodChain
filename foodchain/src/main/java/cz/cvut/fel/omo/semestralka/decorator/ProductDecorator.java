package cz.cvut.fel.omo.semestralka.decorator;

import cz.cvut.fel.omo.semestralka.model.Product;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ProductDecorator implements ProductInterface {
    protected Product decoratedProduct;
}
