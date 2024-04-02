package org.example.model.order;

import org.example.model.item.Item;

import java.util.List;

public class OrderBuilder {
    private Order order;

    public OrderBuilder(){order = new Order();}

    public OrderBuilder setOrderId(Long id){
        order.setOrderId(id);
        return this;
    }

    public OrderBuilder setOwnerId(Long id){
        order.setOwnerId(id);
        return this;
    }

    public OrderBuilder setItems(List<Item> items){
        order.setItems(items);
        return this;
    }

    public Order build() {
        return order;
    }

}
