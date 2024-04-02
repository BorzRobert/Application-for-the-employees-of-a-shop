package org.example.model.cart;

import org.example.model.item.Item;
import org.example.model.security.Role;

import java.util.List;

public class Cart {
    private Long cartId;
    private Long ownerId;
    private List<Item> items;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
