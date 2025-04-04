/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ities45.firealarm.serialcomm;

import com.fazecast.jSerialComm.SerialPort;
import java.util.function.Consumer;

/**
 *
 * @author ali
 */
public class SerialCommHandler {
    
    private SerialPort serialPort;
    private Thread readThread;
    private boolean running = false;
    private Consumer<String> dataListener;  // Listener for processing the data
    
    private int dataBits;
    private int parity;
    private int stopBits;
    private int baudRate;
    
    public SerialCommHandler(int baudRate, int dataBits, int parity, int stopBits){
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.parity = parity;
        this.stopBits = stopBits;
        this.serialPort = null;
    }
    
    // Open the serial port with the specified port name
    public boolean openPort(String portName){
        return true;
    }
    
    // Check if the serial port is open
    public boolean isPortOpen(){
        return true;

    }

    // Close the serial port
    public void closePort(){
        
    }

    // Send data to the serial port
    public boolean sendData(byte[] data){
        return true;
    }

    // Start the non-blocking read in a separate thread
    public void startReading(){
        
    }

    // Stop the reading thread
    public void stopReading(){
        
    }
    
    // Set the data listener for processing received data
    public void setDataListener(Consumer<String> listener) {
        this.dataListener = listener;
    }
}
