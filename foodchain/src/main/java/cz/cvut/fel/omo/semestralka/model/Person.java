package cz.cvut.fel.omo.semestralka.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.fel.omo.semestralka.enums.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"phoneNumber", "wallet", "workplaces", "workAddress"})
public abstract class Person {

    private String name;
    private String phoneNumber;
    private double wallet;
    private List<Place> workplaces;
    private Address workAddress;

    public abstract Storage getStorage();

}
