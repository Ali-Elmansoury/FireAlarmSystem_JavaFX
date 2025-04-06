package com.ities45.firealarm;

import com.ities45.firealarm.sessionmanagement.SessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        String loggedInUser = SessionManager.getLoggedInUser();

        FXMLLoader loader;
        if (loggedInUser != null) {
            // User is already logged in, go to home
            loader = new FXMLLoader(App.class.getResource("normalMode.fxml"));
            scene = new Scene(loader.load(), 604, 839);

            // Get the controller and pass the email
            NormalModeController controller = loader.getController();
            controller.setUserEmail(loggedInUser);
        } else {
            // No user session, show login/register or splash
            loader = new FXMLLoader(App.class.getResource("splash.fxml"));
            scene = new Scene(loader.load(), 604, 839);
        }

        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
