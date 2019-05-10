package com.codebyn.simplestore.service;

public class ItemNotInInventory extends Exception{
    public ItemNotInInventory(String message) {
        super(message);
    }
}
