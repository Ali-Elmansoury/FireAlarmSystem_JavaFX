module com.ities45.firealarm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.ities45.firealarm to javafx.fxml;
    exports com.ities45.firealarm;
    requires com.fazecast.jSerialComm;
    requires derbyclient;
    requires java.sql;
    requires java.mail;
    requires java.prefs;
}
