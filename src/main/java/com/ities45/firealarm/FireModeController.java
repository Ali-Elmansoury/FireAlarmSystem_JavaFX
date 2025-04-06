/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ities45.firealarm;

import com.fazecast.jSerialComm.SerialPort;
import com.ities45.firealarm.notification.NotificationHandler;
import com.ities45.firealarm.serialcomm.SerialCommHandler;
import com.ities45.firealarm.sessionmanagement.SessionManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ali
 */
public class FireModeController implements Initializable {

    @FXML
    private ImageView logo;
    @FXML
    private Label appName;
    @FXML
    private ImageView userIcon;
    @FXML
    private FlowPane topBar;
    @FXML
    private BorderPane anchorScreen;
    @FXML
    private ImageView fire1, fire2, fire3;
    @FXML
    private Button alarmButton;  // The consolidated button
    @FXML
    private Label logoutLabel;

    private boolean isPopupVisible = false;
    private boolean isAlarmOn = true;
    private MediaPlayer alarmPlayer;

    private SerialCommHandler handler;

    // Setter method to receive the handler
    public void setHandler(SerialCommHandler handler) {
        this.handler = handler;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        topBar.setLayoutY(20);

        anchorScreen.widthProperty().addListener((obs, oldVal, newVal) -> {
            double parentWidth = newVal.doubleValue();
            double paneWidth = topBar.getPrefWidth();
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

        // Set random fire GIFs
        String[] gifs = {
            getClass().getResource("/images/fire1.gif").toExternalForm(),
            getClass().getResource("/images/fire2.gif").toExternalForm(),
            getClass().getResource("/images/fire3.gif").toExternalForm()
        };
        Random rand = new Random();

        fire1.setImage(new Image(gifs[rand.nextInt(gifs.length)]));
        fire2.setImage(new Image(gifs[rand.nextInt(gifs.length)]));
        fire3.setImage(new Image(gifs[rand.nextInt(gifs.length)]));

    }

    private void toggleAlarmState() {
        isAlarmOn = !isAlarmOn;

        ImageView imgView = (ImageView) alarmButton.getGraphic();

        if (isAlarmOn) {
            imgView.setImage(new Image(getClass().getResource("/images/alarm-on.png").toExternalForm()));
            alarmButton.setText("Turn Off Alarm");

            // 🔊 Start the alarm sound
            Media alarmSound = new Media(getClass().getResource("/images/alarm1.wav").toExternalForm());
            alarmPlayer = new MediaPlayer(alarmSound);
            alarmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop sound
            alarmPlayer.play();

            System.out.println("Alarm activated");
        } else {
            imgView.setImage(new Image(getClass().getResource("/images/alarm-off.png").toExternalForm()));
            alarmButton.setText("Alarm stopped");

            // 🔇 Stop the sound
            if (alarmPlayer != null) {
                alarmPlayer.stop();
            }

            System.out.println("Alarm stopped");
        }
    }

    @FXML
    public void handleLogoClick(MouseEvent event) {
        System.out.println("Logo clicked! Reloading page...");
        try {
            Stage stage = (Stage) logo.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/ities45/firealarm/fireMode.fxml"));
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
        Popup popup = new Popup();

        // Create a VBox container for the user information
        VBox userInfoBox = new VBox(5);  // 5px spacing between elements
        userInfoBox.setStyle("-fx-background-color: white; -fx-border-color: black; "
                + "-fx-border-radius: 3px; -fx-padding: 10px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");
        userInfoBox.setAlignment(Pos.CENTER_LEFT); // Align text to the left
        userInfoBox.setPadding(new Insets(10));

        // Create labels for user information
        Label nameLabel = new Label("Name: John Doe");
        Label emailLabel = new Label("Email: johndoe@example.com");
        Label emergencyEmailLabel = new Label("Emergency Email: emergency@example.com");

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
        logo.getScene().setCursor(Cursor.HAND);
        userIcon.getScene().setCursor(Cursor.HAND);
        logoutLabel.getScene().setCursor(Cursor.HAND);
    }

    private void handleMouseExited(MouseEvent event) {
        logo.getScene().setCursor(Cursor.DEFAULT);
        userIcon.getScene().setCursor(Cursor.DEFAULT);
        logoutLabel.getScene().setCursor(Cursor.DEFAULT);
    }

    public void setupUI(String data) {
        if ("F".equals(data)) {
            // Enable the alarm sound and GIF if the received data is "start"
            // Setup the alarm sound
            Media alarmSound = new Media(getClass().getResource("/images/alarm1.wav").toExternalForm());

            alarmPlayer = new MediaPlayer(alarmSound);
            alarmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            alarmPlayer.play();

            // Button click handler
            alarmButton.setOnAction(e -> toggleAlarmState());

            //noti
            String subject = "Emergency Alert - Alarm Triggered";
            String body = "The alarm has been triggered. Immediate attention is required.";
            String recipientEmail = "ali_elmansoury2001@live.com";  // Replace with the recipient's email address
            NotificationHandler.sendEmergencyEmail(subject, body, recipientEmail);

            String portName = "/dev/ttyACM0";  // Adjust to your port
            /*handler = new SerialCommHandler(
                    9600,
                    8,
                    SerialPort.NO_PARITY,
                    SerialPort.ONE_STOP_BIT
            );*/

            new Thread(() -> {
                System.out.println("[INFO] Fire detected. Sending 'L' to controller...");
                handler.sendString("L");
                try {
                    Thread.sleep(100);  // Wait for 100 ms before closing the port
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.closePort();
                /*if (handler.openPort(portName)) {
                    
                } else {
                    System.out.println("[ERROR] Could not open port.");
                }*/
            }).start();

        } else {

        }
    }
}
