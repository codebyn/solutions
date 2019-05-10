package com.codebyn.simplestore.service;

import com.codebyn.simplestore.dao.Dao;
import com.codebyn.simplestore.model.Item;

import java.util.*;

import static com.codebyn.simplestore.service.CartLineItem.SINGLE_QUANTITY;

public class Cart {
    private Dao<Item> itemDao;
    private Map<String, CartLineItem> cartLineItems =  new HashMap<>();

    /**
     * Add item to cart and set quantity to 1
     * @param name
     */
    void addItem(String name) {
        this.addItem(name, SINGLE_QUANTITY);
    }

    /**
     * Add Item to cart; increments quantity if item already is
     * in cart
     * @param name
     * @param quantity
     */
    void addItem(String name, int quantity) {
        if(this.isItemInCart(name)) {
            this.findCartLineItem(name).increaseQuantityBy(quantity);
        } else if (this.itemDao.get(name).isPresent()) {
            Item item = this.itemDao.get(name).get();
            CartLineItem newLineItem = new CartLineItem(item, quantity);
            cartLineItems.put(name, newLineItem);
        }
    }

    /**
     * Check if item is in cart already
     * @param name
     * @return
     */
    public boolean isItemInCart(String name) {
        if(getCartLineItems().containsKey(name)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * Finds and returns a CartLineItem if present in cart
     * @param itemName
     * @return
     */
    public CartLineItem findCartLineItem(String itemName) {
        return cartLineItems.get(itemName);
    }

    /**
     * Remove if in cart
     * @param name
     * @throws InvalidItemQuantity
     * @throws ItemNotInCart
     */
    public void removeItem (String name) throws InvalidItemQuantity, ItemNotInCart {
        if(Optional.ofNullable(findCartLineItem(name)).isPresent()) {
            modifyCartItemQuantity(name, findCartLineItem(name).getQuantity());
        }
    }

    /**
     * Remove or reduce quantity of relevant CartLineItem
     * @param name
     * @param quantity
     * @throws InvalidItemQuantity
     * @throws ItemNotInCart
     */
    public void modifyCartItemQuantity(String name, int quantity) throws InvalidItemQuantity, ItemNotInCart {
        if(Optional.ofNullable(findCartLineItem(name)).isPresent()) {
            CartLineItem cartLineItem = findCartLineItem(name);
            if(cartLineItem.getQuantity() >= quantity) {
                cartLineItem.decreaseQuantityBy(quantity);
                if(cartLineItem.isNilQuantity()) {
                    this.getCartLineItems().remove(name);
                }
            } else {
                throw new InvalidItemQuantity("Quantity ("
                        + quantity
                        + ") to be decreased is more than original ("
                        + cartLineItem.getQuantity()
                        + ")");
            }
        } else {
            throw new ItemNotInCart("Item, "
                    + name
                    + " is not in cart");
        }
    }

    /**
     * Calculates cart total
     * @return
     */
    public double getCartTotal() {
        return cartLineItems.values().stream().mapToDouble(lineItem -> lineItem.getLineItemTotal()).sum();
    }

    
    public void clear(){
        this.getCartLineItems().clear();
    }

    public boolean isEmpty(){
        return this.getCartLineItems().isEmpty();
    }

    public CartLineItem[] getSortedCartLineItems() {
        return getCartLineItems().values().stream()
                .sorted(Comparator.comparing(CartLineItem::getName))
                //.collect(Collectors.toList());
                .toArray(CartLineItem[]::new);
    }

    public Map<String, CartLineItem> getCartLineItems() {
        return cartLineItems;
    }


    public Cart(Dao<Item> itemDao) {
        this.itemDao = itemDao;
    }

}
