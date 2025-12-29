package com.zest.model;

/**
 * Review - Customer Review Model Class
 * 
 * PURPOSE:
 * This class represents a customer review for a restaurant. Reviews contain
 * a rating, comment, and reference to the customer who wrote it.
 * 
 * UML SPECIFICATION:
 * - Fields: rating (int), comment (String), author (Customer)
 * 
 * REVIEW SYSTEM:
 * - rating: Numeric rating (typically 1-5 stars)
 * - comment: Text comment from customer
 * - author: Customer who wrote the review
 * 
 * RESTAURANT RATING:
 * - Reviews are associated with restaurants
 * - Used to calculate restaurant average rating
 * - Helps other customers make decisions
 * 
 * CUSTOMER FEEDBACK:
 * - Customers can leave reviews after orders
 * - Reviews help improve restaurant quality
 * - Reviews are displayed on restaurant pages
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: Review System & Customer Feedback
 */

/**
 * Review class
 * Represents a customer review for a restaurant
 */
public class Review {
    
    /**
     * UML SPEC FIELDS:
     * These fields are required by the UML specification
     */
    
    /**
     * rating - Numeric rating for the restaurant
     * PURPOSE: Customer's rating (typically 1-5 stars)
     * TYPE: int
     * VISIBILITY: private
     * UML SPEC: Required field
     * RANGE: Typically 1-5 (1 = poor, 5 = excellent)
     * USAGE: Used to calculate restaurant average rating
     */
    private int rating;
    
    /**
     * comment - Text comment from customer
     * PURPOSE: Customer's written feedback about the restaurant
     * TYPE: String
     * VISIBILITY: private
     * UML SPEC: Required field
     * CONTENT: Customer's opinion, experience, suggestions
     * USAGE: Displayed with rating on restaurant page
     */
    private String comment;
    
    /**
     * author - Customer who wrote the review
     * PURPOSE: Links review to the customer who created it
     * TYPE: Customer
     * VISIBILITY: private
     * UML SPEC: Required field
     * RELATIONSHIP: Review has-a Customer (composition)
     * USAGE: Identifies who wrote the review, prevents duplicate reviews
     */
    private Customer author;
    
    /**
     * Constructor - Creates new Review
     * 
     * PURPOSE:
     * Creates new Review with rating, comment, and author.
     * 
     * UML SPEC: Required constructor
     * 
     * @param rating Numeric rating (typically 1-5)
     * @param comment Review comment text
     * @param author Customer who wrote the review
     */
    public Review(int rating, String comment, Customer author) {
        this.rating = rating; // Set rating
        this.comment = comment; // Set comment
        this.author = author; // Set author
    }
    
    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getRating() - Gets review rating
     * 
     * PURPOSE:
     * Returns the numeric rating for this review.
     * 
     * @return Review rating (typically 1-5)
     */
    public int getRating() {
        return rating; // Return rating
    }
    
    /**
     * getComment() - Gets review comment
     * 
     * PURPOSE:
     * Returns the text comment for this review.
     * 
     * @return Review comment text
     */
    public String getComment() {
        return comment; // Return comment
    }
    
    /**
     * getAuthor() - Gets review author
     * 
     * PURPOSE:
     * Returns the customer who wrote this review.
     * 
     * @return Customer who wrote the review
     */
    public Customer getAuthor() {
        return author; // Return author
    }
    
    /**
     * SETTER METHODS:
     * These methods allow modification of fields
     */
    
    /**
     * setRating() - Sets review rating
     * 
     * PURPOSE:
     * Updates the rating for this review.
     * 
     * @param rating New rating value
     */
    public void setRating(int rating) {
        this.rating = rating; // Set rating
    }
    
    /**
     * setComment() - Sets review comment
     * 
     * PURPOSE:
     * Updates the comment text for this review.
     * 
     * @param comment New comment text
     */
    public void setComment(String comment) {
        this.comment = comment; // Set comment
    }
    
    /**
     * setAuthor() - Sets review author
     * 
     * PURPOSE:
     * Updates the customer who wrote this review.
     * 
     * @param author New review author (Customer)
     */
    public void setAuthor(Customer author) {
        this.author = author; // Set author
    }
}
