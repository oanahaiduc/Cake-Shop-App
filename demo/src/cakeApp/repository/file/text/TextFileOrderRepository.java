package cakeApp.repository.file.text;

import cakeApp.domain.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextFileOrderRepository extends TextFileRepository<Integer, Order<Integer>> {
    private static final DateTimeFormatter  DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TextFileOrderRepository(String filename) {
        super(filename);
        super.readFromFile();
    }

    @Override
    protected Order<Integer> parseEntity(String line) {
        String[] tokens = line.split(",");

        Integer orderId = Integer.parseInt(tokens[0].trim());
        Integer cakeId = Integer.parseInt(tokens[1].trim());
        String customerName = tokens[2].trim();
        String orderStatus = tokens[3].trim();
        LocalDateTime orderDate = LocalDateTime.parse(tokens[4].trim(), DATE_TIME_FORMATTER);

        Order<Integer> order = new Order<>(cakeId, customerName);
        order.setId(orderId);
        order.setOrderStatus(orderStatus);
        order.setOrderDate(orderDate);
        return order;
    }
}
