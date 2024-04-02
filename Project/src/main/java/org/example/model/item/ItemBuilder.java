package org.example.model.item;

import java.time.LocalDate;

public class ItemBuilder {

  private final Item item;

  public ItemBuilder() {
    item = new Item();
  }

  public ItemBuilder setId(Long id) {
    item.setId(id);
    return this;
  }

  public ItemBuilder setManufacturer(String manufacturer) {
    item.setManufacturer(manufacturer);
    return this;
  }

  public ItemBuilder setName(String name) {
    item.setName(name);
    return this;
  }

  public ItemBuilder setPrice(Long price) {
    item.setPrice(price);
    return this;
  }

  public Item build() {
    return item;
  }
}
