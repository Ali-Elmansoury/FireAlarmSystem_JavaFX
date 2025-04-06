/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ities45.firealarm;

import com.fazecast.jSerialComm.SerialPort;
import com.ities45.firealarm.dal.DAO;
import com.ities45.firealarm.dal.UserDTO;
import com.ities45.firealarm.serialcomm.SerialCommHandler;
import com.ities45.firealarm.sessionmanagement.SessionManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ali
 */
public class NormalModeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView logo;
    @FXML
    private Label appName;
    @FXML
    private Label logoutLabel;
    @FXML
    private ImageView userIcon;
    @FXML
    private FlowPane topBar;
    @FXML
    private BorderPane anchorScreen;

    private Popup popup;
    private boolean isPopupVisible = false;

    private SerialCommHandler handler;
    
    private String userEmail; // Add this field to store the email

    // Setter method to receive the email
    public void setUserEmail(String email) {
        this.userEmail = email;
        updateUserInfo(); // Optionally update UI immediately
    }
    
    private UserDTO retrievedUser;
    
    private boolean isAlreadyNavigated = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Check if there's a logged-in user when the app starts
        String loggedInUser = SessionManager.getLoggedInUser();
        if (loggedInUser != null && userEmail == null) {
            setUserEmail(loggedInUser); // Set email from session if app starts here
        }

        String portName = "/dev/ttyACM0";  // Adjust to your port
        handler = new SerialCommHandler(
                9600,
                8,
                SerialPort.NO_PARITY,
                SerialPort.ONE_STOP_BIT
        );

        if (handler.openPort(portName)) {
            handler.setDataListener((data) -> {
                System.out.println("[INFO] Received: " + data.trim());

                if (data.trim().equals("F") && !isAlreadyNavigated) {
                    isAlreadyNavigated = true;
                    navigateToNextScreen(data);
                }
            });

            handler.startReading();
            System.out.println("[INFO] Press Enter to close port...");
            //new Scanner(System.in).nextLine();  // Wait for user input to exit

            //handler.closePort();
        } else {
            System.out.println("[ERROR] Could not open port.");
        }

        // Defer the logic of accessing Stage until the UI is fully initialized
        Platform.runLater(() -> {
            Stage mainStage = (Stage) anchorScreen.getScene().getWindow();

            // Set event listener for when the stage is hidden (e.g., minimized or closed)
            mainStage.setOnHidden(event -> {
                System.out.println("Main window hidden. Hiding popup.");
                hidePopupIfVisible();  // Hide the popup when the main window is hidden
            });
        });
        // Set top offset
        topBar.setLayoutY(20);

        // Center horizontally when width changes
        anchorScreen.widthProperty().addListener((obs, oldVal, newVal) -> {
            double parentWidth = newVal.doubleValue();
            double paneWidth = topBar.getPrefWidth(); // or getWidth() if dynamic
            topBar.setLayoutX((parentWidth - paneWidth) / 2);
        });

        logo.setOnMouseClicked(this::handleLogoClick);

        logo.setOnMouseEntered(this::handleMouseEntered);
        logo.setOnMouseExited(this::handleMouseExited);

        userIcon.setOnMouseClicked(this::handleUserIconClick);
        userIcon.setOnMouseEntered(this::handleMouseEntered);
        userIcon.setOnMouseExited(this::handleMouseExited);

        logoutLabel.setOnMouseEntered(this::handleMouseEntered);
        logoutLabel.setOnMouseExited(this::handleMouseExited);
    }
    
    // Update the popup with the email
    private void updateUserInfo() {
        if (userEmail != null) {
            try {
                retrievedUser = DAO.getUserData(userEmail);
                if (retrievedUser != null) {
                    System.out.println("User retrieved successfully:");
                    System.out.println("Username: " + retrievedUser.getUsername());
                    System.out.println("Email: " + retrievedUser.getEmail());
                    System.out.println("Emergency Email: " + retrievedUser.getEmergencyEmail());
                } else {
                    System.out.println("No user found with that email.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(NormalModeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void handleLogoClick(MouseEvent event) {
        System.out.println("Logo clicked! Reloading page...");
        try {
            // Get the current stage
            Stage stage = (Stage) logo.getScene().getWindow();

            // Reload the FXML file (e.g., "normalmode.fxml")
            Parent root = FXMLLoader.load(getClass().getResource("/com/ities45/firealarm/normalMode.fxml"));

            // Create a new scene and set it to the stage
            Scene scene = new Scene(root, 604, 839);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUserIconClick(MouseEvent event) {
        System.out.println("User icon clicked! Displaying user info...");

        // Check if the popup is already visible do not show it again
        if (isPopupVisible) {
            return;
        }

        // Get the current stage and scene
        Stage stage = (Stage) userIcon.getScene().getWindow();
        Scene scene = stage.getScene();

        double windowX = stage.getX();  // X position on screen
        double windowY = stage.getY();
        double windowWidth = scene.getWidth();
        double windowHeight = scene.getHeight();

        // Create a new Popup to display user info
        popup = new Popup();

        // Create a VBox container for the user information
        VBox userInfoBox = new VBox(5);  // 5px spacing between elements
        userInfoBox.setStyle("-fx-background-color: white; -fx-border-color: black; "
                + "-fx-border-radius: 3px; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        userInfoBox.setAlignment(Pos.CENTER_LEFT); // Align text to the left
        userInfoBox.setPadding(new Insets(10));

        // Create labels for user information
        Label nameLabel = new Label("Name: " + retrievedUser.getUsername());
        Label emailLabel = new Label("Email: " + retrievedUser.getEmail());
        Label emergencyEmailLabel = new Label("Emergency Email: " + retrievedUser.getEmergencyEmail());

        // Set a uniform font style and alignment
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        emailLabel.setStyle("-fx-font-size: 13px;");
        emergencyEmailLabel.setStyle("-fx-font-size: 13px;");

        // Create a separator
        Separator separator = new Separator();
        //separator.setStyle("-fx-background-color: #888; -fx-padding: 5px;");

        // Create a close button
        Label closeLabel = new Label("Close");
        closeLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;");
        closeLabel.setOnMouseClicked(e -> {
            popup.hide();
            isPopupVisible = false;
        });

        // Add elements to VBox
        userInfoBox.getChildren().addAll(nameLabel, emailLabel, emergencyEmailLabel, separator, closeLabel);

        // Add the VBox to the popup
        popup.getContent().add(userInfoBox);

        // Compute the required popup size dynamically based on content
        userInfoBox.applyCss();
        userInfoBox.layout();
        double popupWidth = userInfoBox.prefWidth(-1) + 20;  // Get actual computed width
        double popupHeight = userInfoBox.prefHeight(-1) + 20; // Approximate height dynamically

        // Get the mouse click position
        double clickX = event.getScreenX();
        double clickY = event.getScreenY();

        // Adjust position to keep popup inside the scene
        double adjustedX = Math.min(clickX, windowX + windowWidth - popupWidth - 10);
        double adjustedY = Math.min(clickY, windowY + windowHeight - popupHeight - 10);

        // Show the popup at the adjusted position
        popup.show(stage, adjustedX, adjustedY);

        isPopupVisible = true;

    }

    // Simplified hidePopupIfVisible method
    private void hidePopupIfVisible() {
        if (isPopupVisible && popup != null) {
            popup.hide();
            isPopupVisible = false;
        }
    }

    @FXML
    private void handleLogoutClick(MouseEvent event) {
        System.out.println("logout handle");
        SessionManager.logout();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ities45/firealarm/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMouseEntered(MouseEvent event) {
        logo.getScene().setCursor(Cursor.HAND); // Set cursor to hand when mouse enters the logo
        userIcon.getScene().setCursor(Cursor.HAND);
        logoutLabel.getScene().setCursor(Cursor.HAND);

    }

    private void handleMouseExited(MouseEvent event) {
        logo.getScene().setCursor(Cursor.DEFAULT); // Reset cursor to default (arrow) when mouse exits the logo
        userIcon.getScene().setCursor(Cursor.DEFAULT);
        logoutLabel.getScene().setCursor(Cursor.DEFAULT);

    }

    private void navigateToNextScreen(String data) {
        handler.stopReading();
        //handler.closePort();
        try {
            // Print to debug if this is executed
            System.out.println("[INFO] Navigating to fireMode screen with data: " + data);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ities45/firealarm/fireMode.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Print to debug if this line works
            System.out.println("[INFO] FireMode screen loaded.");

            Stage stage = (Stage) userIcon.getScene().getWindow();
            Platform.runLater(() -> {
                stage.setScene(nextScene);  // Switch to the next scene
                stage.show();
            });

            // Get the controller for the fireMode screen
            FireModeController nextController = loader.getController();

            // Pass the handler reference to the next controller
            nextController.setHandler(handler);
            nextController.setUserData(retrievedUser); // Pass user data
            nextController.setupUI(data);  // Set up the UI components based on the data

            // Print to debug
            System.out.println("[INFO] Scene changed to fireMode.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
