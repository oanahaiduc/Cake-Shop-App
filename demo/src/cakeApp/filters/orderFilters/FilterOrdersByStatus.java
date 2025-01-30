package cakeApp.filters.orderFilters;

import cakeApp.domain.Order;
import cakeApp.filters.Filter;

public class FilterOrdersByStatus<ID> implements Filter<Order<ID>> {
    private final String filterStatus;

    public FilterOrdersByStatus(String desiredStatus) {
        this.filterStatus = desiredStatus;
    }

    @Override
    public boolean accept(Order<ID> order) {
        return order.getOrderStatus().equalsIgnoreCase(filterStatus);
    }
}
