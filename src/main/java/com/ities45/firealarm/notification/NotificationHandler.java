/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ities45.firealarm.notification;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 *
 * @author ali
 */
public class NotificationHandler {
    private static final String SMTP_SERVER = "smtp.gmail.com"; // Use your email service's SMTP server
    private static final String USERNAME = "ali.elmansoury21@gmail.com"; // Your email address
    private static final String PASSWORD = "iyvtcwdziechnpyf"; // Your email password (or App password if 2FA enabled)
    private static final String FROM_EMAIL = "ali.elmansoury21@gmail.com"; // Your email address (same as USERNAME)

    // Method to send email
    public static void sendEmergencyEmail(String subject, String body, String toEmail) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create session with authentication
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Emergency email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send emergency email.");
        }
    }
}
