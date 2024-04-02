package org.example.repository.cart;

import org.example.model.cart.Cart;
import org.example.model.cart.CartBuilder;
import org.example.model.item.Item;
import org.example.model.item.ItemBuilder;
import org.example.model.order.Order;
import org.example.model.security.Role;
import org.example.repository.item.ItemRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Constants.TABLES.*;

public class CartRepositorySQL implements CartRepository {

    private final Connection connection;

    private final ItemRepository itemRepository;

    public CartRepositorySQL(Connection connection, ItemRepository itemRepository) {
        this.connection = connection;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Cart> findAll() {
        final String sql = "Select * from cart";

        List<Cart> carts = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                carts.add(getCartFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return carts;
    }

    public Cart getCartFromResultSet(ResultSet rs) throws SQLException {
        return new CartBuilder()
                .setCartId(rs.getLong("id"))
                .setOwnerId(rs.getLong("user_id"))
                .build();
    }

    @Override
    public Cart create(Cart cart) throws SQLException {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + CART + " values (null, ?)", Statement.RETURN_GENERATED_KEYS);
            insertStatement.setLong(1, cart.getOwnerId());
            insertStatement.executeUpdate();

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            generatedKeys.next();
            long itemId = generatedKeys.getLong(1);
            cart.setCartId(itemId);

            return cart;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public Cart findUserCart(Long id) throws SQLException {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + CART + " where `user_id`=\'" + id + "\'";
            ResultSet cartResultSet = statement.executeQuery(fetchRoleSql);
            cartResultSet.next();
            Long cartId = cartResultSet.getLong("id");
            Cart cart = new CartBuilder()
                    .setCartId(cartId)
                    .setOwnerId(id)
                    .setItems(getItemsFromCart(getItemsIdsFromCart(cartId)))
                    .build();

            return cart;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public List<Item> getItemsFromCart(List<Long> itemsIdsFromCart) throws SQLException {
        Statement statement;
        try {
            List<Item> itemsFromCart = new ArrayList<Item>();
            statement = connection.createStatement();
            for (Long itemId : itemsIdsFromCart) {
                String fetchItemSql = "Select * from " + ITEM + " where `id`=\'" + itemId + "\'";
                ResultSet itemResultSet = statement.executeQuery(fetchItemSql);
                itemResultSet.next();
                itemsFromCart.add(itemRepository.getItemFromResultSet(itemResultSet));
            }

            return itemsFromCart;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void deleteItemsFromCart(Long id) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from cart_item where cart_id=" + id;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getItemsIdsFromCart(Long id) throws SQLException {
        Statement statement;
        try {
            List<Long> itemsIdsFromCart = new ArrayList<Long>();
            statement = connection.createStatement();
            String fetchCartSql = "Select * from " + CART_ITEM + " where `cart_id`=\'" + id + "\'";
            ResultSet cartResultSet = statement.executeQuery(fetchCartSql);
            while (cartResultSet.next()) {
                Long itemId = cartResultSet.getLong("item_id");
                itemsIdsFromCart.add(itemId);
            }

            return itemsIdsFromCart;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    public void addItemToCart(Long cartId, Long itemId) throws SQLException {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT IGNORE INTO " + CART_ITEM + " values (null, ?, ?)");
            insertStatement.setLong(1, cartId);
            insertStatement.setLong(2, itemId);
            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from cart where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
