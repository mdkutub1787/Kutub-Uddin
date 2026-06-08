package com.kutub.paymentapp.model;

public class User {
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
    private int balance; // Use Object to support multiple data types (String, Long, Double)

    // Default constructor
    public User() {}

    // Constructor with all fields
    public User(String userId, String name, String email, String phoneNumber, Object balance) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
    }

    // Constructor without balance (for creating a user without balance initially)
    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phone;
        this.balance = 0;  // Default balance to 0 if not provided
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Get the balance as a String. If the balance is not a String, convert it to one.
    public String getBalanceAsString() {
        if (balance instanceof String) {
            return (String) balance;
        } else if (balance instanceof Long) {
            return String.valueOf(balance);
        } else if (balance instanceof Double) {
            return String.valueOf(balance);
        }
        return "0";  // Default if balance is not set or is an unsupported type
    }

    // Set the balance as an Object (supports String, Long, or Double)
    public void setBalance(Object balance) {
        this.balance = balance;
    }

    // Convert balance to a double. Handles different data types (String, Long, Double)
    public double getBalanceAsDouble() {
        if (balance instanceof String) {
            try {
                return Double.parseDouble((String) balance);
            } catch (NumberFormatException e) {
                return 0;  // Return 0 if balance is not a valid number
            }
        } else if (balance instanceof Long) {
            return (Long) balance;
        } else if (balance instanceof Double) {
            return (Double) balance;
        }
        return 0;  // Return 0 if balance is of an unsupported type
    }

    // Safely set the balance by converting it to an appropriate type (Double or Long)
    public void setBalanceFromDouble(double balance) {
        // Store balance as a Double to maintain precision
        this.balance = balance;
    }

    // Optional: You can also add a method to get the balance as a Long if necessary
    public long getBalanceAsLong() {
        if (balance instanceof Long) {
            return (Long) balance;
        } else if (balance instanceof Double) {
            return ((Double) balance).longValue();
        } else if (balance instanceof String) {
            try {
                return Long.parseLong((String) balance);
            } catch (NumberFormatException e) {
                return 0;  // Return 0 if the String cannot be converted to a Long
            }
        }
        return 0;  // Default if the balance type is unsupported
    }

    public String getBalance() {
        return (String) balance;
    }
}
