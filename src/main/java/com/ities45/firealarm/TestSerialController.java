package com.ities45.firealarm;

import com.fazecast.jSerialComm.SerialPort;
import com.ities45.firealarm.serialcomm.SerialCommHandler;
import java.util.Scanner;

public class TestSerialController {
    public static void main(String[] args) {
        String portName = "/dev/ttyACM0";  // Adjust to your port
        SerialCommHandler handler = new SerialCommHandler(
            9600,
            8,
            SerialPort.NO_PARITY,
            SerialPort.ONE_STOP_BIT
        );

        if (handler.openPort(portName)) {
            handler.setDataListener((data) -> {
                System.out.println("[INFO] Received: " + data.trim());

                if (data.trim().equals("F")) {
                    System.out.println("[INFO] Fire detected. Sending 'L' to controller...");
                    handler.sendString("L");  // Send 'L' to microcontroller
                }
            });

            handler.startReading();
            System.out.println("[INFO] Press Enter to close port...");
            new Scanner(System.in).nextLine();  // Wait for user input to exit

            handler.closePort();
        } else {
            System.out.println("[ERROR] Could not open port.");
        }
    }
}
