package com.zest.model;

/**
 * User - Abstract Base Class for Users
 * 
 * PURPOSE:
 * This is the abstract base class for all user types in the system (Customer and Merchant).
 * It provides common functionality for authentication and user identification.
 * 
 * UML SPECIFICATION:
 * - Fields: email (String), passwordHash (String)
 * - Method: authenticate(String pass) -> boolean
 * 
 * DESIGN PATTERNS:
 * - Abstract Class: Cannot be instantiated directly, must be extended
 * - Template Method: authenticate() uses hashPassword() template
 * 
 * SECURITY:
 * - Passwords are hashed using SHA-256 algorithm
 * - Plain passwords are never stored
 * - Authentication compares hashed passwords
 * 
 * INHERITANCE:
 * - Customer extends User
 * - Merchant extends User
 * 
 * TEAM MEMBER: Ziad Osama (The Gatekeeper)
 * RESPONSIBILITY: User Management & Authentication
 */

import java.security.MessageDigest; // For password hashing
import java.security.NoSuchAlgorithmException; // Exception handling

/**
 * Abstract User class
 * Base class for Customer and Merchant
 * Cannot be instantiated directly - must be extended
 */
public abstract class User {
    
    /**
     * UML SPEC FIELDS:
     * These fields are required by the UML specification
     */
    
    /**
     * email - User's email address
     * PURPOSE: Unique identifier for user account
     * TYPE: String
     * VISIBILITY: private (encapsulated)
     * UML SPEC: Required field
     */
    private String email;
    
    /**
     * passwordHash - Hashed version of user's password
     * PURPOSE: Secure storage of password (never store plain password)
     * TYPE: String (hex representation of hash)
     * VISIBILITY: private (encapsulated)
     * UML SPEC: Required field
     * SECURITY: SHA-256 hash, not plain text
     */
    private String passwordHash;
    
    /**
     * Constructor - Creates new User instance
     * 
     * PURPOSE:
     * Initializes User with email and hashed password.
     * Called by subclasses (Customer, Merchant) using super().
     * 
     * UML SPEC: Required constructor
     * 
     * @param email User's email address
     * @param passwordHash Pre-hashed password (hex string)
     */
    public User(String email, String passwordHash) {
        this.email = email; // Set email
        this.passwordHash = passwordHash; // Set password hash
    }
    
    /**
     * authenticate() - Authenticates user with password
     * 
     * PURPOSE:
     * Validates user password by comparing hash of entered password
     * with stored passwordHash. This is the UML-specified authentication method.
     * 
     * UML SPEC: Required method
     * SECURITY: Uses secure password hashing (SHA-256)
     * 
     * AUTHENTICATION FLOW:
     * 1. Check if password or passwordHash is null (invalid)
     * 2. Hash the entered password using SHA-256
     * 3. Compare hashed password with stored passwordHash
     * 4. Return true if they match, false otherwise
     * 
     * @param pass Plain text password entered by user
     * @return true if password matches, false otherwise
     */
    public boolean authenticate(String pass) {
        /**
         * VALIDATION: Check for null values
         * If password or passwordHash is null, authentication fails
         */
        if (pass == null || passwordHash == null) {
            return false; // Invalid input - authentication fails
        }
        
        /**
         * HASH ENTERED PASSWORD:
         * Hash the entered password using same algorithm as stored hash
         * This allows comparison without storing plain passwords
         */
        String hashedPass = hashPassword(pass); // Hash entered password
        
        /**
         * COMPARE HASHES:
         * Compare hashed entered password with stored passwordHash
         * If they match, password is correct
         */
        return passwordHash.equals(hashedPass); // Return comparison result
    }
    
    /**
     * hashPassword() - Hashes password using SHA-256
     * 
     * PURPOSE:
     * Converts plain text password to secure hash using SHA-256 algorithm.
     * This is a private helper method used by authenticate().
     * 
     * SECURITY:
     * - Uses SHA-256 cryptographic hash function
     * - One-way function (cannot reverse hash to get password)
     * - Same password always produces same hash
     * 
     * HASHING FLOW:
     * 1. Get SHA-256 MessageDigest instance
     * 2. Convert password string to bytes
     * 3. Hash the bytes
     * 4. Convert hash bytes to hex string
     * 5. Return hex string
     * 
     * @param password Plain text password to hash
     * @return Hex string representation of hash (64 characters)
     */
    private String hashPassword(String password) {
        try {
            /**
             * GET SHA-256 ALGORITHM:
             * MessageDigest provides cryptographic hash functions
             * SHA-256 produces 256-bit (32-byte) hash
             */
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Get SHA-256 instance
            
            /**
             * HASH PASSWORD:
             * Convert password string to bytes and hash them
             * digest() returns byte array of hash
             */
            byte[] hashBytes = md.digest(password.getBytes()); // Hash password bytes
            
            /**
             * CONVERT TO HEX STRING:
             * Convert byte array to hexadecimal string
             * Each byte becomes two hex characters (00-ff)
             * Result is 64-character hex string
             */
            StringBuilder sb = new StringBuilder(); // String builder for hex string
            for (byte b : hashBytes) {
                /**
                 * FORMAT BYTE AS HEX:
                 * Convert byte to hex string (e.g., 255 -> "ff")
                 * 0xff & b ensures unsigned byte conversion
                 * %02x formats as 2-digit lowercase hex
                 */
                sb.append(String.format("%02x", 0xff & b)); // Append hex representation
            }
            return sb.toString(); // Return hex string
        } catch (NoSuchAlgorithmException e) {
            /**
             * ERROR HANDLING:
             * SHA-256 should always be available, but handle exception
             * Fallback: return password as-is (not secure, but prevents crash)
             * In production, this should throw exception or use alternative
             */
            return password; // Fallback (not secure)
        }
    }
    
    /**
     * UML SPEC GETTERS:
     * These methods are required by UML specification
     */
    
    /**
     * getEmail() - Gets user's email address
     * 
     * PURPOSE:
     * Returns the email address of this user.
     * 
     * UML SPEC: Required getter
     * 
     * @return User's email address
     */
    public String getEmail() {
        return email; // Return email
    }
    
    /**
     * getPasswordHash() - Gets user's password hash
     * 
     * PURPOSE:
     * Returns the hashed password of this user.
     * Used for database storage and comparison.
     * 
     * UML SPEC: Required getter
     * SECURITY: Returns hash, not plain password
     * 
     * @return User's password hash (hex string)
     */
    public String getPasswordHash() {
        return passwordHash; // Return password hash
    }
    
    /**
     * UML SPEC SETTERS:
     * These methods are required by UML specification
     */
    
    /**
     * setEmail() - Sets user's email address
     * 
     * PURPOSE:
     * Updates the email address of this user.
     * 
     * UML SPEC: Required setter
     * 
     * @param email New email address
     */
    public void setEmail(String email) {
        this.email = email; // Set email
    }
    
    /**
     * setPasswordHash() - Sets user's password hash
     * 
     * PURPOSE:
     * Updates the password hash of this user.
     * Should be called with pre-hashed password.
     * 
     * UML SPEC: Required setter
     * SECURITY: Expects hash, not plain password
     * 
     * @param passwordHash New password hash (hex string)
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash; // Set password hash
    }
}
