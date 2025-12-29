package com.zest.controller;

/**
 * ReviewController - Customer Reviews Controller
 * 
 * PURPOSE:
 * This controller manages the display and submission of customer reviews for restaurants.
 * Customers can view existing reviews and submit new reviews after placing orders.
 * 
 * UML CLASSES USED:
 * - Review: Displays Review objects with rating, comment, author, reviewDate
 * - Restaurant: Gets restaurant information to display reviews for
 * - Customer: Creates Customer objects for review authors
 * 
 * REVIEW DISPLAY:
 * - Shows all reviews for a restaurant
 * - Displays average rating
 * - Shows review cards with rating stars, comment, author name, and date
 * 
 * REVIEW SUBMISSION:
 * - Customers can submit reviews with rating (1-5 stars) and comment
 * - Validates rating and comment before submission
 * - Saves review to database
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: Review System & Customer Feedback
 */

import com.zest.Main; // Main application class for scene switching
import com.zest.dao.DataService; // Data access layer for review operations
import com.zest.model.Review; // UML Class: Review model
import com.zest.model.Restaurant; // UML Class: Restaurant model
import com.zest.model.Customer; // UML Class: Customer model
import javafx.fxml.FXML; // JavaFX annotation for FXML injection
import javafx.geometry.Insets; // Spacing for UI elements
import javafx.scene.control.Alert; // Shows popup messages
import javafx.scene.control.Button; // Button for actions
import javafx.scene.control.Label; // Text labels
import javafx.scene.control.ListView; // List view for displaying reviews
import javafx.scene.control.RadioButton; // Radio buttons for rating selection
import javafx.scene.control.TextArea; // Text area for review comment
import javafx.scene.control.ToggleGroup; // Toggle group for rating radio buttons
import javafx.scene.layout.HBox; // Horizontal container
import javafx.scene.layout.VBox; // Vertical container for review cards
import java.io.IOException; // Exception handling
import java.util.List; // List interface

/**
 * Controller for reviews screen
 * Displays reviews for a restaurant and allows customers to submit reviews
 */
public class ReviewController {
    
    /**
     * FXML INJECTED FIELDS:
     * These fields are automatically populated from Reviews.fxml when screen loads
     */
    
    /**
     * restaurantNameLabel - Label showing restaurant name
     * PURPOSE: Displays name of restaurant being reviewed
     * CONNECTS TO: Reviews.fxml fx:id="restaurantNameLabel"
     */
    @FXML private Label restaurantNameLabel;
    
    /**
     * averageRatingLabel - Label showing average rating
     * PURPOSE: Displays average rating for restaurant
     * CONNECTS TO: Reviews.fxml fx:id="averageRatingLabel"
     */
    @FXML private Label averageRatingLabel;
    
    /**
     * reviewsListView - ListView displaying review cards
     * PURPOSE: Shows all reviews as cards
     * CONNECTS TO: Reviews.fxml fx:id="reviewsListView"
     */
    @FXML private ListView<VBox> reviewsListView;
    
    /**
     * emptyLabel - Label shown when no reviews found
     * PURPOSE: Displays message when restaurant has no reviews
     * CONNECTS TO: Reviews.fxml fx:id="emptyLabel"
     */
    @FXML private Label emptyLabel;
    
    /**
     * ratingToggleGroup - Toggle group for rating radio buttons
     * PURPOSE: Ensures only one rating can be selected
     * CONNECTS TO: Reviews.fxml fx:id="ratingToggleGroup"
     */
    @FXML private ToggleGroup ratingToggleGroup;
    
    /**
     * rating1Radio - Radio button for 1 star rating
     * PURPOSE: Allows customer to select 1 star rating
     * CONNECTS TO: Reviews.fxml fx:id="rating1Radio"
     */
    @FXML private RadioButton rating1Radio;
    
    /**
     * rating2Radio - Radio button for 2 star rating
     * PURPOSE: Allows customer to select 2 star rating
     * CONNECTS TO: Reviews.fxml fx:id="rating2Radio"
     */
    @FXML private RadioButton rating2Radio;
    
    /**
     * rating3Radio - Radio button for 3 star rating
     * PURPOSE: Allows customer to select 3 star rating
     * CONNECTS TO: Reviews.fxml fx:id="rating3Radio"
     */
    @FXML private RadioButton rating3Radio;
    
