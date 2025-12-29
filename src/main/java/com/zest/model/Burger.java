package com.zest.model;

public class Burger extends MenuItem {
    public Burger(int id, int restaurantId, String name, double price, String description, String imageUrl) {
        super(id, restaurantId, name, price, description, imageUrl);
    }
}