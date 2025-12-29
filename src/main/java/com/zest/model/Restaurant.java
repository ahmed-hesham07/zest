package com.zest.model;

/**
 * Restaurant Model
 * Represents a restaurant in the system
 */
public class Restaurant {
    private int id;
    private String name;
    private String imageUrl;
    
    public Restaurant(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

