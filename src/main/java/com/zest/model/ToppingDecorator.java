package com.zest.model;

public abstract class ToppingDecorator extends MenuItem {
    protected MenuItem protectedItem;

    public ToppingDecorator(MenuItem item) {
        // Pass the item data up to the MenuItem parent
        super(0, item.getRestaurantId(), item.getName(), item.getPrice(), "", item.getImageUrl());
        this.protectedItem = item;
    }
}