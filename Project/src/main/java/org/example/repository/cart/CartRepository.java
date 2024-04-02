package org.example.repository.cart;

import org.example.model.cart.Cart;
import org.example.model.item.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface CartRepository {

    public List<Cart> findAll();

    public Cart getCartFromResultSet(ResultSet rs) throws SQLException;
    Cart create(Cart order) throws SQLException;

    Cart findUserCart(Long id) throws SQLException;

    public void deleteItemsFromCart(Long id) throws SQLException;

    public List<Long> getItemsIdsFromCart(Long id) throws SQLException;

    public List<Item> getItemsFromCart(List<Long> itemsIdsFromCart) throws SQLException;

    public void addItemToCart(Long cartId, Long itemId) throws SQLException;

    void deleteAll();
}
