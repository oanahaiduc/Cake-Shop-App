package cakeApp.service;

import cakeApp.domain.Order;
import cakeApp.domain.Cake;
import cakeApp.filters.orderFilters.FilterOrdersByStatus;
import cakeApp.filters.orderFilters.FilterOrderByCustomerName;
import cakeApp.repository.IRepository;
import cakeApp.repository.memory.InMemoryFilteredRepository;
import cakeApp.validators.OrderValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderService {
    private IRepository<Integer, Order<Integer>> orderRepository;
    private IRepository<Integer, Cake<Integer>> cakeRepository;
    private OrderValidator<Integer> orderValidator;
    private Integer currentOrderId;


    public OrderService(IRepository<Integer, Order<Integer>> orderRepository, IRepository<Integer, Cake<Integer>> cakeRepository) {
        this.orderRepository = orderRepository;
        this.cakeRepository = cakeRepository;
        this.orderValidator = new OrderValidator<>();
        this.currentOrderId = ((List<Order<Integer>>) orderRepository.findAll()).size() + 1;
    }

    public Integer createOrder(Integer cakeId, String customerName) {
        if (cakeRepository.findById(cakeId).isEmpty()) {
            throw new IllegalArgumentException("Cake ID " + cakeId + " not found");
        }
        Order<Integer> newOrder = new Order<>(cakeId, customerName);
        orderValidator.validate(newOrder);
        newOrder.setId(currentOrderId++);
        orderRepository.add(newOrder);
        return newOrder.getOrderId();
    }

    public Iterable<Order<Integer>> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order<Integer>> getOrderById(Integer idToViewBy) {
        return orderRepository.findById(idToViewBy);
    }

    public void cancelOrder(Integer idToBeCancelled) {
        Optional<Order<Integer>> orderToBeCancelled = orderRepository.findById(idToBeCancelled);
        if (orderToBeCancelled.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + idToBeCancelled + " not found");
        }
        Order<Integer> order = orderToBeCancelled.get();
        order.setOrderStatus("cancelled");
        orderValidator.validate(order);
        orderRepository.modify(order);
    }

    public void finishOrder(Integer idToBeFinished) {
        Optional<Order<Integer>> orderToBeFinished = orderRepository.findById(idToBeFinished);
        if (orderToBeFinished.isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + idToBeFinished + " not found");
        }
        Order<Integer> order = orderToBeFinished.get();
        order.setOrderStatus("finished");
        orderValidator.validate(order);
        orderRepository.modify(order);
    }

    public void deleteOrder(Integer idToBeDeleted) {
        if (orderRepository.findById(idToBeDeleted).isEmpty()) {
            throw new IllegalArgumentException("Order with ID " + idToBeDeleted + " not found");
        }
        orderRepository.delete(idToBeDeleted);
    }

    public Iterable<Order<Integer>> filterByStatus(String statusToFilterBy) {
        FilterOrdersByStatus<Integer> statusFilter = new FilterOrdersByStatus<>(statusToFilterBy);

        InMemoryFilteredRepository<Integer, Order<Integer>> filteredRepository = new InMemoryFilteredRepository<>(orderRepository, statusFilter);
        return filteredRepository.findAll();
    }

    public Iterable<Order<Integer>> filterByCustomerName(String customerNameToFilterBy) {
        FilterOrderByCustomerName<Integer> customerFilter = new FilterOrderByCustomerName<>(customerNameToFilterBy);
        InMemoryFilteredRepository<Integer, Order<Integer>> filteredRepository = new InMemoryFilteredRepository<>(orderRepository, customerFilter);
        return filteredRepository.findAll();
    }

    public List<Cake<Integer>> getCakesOrderedOnDate(LocalDate orderedOnDate) {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getOrderDate().toLocalDate().equals(orderedOnDate))
                .map(order -> cakeRepository.findById(order.getCakeId()).orElse(null))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Order<Integer>> getOrdersThatHaveTheSameCake(Integer cakeId) {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getCakeId().equals(cakeId))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getTotalOrdersPlacedByCustomer() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getCustomerName, Collectors.counting()));
    }

    public double getTotalFromPreparingOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> "preparing".equalsIgnoreCase(order.getOrderStatus()))
                .mapToDouble(order -> cakeRepository.findById(order.getCakeId())
                        .map(Cake::getPrice).orElse(0.0))
                .sum();
    }

    public Map<Integer, Long> getOrderCountPerCake(){
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .collect(Collectors.groupingBy(Order::getCakeId, Collectors.counting()));
    }

}
