package org.example.model.item;

public class Item {

  private Long id;
  private String manufacturer;
  private String name;
  private Long price;

  public Long getId() {
    return id;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public String getName() {
    return name;
  }

  public Long getPrice() {
    return price;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Name= " + name + "  " +
            "Manufacturer= " + manufacturer + "  " +
            "Price= " + price + "$";
  }
}
