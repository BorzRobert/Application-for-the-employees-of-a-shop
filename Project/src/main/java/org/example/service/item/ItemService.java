package org.example.service.item;


import org.example.model.item.Item;
import org.example.repository.item.ItemRepository;

import java.sql.SQLException;
import java.util.List;

public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> findAll() {
        return repository.findAll();
    }
}
