package cz.cvut.fel.omo.semestralka.configuration;

import cz.cvut.fel.omo.semestralka.decorator.ProductInterface;
import cz.cvut.fel.omo.semestralka.enums.OperationType;
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
import cz.cvut.fel.omo.semestralka.transaction.StorageModificationSecurityTransaction;
import cz.cvut.fel.omo.semestralka.transaction.StorageSecurityTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandlingConfiguration extends Configuration {

    private Farmer farmer_VERCA;
    private Farmer farmer_AZUL;
    private Producer producer_MINA;
    private Producer producer_JOSEF;
    private ShopOwner shopOwner_MERIDIAN;
    private Customer customer_FIRST;
    private Distributor distributor_POSTA;

    private FarmerFactory farmerFactory_VERCA;
    private FarmerFactory farmerFactory_AZUL;
    private ProducerFactory producerFactory_MINA;
    private ProducerFactory producerFactory_JOSEF;
    private ShopOwnerFactory shopOwnerFactory_MERIDIAN;
    private CustomerFactory customerFactory_FIRST;

    ProductInterface COW = new Product(ProductsCatalogue.COW.name(), LocalDate.now().minusDays(20));
    ProductInterface CHICKEN = new Product(ProductsCatalogue.CHICKEN.name(), LocalDate.now().minusDays(20));
    ProductInterface STRAWBERRY = new Product(ProductsCatalogue.STRAWBERRY.name(), LocalDate.now().minusDays(9));
    ProductInterface CHEESECAKE = new Product(ProductsCatalogue.CHEESECAKE.name(), LocalDate.now().minusDays(8));
    ProductInterface PASTA_FRESH = new Product(ProductsCatalogue.PASTA_FRESH.name(), LocalDate.now().minusDays(50));

    @Override
    protected void saveProductReport() {
        // do nothing
    }

    @Override
    protected void saveSecurityReport() {
        ReportSaver.saveSecurityReportToJson(StorageSecurityTransaction.securityTransactions, "SecurityReport.json");
    }

    @Override
    protected void saveModificationSecurityReport() {
        ReportSaver.saveModificationSecurityToJson(StorageModificationSecurityTransaction.securityTransactions, "ModificationSecurityReport.json");
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

        // Create new products, but storage doesn't have certain origin product to create
        ProductInterface CHICKEN_MEAT = farmerFactory_VERCA.createProduct(ProductsCatalogue.CHICKEN_MEAT, LocalDate.now().minusDays(9)); // error
        ProductInterface BEEF = farmerFactory_AZUL.createProduct(ProductsCatalogue.BEEF, LocalDate.now().minusDays(9)); // error

        // Sell products
        farmerFactory_VERCA.sellProduct(CHICKEN_MEAT); // error, farmer doesn't have chicken meat
        farmerFactory_AZUL.sellProduct(CHICKEN); // ok

        // Purchase products
        producerFactory_MINA.purchaseProduct(CHICKEN, distributor_POSTA, farmer_AZUL, LocalDate.now().minusDays(7)); // ok
        producerFactory_JOSEF.purchaseProduct(CHICKEN, distributor_POSTA, farmer_AZUL, LocalDate.now().minusDays(7)); // error, double spending
        producerFactory_JOSEF.purchaseProduct(CHICKEN, distributor_POSTA, farmer_AZUL, LocalDate.now().minusDays(6)); // error, double spending

        // Return products
        producerFactory_MINA.returnProduct(CHICKEN, distributor_POSTA, farmer_AZUL, LocalDate.now().minusDays(5)); // ok
        producerFactory_JOSEF.returnProduct(CHICKEN, distributor_POSTA, farmer_AZUL, LocalDate.now().minusDays(5)); // error, producer doesn't have chicken

        // Change product's transaction
        farmer_AZUL.getStorage().getProduct(CHICKEN).getTransactionHistory().getLast().setOperationType(OperationType.CREATE); // error, cannot change data in transaction

        // Sell product
        producerFactory_MINA.sellProduct(STRAWBERRY); // ok
        producerFactory_JOSEF.sellProduct(PASTA_FRESH); // error, product is expired
        producerFactory_MINA.sellProduct(CHEESECAKE); // ok

        // Purchase product
        shopOwnerFactory_MERIDIAN.addSubscribedCustomer(customer_FIRST);
        shopOwnerFactory_MERIDIAN.purchaseProduct(STRAWBERRY, farmer_AZUL, producer_MINA, LocalDate.now().minusDays(5)); // error, Azul is not distributor
        shopOwnerFactory_MERIDIAN.purchaseProduct(CHEESECAKE, distributor_POSTA, producer_MINA, LocalDate.now().minusDays(5)); // ok
        shopOwner_MERIDIAN.getStorage().getProduct(CHEESECAKE).getTransactionHistory().getLast().setPrice(100); // error, cannot change data
        shopOwnerFactory_MERIDIAN.sellProduct(CHEESECAKE); // ok

        // Purchase and sell product
        customerFactory_FIRST.purchaseProduct(CHEESECAKE, distributor_POSTA, producer_MINA, LocalDate.now().minusDays(4)); // ok
        customerFactory_FIRST.sellProduct(CHEESECAKE); // error, customer cannot sell product

    }

    /**
     * Initializes the parties.
     */
    private void initializeParties() {
        String PHONE_NUMBER = "123 123 123";
        farmer_AZUL = new Farmer(
                "Farmer Azul", PHONE_NUMBER, 600_000, getFarmerPlaces(), Address.generateRandomAddress());
        farmer_VERCA = new Farmer(
                "Farmer Verca", PHONE_NUMBER, 500_000, getFarmerPlaces(), Address.generateRandomAddress());
        producer_MINA = new Producer(
                "Producer Mina", PHONE_NUMBER, 10_000, getProducerPlaces(), Address.generateRandomAddress());
        producer_JOSEF = new Producer(
                "Producer Josef", PHONE_NUMBER, 20_000, getProducerPlaces(), Address.generateRandomAddress());
        shopOwner_MERIDIAN = new ShopOwner(
                "ShopOwner Meridian", PHONE_NUMBER, 45_000, getShopOwnerPlaces(), Address.generateRandomAddress());
        customer_FIRST = new Customer(
                "Customer First", PHONE_NUMBER, 2_000, getCustomerPlaces(), Address.generateRandomAddress());
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
        shopOwnerFactory_MERIDIAN = new ShopOwnerFactory(shopOwner_MERIDIAN);
        customerFactory_FIRST = new CustomerFactory(customer_FIRST);
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
