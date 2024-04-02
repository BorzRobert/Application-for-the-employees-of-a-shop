package org.example.repository.item;

import org.example.model.cart.Cart;
import org.example.model.item.Item;
import org.example.model.order.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findAll();

    Item create(Item item) throws SQLException;

    public Item getItemFromResultSet(ResultSet rs) throws SQLException;

    void removeAll();

}
