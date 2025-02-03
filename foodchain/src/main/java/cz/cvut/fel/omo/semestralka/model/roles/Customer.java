package cz.cvut.fel.omo.semestralka.model.roles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Message;
import cz.cvut.fel.omo.semestralka.model.Person;
import cz.cvut.fel.omo.semestralka.model.Storage;
import cz.cvut.fel.omo.semestralka.enums.Place;
import lombok.Getter;

import java.util.List;

@Getter
public class Customer extends Person {

    @JsonBackReference
    private final Storage storage;
    private final int WAREHOUSE_TEMPERATURE = -50;

    public Customer(String name, String phoneNumber, int wallet, List<Place> workplaces, Address workAddress) {
        super(name, phoneNumber, wallet, workplaces, workAddress);
        this.storage = new Storage(WAREHOUSE_TEMPERATURE);
    }

    public void receive(Message message) {
        System.out.println(String.format(
                "%s received message: %s",
                getName(),
                message.toString()
        ));
    }

}
