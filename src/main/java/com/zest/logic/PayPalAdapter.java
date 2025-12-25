package com.zest.logic;

/**
 * PayPal Payment Adapter
 * Adapter Pattern: Adapts the complex XPayPalApi to work with PaymentStrategy interface
 */
public class PayPalAdapter implements PaymentStrategy {
    
    /**
     * Adapter Pattern Applied Here
     * This class adapts the XPayPalApi (simulated 3rd party API) to match PaymentStrategy interface
     */
    
    // Simulated 3rd party PayPal API class
    private static class XPayPalApi {
        /**
         * Simulated complex 3rd party method with different signature
         * @param paymentAmount The amount to process
         * @param currencyCode Currency code (e.g., "USD")
         */
        public void processPayment(double paymentAmount, String currencyCode) {
            // Simulated complex 3rd party logic
            System.out.println("XPayPalApi: Processing payment of " + paymentAmount + " " + currencyCode);
        }
    }
    
    private XPayPalApi payPalApi;
    
    public PayPalAdapter() {
        this.payPalApi = new XPayPalApi();
    }
    
    /**
     * Adapter Pattern: Adapts XPayPalApi.processPayment() to PaymentStrategy.pay()
     * @param amount The amount to be paid
     */
    @Override
    public void pay(double amount) {
        System.out.println("Processing payment of " + amount + " via PayPal Adapter...");
        // Adapt the call to the 3rd party API
        payPalApi.processPayment(amount, "USD");
    }
}
