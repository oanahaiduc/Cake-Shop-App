package cakeApp.ui;

import java.util.Scanner;

import cakeApp.service.CakeService;
import cakeApp.service.OrderService;

public class CakeApp {
    private CakeUI cakeUI;
    private OrderUI orderUI;
    private Scanner scanner;

    private static final int ENTER_CAKE_MENU = 1;
    private static final int ENTER_ORDER_MENU = 2;
    private static final int EXIT_MAIN_MENU = 3;


    public CakeApp(CakeService cakeService, OrderService orderService) {
        this.cakeUI= new CakeUI(cakeService);
        this.orderUI = new OrderUI(orderService);
        this.scanner = new Scanner(System.in);
    }

    public void start(){
        while(true){
            System.out.println("\nCake Shop:");
            System.out.println("1. Enter cake menu");
            System.out.println("2. Enter order menu");
            System.out.println("3. Exit");
            System.out.println(" ");
            System.out.println("Choose an option: ");

            int option;
            while (true) {
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.nextLine();
                }
            }
            switch(option){
                case ENTER_CAKE_MENU:
                    cakeUI.manageCakes();
                    break;
                case ENTER_ORDER_MENU:
                    orderUI.manageOrders();
                    break;
                case EXIT_MAIN_MENU:
                    System.out.println("Exit!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

}
