/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ities45.firealarm;

import com.ities45.firealarm.dal.DAO;
import com.ities45.firealarm.dal.UserDTO;
import com.ities45.firealarm.sessionmanagement.SessionManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ali
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TextField email;

    @FXML
    private PasswordField password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
private void handleLogin(ActionEvent event) {
    String userEmail = email.getText().trim();
    String userPassword = password.getText().trim();

    if (userEmail.isEmpty() || userPassword.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Invalid Input", "Email and password cannot be empty.");
        return;
    }

    try {
        boolean result = DAO.login(new UserDTO("", userEmail, userPassword, ""));

        if (result) {
            // 🔐 Save session
            SessionManager.setLoggedInUser(userEmail);
            
            // ✅ Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Login Successful!");

            // 👉 Navigate to Home
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ities45/firealarm/normalMode.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "User login failed.");
        }
    } catch (SQLException | IOException ex) {
        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while logging in. Please try again.");
    }
}


    @FXML
    private void switchToRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ities45/firealarm/register.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }  
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
