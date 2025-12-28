package com.zest.model;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageUrl;

    public MenuItem(int id, String name, double price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // These "Getters" fix Hamdy's red errors
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}