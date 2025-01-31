package cz.cvut.fel.omo.semestralka.state;

import org.apache.commons.lang3.NotImplementedException;

public class EatableProductState implements ProductState{

    @Override
    public boolean eatable() {
        System.out.println("Product is eatable. You can sell it.");
        return true;
    }

    @Override
    public boolean expired() {
        return false;
    }

}
