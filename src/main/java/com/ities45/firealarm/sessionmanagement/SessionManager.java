/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ities45.firealarm.sessionmanagement;

import java.util.prefs.Preferences;

/**
 *
 * @author ali
 */
public class SessionManager {
    private static final Preferences prefs = Preferences.userRoot().node("FireAlarmApp/session");

    public static void setLoggedInUser(String username) {
        prefs.put("loggedInUser", username);
    }

    public static String getLoggedInUser() {
        return prefs.get("loggedInUser", null); // null if not logged in
    }

    public static void logout() {
        prefs.remove("loggedInUser");
    }
}
