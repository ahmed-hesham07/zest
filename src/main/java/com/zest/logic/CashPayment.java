package com.zest.logic;

/**
 * CashPayment - Cash Payment Strategy Implementation
 * 
 * PURPOSE:
 * This class implements PaymentStrategy for cash payments. It represents
 * the simplest payment method where payment is always successful (simulation).
 * 
 * UML SPECIFICATION:
 * - Implements: PaymentStrategy interface
 * - Method: pay(double amount) -> boolean
 * 
 * DESIGN PATTERNS:
 * - Strategy Pattern: Concrete strategy for cash payments
 * 
 * CASH PAYMENT CHARACTERISTICS:
 * - Simple payment method
 * - Always succeeds (simulation)
 * - No external API calls
 * - Instant processing
 * 
 * SIMULATION:
 * - In real system, cash payment would be handled at delivery
 * - This implementation simulates successful cash payment
 * - Prints payment confirmation to console
 * 
 * USAGE:
 * - Used when customer selects "Cash" payment method
 * - Order.pay() calls this implementation
 * - Returns true to indicate successful payment
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Payment System Implementation
 */

/**
 * CashPayment class
 * Concrete strategy for cash payments (Strategy Pattern)
 */
public class CashPayment implements PaymentStrategy {
    
    /**
     * pay() - Processes cash payment
     * 
     * PURPOSE:
     * Processes a cash payment for the specified amount.
     * Cash payments always succeed in this simulation.
     * 
     * UML SPEC: Required method (implements PaymentStrategy)
     * STRATEGY PATTERN: Concrete implementation of payment strategy
     * 
     * CASH PAYMENT FLOW:
     * 1. Receive payment amount
     * 2. Print payment confirmation (simulation)
     * 3. Return true (payment always succeeds)
     * 
     * SIMULATION:
     * - In real system, cash would be collected at delivery
     * - This simulates successful payment for testing
     * - Prints confirmation message to console
     * 
     * @param amount The amount to be paid in EGP
     * @return true (cash payment always succeeds)
     */
    @Override
    public boolean pay(double amount) {
        /**
         * SIMULATE CASH PAYMENT:
         * Print confirmation message to console
         * In real system, this would handle cash collection at delivery
         */
        System.out.println("Paid cash: " + amount + " EGP");
        
        /**
         * RETURN SUCCESS:
         * Cash payment always succeeds in this simulation
         * In real system, this would depend on actual cash collection
         */
        return true; // Cash payment always succeeds
    }
}
