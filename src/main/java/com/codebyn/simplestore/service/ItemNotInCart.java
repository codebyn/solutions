package com.codebyn.simplestore.service;

public class ItemNotInCart extends Exception{
    public ItemNotInCart(String message) {
        super(message);
    }
}
