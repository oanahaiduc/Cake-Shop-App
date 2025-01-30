package cakeApp.ui;

import cakeApp.domain.Cake;
import cakeApp.domain.Order;
import cakeApp.service.OrderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OrderUI {
    private OrderService orderService;
    private final Scanner scanner;

    private static final int PLACE_ORDER = 1;
    private static final int VIEW_ALL_ORDERS = 2;
    private static final int CANCEL_ORDER = 3;
    private static final int FINISH_ORDER = 4;
    private static final int DELETE_ORDER = 5;
    private static final int FILTER_BY_STATUS = 6;
    private static final int FILTER_BY_CUSTOMER = 7;
    private static final int GET_CAKES_ORDERED_ON_DATE = 8;
    private static final int GET_ORDERS_WITH_SAME_CAKES = 9;
    private static final int GET_TOTAL_ORDERS_PLACED_BY_CUSTOMER = 10;
    private static final int GET_TOTAL_FROM_PREPARING_ORDERS = 11;
    private static final int GET_ORDER_COUNT_PER_CAKE = 12;
    private static final int BACK_MAIN_MENU = 13;



    public OrderUI(OrderService orderService) {
        this.orderService = orderService;
        this.scanner = new Scanner(System.in);
    }

    public void manageOrders(){
        boolean exit = false;
        while(!exit){
            System.out.println("\nOrder menu:");
            System.out.println("1. Place an order");
            System.out.println("2. View all orders");
            System.out.println("3. Cancel an order");
            System.out.println("4. Finish an order");
            System.out.println("5. Delete an order");
            System.out.println("6. Filter orders by status");
            System.out.println("7. Filter orders by customer name");
            System.out.println("8. Get all the cakes that are ordered on the same date");
            System.out.println("9. Get orders that have the same cake ordered ");
            System.out.println("10. Get total number of orders placed by customer");
            System.out.println("11. Get total revenue from preparing orders");
            System.out.println("12. Get order count per cake");
            System.out.println("13. Back to main menu");
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
                case PLACE_ORDER:
                    placeOrder();
                    break;
                case VIEW_ALL_ORDERS:
                    viewAllOrders();
                    break;
                case CANCEL_ORDER:
                    cancelOrder();
                    break;
                case FINISH_ORDER:
                    finishOrder();
                    break;
                case DELETE_ORDER:
                    deleteOrder();
                    break;
                case FILTER_BY_STATUS:
                    filterOrdersByStatus();
                    break;
                case FILTER_BY_CUSTOMER:
                    filterOrdersByCustomerName();
                    break;
                case GET_CAKES_ORDERED_ON_DATE:
                    getCakesOrderedOnDate();
                    break;
                case GET_ORDERS_WITH_SAME_CAKES:
                    getOrdersThatHaveTheSameCake();
                    break;
                case GET_TOTAL_ORDERS_PLACED_BY_CUSTOMER:
                    getTotalCakesByCustomer();
                    break;
                case GET_TOTAL_FROM_PREPARING_ORDERS:
                    getTotalFromPreparingOrders();
                    break;
                case GET_ORDER_COUNT_PER_CAKE:
                    getOrderCountPerCake();
                    break;
                case BACK_MAIN_MENU:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private void placeOrder(){
        System.out.println("Enter cake ID: ");
        Integer cakeID = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter customer name: ");
        String customerNameInput = scanner.nextLine();

        try{
            orderService.createOrder(cakeID, customerNameInput);
            System.out.println("Order placed successfully!");
        } catch(IllegalArgumentException e){
            System.out.println("Error placing order: " + e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void viewAllOrders(){
        orderService.getAllOrders().forEach(System.out::println);
    }

    private void cancelOrder(){
        System.out.println("Enter the ID of the order you want to cancel: ");
        Integer orderIDToCancel = Integer.parseInt(scanner.nextLine());
        try{
            orderService.cancelOrder(orderIDToCancel);
            System.out.println("Order cancelled successfully!");
        }catch (IllegalArgumentException e){
            System.out.println("Error cancelling order: " + e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void finishOrder(){
        System.out.println("Enter the ID of the order you want to finish: ");
        Integer orderIDToFinish = Integer.parseInt(scanner.nextLine());
        try{
            orderService.finishOrder(orderIDToFinish);
            System.out.println("Order finished successfully!");
        }catch (IllegalArgumentException e){
            System.out.println("Error finishing order: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteOrder(){
        System.out.println("Enter the ID of the order you want to delete: ");
        Integer orderIDToDelete = Integer.parseInt(scanner.nextLine());
        try{
            orderService.deleteOrder(orderIDToDelete);
            System.out.println("Order deleted successfully!");
        }catch (IllegalArgumentException e){
            System.out.println("Error deleting order: " + e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void filterOrdersByStatus(){
        try {
            System.out.println("Enter an order status you want to filter by (preparing/cancelled/finished): ");
            String orderStatusInput = scanner.nextLine();
            orderService.filterByStatus(orderStatusInput).forEach(System.out::println);
        }catch(Exception e){
            System.out.println("Error filtering order by status: " + e.getMessage());
        }
    }

    private void filterOrdersByCustomerName(){
        try {
            System.out.println("Enter an customer name yo want to filter by: ");
            String customerNameInput = scanner.nextLine();
            orderService.filterByCustomerName(customerNameInput).forEach(System.out::println);
        }catch(Exception e){
            System.out.println("Error filtering order by name: " + e.getMessage());
        }
    }

    private void getCakesOrderedOnDate(){
        System.out.println("Enter date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        try{
            LocalDate orderDate = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
            List<Cake<Integer>> cakesOrderedOnDate = orderService.getCakesOrderedOnDate(orderDate);

            if(cakesOrderedOnDate.isEmpty()){
                System.out.println("No cakes were ordered on this date!");
            }else{
                System.out.println("Cakes ordered on this date: ");
                cakesOrderedOnDate.forEach(System.out::println);
            }

        }catch(DateTimeParseException e){
            System.out.println("Invalid date format.");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void getOrdersThatHaveTheSameCake(){
        try {
            System.out.println("Enter the ID of the cake: ");
            Integer cakeID = Integer.parseInt(scanner.nextLine());

            List<Order<Integer>> orderList = orderService.getOrdersThatHaveTheSameCake(cakeID);
            if (orderList.isEmpty()) {
                System.out.println("No cakes were ordered on this date!");
            } else {
                System.out.println("Orders with the same cake: ");
                orderList.forEach(System.out::println);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getTotalCakesByCustomer(){
        try {
            Map<String, Long> totalCakes = orderService.getTotalOrdersPlacedByCustomer();
            if (totalCakes.isEmpty()) {
                System.out.println("No cakes have been ordered!");
            } else {
                System.out.println("Total cakes ordered by each customer: ");
                totalCakes.forEach((customerName, cakeCount) ->
                        System.out.println(customerName + ": " + cakeCount + " cakes"));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getTotalFromPreparingOrders(){
        try {
            double totalRevenue = orderService.getTotalFromPreparingOrders();
            System.out.println("Total revenue from preparing orders: " + totalRevenue);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void getOrderCountPerCake(){
        try{
        Map<Integer, Long> orderCount = orderService.getOrderCountPerCake();
        if(orderCount.isEmpty()){
            System.out.println("No order have been placed yet!");
        }else{
            System.out.println("Order count per cake: ");
            orderCount.forEach((cakeId, count) ->
                    System.out.println("Cake ID " + cakeId + ": " + count + " orders"));
        }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
