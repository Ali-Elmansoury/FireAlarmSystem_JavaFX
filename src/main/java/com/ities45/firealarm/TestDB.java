package com.ities45.firealarm;

import com.ities45.firealarm.dal.DAO;
import com.ities45.firealarm.dal.UserDTO;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        // Test the register method
        UserDTO newUser = new UserDTO("testUser", "test@example.com", "password123", "emergency@example.com");

        try {
            // Register the new user
            int registerResult = DAO.register(newUser);
            if (registerResult > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("User registration failed!");
            }

            // Test the login method
            UserDTO loginUser = new UserDTO("", "test@example.com", "password123", ""); // Only email and password are needed
            boolean loginResult = DAO.login(loginUser);
            if (loginResult) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed!");
            }

            // Test the retrieval method
            UserDTO retrievedUser = DAO.getUserData("test@example.com");
            if (retrievedUser != null) {
                System.out.println("User retrieved successfully:");
                System.out.println("Username: " + retrievedUser.getUsername());
                System.out.println("Email: " + retrievedUser.getEmail());
                System.out.println("Emergency Email: " + retrievedUser.getEmergencyEmail());
            } else {
                System.out.println("No user found with that email.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
