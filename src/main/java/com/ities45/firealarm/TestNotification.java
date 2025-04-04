/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ities45.firealarm;

import com.ities45.firealarm.notification.NotificationHandler;

/**
 *
 * @author ali
 */
public class TestNotification {

    public static void main(String[] args) {

        String subject = "Emergency Alert - Alarm Triggered";
        String body = "The alarm has been triggered. Immediate attention is required.";
        String recipientEmail = "ali_elmansoury2001@live.com";  // Replace with the recipient's email address
        NotificationHandler.sendEmergencyEmail(subject, body, recipientEmail);

    }
}
