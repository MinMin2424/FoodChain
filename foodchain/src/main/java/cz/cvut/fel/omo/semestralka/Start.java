package cz.cvut.fel.omo.semestralka;

import cz.cvut.fel.omo.semestralka.configuration.BasicConfiguration;
import cz.cvut.fel.omo.semestralka.configuration.Configuration;
import cz.cvut.fel.omo.semestralka.configuration.ErrorHandlingConfiguration;
import cz.cvut.fel.omo.semestralka.decorator.BioProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.DiscountedProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.factory.CustomerFactory;
import cz.cvut.fel.omo.semestralka.factory.ShopOwnerFactory;
import cz.cvut.fel.omo.semestralka.report.*;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.model.roles.*;
import cz.cvut.fel.omo.semestralka.factory.FarmerFactory;
import cz.cvut.fel.omo.semestralka.factory.ProducerFactory;
import cz.cvut.fel.omo.semestralka.transaction.StorageMoneyTransaction;
import cz.cvut.fel.omo.semestralka.transaction.StorageSecurityTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Start {

    public static void main(String[] args) {

        Configuration configuration = new BasicConfiguration();
        configuration.run();

    }
}