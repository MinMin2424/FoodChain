package cz.cvut.fel.omo.semestralka.model;

import com.github.javafaker.Faker;
import cz.cvut.fel.omo.semestralka.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@AllArgsConstructor
@Getter
@Setter
public class Address {

    private String street;
    private String city;
    private String zipCode;
    private String country;

    private static Faker faker = new Faker(new Random(Constants.RANDOM_SEED));

    /**
     * Generates a random address using the Faker library.
     * The generated address includes a random street, city, ZIP code,
     * and a fixed country value ("Czechia").
     * @return a randomly generated Address object.
     */
    public static Address generateRandomAddress() {
        com.github.javafaker.Address randomAddress = faker.address();
        String street = randomAddress.streetAddress();
        String city = randomAddress.city();
        String zipCode = randomAddress.zipCode();
        String country = "Czechia";

        return new Address(street, city, zipCode, country);
    }
}
