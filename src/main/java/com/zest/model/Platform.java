package com.zest.model;

/**
 * Platform - Singleton Platform Class
 * 
 * PURPOSE:
 * This class implements the Singleton Pattern to serve as the "Single Source of Truth"
 * for the entire system. It manages all restaurants and users in a centralized location.
 * 
 * UML SPECIFICATION:
 * - Fields: instance (static Platform), restaurants (List<Restaurant>), users (List<User>)
 * - Methods: getInstance() -> Platform, registerUser(User u), login(String email, String pass) -> User,
 *           searchRestaurant(String query) -> List<Restaurant>
 * 
 * DESIGN PATTERNS:
 * - Singleton Pattern: Only one Platform instance exists
 * - Double-Checked Locking: Thread-safe singleton initialization
 * 
 * SINGLETON PATTERN:
 * - Private constructor prevents external instantiation
 * - getInstance() returns the single instance
 * - Thread-safe using synchronized block
 * - Ensures only one Platform exists in the system
 * 
 * CENTRALIZED MANAGEMENT:
 * - restaurants: All restaurants in the system
 * - users: All users (Customers and Merchants) in the system
 * - Single point of access for system-wide data
 * 
 * SEARCH FUNCTIONALITY:
 * - searchRestaurant(): Searches restaurants by name
 * - Case-insensitive search
 * - Returns matching restaurants
 * 
 * TEAM MEMBER: Ahmed Hesham (The Architect)
 * RESPONSIBILITY: Platform Architecture & System Management
 */

import java.util.ArrayList; // For restaurants and users lists
import java.util.List; // List interface
import java.util.stream.Collectors; // For stream operations

/**
 * Platform class
 * Singleton that manages all restaurants and users (Single Source of Truth)
 */
public class Platform {
    
    /**
     * UML SPEC FIELDS:
     * These fields are required by the UML specification
     */
    
    /**
     * instance - Single Platform instance (Singleton Pattern)
     * PURPOSE: Stores the only instance of Platform
     * TYPE: static Platform
     * VISIBILITY: private
     * UML SPEC: Required field
     * SINGLETON: Only one instance exists in the entire system
     * THREAD-SAFE: Protected by synchronized block in getInstance()
     */
    private static Platform instance;
    
    /**
     * restaurants - List of all restaurants in the system
     * PURPOSE: Centralized storage of all restaurants
     * TYPE: List<Restaurant>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * USAGE: Single source of truth for restaurant data
     */
    private List<Restaurant> restaurants;
    
    /**
     * users - List of all users in the system
     * PURPOSE: Centralized storage of all users (Customers and Merchants)
     * TYPE: List<User>
     * VISIBILITY: private
     * UML SPEC: Required field
     * INITIALIZATION: Empty ArrayList created in constructor
     * USAGE: Single source of truth for user data
     */
    private List<User> users;
    
    /**
     * Private constructor - Singleton Pattern
     * 
     * PURPOSE:
     * Private constructor prevents external instantiation.
     * Only getInstance() can create Platform instance.
     * 
     * SINGLETON PATTERN:
     * - Constructor is private
     * - Cannot be called from outside class
     * - Ensures only one instance exists
     * 
     * INITIALIZATION:
     * - Creates empty lists for restaurants and users
     */
    private Platform() {
        this.restaurants = new ArrayList<>(); // Initialize empty restaurants list
        this.users = new ArrayList<>(); // Initialize empty users list
    }
    
    /**
     * getInstance() - Gets singleton instance
     * 
     * PURPOSE:
     * Returns the single Platform instance. Creates it if it doesn't exist.
     * Uses Double-Checked Locking for thread safety.
     * 
     * UML SPEC: Required method
     * SINGLETON PATTERN: Ensures only one instance exists
     * 
     * THREAD SAFETY:
     * - Double-Checked Locking pattern
     * - First check: Fast path if instance exists
     * - Synchronized block: Thread-safe creation
     * - Second check: Prevents multiple instances in multi-threaded environment
     * 
     * FLOW:
     * 1. Check if instance is null (fast path)
     * 2. If null, enter synchronized block
     * 3. Check again if instance is null (double-check)
     * 4. If still null, create new instance
     * 5. Return instance
     * 
     * @return The single Platform instance
     */
    public static Platform getInstance() {
        /**
         * FIRST CHECK (FAST PATH):
         * If instance already exists, return it immediately
         * This avoids synchronization overhead in most cases
         */
        if (instance == null) {
            /**
             * SYNCHRONIZED BLOCK:
             * Only one thread can enter at a time
             * Prevents race condition in multi-threaded environment
             */
            synchronized (Platform.class) {
                /**
                 * SECOND CHECK (DOUBLE-CHECKED LOCKING):
                 * Check again after acquiring lock
                 * Another thread might have created instance while we waited
                 */
                if (instance == null) {
                    instance = new Platform(); // Create single instance
                }
            }
        }
        return instance; // Return instance
    }
    
