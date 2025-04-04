/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ities45.firealarm.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author ali
 */
public class DAO {

    public static int register(UserDTO user) throws SQLException {
        int result = -1;
        DriverManager.registerDriver(new ClientDriver());

        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/fireAlarmDB", "root", "root"); PreparedStatement statement = connection.prepareStatement("INSERT INTO USERTABLE (NAME, EMAIL, PASSWORD, EMERGENCY_EMAIL) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword()); // hash password before storing in real applications
            statement.setString(4, user.getEmergencyEmail());

            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error (for debugging or production logging)
        }
        return result;
    }

    public static boolean login(UserDTO user) throws SQLException {
        boolean result = false;
        DriverManager.registerDriver(new ClientDriver());

        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/fireAlarmDB", "root", "root"); PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERTABLE WHERE email = ? AND password = ?")) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword()); // compare hashed password here

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error (for debugging or production logging)
        }
        return result;
    }

    public static UserDTO getUserData(String email) throws SQLException {
        UserDTO user = null;

        DriverManager.registerDriver(new ClientDriver());

        String url = "jdbc:derby://localhost:1527/fireAlarmDB";
        String dbUser = "root";
        String dbPassword = "root";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "SELECT * FROM USERTABLE WHERE EMAIL = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    // Mapping the result to UserDTO
                    String username = rs.getString("NAME");
                    String password = rs.getString("PASSWORD");
                    String emergencyEmail = rs.getString("EMERGENCY_EMAIL");

                    // Create UserDTO object with the retrieved data
                    user = new UserDTO(username, email, password, emergencyEmail);
                }
            }
        }

        return user;
    }

}
