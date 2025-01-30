package cakeApp.repository.database;

import cakeApp.domain.Order;
import cakeApp.repository.IRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDBRepository implements IRepository<Integer, Order<Integer>> {
    private final String url;

    public OrderDBRepository(String url) {
        this.url = url;
    }

    @Override
    public Integer add(Order<Integer> order) {
        String sql = "INSERT INTO \"order\" (orderID, cakeID, customerName, orderStatus, orderDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1,order.getId());
            pstmt.setInt(2, order.getCakeId());
            pstmt.setString(3, order.getCustomerName());
            pstmt.setString(4, order.getOrderStatus());
            pstmt.setString(5, order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            pstmt.executeUpdate();
            return order.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<Order<Integer>> findAll() {
        List<Order<Integer>> orders = new ArrayList<>();
        String sql = "SELECT * FROM \"order\"";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order<Integer> order = new Order<>(
                        rs.getInt("cakeID"),
                        rs.getString("customerName")
                );
                order.setId(rs.getInt("orderID"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setOrderDate(LocalDateTime.parse(rs.getString("orderDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Optional<Order<Integer>> findById(Integer id) {
        String sql = "SELECT * FROM \"order\" WHERE orderID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Order<Integer> order = new Order<>(
                        rs.getInt("cakeID"),
                        rs.getString("customerName")
                );
                order.setId(rs.getInt("orderID"));
                order.setOrderStatus(rs.getString("orderStatus"));
                order.setOrderDate(LocalDateTime.parse(rs.getString("orderDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return Optional.of(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void modify(Order<Integer> updatedOrder) {
        String sql = "UPDATE \"order\" SET cakeID = ?, customerName = ?, orderStatus = ?, orderDate = ? WHERE orderID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, updatedOrder.getCakeId());
            pstmt.setString(2, updatedOrder.getCustomerName());
            pstmt.setString(3, updatedOrder.getOrderStatus());
            pstmt.setString(4, updatedOrder.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setInt(5, updatedOrder.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM \"order\" WHERE orderID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}