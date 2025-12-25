package com.zest.logic;

/**
 * Cash Payment Implementation
 * Strategy Pattern: Concrete implementation of PaymentStrategy for cash payments
 */
public class CashPayment implements PaymentStrategy {
    
    /**
     * Strategy Pattern: Concrete strategy for cash payments
     * @param amount The amount to be paid
     */
    @Override
    public void pay(double amount) {
        System.out.println("Paid cash: $" + amount);
    }
}
