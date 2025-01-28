package cz.cvut.fel.omo.semestralka.model;

import cz.cvut.fel.omo.semestralka.model.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Person {

    private String name;
    private String phoneNumber;
    private int wallet;
    private List<Place> workplaces;
    private Address workAddress;

}

