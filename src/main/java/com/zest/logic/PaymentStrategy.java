package com.zest.logic;

/**
 * PaymentStrategy - Payment Strategy Interface
 * 
 * PURPOSE:
 * This interface defines the contract for different payment methods using the Strategy Pattern.
 * It allows the system to support multiple payment methods (Cash, PayPal, etc.) without
 * modifying the Order class when adding new payment methods.
 * 
 * UML SPECIFICATION:
 * - Method: pay(double amount) -> boolean
 * 
 * DESIGN PATTERNS:
 * - Strategy Pattern: Defines family of algorithms (payment methods) that are interchangeable
 * 
 * STRATEGY PATTERN BENEFITS:
 * - Encapsulates payment algorithms
 * - Makes payment methods interchangeable
 * - Easy to add new payment methods without modifying Order class
 * - Follows Open/Closed Principle (open for extension, closed for modification)
 * 
 * IMPLEMENTATIONS:
 * - CashPayment: Simple cash payment (always succeeds)
 * - PayPalAdapter: PayPal payment using Adapter Pattern
 * 
 * USAGE:
 * - Order class uses PaymentStrategy interface
 * - Order.pay() calls PaymentStrategy.pay()
 * - Different payment strategies implement this interface differently
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Payment System Architecture & Strategy Pattern
 */

/**
 * PaymentStrategy interface
 * Defines contract for payment processing (Strategy Pattern)
 */
public interface PaymentStrategy {
    
    /**
     * pay() - Processes payment for given amount
     * 
     * PURPOSE:
     * Processes a payment for the specified amount.
     * Different implementations handle payment differently (Cash, PayPal, etc.).
     * 
     * UML SPEC: Required method
     * STRATEGY PATTERN: Each payment method implements this differently
     * 
     * PAYMENT PROCESSING:
     * - CashPayment: Always succeeds (simulation)
     * - PayPalAdapter: Calls PayPal API (simulated)
     * - Future: CreditCard, BankTransfer, etc.
     * 
     * RETURN VALUE:
     * - true: Payment was successful
     * - false: Payment failed (insufficient funds, network error, etc.)
     * 
     * @param amount The amount to be paid in EGP (Egyptian Pounds)
     * @return true if payment was successful, false otherwise
     */
    boolean pay(double amount);
}