    /**
     * rating4Radio - Radio button for 4 star rating
     * PURPOSE: Allows customer to select 4 star rating
     * CONNECTS TO: Reviews.fxml fx:id="rating4Radio"
     */
    @FXML private RadioButton rating4Radio;
    
    /**
     * rating5Radio - Radio button for 5 star rating
     * PURPOSE: Allows customer to select 5 star rating
     * CONNECTS TO: Reviews.fxml fx:id="rating5Radio"
     */
    @FXML private RadioButton rating5Radio;
    
    /**
     * commentTextArea - Text area for review comment
     * PURPOSE: Allows customer to enter review comment text
     * CONNECTS TO: Reviews.fxml fx:id="commentTextArea"
     */
    @FXML private TextArea commentTextArea;
    
    /**
     * submitButton - Button for submitting review
     * PURPOSE: Submits review to database
     * CONNECTS TO: Reviews.fxml fx:id="submitButton"
     */
    @FXML private Button submitButton;
    
    /**
     * dataService - Data access object for database operations
     * PURPOSE: Fetches and saves reviews from/to database
     */
    private DataService dataService;
    
    /**
     * currentRestaurantId - Stores restaurant ID for reviews
     * PURPOSE: Tracks which restaurant's reviews to display
     * SCOPE: Static so it can be set from other controllers
     */
    private static Integer currentRestaurantId;
    
    /**
     * Constructor - Initializes data service
     * PURPOSE: Sets up data service when controller is created
     */
    public ReviewController() {
        this.dataService = new DataService(); // Create data service for database access
    }
    
    /**
     * initialize() - JavaFX lifecycle method
     * 
     * PURPOSE:
     * Called automatically when Reviews.fxml loads.
     * Loads and displays reviews for selected restaurant.
     * 
     * FLOW:
     * 1. JavaFX loads Reviews.fxml
     * 2. JavaFX creates ReviewController instance
     * 3. JavaFX injects @FXML fields
     * 4. JavaFX calls initialize()
     * 5. We load reviews
     */
    @FXML
    public void initialize() {
        if (currentRestaurantId != null) {
            loadReviews(); // Load and display reviews
        } else {
            emptyLabel.setText("No restaurant selected"); // Show message
        }
    }
    
    /**
     * setCurrentRestaurantId() - Sets restaurant ID for reviews
     * 
     * PURPOSE:
     * Called from other controllers to set which restaurant's reviews to display.
     * 
     * @param restaurantId Restaurant ID
     */
    public static void setCurrentRestaurantId(Integer restaurantId) {
        currentRestaurantId = restaurantId; // Store restaurant ID
    }
    
    /**
     * getCurrentRestaurantId() - Gets restaurant ID for reviews
     * 
     * PURPOSE:
     * Returns the ID of the restaurant whose reviews are being displayed.
     * 
     * @return Restaurant ID or null if not set
     */
    public static Integer getCurrentRestaurantId() {
        return currentRestaurantId; // Return stored restaurant ID
    }
    
