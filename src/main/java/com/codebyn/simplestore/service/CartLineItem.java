package com.codebyn.simplestore.service;

import com.codebyn.simplestore.model.Item;

import java.util.Objects;

public class CartLineItem implements LineItem {
    public static final int SINGLE_QUANTITY = 1;
    public static final int EMPTY = 0;

    private Item item;
    private int quantity;

    public CartLineItem(Item item) {
        this(item, SINGLE_QUANTITY);
    }

    public CartLineItem(Item item, int quantity) {
        this.item = item;
        this.increaseQuantityBy(quantity);
    }

    public void increaseQuantityBy(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantityBy(int quantity) {
        if(this.getQuantity() >= quantity) {
            this.quantity -= quantity;
        }
    }

    public boolean isNilQuantity() {
        return this.getQuantity() <= EMPTY;
    }

    public double getLineItemTotal() {
        return this.item.getCost() * quantity;
    }

    @Override
    public String getName() {
        return item.getName();
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartLineItem that = (CartLineItem) o;
        return getItem().equals(that.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem());
    }

    @Override
    public String toString() {
        return "CartLineItem{" +
                "item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
