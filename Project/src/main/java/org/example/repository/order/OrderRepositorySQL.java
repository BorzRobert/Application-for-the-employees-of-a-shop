package org.example.repository.order;

import org.example.model.item.Item;
import org.example.model.order.Order;
import org.example.model.order.OrderBuilder;
import org.example.repository.item.ItemRepository;
import org.example.repository.security.RoleRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Constants.TABLES.*;

public class OrderRepositorySQL implements OrderRepository {

    private final Connection connection;

    private final ItemRepository itemRepository;

    public OrderRepositorySQL(Connection connection, ItemRepository itemRepository) {
        this.connection = connection;
        this.itemRepository = itemRepository;
    }

    @Override
    public Order create(Order order) throws SQLException {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + ORDER + " values (null, ?)");
            insertStatement.setLong(1, order.getOwnerId());
            insertStatement.executeUpdate();

            Statement statement;
            statement = connection.createStatement();
            String fetchOrderSql = "SELECT * FROM order_ ORDER BY id DESC LIMIT 1;";
            ResultSet orderResultSet = statement.executeQuery(fetchOrderSql);
            orderResultSet.next();
            Long id = orderResultSet.getLong("id");
            order.setOrderId(id);

            addItemsToOrder(order, order.getItems());

            return order;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void addProcessedOrder(Long orderId, Long userId, Long cashierId, Long numberOfItems, Long totalValue, Date processingDate) throws SQLException {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + PROCESSED_ORDERS + " values (null, ?, ?, ?, ?, ?, ?)");
            insertStatement.setLong(1, orderId);
            insertStatement.setLong(2, userId);
            insertStatement.setLong(3, cashierId);
            insertStatement.setLong(4, numberOfItems);
            insertStatement.setLong(5, totalValue);
            insertStatement.setDate(6, processingDate);
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public List<Order> findAllOrders() throws SQLException {
        Statement statement;
        try {
            List<Order> allOrders = new ArrayList<Order>();
            statement = connection.createStatement();
            String fetchOrderSql = "SELECT * FROM " + ORDER;
            ResultSet orderResultSet = statement.executeQuery(fetchOrderSql);
            while (orderResultSet.next()) {
                allOrders.add(new OrderBuilder()
                        .setOwnerId(orderResultSet.getLong("user_id"))
                        .setOrderId(orderResultSet.getLong("id"))
                        .setItems(getItemsFromOrder(orderResultSet.getLong("id")))
                        .build());
            }
            return allOrders;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

    }

    public void deleteOrder(Long id) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from order_ where id=" + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addItemsToOrder(Order order, List<Item> items) throws SQLException {

        for (Item item : items) {
            try {
                PreparedStatement insertStatement = connection
                        .prepareStatement("INSERT IGNORE INTO " + ORDER_ITEM + " values (null, ?, ?)");
                insertStatement.setLong(1, order.getOrderId());
                insertStatement.setLong(2, item.getId());
                insertStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException(e);
            }
        }

    }

    public List<Item> getItemsFromOrder(Long orderId) throws SQLException {
        Statement statement;
        try {
            List<Item> itemsFromOrder = new ArrayList<Item>();
            statement = connection.createStatement();

            String fetchItemSql = "SELECT i.id, i.manufacturer, i.name, i.price " +
                    "FROM " + ORDER + " o " +
                    "JOIN " + ORDER_ITEM + " oi ON o.id = oi.order_id " +
                    "JOIN " + ITEM + " i ON oi.item_id = i.id " +
                    "WHERE o.id =" + orderId + ";";
            ResultSet itemResultSet = statement.executeQuery(fetchItemSql);
            while (itemResultSet.next()) {
                itemsFromOrder.add(itemRepository.getItemFromResultSet(itemResultSet));
            }


            return itemsFromOrder;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from order_ where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
