package GUI;

import cakeApp.domain.Cake;
import cakeApp.domain.Order;
import cakeApp.service.CakeService;
import cakeApp.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    CakeService cakeService;
    OrderService orderService;

    @FXML
    private Button addCakeButton;

    @FXML
    private Button updateCakeButton;

    @FXML
    private Button filterCakesByFlavourButton;

    @FXML
    private Button filterByPriceRangeButton;

    @FXML
    private Button placeOrderButton;

    @FXML
    private Button finishOrderButton;

    @FXML
    private Button cancelOrderButton;

    @FXML
    private Button deleteOrderButton;

    @FXML
    private Button filterOrdersByStatusButton;

    @FXML
    private Button filterOrdersByCustomerNameButton;

    @FXML
    private Button ordersPlacedOnTheSameDateButton;

    @FXML
    private Button ordersWithTheSameCakeButton;

    @FXML
    private Button orderCountPerCakeButton;

    @FXML
    private Button orderCountPerCustomerButton;

    @FXML
    private Button totalRevenueFromPreparingOrdersButton;

    @FXML
    private ListView<Cake<Integer>> cakesListView;

    @FXML
    private ListView<Order<Integer>> ordersListView;

    ObservableList<Cake<Integer>> cakesList;
    ObservableList<Order<Integer>> ordersList;


    public Controller(CakeService cakeService, OrderService orderService) {
        this.cakeService = cakeService;
        this.orderService = orderService;
    }

    public void initialize(){
        Iterable<Cake<Integer>> elems = cakeService.getCakes();
        ArrayList<Cake<Integer>> cakes = new ArrayList<>();
        for(Cake<Integer> cake : elems){
            cakes.add(cake);
        }

        cakesList = FXCollections.observableArrayList(cakes);
        cakesListView.setItems(cakesList);

        Iterable<Order<Integer>> elements = orderService.getAllOrders();
        ArrayList<Order<Integer>> orders = new ArrayList<>();
        for(Order<Integer> order : elements){
            orders.add(order);
        }
        ordersList = FXCollections.observableArrayList(orders);
        ordersListView.setItems(ordersList);
    }

    void resetObservableList(){
        ArrayList<Cake<Integer>> cakes = new ArrayList<>();
        cakeService.getCakes().forEach(cakes::add);
        cakesList = FXCollections.observableArrayList(cakes);
        cakesListView.setItems(cakesList);

        ArrayList<Order<Integer>> orders = new ArrayList<>();
        orderService.getAllOrders().forEach(orders::add);
        ordersList = FXCollections.observableArrayList(orders);
        ordersListView.setItems(ordersList);
    }

    private void showError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    private String showInputDialog(String title, String prompt){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setContentText(prompt);
        return dialog.showAndWait().orElse(null);
    }

    @FXML
    void addCakeHandler(ActionEvent event) {
        String cakeName = showInputDialog("Add cake", "Enter cake name: ");
        if(cakeName == null || cakeName.isBlank() ){
            showError("Name cannot be empty!");
            return;
        }

        String cakeFlavour = showInputDialog("Add cake", "Enter cake flavour: ");
        if(cakeFlavour == null || cakeFlavour.isBlank() ){
            showError("Flavour cannot be empty!");
            return;
        }

        String cakePriceString = showInputDialog("Add cake", "Enter cake price: ");
        if(cakePriceString == null || cakePriceString.isBlank() ){
            showError("Price cannot be empty!");
            return;
        }

        String cakeLayersString = showInputDialog("Add cake", "Enter cake layers: ");
        if(cakeLayersString == null || cakeLayersString.isBlank() ){
            showError("Layer cannot be empty!");
            return;
        }

        try{
            double cakePrice = Double.parseDouble(cakePriceString);
            int cakeLayers = Integer.parseInt(cakeLayersString);
            cakeService.addCake(new Cake<>(cakeName,cakeFlavour,cakePrice,cakeLayers));
            resetObservableList();
        }catch(NumberFormatException e){
            showError("Invalid price format!");
        }catch(IllegalArgumentException e){
            showError(e.getMessage());
        }
    }

    @FXML
    void updateCakeHandler(ActionEvent event) {
        Cake<Integer> selectedCake = cakesListView.getSelectionModel().getSelectedItem();
        if(selectedCake == null){
            showError("No cake selected!");
            return;
        }

        String cakeName = showInputDialog("Update cake", "Enter new cake name: ");
        if(cakeName == null || cakeName.isBlank() ){
            showError("Name cannot be empty!");
            return;
        }

        String cakeFlavour = showInputDialog("Update cake", "Enter new cake flavour: ");
        if(cakeFlavour == null || cakeFlavour.isBlank() ){
            showError("Flavour cannot be empty!");
            return;
        }

        String cakePriceString = showInputDialog("Update cake", "Enter new cake price: ");
        if (cakePriceString == null || cakePriceString.isBlank()) {
            showError("Price cannot be empty!");
            return;
        }

        String cakeLayersString = showInputDialog("Update cake", "Enter new cake layers: ");
        if (cakeLayersString == null || cakeLayersString.isBlank()) {
            showError("Layers cannot be empty!");
            return;
        }

        try {
            double cakePrice = Double.parseDouble(cakePriceString);
            int cakeLayers = Integer.parseInt(cakeLayersString);

            selectedCake.setCakeName(cakeName);
            selectedCake.setCakeFlavour(cakeFlavour);
            selectedCake.setPrice(cakePrice);
            selectedCake.setCakeLayers(cakeLayers);

            cakeService.updateCake(selectedCake);
            resetObservableList();

        } catch (NumberFormatException e) {
            showError("Invalid price or layer format!");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void filterCakesByFlavourHandler(ActionEvent event) {
        try {
            String flavourToFilter = showInputDialog("Filter by Flavour", "Enter flavour to filter by:");

            if (flavourToFilter == null || flavourToFilter.isBlank()) {
                showError("Please enter a flavour to filter by.");
                return;
            }

            Iterable<Cake<Integer>> filteredCakes = cakeService.filterByFlavour(flavourToFilter);
            ArrayList<Cake<Integer>> filteredCakesList = new ArrayList<>();
            for (Cake<Integer> cake : filteredCakes) {
                filteredCakesList.add(cake);
            }

            if (filteredCakesList.isEmpty()) {
                showError("No cakes found for flavour: " + flavourToFilter);
                return;
            }

            ListView<Cake<Integer>> filteredCakesListView = new ListView<>();
            ObservableList<Cake<Integer>> observableFilteredCakes = FXCollections.observableArrayList(filteredCakesList);
            filteredCakesListView.setItems(observableFilteredCakes);

            Stage popupStage = new Stage();
            popupStage.setTitle("Filtered Cakes by Flavour");

            VBox vbox = new VBox(filteredCakesListView);
            vbox.setSpacing(10);
            vbox.setPadding(new javafx.geometry.Insets(10));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());
            vbox.getChildren().add(closeButton);

            Scene scene = new Scene(vbox, 400, 300);
            popupStage.setScene(scene);
            popupStage.show();
        }catch(RuntimeException e){
            showError(e.getMessage());
        }
    }

    @FXML
    void filterByPriceRangeHandler(ActionEvent event) {
        try {
            String minPriceString = showInputDialog("Filter by Price Range", "Enter minimum price:");
            if (minPriceString == null || minPriceString.isBlank()) {
                showError("Minimum price cannot be empty!");
                return;
            }

            String maxPriceString = showInputDialog("Filter by Price Range", "Enter maximum price:");
            if (maxPriceString == null || maxPriceString.isBlank()) {
                showError("Maximum price cannot be empty!");
                return;
            }

            double minPrice = Double.parseDouble(minPriceString);
            double maxPrice = Double.parseDouble(maxPriceString);

            Iterable<Cake<Integer>> filteredCakes = cakeService.filterByPriceRange(minPrice, maxPrice);
            ArrayList<Cake<Integer>> filteredCakesList = new ArrayList<>();
            for (Cake<Integer> cake : filteredCakes) {
                filteredCakesList.add(cake);
            }

            if (filteredCakesList.isEmpty()) {
                showError("No cakes found within the price range: " + minPrice + " - " + maxPrice);
                return;
            }

            ListView<Cake<Integer>> filteredCakesListView = new ListView<>();
            ObservableList<Cake<Integer>> observableFilteredCakes = FXCollections.observableArrayList(filteredCakesList);
            filteredCakesListView.setItems(observableFilteredCakes);

            Stage popupStage = new Stage();
            popupStage.setTitle("Filtered Cakes by Price Range");

            VBox vbox = new VBox(filteredCakesListView);
            vbox.setSpacing(10);
            vbox.setPadding(new javafx.geometry.Insets(10));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());
            vbox.getChildren().add(closeButton);

            Scene scene = new Scene(vbox, 400, 300);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (NumberFormatException e) {
            showError("Invalid price format! Please enter numeric values.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void placeOrderHandler(ActionEvent event) {
        try{
            String cakeIdString = showInputDialog("Place Order", "Enter cake ID:");
            if (cakeIdString == null || cakeIdString.isBlank()) {
                showError("Cake ID cannot be empty!");
                return;
            }

            int cakeID = Integer.parseInt(cakeIdString);

            String customerName = showInputDialog("Place Order", "Enter the Customer Name:");
            if (customerName == null || customerName.isBlank()) {
                showError("Customer Name cannot be empty!");
                return;
            }
            orderService.createOrder(cakeID, customerName);
            resetObservableList();

        }catch(NumberFormatException e){
            showError("Invalid Cake ID!");
        }catch(IllegalArgumentException e){
            showError("Error placing order!");
        }catch(RuntimeException e){
            showError(e.getMessage());
        }
    }

    @FXML
    void finishOrderHandler(ActionEvent event) {
        Order<Integer> selectedOrder = ordersListView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showError("No order selected!");
            return;
        }
        try {
            orderService.finishOrder(selectedOrder.getId());
            resetObservableList();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Finished");
            alert.setContentText("Order successfully marked as finished!");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            showError("Error finishing order: " + e.getMessage());
        } catch (RuntimeException e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    void cancelOrderHandler(ActionEvent event) {
        Order<Integer> selectedOrder = ordersListView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showError("No order selected!");
            return;
        }

        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Cancel Order");
            confirmAlert.setHeaderText("Are you sure you want to cancel this order?");
            confirmAlert.setContentText("Order ID: " + selectedOrder.getId() + "\nCustomer: " + selectedOrder.getCustomerName());

            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                orderService.cancelOrder(selectedOrder.getId());

                resetObservableList();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Order Canceled");
                successAlert.setContentText("Order successfully canceled!");
                successAlert.showAndWait();
            }
        } catch (IllegalArgumentException e) {
            showError("Error canceling order: " + e.getMessage());
        } catch (RuntimeException e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    void deleteOrderHandler(ActionEvent event) {
        Order<Integer> selectedOrder = ordersListView.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showError("No order selected!");
            return;
        }

        try {
            orderService.deleteOrder(selectedOrder.getId());

            resetObservableList();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Order Deleted");
            successAlert.setContentText("Order successfully deleted!");
            successAlert.showAndWait();
        } catch (IllegalArgumentException e) {
            showError("Error deleting order: " + e.getMessage());
        } catch (RuntimeException e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }


    @FXML
    void filterOrdersByStatusHandler(ActionEvent event) {
        try {
            String statusToFilter = showInputDialog("Filter by Status", "Enter status to filter by:");

            if (statusToFilter == null || statusToFilter.isBlank()) {
                showError("Please enter a status to filter by.");
                return;
            }

            Iterable<Order<Integer>> filteredOrders = orderService.filterByStatus(statusToFilter);
            ArrayList<Order<Integer>> filteredOrdersList = new ArrayList<>();
            for (Order<Integer> order : filteredOrders) {
                filteredOrdersList.add(order);
            }

            if (filteredOrdersList.isEmpty()) {
                showError("No orders found with status: " + statusToFilter);
                return;
            }

            ListView<Order<Integer>> filteredOrdersListView = new ListView<>();
            ObservableList<Order<Integer>> observableFilteredOrders = FXCollections.observableArrayList(filteredOrdersList);
            filteredOrdersListView.setItems(observableFilteredOrders);

            Stage popupStage = new Stage();
            popupStage.setTitle("Filtered Orders by Status");

            VBox vbox = new VBox(filteredOrdersListView);
            vbox.setSpacing(10);
            vbox.setPadding(new javafx.geometry.Insets(10));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());
            vbox.getChildren().add(closeButton);

            Scene scene = new Scene(vbox, 400, 300);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void filterOrdersByCustomerNameHandler(ActionEvent event) {
        try {
            String customerNameToFilter = showInputDialog("Filter by Customer Name", "Enter customer name to filter by:");

            if (customerNameToFilter == null || customerNameToFilter.isBlank()) {
                showError("Please enter a customer name to filter by.");
                return;
            }

            Iterable<Order<Integer>> filteredOrders = orderService.filterByCustomerName(customerNameToFilter);
            ArrayList<Order<Integer>> filteredOrdersList = new ArrayList<>();
            for (Order<Integer> order : filteredOrders) {
                filteredOrdersList.add(order);
            }

            if (filteredOrdersList.isEmpty()) {
                showError("No orders found for customer: " + customerNameToFilter);
                return;
            }

            ListView<Order<Integer>> filteredOrdersListView = new ListView<>();
            ObservableList<Order<Integer>> observableFilteredOrders = FXCollections.observableArrayList(filteredOrdersList);
            filteredOrdersListView.setItems(observableFilteredOrders);

            Stage popupStage = new Stage();
            popupStage.setTitle("Filtered Orders by Customer Name");

            VBox vbox = new VBox(filteredOrdersListView);
            vbox.setSpacing(10);
            vbox.setPadding(new javafx.geometry.Insets(10));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());
            vbox.getChildren().add(closeButton);

            Scene scene = new Scene(vbox, 400, 300);
            popupStage.setScene(scene);
            popupStage.show();
        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void ordersPlacedOnTheSameDateHandler(ActionEvent event) {
        String dateString = showInputDialog("Orders Placed on Same Date", "Enter the date (YYYY-MM-DD):");

        if (dateString == null || dateString.isBlank()) {
            showError("Date cannot be empty!");
            return;
        }
        try {
            LocalDate orderedOnDate = LocalDate.parse(dateString);

            List<Cake<Integer>> cakesOrderedOnDate = orderService.getCakesOrderedOnDate(orderedOnDate);

            if (cakesOrderedOnDate.isEmpty()) {
                showError("No cakes were ordered on this date: " + orderedOnDate);
                return;
            }

            ListView<Cake<Integer>> cakesListView = new ListView<>();
            ObservableList<Cake<Integer>> observableCakes = FXCollections.observableArrayList(cakesOrderedOnDate);
            cakesListView.setItems(observableCakes);

            Stage popupStage = new Stage();
            popupStage.setTitle("Cakes Ordered on " + orderedOnDate);

            VBox vbox = new VBox(cakesListView);
            vbox.setSpacing(10);
            vbox.setPadding(new javafx.geometry.Insets(10));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());
            vbox.getChildren().add(closeButton);

            Scene scene = new Scene(vbox, 400, 300);
            popupStage.setScene(scene);
            popupStage.show();

        } catch (DateTimeParseException e) {
            showError("Invalid date format! Please use the format YYYY-MM-DD.");
        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void ordersWithTheSameCakeHandler(ActionEvent event) {
        String cakeIdString = showInputDialog("Orders with the Same Cake", "Enter the Cake ID:");

        if (cakeIdString == null || cakeIdString.isBlank()) {
            showError("Cake ID cannot be empty!");
            return;
        }

        try {
            Integer cakeId = Integer.parseInt(cakeIdString);

            List<Order<Integer>> ordersWithSameCake = orderService.getOrdersThatHaveTheSameCake(cakeId);

            if (ordersWithSameCake.isEmpty()) {
                showError("No orders found for Cake ID: " + cakeId);
                return;
            }

            ListView<Order<Integer>> ordersListView = new ListView<>();
            ObservableList<Order<Integer>> observableOrders = FXCollections.observableArrayList(ordersWithSameCake);
            ordersListView.setItems(observableOrders);

            Stage popupStage = new Stage();
            popupStage.setTitle("Orders with Cake ID: " + cakeId);

            VBox vbox = new VBox(ordersListView);
            vbox.setSpacing(10);
            vbox.setPadding(new javafx.geometry.Insets(10));

            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());
            vbox.getChildren().add(closeButton);

            Scene scene = new Scene(vbox, 400, 300);
            popupStage.setScene(scene);
            popupStage.show();

        } catch (NumberFormatException e) {
            showError("Invalid Cake ID format! Please enter a valid number.");
        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }


    @FXML
    void totalRevenueFromPreparingOrdersHandler(ActionEvent event) {
        try {
            double totalRevenue = orderService.getTotalFromPreparingOrders();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Total Revenue from Preparing Orders");
            alert.setHeaderText("The total revenue from orders in 'preparing' status is:");
            alert.setContentText(String.format("%.2f", totalRevenue));

            alert.showAndWait();

        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void orderCountPerCakeHandler(ActionEvent event) {
        try {
            Map<Integer, Long> orderCountPerCake = orderService.getOrderCountPerCake();

            StringBuilder content = new StringBuilder();

            if (orderCountPerCake.isEmpty()) {
                content.append("No orders have been placed yet.");
            } else {
                for (Map.Entry<Integer, Long> entry : orderCountPerCake.entrySet()) {
                    content.append(String.format("Cake ID: %d - Orders: %d%n", entry.getKey(), entry.getValue()));
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Count Per Cake");
            alert.setHeaderText("The number of orders for each cake is:");
            alert.setContentText(content.toString());

            alert.showAndWait();

        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void orderCountPerCustomerHandler(ActionEvent event) {
        try {
            Map<String, Long> totalOrdersByCustomer = orderService.getTotalOrdersPlacedByCustomer();

            StringBuilder content = new StringBuilder();

            if (totalOrdersByCustomer.isEmpty()) {
                content.append("No orders have been placed yet.");
            } else {
                for (Map.Entry<String, Long> entry : totalOrdersByCustomer.entrySet()) {
                    content.append(String.format("Customer: %s - Orders: %d%n", entry.getKey(), entry.getValue()));
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Total Orders by Customer");
            alert.setHeaderText("Total orders placed by each customer:");
            alert.setContentText(content.toString());

            alert.showAndWait();

        } catch (RuntimeException e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }
}
