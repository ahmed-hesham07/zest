package com.zest.logic;

import com.zest.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<MenuItem> items;

    private CartManager() {
        items = new ArrayList<>(); // Ensures ArrayList is initialized
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }
    public void removeItem(MenuItem item) {
        items.remove(item); 
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        // Stream the list to sum the prices of all items, including decorated ones
        return items.stream().mapToDouble(MenuItem::getPrice).sum();
    }
}