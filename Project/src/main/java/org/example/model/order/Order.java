package org.example.model.order;

import org.example.model.item.Item;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private Long orderId;
    private Long ownerId;
    private List<Item> items;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderNumber) {
        this.orderId = orderNumber;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerID) {
        this.ownerId = ownerID;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long orderCost(){
        Long sum = 0L;
        for (Item item: this.getItems()) {
            sum += item.getPrice();
        }
        return sum;
    }

    public Long orderNumberOfItems(){
        Long sum = 0L;
        for (Item item: this.getItems()) {
            sum += 1;
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Number= " + orderId + "  " +
                "Owner Id= " + ownerId;
    }
}
