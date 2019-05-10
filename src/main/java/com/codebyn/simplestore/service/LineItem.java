package com.codebyn.simplestore.service;

interface LineItem
{
    /*
     * Get unique item name (ID).
     */
    String getName();
    /*
     * Get item quantity.
     */
    int getQuantity();
}
