package com.zest.model;

public class MenuItem {
    private int id;
    private int restaurantId; // Added to track which restaurant this item belongs to
    private String name;
    private double price;
    private String description;
    private String imageUrl;

    public MenuItem(int id, int restaurantId, String name, double price, String description, String imageUrl) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() { return id; }
    public int getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}