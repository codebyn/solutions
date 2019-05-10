package com.codebyn.simplestore.service;

import com.codebyn.simplestore.dao.Dao;
import com.codebyn.simplestore.model.Item;

import java.util.Optional;

public class SimpleStore implements Store {
    private Cart cart;

    /**
     * Simple DAO to access in-memory data
     * Replace with other DAO implementations for other data repositories
     */
    private Dao<Item> itemDao;

    public SimpleStore(Dao<Item> itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void addItemToCart(String name, int quantity) {
        if(itemInInventory(name)) {
            this.getCart().addItem(name, quantity);
        } /*else {
            throw new ItemNotInInventory("Item, "
                    + name
                    + " is not in inventory");
        }*/
    }

    /**
     * Removes if item in cart
     * @param name
     */
    public void removeItemFromCart(String name) {
        if(isItemInCart(name)) {
            modifyCartQuantity(name, getCartItemQuantity(name).get());
        }/*else {
            throw new ItemNotInInventory("Item, "
                    + name
                    + " is not in inventory");
        }*/
    }

    /**
     * Removes item quantity if in cart
     * @param name
     * @param quantity
     */
    public void modifyCartQuantity(String name, int quantity) {
        try {
            getCart().modifyCartItemQuantity(name, quantity);
        } catch (InvalidItemQuantity invalidItemQuantity) {
            //Do nothing currently
        } catch (ItemNotInCart itemNotInCart) {
            //Do nothing currently
        }
    }

    @Override
    public void addItemToStore(String name, double price) {
        if(price >= 0) {
            Item item = new Item(name, price);
            getItemDao().save(item);
        } /*else {
            throw new InvalidItemPrice("Invalid item price: "
                    + price);
        }*/
    }

    public void deleteItemFromStore(String name) {
        if(itemInInventory(name)) {
            Item item = getItemDao().get(name).get();
            getItemDao().delete(item);
        } /*else {
            throw new ItemNotInInventory("Item, "
                    + name
                    + " is not in inventory");
        }*/
    }

    public void deleteItemFromInventory(String name) {
        if(itemInInventory(name)) {
            if(getCart().isItemInCart(name)) {
                removeItemFromCart(name);
            }

            getItemDao().delete(getItemFromInventoryIfPresent(name).get());

        } /*else {
            throw new ItemNotInInventory("Item, "
                    + name
                    + " is not in inventory");
        }*/
    }

    public boolean isItemInCart(String name) {
        return getCart().isItemInCart(name);
    }

    public Optional<LineItem> getCartLineItem(String name) {
        return Optional.ofNullable(getCart().findCartLineItem(name));
    }

    public Optional<Integer> getCartItemQuantity(String name) {
        return Optional.ofNullable(getCart().findCartLineItem(name).getQuantity());
    }

    @Override
    public LineItem[] getCartItems() {
        return this.getCart().getSortedCartLineItems();
    }

    @Override
    public double getCartTotal() {
        return getCart().getCartTotal();
    }

    public Optional<Item> getItemFromInventoryIfPresent(String name) {
        return getItemDao().get(name);
    }

    public boolean itemInInventory(String name) {
        return getItemDao().get(name).isPresent();
    }

    public void clearCart() {
        getCart().clear();
    }

    private Dao<Item> getItemDao() {
        return itemDao;
    }

    public void setItemDao(Dao<Item> itemDao) {
        this.itemDao = itemDao;
    }

    private Cart getCart() {
        if(this.cart == null) {
            this.cart = new Cart(getItemDao());
        }
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}



