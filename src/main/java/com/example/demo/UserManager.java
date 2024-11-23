package com.example.demo;

public class UserManager {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    public static User getCurrentUser() {
        if (currentUser == null) {
            // Provide a default user or throw an exception
            throw new IllegalStateException("No current user set. Please set a user before accessing.");
        }
        return currentUser;
    }
    // Optional: method to clear current user
    public static void clearCurrentUser() {
        currentUser = null;
    }
}