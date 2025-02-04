package cz.cvut.fel.omo.semestralka;

import cz.cvut.fel.omo.semestralka.configuration.BasicConfiguration;
import cz.cvut.fel.omo.semestralka.configuration.Configuration;

public class Start {

    public static void main(String[] args) {

        Configuration configuration = new BasicConfiguration();
        configuration.run();

    }
}