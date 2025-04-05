package com.ities45.firealarm;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.ities45.firealarm.dal.DAO;
import com.ities45.firealarm.dal.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private TextField emergencyEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Any initialization you want to perform when the controller loads
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String userName = name.getText().trim();
        String userEmail = email.getText().trim();
        String userPassword = password.getText().trim();
        String userEmergencyEmail = emergencyEmail.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Email and password cannot be empty.");
            return;
        }

        try {
            int result = DAO.register(new UserDTO(userName, userEmail, userPassword, userEmergencyEmail));

            if (result == 1) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful!");
                switchToLogin(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "User registration failed.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong with the database.");
        } catch (IOException e) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, e);
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load login screen.");
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ities45/firealarm/login.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