    /**
     * loadReviews() - Loads and displays reviews for restaurant
     * 
     * PURPOSE:
     * Fetches reviews from database and displays them with rating, comment, author, and date.
     * Also calculates and displays average rating.
     * 
     * UML CLASSES USED:
     * - Review: Fetches Review objects from database
     * - Restaurant: Gets restaurant name for display
     * 
     * FLOW:
     * 1. Get restaurant ID
     * 2. Fetch restaurant information
     * 3. Fetch reviews from database
     * 4. Calculate average rating
     * 5. Display restaurant name and average rating
     * 6. Display review cards
     */
    private void loadReviews() {
        /**
         * STEP 1: VALIDATE RESTAURANT ID
         * Check if restaurant ID is set
         */
        if (currentRestaurantId == null) {
            emptyLabel.setText("No restaurant selected"); // Show message
            return; // Exit method early
        }
        
        /**
         * STEP 2: GET RESTAURANT INFORMATION
         * Fetch restaurant from database to get name
         */
        List<Restaurant> restaurants = dataService.getAllRestaurants();
        Restaurant restaurant = null;
        for (Restaurant r : restaurants) {
            if (r.getId() == currentRestaurantId) {
                restaurant = r; // Found restaurant
                break;
            }
        }
        
        if (restaurant == null) {
            emptyLabel.setText("Restaurant not found"); // Show error message
            return; // Exit method early
        }
        
        /**
         * STEP 3: DISPLAY RESTAURANT NAME
         * Set restaurant name label
         */
        restaurantNameLabel.setText(restaurant.getName()); // Set restaurant name
        
        /**
         * STEP 4: FETCH REVIEWS FROM DATABASE
         * Get all reviews for this restaurant
         */
        List<Review> reviews = dataService.getReviewsByRestaurant(currentRestaurantId);
        
        /**
         * STEP 5: CALCULATE AVERAGE RATING
         * Calculate average rating from all reviews
         */
        if (reviews.isEmpty()) {
            averageRatingLabel.setText("No reviews yet"); // Show message if no reviews
            emptyLabel.setText("Be the first to review this restaurant!"); // Show empty message
            reviewsListView.setVisible(false); // Hide list view
        } else {
            double totalRating = 0; // Sum of all ratings
            for (Review review : reviews) {
                totalRating += review.getRating(); // Add rating to total
            }
            double averageRating = totalRating / reviews.size(); // Calculate average
            averageRatingLabel.setText(String.format("⭐ %.1f / 5.0 (%d reviews)", averageRating, reviews.size())); // Display average
            
            /**
             * STEP 6: DISPLAY REVIEW CARDS
             * Create and display cards for each review
             */
            emptyLabel.setText(""); // Clear empty message
            reviewsListView.getItems().clear(); // Clear existing cards
            
            for (Review review : reviews) {
                VBox reviewCard = createReviewCard(review); // Create card for review
                reviewsListView.getItems().add(reviewCard); // Add to list view
            }
            reviewsListView.setVisible(true); // Show list view
        }
    }
    
    /**
     * createReviewCard() - Creates visual card for a review
     * 
     * PURPOSE:
     * Creates a VBox card displaying review information with rating stars, comment, author name, and date.
     * 
     * UML CLASSES USED:
     * - Review: Gets review rating, comment, author, reviewDate
     * 
     * @param review The Review object to display
     * @return VBox card containing review information
     */
    private VBox createReviewCard(Review review) {
        /**
         * CREATE CARD CONTAINER:
         * VBox with spacing and padding for review information
         */
        VBox card = new VBox(8); // Create VBox with 8px spacing
        card.setPadding(new Insets(15)); // Add 15px padding
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;"); // Card styling
        
        /**
         * CREATE HEADER WITH AUTHOR AND DATE:
         * HBox containing author name and review date
         */
        HBox headerBox = new HBox(10); // Create HBox with 10px spacing
        Label authorLabel = new Label(review.getAuthor().getName()); // Create author label
        authorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;"); // Bold, medium font
        
        /**
         * FORMAT REVIEW DATE:
         * Display date in readable format
         */
        String dateText = review.getReviewDate();
        if (dateText != null && dateText.length() > 10) {
            dateText = dateText.substring(0, 10); // Get date part only
        }
        Label dateLabel = new Label(dateText != null ? dateText : ""); // Create date label
        dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;"); // Small, gray text
        
        headerBox.getChildren().addAll(authorLabel, dateLabel); // Add labels to header
        
        /**
         * CREATE RATING STARS:
         * Display rating as stars (⭐)
         */
        String stars = "⭐".repeat(review.getRating()); // Create stars string
        Label ratingLabel = new Label(stars + " (" + review.getRating() + "/5)"); // Create rating label
        ratingLabel.setStyle("-fx-font-size: 16px;"); // Large font for stars
        
        /**
         * CREATE COMMENT LABEL:
         * Display review comment text
         */
        Label commentLabel = new Label(review.getComment()); // Create comment label
        commentLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #333; -fx-wrap-text: true;"); // Medium font, wrap text
        commentLabel.setMaxWidth(700); // Set max width for wrapping
        
        /**
         * ADD ELEMENTS TO CARD:
         * Add header, rating, and comment to card
         */
        card.getChildren().addAll(headerBox, ratingLabel, commentLabel); // Add to card
        
        return card; // Return completed card
    }
    
