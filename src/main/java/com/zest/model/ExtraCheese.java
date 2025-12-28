package com.zest.model;

public class ExtraCheese extends ToppingDecorator {
    
    public ExtraCheese(MenuItem item) {
        super(item);
    }

    @Override
    public String getName() {
        return protectedItem.getName() + ", Extra Cheese";
    }

    @Override
    public double getPrice() {
        return protectedItem.getPrice() + 15.00; // Adding the price of the topping
    }
}