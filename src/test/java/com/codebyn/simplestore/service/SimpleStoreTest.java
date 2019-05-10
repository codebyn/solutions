package com.codebyn.simplestore.service;

import com.codebyn.simplestore.dao.Dao;
import com.codebyn.simplestore.dao.ItemDao;
import com.codebyn.simplestore.model.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.*;

class SimpleStoreTest {
    public static final String X_WALLPAPER = "X Wallpaper";
    public static final String A_WALLPAPER = "A Wallpaper";
    public static final String B_WALLPAPER = "B Wallpaper";
    public static final String C_WALLPAPER = "C Wallpaper";
    public static final String D_WALLPAPER = "D Wallpaper";
    public static final String ITEM_DOESNT_EXIST = "Item Doesn't' Exist";

    public static final double X_PRICE = 1.0;
    public static final double X_NEW_PRICE = 10.0;
    public static final double A_PRICE = 10.0;
    public static final double B_PRICE = 20.0;
    public static final double C_PRICE = 30.0;
    public static final double D_PRICE = 40.0;

    public static final int A_QUANTITY = 10;
    public static final int B_QUANTITY = 100;
    public static final int C_QUANTITY = 1000;
    public static final int D_QUANTITY = 10000;
    public static final int NEGATIVE_QUANTITY = -100000;

    private static Dao<Item> itemDao = new ItemDao();
    private static SimpleStore simpleStore = new SimpleStore(itemDao);

    @BeforeAll
    public static void setUpInventory() {
        simpleStore.addItemToStore(A_WALLPAPER, A_PRICE);
        simpleStore.addItemToStore(B_WALLPAPER, B_PRICE);
        simpleStore.addItemToStore(C_WALLPAPER, C_PRICE);
        simpleStore.addItemToStore(D_WALLPAPER, D_PRICE);
    }

    @Test
    void notInInventory_AddItemToStore_AddsItem() {
        simpleStore.addItemToStore(X_WALLPAPER, X_PRICE);
        assertTrue(simpleStore.itemInInventory(X_WALLPAPER));
    }

    @Test
    void inInventoryButDifferentPrice_addItemToStore_updatesItemPrice() {
        simpleStore.addItemToStore(X_WALLPAPER, X_NEW_PRICE);
        double actual = simpleStore.getItemFromInventoryIfPresent(X_WALLPAPER)
                                    .get()
                                    .getCost();
        assertEquals(X_NEW_PRICE, actual);
    }

    @Test
    void itemInInventory_deleteItemFromInventory_Deletes() {
        simpleStore.deleteItemFromInventory(X_WALLPAPER);
        assertFalse(simpleStore.itemInInventory(X_WALLPAPER));
    }

    @Test
    void notInInventory_deleteItemFromInventory_DoesNothing() {
        simpleStore.addItemToStore(X_WALLPAPER, X_NEW_PRICE);
        simpleStore.deleteItemFromInventory(X_WALLPAPER);
        assertFalse(simpleStore.itemInInventory(X_WALLPAPER));
    }


    @Test
    void notInInventory_addItemToCart_DoesntAdd() {
        simpleStore.addItemToCart(ITEM_DOESNT_EXIST, 555);
        assertFalse(simpleStore.isItemInCart(ITEM_DOESNT_EXIST));
    }

    @Test
    void itemInInventory_AddtoCart_AddsItem() {
        simpleStore.addItemToCart(A_WALLPAPER, A_QUANTITY);
        assertTrue(simpleStore.isItemInCart(A_WALLPAPER));
    }

    @Test
    void inCart_AddtoCart_UpdatesQuantity() {
        simpleStore.clearCart();
        simpleStore.addItemToCart(A_WALLPAPER, A_QUANTITY);

        int expectedQuantityBeforeUpdate = A_QUANTITY;
        int actualBeforeUpdate = simpleStore.getCartItemQuantity(A_WALLPAPER).get();
        assertEquals(expectedQuantityBeforeUpdate,actualBeforeUpdate);

        simpleStore.addItemToCart(A_WALLPAPER, A_QUANTITY);
        int expected = A_QUANTITY + A_QUANTITY;
        int actual = simpleStore.getCartItemQuantity(A_WALLPAPER).get();
        assertEquals(expected,actual);
    }

    @Test
    void inCart_RemoveItem_RemovesLineItem() {
        setUpCart();
        double cartTotal = simpleStore.getCartTotal();
        simpleStore.removeItemFromCart(A_WALLPAPER);
        assertFalse(simpleStore.getCartLineItem(A_WALLPAPER).isPresent());

        double expectedCartTotalAfterRemoving = cartTotal - A_QUANTITY*A_PRICE;
        double actual = simpleStore.getCartTotal();
        assertEquals(expectedCartTotalAfterRemoving,actual);
    }

    @Test
    void inCart_RemoveFullQuantity_RemovesLineItem() {
        setUpCart();
        double cartTotal = simpleStore.getCartTotal();

        simpleStore.modifyCartQuantity(A_WALLPAPER, A_QUANTITY);
        assertFalse(simpleStore.getCartLineItem(A_WALLPAPER).isPresent());

        double expectedCartTotalAfterRemoving = cartTotal - A_QUANTITY*A_PRICE;
        double actual = simpleStore.getCartTotal();
        assertEquals(expectedCartTotalAfterRemoving,actual);
    }

    @Test
    void inCart_ModifyItemQuantity_UpdatesLineItemQuantity() {
        setUpCart();
        double cartTotal = simpleStore.getCartTotal();

        int quantityToReduceBy = A_QUANTITY - A_QUANTITY / 2;
        simpleStore.modifyCartQuantity(A_WALLPAPER, quantityToReduceBy);
        assertTrue(simpleStore.getCartLineItem(A_WALLPAPER).isPresent());

        double expectedCartTotalAfterRemoving = cartTotal - quantityToReduceBy*A_PRICE;
        double actual = simpleStore.getCartTotal();
        assertEquals(expectedCartTotalAfterRemoving,actual);
    }

    @Test
    void itemsInCart_getCartTotal_ReturnsTotalCost() {
        setUpCart();
        double expected = (A_PRICE * A_QUANTITY)
                            + (B_PRICE * B_QUANTITY)
                            + (C_PRICE * C_QUANTITY)
                            + (D_PRICE * D_QUANTITY);
        double actual = simpleStore.getCartTotal();
        assertEquals(expected, actual);
    }

    @Test
    void itemsInCart_getCartItems_ReturnsSortedLineItems() {
        setUpCart();

        LineItem[] lineItems  = simpleStore.getCartItems();
        assertTrue(A_WALLPAPER.equals(lineItems[0].getName()));
        assertTrue(B_WALLPAPER.equals(lineItems[1].getName()));
        assertTrue(C_WALLPAPER.equals(lineItems[2].getName()));
        assertTrue(D_WALLPAPER.equals(lineItems[3].getName()));
    }

    private void setUpCart() {
        simpleStore.clearCart();
        simpleStore.addItemToCart(A_WALLPAPER, A_QUANTITY);
        simpleStore.addItemToCart(D_WALLPAPER, D_QUANTITY);
        simpleStore.addItemToCart(B_WALLPAPER, B_QUANTITY);
        simpleStore.addItemToCart(C_WALLPAPER, C_QUANTITY);
    }


}