package cakeApp.filters.orderFilters;

import cakeApp.domain.Order;
import cakeApp.filters.Filter;

public class FilterOrderByCustomerName<ID> implements Filter<Order<ID>> {
    private final String customerName;

    public FilterOrderByCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public boolean accept(Order<ID> order) {
        return order.getCustomerName().equalsIgnoreCase(customerName);
    }
}
