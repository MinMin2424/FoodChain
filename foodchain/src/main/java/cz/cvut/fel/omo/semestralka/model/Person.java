package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.model.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public abstract class Person {

    private String name;
    private String phoneNumber;
    private double wallet;
    private List<Place> workplaces;
    private Address workAddress;

    protected abstract Storage getStorage();

}
