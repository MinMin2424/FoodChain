package cz.cvut.fel.omo.semestralka.model.roles;

import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Warehouse;
import cz.cvut.fel.omo.semestralka.model.enums.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class ShopOwner extends Person {

    private final Warehouse warehouse;
    private final int WAREHOUSE_TEMPERATURE = 5;

    public ShopOwner(String name, String phoneNumber, int wallet, List<Place> workplaces, Address workAddress) {
        super(name, phoneNumber, wallet, workplaces, workAddress);
        this.warehouse = new Warehouse(WAREHOUSE_TEMPERATURE);
    }

}
