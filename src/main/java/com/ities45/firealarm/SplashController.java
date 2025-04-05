package com.ities45.firealarm;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import javafx.scene.image.ImageView;

public class SplashController {

    @FXML
    private ImageView splashImage;

    public void initialize() {
        // Pause for 3 seconds before transitioning to the registration screen
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            // Load the registration screen
            try {
                // Load the Register Screen FXML
                AnchorPane registerScreen = FXMLLoader.load(getClass().getResource("/com/ities45/firealarm/register.fxml"));
                Scene registerScene = new Scene(registerScreen);

                // Get the current stage and set the new scene
                Stage stage = (Stage) splashImage.getScene().getWindow();
                stage.setScene(registerScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pause.play();  // Start the timer
    }
}
