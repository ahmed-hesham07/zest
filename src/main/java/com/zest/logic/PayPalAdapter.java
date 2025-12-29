package com.zest.logic;

/**
 * PayPalAdapter - PayPal Payment Adapter
 * 
 * PURPOSE:
 * This class implements PaymentStrategy for PayPal payments using the Adapter Pattern.
 * It adapts a complex third-party PayPal API (XPayPalApi) to work with the PaymentStrategy interface.
 * 
 * UML SPECIFICATION:
 * - Implements: PaymentStrategy interface
 * - Method: pay(double amount) -> boolean
 * 
 * DESIGN PATTERNS:
 * - Strategy Pattern: Concrete strategy for PayPal payments
 * - Adapter Pattern: Adapts XPayPalApi to PaymentStrategy interface
 * 
 * ADAPTER PATTERN:
 * - XPayPalApi: Third-party API with different interface (processPayment(amount, currency))
 * - PaymentStrategy: Our interface (pay(amount))
 * - PayPalAdapter: Adapts XPayPalApi to match PaymentStrategy interface
 * 
 * WHY ADAPTER PATTERN:
 * - Third-party API has different method signature
 * - We need to use it with our PaymentStrategy interface
 * - Adapter bridges the gap between incompatible interfaces
 * - Allows us to use third-party code without modifying it
 * 
 * PAYPAL PAYMENT FLOW:
 * 1. Receive payment amount
 * 2. Adapt call to XPayPalApi.processPayment(amount, "EGP")
 * 3. Process payment through PayPal API (simulated)
 * 4. Return success status
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Payment System Integration & Adapter Pattern
 */

/**
 * PayPalAdapter class
 * Adapts PayPal API to PaymentStrategy interface (Adapter Pattern + Strategy Pattern)
 */
public class PayPalAdapter implements PaymentStrategy {
    
    /**
     * XPayPalApi - Simulated third-party PayPal API class
     * 
     * PURPOSE:
     * Simulates a complex third-party PayPal API with different interface.
     * In real system, this would be an external library.
     * 
     * ADAPTER PATTERN:
     * - This class has different method signature than PaymentStrategy
     * - PayPalAdapter adapts it to work with PaymentStrategy interface
     * 
     * NOTE: This is a nested static class for simulation purposes.
     * In real system, this would be an external library class.
     */
    private static class XPayPalApi {
        /**
         * processPayment() - Third-party PayPal API method
         * 
         * PURPOSE:
         * Simulates complex third-party PayPal payment processing.
         * Has different signature than PaymentStrategy.pay().
         * 
         * DIFFERENCE:
         * - PaymentStrategy.pay(amount): Single parameter
         * - XPayPalApi.processPayment(amount, currency): Two parameters
         * 
         * ADAPTER NEEDED:
         * - PayPalAdapter bridges this difference
         * - Converts PaymentStrategy.pay(amount) to XPayPalApi.processPayment(amount, "EGP")
         * 
         * @param paymentAmount The amount to process
         * @param currencyCode Currency code (e.g., "EGP", "USD")
         */
        public void processPayment(double paymentAmount, String currencyCode) {
            /**
             * SIMULATE PAYPAL API CALL:
             * In real scenario, this would call actual PayPal API
             * This is simulated for demonstration purposes
             */
            System.out.println("XPayPalApi: Processing payment of " + paymentAmount + " " + currencyCode);
        }
    }
    
    /**
     * payPalApi - Instance of simulated PayPal API
     * PURPOSE: Reference to third-party PayPal API
     * TYPE: XPayPalApi
     * VISIBILITY: private
     */
    private XPayPalApi payPalApi;
    
    /**
     * Constructor - Initializes PayPal API adapter
     * 
     * PURPOSE:
     * Creates new PayPalAdapter and initializes XPayPalApi instance.
     * 
     * INITIALIZATION:
     * - Creates instance of simulated PayPal API
     */
    public PayPalAdapter() {
        this.payPalApi = new XPayPalApi(); // Initialize PayPal API instance
    }
    
    /**
     * pay() - Processes PayPal payment (Adapter Pattern)
     * 
     * PURPOSE:
     * Adapts PaymentStrategy.pay() to XPayPalApi.processPayment().
     * This is the UML-specified method for PayPal payments.
     * 
     * UML SPEC: Required method (implements PaymentStrategy)
     * ADAPTER PATTERN: Adapts XPayPalApi to PaymentStrategy interface
     * 
     * ADAPTATION FLOW:
     * 1. Receive payment amount from PaymentStrategy interface
     * 2. Adapt call to XPayPalApi.processPayment(amount, "EGP")
     * 3. Process payment through PayPal API
     * 4. Return success status
     * 
     * ADAPTER PATTERN EXPLANATION:
     * - Our interface: pay(double amount)
     * - Third-party API: processPayment(double amount, String currency)
     * - Adapter converts: pay(amount) -> processPayment(amount, "EGP")
     * 
     * @param amount The amount to be paid in EGP
     * @return true if payment was successful (simulated as always successful)
     */
    @Override
    public boolean pay(double amount) {
        /**
         * STEP 1: LOG PAYMENT PROCESSING
         * Print message indicating PayPal payment is being processed
         */
        System.out.println("Processing payment of " + amount + " EGP via PayPal Adapter...");
        
        /**
         * STEP 2: ADAPT THE CALL (ADAPTER PATTERN)
         * Convert PaymentStrategy.pay(amount) to XPayPalApi.processPayment(amount, "EGP")
         * This is the core of the Adapter Pattern - bridging incompatible interfaces
         */
        payPalApi.processPayment(amount, "EGP"); // Adapt call to third-party API
        
        /**
         * STEP 3: LOG SUCCESS
         * Print confirmation message
         */
        System.out.println("PayPal payment processed successfully: " + amount + " EGP");
        
        /**
         * STEP 4: RETURN SUCCESS
         * In real system, this would check actual PayPal API response
         * For simulation, always return true (success)
         */
        return true; // Assume success for simulation
    }
}
