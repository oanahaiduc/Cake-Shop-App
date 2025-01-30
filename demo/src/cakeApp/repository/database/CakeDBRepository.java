package cakeApp.repository.database;

import cakeApp.domain.Cake;
import cakeApp.repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CakeDBRepository implements IRepository<Integer, Cake<Integer>> {
    private final String url;

    public CakeDBRepository(String url) {
        this.url = url;
    }

    @Override
    public Integer add (Cake<Integer> cake) {
        String sql = "INSERT INTO cake(cakeID, cakeName, cakeFlavour, cakePrice, cakeLayers) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(url);
        PreparedStatement prepStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            prepStatement.setInt(1, cake.getId());
            prepStatement.setString(2, cake.getCakeName());
            prepStatement.setString(3, cake.getCakeFlavour());
            prepStatement.setDouble(4, cake.getPrice());
            prepStatement.setInt(5, cake.getCakeLayers());

            prepStatement.executeUpdate();
            return cake.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Cake<Integer>> findAll() {
        List<Cake<Integer>> cakes = new ArrayList<>();
        String sql = "SELECT * FROM cake";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {
                Cake<Integer> cake = new Cake<>(
                        result.getInt("cakeID"),
                        result.getString("cakeName"),
                        result.getString("cakeFlavour"),
                        result.getDouble("cakePrice"),
                        result.getInt("cakeLayers")
                );
                cakes.add(cake);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cakes;
    }

    @Override
    public Optional<Cake<Integer>> findById(Integer id) {
        String sql = "SELECT * FROM cake WHERE cakeID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prepStatement = conn.prepareStatement(sql)) {

            prepStatement.setLong(1, id);
            ResultSet result = prepStatement.executeQuery();
            if (result.next()) {
                Cake<Integer> cake = new Cake<>(
                        result.getInt("cakeID"),
                        result.getString("cakeName"),
                        result.getString("cakeFlavour"),
                        result.getDouble("cakePrice"),
                        result.getInt("cakeLayers")
                );
                return Optional.of(cake);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void modify(Cake<Integer> updatedCake) {
        String sql = "UPDATE cake SET cakeName = ?, cakeFlavour = ?, cakePrice = ?, cakeLayers = ? WHERE cakeID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prepStatement = conn.prepareStatement(sql)) {

            prepStatement.setString(1, updatedCake.getCakeName());
            prepStatement.setString(2, updatedCake.getCakeFlavour());
            prepStatement.setDouble(3, updatedCake.getPrice());
            prepStatement.setInt(4, updatedCake.getCakeLayers());
            prepStatement.setLong(5, updatedCake.getId());

            prepStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM cake WHERE cakeID = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prepStatement = conn.prepareStatement(sql)) {

            prepStatement.setLong(1, id);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
