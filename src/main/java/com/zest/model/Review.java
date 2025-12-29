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
     * id - Unique identifier for review
     * PURPOSE: Primary key in database, unique identifier
     * TYPE: int
     * VISIBILITY: private
     * DATABASE: Auto-generated primary key
     */
    private int id;
    
    /**
     * restaurantId - ID of restaurant being reviewed
     * PURPOSE: Links review to restaurant
     * TYPE: int
     * VISIBILITY: private
     * DATABASE: Foreign key to restaurants table
     */
    private int restaurantId;
    
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
     * reviewDate - Date when review was created
     * PURPOSE: Timestamp for when review was submitted
     * TYPE: String
     * VISIBILITY: private
     * DATABASE: Stored as TEXT in SQLite (ISO format)
     * USAGE: Displayed with review to show when it was written
     */
    private String reviewDate;
    
    /**
     * Constructor - Creates new Review (UML spec)
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
        this.reviewDate = java.time.LocalDateTime.now().toString(); // Set current date/time
    }
    
    /**
     * Constructor - Creates Review from database
     * 
     * PURPOSE:
     * Creates Review with all fields including id and reviewDate from database.
     * Used when loading reviews from database.
     * 
     * @param id Review ID from database
     * @param restaurantId Restaurant ID being reviewed
     * @param rating Numeric rating (typically 1-5)
     * @param comment Review comment text
     * @param author Customer who wrote the review
     * @param reviewDate Date when review was created
     */
    public Review(int id, int restaurantId, int rating, String comment, Customer author, String reviewDate) {
        this.id = id; // Set review ID
        this.restaurantId = restaurantId; // Set restaurant ID
        this.rating = rating; // Set rating
        this.comment = comment; // Set comment
        this.author = author; // Set author
        this.reviewDate = reviewDate; // Set review date
    }
    
    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     */
    
    /**
     * getId() - Gets review ID
     * 
     * PURPOSE:
     * Returns the unique identifier for this review.
     * 
     * @return Review ID
     */
    public int getId() {
        return id; // Return review ID
    }
    
    /**
     * getRestaurantId() - Gets restaurant ID
     * 
     * PURPOSE:
     * Returns the ID of the restaurant being reviewed.
     * 
     * @return Restaurant ID
     */
    public int getRestaurantId() {
        return restaurantId; // Return restaurant ID
    }
    
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
     * getReviewDate() - Gets review date
     * 
     * PURPOSE:
     * Returns the date when this review was created.
     * 
     * @return Review date string
     */
    public String getReviewDate() {
        return reviewDate; // Return review date
    }
    
    /**
     * SETTER METHODS:
     * These methods allow modification of fields
     */
    
    /**
     * setId() - Sets review ID
     * 
     * PURPOSE:
     * Updates the unique identifier for this review.
     * 
     * @param id New review ID
     */
    public void setId(int id) {
        this.id = id; // Set review ID
    }
    
    /**
     * setRestaurantId() - Sets restaurant ID
     * 
     * PURPOSE:
     * Updates the ID of the restaurant being reviewed.
     * 
     * @param restaurantId New restaurant ID
     */
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId; // Set restaurant ID
    }
    
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
    
    /**
     * setReviewDate() - Sets review date
     * 
     * PURPOSE:
     * Updates the date when this review was created.
     * 
     * @param reviewDate New review date string
     */
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate; // Set review date
    }
}
