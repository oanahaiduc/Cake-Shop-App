package cakeApp.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Order<ID> implements Identifiable<ID>, Serializable {
    private ID orderId;
    private ID cakeId;
    private String customerName;
    private String orderStatus;
    private LocalDateTime orderDate;


    public static DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Order(ID cakeId, String customerName) {
        this.customerName = customerName;
        this.cakeId = cakeId;
        this.orderStatus = "preparing";
        this.orderDate = LocalDateTime.now();
    }

    @Override
    public ID getId(){
        return orderId;
    }

    @Override
    public void setId(ID orderId){
        this.orderId = orderId;
    }

    public ID getCakeId() {
        return cakeId;
    }

    public ID getOrderId() {
        return orderId;
    }

    public void setCakeId(ID cakeId) {
        this.cakeId = cakeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return orderId + "," + cakeId + "," + customerName + "," + orderStatus + "," + orderDate.format(DATE_TIME_FORMATTER);
    }
}
