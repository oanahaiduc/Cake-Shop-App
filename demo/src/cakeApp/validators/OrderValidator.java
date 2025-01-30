package cakeApp.validators;

import cakeApp.domain.Order;

public class OrderValidator<ID> {
    public void validate(Order<ID> order){
        validateOrder(order);
    }

    private void validateOrder(Order<ID> order){
        if(order.getCakeId() == null){
            throw new IllegalArgumentException("Cake ID cannot be null!");
        }
        if(order.getCustomerName() == null){
            throw new IllegalArgumentException("Customer name cannot be empty!");
        }
        if(order.getOrderStatus() == null || ( !order.getOrderStatus().equalsIgnoreCase("finished") &&
                !order.getOrderStatus().equalsIgnoreCase("preparing") &&
                !order.getOrderStatus().equalsIgnoreCase("cancelled"))) {
            throw new IllegalArgumentException("Invalid order status!");
        }
        if(order.getOrderDate() == null){
            throw new IllegalArgumentException("Order date cannot be null!");
        }
    }
}
