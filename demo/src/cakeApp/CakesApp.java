package cakeApp;

import cakeApp.repository.CreateRepository;
import cakeApp.ui.CakeApp;

import cakeApp.domain.Cake;
import cakeApp.domain.Order;
import cakeApp.repository.IRepository;
import cakeApp.service.CakeService;
import cakeApp.service.OrderService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class CakesApp {
    public static void main(String[] args) {

        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream("settings.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            return;
        }


        String repositoryType = properties.getProperty("Repository", "inmemory");
        String cakesFile = properties.getProperty("Cakes", "cakes.txt");
        String orderFile = properties.getProperty("Order", "order.txt");


        if (repositoryType.equalsIgnoreCase("database")) {
            cakesFile = properties.getProperty("Location", "jdbc:sqlite:C:\\Users\\Lenovo\\Desktop\\teme\\Cake-Shop-App\\demo\\data\\cakeapp.db");
            orderFile = cakesFile;
        }

        IRepository<Integer, Cake<Integer>> cakeRepository = CreateRepository.createCakeRepository(repositoryType, cakesFile);
        IRepository<Integer, Order<Integer>> orderRepository = CreateRepository.createOrderRepository(repositoryType, orderFile);

        CakeService cakeService = new CakeService(cakeRepository);
        OrderService orderService = new OrderService(orderRepository, cakeRepository);

       if (repositoryType.equalsIgnoreCase("inmemory")) {
            cakeService.addCake(new Cake("Forest", "Hazelnut", 90.5, 2));
            cakeService.addCake(new Cake("Jungle", "Banana", 107.0, 6));
            cakeService.addCake(new Cake("Red", "Strawberry", 65.8, 2));
            cakeService.addCake(new Cake("BeeBee", "Honey", 200.5, 5));
            cakeService.addCake(new Cake("Darkness", "Chocolate", 78.3, 1));

            orderService.createOrder(5, "Anna");
            orderService.createOrder(4, "Jack");
            orderService.createOrder(1, "Richard");
            orderService.createOrder(2, "John");
            orderService.createOrder(4, "Sonia");
        }

        CakeApp cakeApp = new CakeApp(cakeService, orderService);
        cakeApp.start();
    }
}
