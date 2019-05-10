package com.codebyn.simplestore.service;

public class InvalidItemQuantity extends Exception{
    public InvalidItemQuantity(String message) {
        super(message);
    }
}
