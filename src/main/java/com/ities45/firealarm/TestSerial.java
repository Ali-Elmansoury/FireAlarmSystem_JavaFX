package com.ities45.firealarm;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;
import com.ities45.firealarm.serialcomm.SerialCommHandler;

public class TestSerial {
    public static void main(String[] args) {
        // Adjust with your virtual port (e.g., /dev/pts/3)
        String portName = "/dev/pts/3"; 
        SerialCommHandler handler = new SerialCommHandler(
            9600,
            8,
            SerialPort.NO_PARITY,
            SerialPort.ONE_STOP_BIT
        );

        if (handler.openPort(portName)) {
            handler.sendData("Hello from Java!\n".getBytes());

            System.out.println("[INFO] Press Enter to close port...");
            handler.sendData(new Scanner(System.in).nextLine().getBytes()); // Wait for user input

            handler.closePort();
        } else {
            System.out.println("[ERROR] Could not open port.");
        }
    }
}
