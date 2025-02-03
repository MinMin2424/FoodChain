package cz.cvut.fel.omo.semestralka.state;

public class ExpiredProductState implements ProductState{

    @Override
    public boolean eatable() {
        return false;
    }

    @Override
    public boolean expired() {
//        System.out.println("Product is expired. You cannot sell it.");
        return true;
    }

}
