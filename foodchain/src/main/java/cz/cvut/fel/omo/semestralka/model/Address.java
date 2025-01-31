package cz.cvut.fel.omo.semestralka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Address {

    private String street;
    private String city;
    private String zipCode;
    private String country;
}
