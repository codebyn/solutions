package com.codebyn.simplestore.service;

public class InvalidItemPrice extends Exception{
    public InvalidItemPrice(String message) {
        super(message);
    }
}
