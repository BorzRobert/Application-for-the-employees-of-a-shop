package org.example.model.cart;

import org.example.model.item.Item;

import java.util.List;

public class CartBuilder {
    private Cart cart;

    public CartBuilder() {
        cart = new Cart();
    }

    public CartBuilder setCartId(Long id) {
        cart.setCartId(id);
        return this;
    }

    public CartBuilder setOwnerId(Long id) {
        cart.setOwnerId(id);
        return this;
    }

    public CartBuilder setItems(List<Item> items) {
        cart.setItems(items);
        return this;
    }

    public Cart build() {
        return cart;
    }

}
