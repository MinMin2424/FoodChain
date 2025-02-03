package cz.cvut.fel.omo.semestralka.configuration;

import cz.cvut.fel.omo.semestralka.decorator.BioProductDecorator;
import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.Place;
import cz.cvut.fel.omo.semestralka.enums.ProductsCatalogue;
import cz.cvut.fel.omo.semestralka.factory.CustomerFactory;
import cz.cvut.fel.omo.semestralka.factory.FarmerFactory;
import cz.cvut.fel.omo.semestralka.factory.ProducerFactory;
import cz.cvut.fel.omo.semestralka.factory.ShopOwnerFactory;
import cz.cvut.fel.omo.semestralka.model.Address;
import cz.cvut.fel.omo.semestralka.model.Product;
import cz.cvut.fel.omo.semestralka.model.roles.Farmer;
import cz.cvut.fel.omo.semestralka.model.roles.Producer;
import cz.cvut.fel.omo.semestralka.model.roles.ShopOwner;
import cz.cvut.fel.omo.semestralka.model.roles.Distributor;
import cz.cvut.fel.omo.semestralka.model.roles.Customer;
import cz.cvut.fel.omo.semestralka.report.ReportSaver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BasicConfiguration extends Configuration {

    private Farmer farmer_VERCA;
    private Farmer farmer_AZUL;
    private Producer producer_MINA;
    private Producer producer_JOSEF;
    private ShopOwner shopOwner_KAUFLAND;
    private ShopOwner shopOwner_MERIDIAN;
    private Customer customer_FIRST;
    private Customer customer_SECOND;
    private Customer customer_THIRD;
    private Customer customer_FOURTH;
    private Distributor distributor_POSTA;

    private FarmerFactory farmerFactory_VERCA;
    private FarmerFactory farmerFactory_AZUL;
    private ProducerFactory producerFactory_MINA;
    private ProducerFactory producerFactory_JOSEF;
    private ShopOwnerFactory shopOwnerFactory_KAUFLAND;
    private ShopOwnerFactory shopOwnerFactory_MERIDIAN;
    private CustomerFactory customerFactory_FIRST;
    private CustomerFactory customerFactory_SECOND;
    private CustomerFactory customerFactory_THIRD;
    private CustomerFactory customerFactory_FOURTH;

    ProductInterface COW = new Product(ProductsCatalogue.COW.name(), LocalDate.now().minusDays(20));
    ProductInterface CHICKEN = new Product(ProductsCatalogue.CHICKEN.name(), LocalDate.now().minusDays(20));
    ProductInterface STRAWBERRY = new Product(ProductsCatalogue.STRAWBERRY.name(), LocalDate.now().minusDays(9));
    ProductInterface CHEESECAKE = new Product(ProductsCatalogue.CHEESECAKE.name(), LocalDate.now().minusDays(8));
    ProductInterface PASTA_FRESH = new Product(ProductsCatalogue.PASTA_FRESH.name(), LocalDate.now().minusDays(10));

    @Override
    protected void saveProductReport() {
        for (ProductInterface product : products) {
            ReportSaver.saveReportToJson(product.getTransactionHistory(), product.getDescription().toLowerCase() + "Report.json");
            product.generatePartiesReport(product.getDescription().toLowerCase() + "PartiesReport.json");
        }
    }

    @Override
    protected void saveSecurityReport() {
        // do nothing
    }

    @Override
    protected void saveModificationSecurityReport() {
        // do nothing
    }

    @Override
    protected void initialize() {

        addProductsToList();

        // Initialization parties and their factories
        initializeParties();
        initializeFactories();

        // Store products
        farmerFactory_VERCA.storeProduct(COW, COW.getProducedOnDate());
        farmerFactory_AZUL.storeProduct(CHICKEN, CHICKEN.getProducedOnDate());
        producerFactory_MINA.storeProduct(STRAWBERRY, STRAWBERRY.getProducedOnDate());
        producerFactory_MINA.storeProduct(CHEESECAKE, CHEESECAKE.getProducedOnDate());
        producerFactory_JOSEF.storeProduct(PASTA_FRESH, PASTA_FRESH.getProducedOnDate());

        // Create new products by existing products in storage
        ProductInterface BEEF = farmerFactory_VERCA.createProduct(ProductsCatalogue.BEEF, LocalDate.now().minusDays(9));
        ProductInterface CHICKEN_MEAT = farmerFactory_AZUL.createProduct(ProductsCatalogue.CHICKEN_MEAT, LocalDate.now().minusDays(9));

        // Sell products
        farmerFactory_VERCA.sellProduct(BEEF);
        farmerFactory_AZUL.sellProduct(CHICKEN_MEAT);

        // Purchase products
        producerFactory_MINA.purchaseProduct(BEEF, distributor_POSTA, farmer_VERCA, LocalDate.now().minusDays(8));
        producerFactory_MINA.purchaseProduct(CHICKEN_MEAT, distributor_POSTA, farmer_AZUL, LocalDate.now().minusDays(7));

        // Producer Mina returns product BEEF
        producerFactory_MINA.returnProduct(BEEF, distributor_POSTA, farmer_VERCA, LocalDate.now().minusDays(7));

        // Producers sell all their products
        producerFactory_MINA.sellProduct(STRAWBERRY); producerFactory_MINA.sellProduct(CHEESECAKE);
        producerFactory_MINA.sellProduct(CHICKEN_MEAT);
        ProductInterface PASTA_FRESH_BIO = producerFactory_JOSEF.createProduct(new BioProductDecorator(PASTA_FRESH), LocalDate.now().minusDays(9));
        producerFactory_JOSEF.sellProduct(PASTA_FRESH_BIO);

        // Shop owners purchase products from producers and then sell some products
        shopOwnerFactory_MERIDIAN.purchaseProduct(STRAWBERRY, distributor_POSTA, producer_MINA, LocalDate.now().minusDays(9));
        shopOwnerFactory_MERIDIAN.purchaseProduct(PASTA_FRESH_BIO, distributor_POSTA, producer_JOSEF, LocalDate.now().minusDays(9));
        shopOwnerFactory_KAUFLAND.purchaseProduct(CHEESECAKE, distributor_POSTA, producer_MINA, LocalDate.now().minusDays(7));
        shopOwnerFactory_KAUFLAND.purchaseProduct(CHICKEN_MEAT, distributor_POSTA, producer_MINA, LocalDate.now().minusDays(8));

        // Add customers to shop as subscribers
        shopOwnerFactory_MERIDIAN.addSubscribedCustomer(customer_FIRST);
        shopOwnerFactory_MERIDIAN.addSubscribedCustomer(customer_SECOND);
        shopOwnerFactory_KAUFLAND.addSubscribedCustomer(customer_THIRD);
        shopOwnerFactory_KAUFLAND.addSubscribedCustomer(customer_FOURTH);

        shopOwnerFactory_MERIDIAN.sellProduct(PASTA_FRESH_BIO);
        shopOwnerFactory_KAUFLAND.sellProduct(CHEESECAKE);

        // customers purchase products
        customerFactory_FIRST.purchaseProduct(PASTA_FRESH_BIO, distributor_POSTA, shopOwner_MERIDIAN, LocalDate.now().minusDays(7));
        customerFactory_FOURTH.purchaseProduct(CHEESECAKE, distributor_POSTA, shopOwner_KAUFLAND, LocalDate.now().minusDays(6));

        // customers return products
        customerFactory_FIRST.returnProduct(PASTA_FRESH_BIO, distributor_POSTA, shopOwner_MERIDIAN, LocalDate.now().minusDays(7));
        customerFactory_FOURTH.returnProduct(CHEESECAKE, distributor_POSTA, shopOwner_KAUFLAND, LocalDate.now().minusDays(6));

        // shop owners sell products again
        shopOwnerFactory_MERIDIAN.sellProduct(PASTA_FRESH_BIO);
        shopOwnerFactory_KAUFLAND.sellProduct(CHEESECAKE);

        // other customers purchase products
        customerFactory_SECOND.returnProduct(PASTA_FRESH_BIO, distributor_POSTA, shopOwner_MERIDIAN, LocalDate.now().minusDays(4));
        customerFactory_THIRD.returnProduct(CHEESECAKE, distributor_POSTA, shopOwner_KAUFLAND, LocalDate.now().minusDays(5));

    }

    /**
     * Initializes the parties.
     */
    private void initializeParties() {
        String PHONE_NUMBER = "123 456 789";
        farmer_VERCA = new Farmer(
                "Farmer Verca", PHONE_NUMBER, 500_000, getFarmerPlaces(), Address.generateRandomAddress());
        farmer_AZUL = new Farmer(
                "Farmer Azul", PHONE_NUMBER, 600_000, getFarmerPlaces(), Address.generateRandomAddress());
        producer_MINA = new Producer(
                "Producer Mina", PHONE_NUMBER, 10_000, getProducerPlaces(), Address.generateRandomAddress());
        producer_JOSEF = new Producer(
                "Producer Josef", PHONE_NUMBER, 20_000, getProducerPlaces(), Address.generateRandomAddress());
        shopOwner_KAUFLAND = new ShopOwner(
                "ShopOwner Kaufland", PHONE_NUMBER, 50_000, getShopOwnerPlaces(), Address.generateRandomAddress());
        shopOwner_MERIDIAN = new ShopOwner(
                "ShopOwner Meridian", PHONE_NUMBER, 45_000, getShopOwnerPlaces(), Address.generateRandomAddress());
        customer_FIRST = new Customer(
                "Customer First", PHONE_NUMBER, 2_000, getCustomerPlaces(), Address.generateRandomAddress());
        customer_SECOND = new Customer(
                "Customer Second", PHONE_NUMBER, 3_000, getCustomerPlaces(), Address.generateRandomAddress());
        customer_THIRD = new Customer(
                "Customer Third", PHONE_NUMBER, 1_500, getCustomerPlaces(), Address.generateRandomAddress());
        customer_FOURTH = new Customer(
                "Customer Fourth", PHONE_NUMBER, 4_000, getCustomerPlaces(), Address.generateRandomAddress());
        distributor_POSTA = new Distributor(
                "Distributor Posta", PHONE_NUMBER, 0, getDistributorPlaces(), Address.generateRandomAddress());
    }

    /**
     * Initializes the factories for each party.
     */
    private void initializeFactories() {
        farmerFactory_VERCA = new FarmerFactory(farmer_VERCA);
        farmerFactory_AZUL = new FarmerFactory(farmer_AZUL);
        producerFactory_MINA = new ProducerFactory(producer_MINA);
        producerFactory_JOSEF = new ProducerFactory(producer_JOSEF);
        shopOwnerFactory_KAUFLAND = new ShopOwnerFactory(shopOwner_KAUFLAND);
        shopOwnerFactory_MERIDIAN = new ShopOwnerFactory(shopOwner_MERIDIAN);
        customerFactory_FIRST = new CustomerFactory(customer_FIRST);
        customerFactory_SECOND = new CustomerFactory(customer_SECOND);
        customerFactory_THIRD = new CustomerFactory(customer_THIRD);
        customerFactory_FOURTH = new CustomerFactory(customer_FOURTH);
    }

    /**
     * Adds products to the list for generating reports.
     */
    private void addProductsToList() {
        products.add(COW);
        products.add(CHICKEN);
        products.add(STRAWBERRY);
        products.add(CHEESECAKE);
        products.add(PASTA_FRESH);
    }

    /**
     * @return List of places for farmers.
     */
    private List<Place> getFarmerPlaces() {
        List<Place> farmerPlaces = new ArrayList<>();
        farmerPlaces.add(Place.FARM);
        farmerPlaces.add(Place.WAREHOUSE_FARMER);
        farmerPlaces.add(Place.MANUFACTORY);
        return farmerPlaces;
    }

    /**
     * @return List of places for producers.
     */
    private List<Place> getProducerPlaces() {
        List<Place> producerPlaces = new ArrayList<>();
        producerPlaces.add(Place.WAREHOUSE_PRODUCER);
        producerPlaces.add(Place.MANUFACTORY);
        return producerPlaces;
    }

    /**
     * @return List of places for shop owners.
     */
    private List<Place> getShopOwnerPlaces() {
        List<Place> shopOwnerPlaces = new ArrayList<>();
        shopOwnerPlaces.add(Place.SHOP);
        shopOwnerPlaces.add(Place.WAREHOUSE);
        return shopOwnerPlaces;
    }

    /**
     * @return List of places for customers.
     */
    private List<Place> getCustomerPlaces() {
        List<Place> customerPlaces = new ArrayList<>();
        customerPlaces.add(null);
        return customerPlaces;
    }

    /**
     * @return List of places for distributors.
     */
    private List<Place> getDistributorPlaces() {
        List<Place> distributorPlaces = new ArrayList<>();
        distributorPlaces.add(Place.VAN);
        return distributorPlaces;
    }

}
