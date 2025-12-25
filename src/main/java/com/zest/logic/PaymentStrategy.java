package com.zest.logic;

/**
 * Payment Strategy Interface
 * Strategy Pattern: Defines the contract for different payment methods
 */
public interface PaymentStrategy {
    /**
     * Process a payment for the given amount
     * @param amount The amount to be paid
     */
    void pay(double amount);
}
