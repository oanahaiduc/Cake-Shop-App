package cakeApp.ui;

import cakeApp.domain.Cake;
import cakeApp.service.CakeService;

import java.util.Scanner;


public class CakeUI {
    private CakeService cakeService;
    private Scanner scanner;

    private static final int ADD_CAKE = 1;
    private static final int VIEW_ALL_CAKES = 2;
    private static final int UPDATE_CAKE = 3;
    private static final int FILTER_CAKE_FLAVOUR = 4;
    private static final int FILTER_CAKE_PRICE = 5;
    private static final int BACK_MAIN_MENU = 6;


    public CakeUI(CakeService cakeService) {
        this.cakeService = cakeService;
        this.scanner = new Scanner(System.in);

    }

    public void manageCakes(){
        boolean exit = false;

        while(!exit){
            System.out.println("\nCake menu: ");
            System.out.println("1. Add cake");
            System.out.println("2. View all cakes");
            System.out.println("3. Update cake");
            System.out.println("4. Filter cakes by flavour");
            System.out.println("5. Filter cakes by price range");
            System.out.println("6. Back to main menu");
            System.out.println(" ");

            System.out.println("Choose an option:");

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
                case ADD_CAKE:
                    addCake();
                    break;
                case VIEW_ALL_CAKES:
                    viewAllCakes();
                    break;
                case UPDATE_CAKE:
                    updateCake();
                    break;
                case FILTER_CAKE_FLAVOUR:
                    filterCakesByFlavour();
                    break;
                case FILTER_CAKE_PRICE:
                    filterCakesByPriceRange();
                    break;
                case BACK_MAIN_MENU:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option!");


            }
        }
    }

    public void addCake(){
        System.out.println("Enter cake name: ");
        String cakeName = scanner.nextLine();
        System.out.println("Enter cake flavour: ");
        String cakeFlavour = scanner.nextLine();
        System.out.println("Enter cake price: ");
        double cakePrice = scanner.nextDouble();
        System.out.println("Enter cake layers: ");
        int cakeLayers = scanner.nextInt();

        try{
            cakeService.addCake(new Cake<>(cakeName, cakeFlavour, cakePrice, cakeLayers));
            System.out.println("Cake added!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding cake: " + e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void viewAllCakes(){
        cakeService.getCakes().forEach(System.out::println);
    }

    private void updateCake() {
        System.out.println("Enter cake ID to update: ");
        Integer cakeIDToBeUpdated = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter a new cake name: ");
        String cakeName = scanner.nextLine();
        System.out.println("Enter a new cake flavour: ");
        String cakeFlavour = scanner.nextLine();
        System.out.println("Enter a new cake price: ");
        double cakePrice = scanner.nextDouble();
        System.out.println("Enter a new number of cake layers: ");
        int cakeLayers = scanner.nextInt();
        try {
            cakeService.updateCake(new Cake<>(cakeName, cakeFlavour, cakePrice, cakeLayers));
            System.out.println("Cake updated!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error updating cake: " + e.getMessage());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void filterCakesByFlavour(){
        try{
        System.out.println("Enter cake flavour: ");
        String flavourToFilterBy = scanner.nextLine();
        cakeService.filterByFlavour(flavourToFilterBy).forEach(System.out::println);
        }catch(RuntimeException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void filterCakesByPriceRange(){
        try{
        System.out.println("Enter minimum cake price: ");
        double minimumCakePrice = scanner.nextDouble();
        System.out.println("Enter maximum cake price: ");
        double maximumCakePrice = scanner.nextDouble();
        cakeService.filterByPriceRange(minimumCakePrice, maximumCakePrice).forEach(System.out::println);
    }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