    /**
     * handleSubmitReview() - Event handler for submit review button
     * 
     * PURPOSE:
     * Validates review input and saves review to database.
     * 
     * UML CLASSES USED:
     * - Review: Creates Review object to save
     * - Customer: Creates Customer object for review author
     * 
     * FLOW:
     * 1. User clicks submit button
     * 2. Get rating from radio buttons
     * 3. Get comment from text area
     * 4. Validate input
     * 5. Create Review object
     * 6. Save to database
     * 7. Refresh display
     */
    @FXML
    private void handleSubmitReview() {
        /**
         * STEP 1: VALIDATE RESTAURANT ID
         * Check if restaurant ID is set
         */
        if (currentRestaurantId == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No restaurant selected.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * STEP 2: GET RATING FROM RADIO BUTTONS
         * Determine which rating radio button is selected
         */
        int rating = 0; // Initialize rating
        if (rating1Radio.isSelected()) {
            rating = 1; // 1 star
        } else if (rating2Radio.isSelected()) {
            rating = 2; // 2 stars
        } else if (rating3Radio.isSelected()) {
            rating = 3; // 3 stars
        } else if (rating4Radio.isSelected()) {
            rating = 4; // 4 stars
        } else if (rating5Radio.isSelected()) {
            rating = 5; // 5 stars
        }
        
        /**
         * STEP 3: VALIDATE RATING IS SELECTED
         * Check that user selected a rating
         */
        if (rating == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a rating.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * STEP 4: GET COMMENT FROM TEXT AREA
         * Get comment text from text area
         */
        String comment = commentTextArea.getText().trim(); // Get comment and trim whitespace
        
        /**
         * STEP 5: VALIDATE COMMENT IS NOT EMPTY
         * Check that comment is provided
         */
        if (comment.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a comment.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * STEP 6: GET CURRENT USER EMAIL
         * Need user email to create Customer object
         */
        String userEmail = HistoryController.getCurrentUserEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You must be logged in to submit a review.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * STEP 7: GET USER ID AND NAME
         * Need user information to create Customer object
         */
        int userId = dataService.getUserIdByEmail(userEmail);
        if (userId == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User not found.");
            alert.showAndWait();
            return; // Exit method early
        }
        
        /**
         * GET USER NAME FROM DATABASE:
         * Get user name to create Customer object
         */
        String userName = dataService.getUserNameByEmail(userEmail);
        if (userName == null) {
            userName = "Customer"; // Default name if not found
        }
        
        /**
         * STEP 8: CREATE CUSTOMER OBJECT
         * Create Customer object for review author
         */
        Customer customer = new Customer(userName, userEmail, ""); // Create customer with name from DB
        
        /**
         * STEP 9: CREATE REVIEW OBJECT
         * Create Review with rating, comment, and author
         */
        Review review = new Review(rating, comment, customer); // Create review
        review.setRestaurantId(currentRestaurantId); // Set restaurant ID
        
        /**
         * STEP 10: SAVE REVIEW TO DATABASE
         * Save review using DataService
         */
        int reviewId = dataService.saveReview(review);
        if (reviewId > 0) {
            /**
             * SUCCESS: Review saved successfully
             * Show success message and refresh display
             */
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Review submitted successfully!");
            alert.showAndWait();
            
            /**
             * CLEAR FORM:
             * Reset rating selection and comment text
             */
            if (ratingToggleGroup != null) {
                ratingToggleGroup.selectToggle(null); // Clear rating selection
            }
            // Clear individual radio buttons if toggle group is null
            if (ratingToggleGroup == null) {
                if (rating1Radio != null) rating1Radio.setSelected(false);
                if (rating2Radio != null) rating2Radio.setSelected(false);
                if (rating3Radio != null) rating3Radio.setSelected(false);
                if (rating4Radio != null) rating4Radio.setSelected(false);
                if (rating5Radio != null) rating5Radio.setSelected(false);
            }
            if (commentTextArea != null) {
                commentTextArea.clear(); // Clear comment text
            }
            
            /**
             * REFRESH DISPLAY:
             * Reload reviews to show new review
             */
            loadReviews(); // Reload reviews
        } else {
            /**
             * FAILURE: Review save failed
             * Show error message
             */
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to submit review. Please try again.");
            alert.showAndWait();
        }
    }
    
    /**
     * handleBackToHome() - Event handler for back button
     * 
     * PURPOSE:
     * Returns user to menu/home screen.
     * 
     * FLOW:
     * 1. User clicks back button
     * 2. Navigate to Home.fxml screen
     */
    @FXML
    private void handleBackToHome() {
        try {
            Main.switchScene("/fxml/Home.fxml"); // Navigate to menu screen
        } catch (IOException e) {
            /**
             * ERROR HANDLING:
             * If navigation fails, log error
             */
            e.printStackTrace();
            System.err.println("Could not load Home.fxml");
        }
    }
}