    /**
     * registerUser() - Registers a new user
     * 
     * PURPOSE:
     * Adds a new user to the platform's user list.
     * This is the UML-specified method for user registration.
     * 
     * UML SPEC: Required method
     * 
     * REGISTRATION FLOW:
     * 1. Validate user is not null
     * 2. Check if user already exists in list
     * 3. If new, add user to list
     * 
     * @param u The User object to register (Customer or Merchant)
     */
    public void registerUser(User u) {
        /**
         * VALIDATION: Check user is valid and not already registered
         * Only add non-null users that aren't already in the list
         */
        if (u != null && !users.contains(u)) {
            users.add(u); // Add user to list
        }
    }
    
    /**
     * login() - Authenticates and logs in a user
     * 
     * PURPOSE:
     * Searches for user with matching email and password.
     * Returns User if authenticated, null otherwise.
     * 
     * UML SPEC: Required method
     * AUTHENTICATION: Uses User.authenticate() method
     * 
     * LOGIN FLOW:
     * 1. Loop through all users in platform
     * 2. Check if email matches
     * 3. Check if password authenticates (using User.authenticate())
     * 4. Return user if both match, null otherwise
     * 
     * @param email User's email address
     * @param pass User's password (plain text)
     * @return User if authenticated, null if not found or wrong password
     */
    public User login(String email, String pass) {
        /**
         * SEARCH THROUGH USERS:
         * Loop through all users to find matching email and password
         */
        for (User user : users) {
            /**
             * AUTHENTICATION CHECK:
             * Check if email matches and password authenticates
             * Uses User.authenticate() which compares password hash
             */
            if (user.getEmail().equals(email) && user.authenticate(pass)) {
                return user; // Return authenticated user
            }
        }
        return null; // User not found or wrong password
    }
    
    /**
     * searchRestaurant() - Searches restaurants by query
     * 
     * PURPOSE:
     * Searches restaurants by name using case-insensitive matching.
     * This is the UML-specified method for restaurant search.
     * 
     * UML SPEC: Required method
     * 
     * SEARCH FLOW:
     * 1. Check if query is null or empty
     * 2. If empty, return all restaurants
     * 3. If not empty, filter restaurants by name (case-insensitive)
     * 4. Return matching restaurants
     * 
     * SEARCH LOGIC:
     * - Case-insensitive: Converts both query and restaurant names to lowercase
     * - Partial match: Uses contains() for substring matching
     * - Stream API: Uses Java 8 streams for filtering
     * 
     * @param query Search query string (restaurant name)
     * @return List of restaurants matching the query (or all restaurants if query is empty)
     */
    public List<Restaurant> searchRestaurant(String query) {
        /**
         * VALIDATION: Check if query is empty
         * If query is null or empty, return all restaurants
         */
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(restaurants); // Return copy of all restaurants
        }
        
        /**
         * SEARCH FILTERING:
         * Convert query to lowercase for case-insensitive matching
         * Filter restaurants whose names contain the query
         * Use Stream API for functional programming approach
         */
        String lowerQuery = query.toLowerCase(); // Convert query to lowercase
        return restaurants.stream()
            .filter(r -> r.getName().toLowerCase().contains(lowerQuery)) // Filter by name match
            .collect(Collectors.toList()); // Collect results into list
    }
    
    /**
     * GETTER METHODS:
     * These methods provide access to private fields
     * Returns copies to prevent external modification
     */
    
    /**
     * getRestaurants() - Gets all restaurants
     * 
     * PURPOSE:
     * Returns a copy of the restaurants list.
     * Returns copy to prevent external modification.
     * 
     * @return Copy of restaurants list
     */
    public List<Restaurant> getRestaurants() {
        return new ArrayList<>(restaurants); // Return copy of restaurants list
    }
    
    /**
     * getUsers() - Gets all users
     * 
     * PURPOSE:
     * Returns a copy of the users list.
     * Returns copy to prevent external modification.
     * 
     * @return Copy of users list
     */
    public List<User> getUsers() {
        return new ArrayList<>(users); // Return copy of users list
    }
    
    /**
     * SETTER METHODS:
     * These methods allow modification of fields
     * Creates copies to prevent external modification
     */
    
    /**
     * setRestaurants() - Sets restaurants list
     * 
     * PURPOSE:
     * Updates the restaurants list.
     * Creates copy to prevent external modification.
     * 
     * @param restaurants New restaurants list (can be null)
     */
    public void setRestaurants(List<Restaurant> restaurants) {
        // Create copy to prevent external modification
        this.restaurants = restaurants != null ? new ArrayList<>(restaurants) : new ArrayList<>();
    }
    
    /**
     * setUsers() - Sets users list
     * 
     * PURPOSE:
     * Updates the users list.
     * Creates copy to prevent external modification.
     * 
     * @param users New users list (can be null)
     */
    public void setUsers(List<User> users) {
        // Create copy to prevent external modification
        this.users = users != null ? new ArrayList<>(users) : new ArrayList<>();
    }
}
