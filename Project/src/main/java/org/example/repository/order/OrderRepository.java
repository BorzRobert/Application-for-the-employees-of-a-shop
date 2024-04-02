package org.example.repository.order;

import org.example.model.item.Item;
import org.example.model.order.Order;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface OrderRepository {

    Order create(Order order) throws SQLException;

    List<Order> findAllOrders() throws SQLException;

    public void deleteOrder(Long id) throws SQLException;

    public void addProcessedOrder(Long orderId, Long userId, Long cashierId, Long numberOfItems, Long totalValue, Date processingDate) throws SQLException;

    public void addItemsToOrder(Order order, List<Item> items) throws SQLException;

    public List<Item> getItemsFromOrder(Long orderId) throws SQLException;
    void deleteAll();
}
