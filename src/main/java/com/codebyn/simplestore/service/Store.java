package com.codebyn.simplestore.service;

interface Store
{
    /*
     * Add item with given unique name (ID) and quantity into
     * cart. Name is case-sensitive.
     */
    void addItemToCart(String name, int quantity);
    /*
     * Add item with given unique name (ID) and price into store
     * inventory. Name is case-sensitive.
     */
    void addItemToStore(String name, double price);
    /*
     * Get items in cart, consolidating all duplicate items,
     * ordered by item name (ascending).
     */
    LineItem[] getCartItems();
    /*
     * Get total cost of items currently in cart.
     */
    double getCartTotal();
}
