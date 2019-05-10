package com.codebyn.simplestore.model;

import java.util.Objects;

public class Item implements Comparable<Item>{
    private final String name;
    private double cost;

    public Item(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void updateCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return getName().equals(item.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }
}
