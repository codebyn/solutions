package com.codebyn.simplestore.dao;

import com.codebyn.simplestore.model.Item;

import java.util.*;
import java.util.stream.Collectors;

//In-Memory Dao
public class ItemDao implements Dao <Item>{
    private Map<String,Item> items = new HashMap<>();

    public ItemDao() {
    }

    public ItemDao(Map<String,Item> items) {
        this.items = items;
    }

    @Override
    public Optional<Item> get(String name) {
        return Optional.ofNullable(items.get(name));
    }

    @Override
    public void save(Item item) {
        items.put(item.getName(),item);
    }

    @Override
    public void update(Item item) {
        items.put(item.getName(), item);
    }

    @Override
    public void delete(Item item) {
        items.remove(item.getName());
    }

    @Override
    public List<Item> getAll() {
        return items.values().stream().collect(Collectors.toList());
    }
}
