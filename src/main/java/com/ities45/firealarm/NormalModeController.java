/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ities45.firealarm;

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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private ImageView userIcon;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //logo.getStyleClass().add("image-view");
        
        double radius = logo.getFitWidth() / 2;  
        Circle clip = new Circle(radius, radius, radius);
        logo.setClip(clip);
        
        logo.setOnMouseClicked(this::handleLogoClick);
        
        logo.setOnMouseEntered(this::handleMouseEntered);
        logo.setOnMouseExited(this::handleMouseExited);
    } 
    
    @FXML
    public void handleLogoClick(MouseEvent event) {
        System.out.println("Logo clicked! Reloading page...");
        try{
                        // Get the current stage
            Stage stage = (Stage) logo.getScene().getWindow();

            // Reload the FXML file (e.g., "normalmode.fxml")
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/normalmode.fxml"));

            // Create a new scene and set it to the stage
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleUserIconClick(MouseEvent event){
        System.out.println("User icon clicked! Displaying user info...");

        // Create a new Popup to display user info
        Popup popup = new Popup();
        
        // Create a VBox container for the user information
        VBox userInfoBox = new VBox(10);  // 10px spacing between elements
        userInfoBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-radius: 10px;");
        userInfoBox.setAlignment(Pos.CENTER);
        userInfoBox.setMinWidth(200);
        userInfoBox.setMaxWidth(250);
        
        // Create labels for user information (you can get this data from a database or static values)
        Label nameLabel = new Label("Name: John Doe");
        Label emailLabel = new Label("Email: johndoe@example.com");
        Label emergencyEmailLabel = new Label("Emergency Email: emergency@example.com");
        
        // Add the labels to the VBox
        userInfoBox.getChildren().addAll(nameLabel, emailLabel, emergencyEmailLabel);
        
        // Set a close button (optional)
        Label closeLabel = new Label("Close");
        closeLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        closeLabel.setOnMouseClicked(e -> popup.hide());  // Hide the popup when clicked
        
        userInfoBox.getChildren().add(closeLabel);
        
        // Show the popup at the mouse click position
        popup.getContent().add(userInfoBox);
        popup.show(userIcon, event.getScreenX(), event.getScreenY());
    }
    @FXML
    // Method to handle mouse entering the logo (change cursor to hand)
    private void handleMouseEntered(MouseEvent event) {
        logo.getScene().setCursor(Cursor.HAND); // Set cursor to hand when mouse enters the logo
    }
    @FXML
    // Method to handle mouse exiting the logo (reset cursor to default)
    private void handleMouseExited(MouseEvent event) {
        logo.getScene().setCursor(Cursor.DEFAULT); // Reset cursor to default (arrow) when mouse exits the logo
    }
    
}
