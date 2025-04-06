package com.ities45.firealarm.serialcomm;

import com.fazecast.jSerialComm.SerialPort;
import java.util.function.Consumer;

public class SerialCommHandler {

    private SerialPort serialPort;
    private Thread readThread;
    private boolean running = false;
    private Consumer<String> dataListener;

    private final int dataBits;
    private final int parity;
    private final int stopBits;
    private final int baudRate;

    public SerialCommHandler(int baudRate, int dataBits, int parity, int stopBits) {
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.parity = parity;
        this.stopBits = stopBits;
    }

    // Open the serial port with a specified timeout
    public boolean openPort(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);
        serialPort.setNumDataBits(dataBits);
        serialPort.setNumStopBits(stopBits);
        serialPort.setParity(parity);

        boolean opened = serialPort.openPort();
        System.out.println(opened ? "[INFO] Port opened successfully: " + portName
                                  : "[ERROR] Failed to open port: " + portName);
        return opened;
    }

    public boolean isPortOpen() {
        return serialPort != null && serialPort.isOpen();
    }

    public void closePort() {
        stopReading();  // Ensure the thread stops
        if (isPortOpen()) {
            boolean closed = serialPort.closePort();
            System.out.println(closed ? "[INFO] Port closed successfully."
                                      : "[ERROR] Failed to close port.");
        }
    }

    public boolean sendData(byte[] data) {
        if (isPortOpen()) {
            int bytesWritten = serialPort.writeBytes(data, data.length);
            System.out.println("[DEBUG] Sent bytes: " + bytesWritten);
            return bytesWritten == data.length;
        }
        System.out.println("[WARN] Cannot send data. Port is not open.");
        return false;
    }

    // Send string data directly
    public boolean sendString(String data) {
    return sendData(data.getBytes());
}

    public void setDataListener(Consumer<String> listener) {
        this.dataListener = listener;
    }

    public void startReading() {
    if (isPortOpen() && !running) {
        running = true;
        readThread = new Thread(() -> {
            byte[] buffer = new byte[1024];
            StringBuilder messageBuilder = new StringBuilder(); // Accumulate message
            System.out.println("[INFO] Started reading thread.");

            while (running) {
                try {
                    int bytesRead = serialPort.readBytes(buffer, buffer.length);
                    if (bytesRead > 0) {
                        for (int i = 0; i < bytesRead; i++) {
                            char receivedChar = (char) buffer[i];
                            messageBuilder.append(receivedChar);

                            // Check for end of message (i.e., "\r\n")
                            if (receivedChar == '\n' && messageBuilder.length() > 1 && messageBuilder.charAt(messageBuilder.length() - 2) == '\r') {
                                String fullMessage = messageBuilder.toString();
                                System.out.println("[DEBUG] Received: " + fullMessage);
                                if (dataListener != null) {
                                    dataListener.accept(fullMessage.trim());  // Send complete message to listener
                                }
                                messageBuilder.setLength(0);  // Reset builder for next message
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[ERROR] Error while reading from port: " + e.getMessage());
                    running = false;
                }
            }

            System.out.println("[INFO] Reading thread stopped.");
        });
        readThread.start();
    }
}


    public void stopReading() {
        if (running) {
            running = false;
            if (readThread != null && readThread.isAlive()) {
                try {
                    readThread.join(500); // Wait briefly for thread to finish
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("[WARN] Reading thread interrupted during shutdown.");
                }
            }
        }
    }
}
