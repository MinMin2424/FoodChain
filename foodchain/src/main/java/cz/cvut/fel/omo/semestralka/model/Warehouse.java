package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.model.enums.ProductsCatalogue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Warehouse {

    private int temperature;
    private List<Product> productList;

    public Warehouse(int temperature) {
        this.temperature = temperature;
        this.productList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            return;
        }

        boolean found = false;
        for (Product p : productList) {
            if (p.getName().equals(product.getName())) {

                p.setQuantity(p.getQuantity() + product.getQuantity());
                found = true;

                if (!checkTemperature(ProductsCatalogue.getTemperatureByName(product.getName()), getTemperature())) {
                    System.out.println("Wrong temperature");
                }
                break;
            }
        }
        if (!found) {
            productList.add(product);
        }
    }

    public void removeProduct(Product product, int removeQuantity) {
        if (product == null) {
            return;
        }

        for (int i = 0; i < productList.size(); i++) {
            Product currentProduct = productList.get(i);
            if (currentProduct.getName().equals(product.getName())) {
                if (currentProduct.getQuantity() > removeQuantity) {
                    currentProduct.setQuantity(currentProduct.getQuantity() - removeQuantity);
                } else if (currentProduct.getQuantity() == removeQuantity) {
                    productList.remove(i);
                } else {
                    System.out.println("Not enough product to remove");
                }
                break;
            }
        }
    }

    private boolean checkTemperature(int productTemperature, int warehouseTemperature) {
        return productTemperature <= warehouseTemperature;
    }
}

