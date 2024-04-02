package org.example.repository.item;

import org.example.model.cart.Cart;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.model.order.Order;
import org.example.model.security.Role;
import org.example.model.security.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.database.Constants.TABLES.*;

public class ItemRepositorySQL implements ItemRepository {

    private final Connection connection;

    public ItemRepositorySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Item> findAll() {
        final String sql = "Select * from item";

        List<Item> items = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                items.add(getItemFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return items;
    }

    @Override
    public Item create(Item item) throws SQLException {
        String sql = "INSERT INTO item values (null, ?, ?, ?)";

        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, item.getManufacturer());
            insertStatement.setString(2, item.getName());
            insertStatement.setLong(3, item.getPrice());
            insertStatement.executeUpdate();

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            generatedKeys.next();
            long itemId = generatedKeys.getLong(1);
            item.setId(itemId);
            return item;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void removeAll() {
        String sql = "DELETE from item where id >= 0";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Item getItemFromResultSet(ResultSet rs) throws SQLException {
        return new ItemBuilder()
                .setId(rs.getLong("id"))
                .setManufacturer(rs.getString("manufacturer"))
                .setName(rs.getString("name"))
                .setPrice(rs.getLong("price"))
                .build();
    }
}
