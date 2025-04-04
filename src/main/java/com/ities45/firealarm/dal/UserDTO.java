/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ities45.firealarm.dal;

/**
 *
 * @author ali
 */
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String emergencyEmail;

    public UserDTO(String username, String email, String password, String emergencyEmail) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.emergencyEmail = emergencyEmail;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setEmergencyEmail(String emergencyEmail){
        this.emergencyEmail = emergencyEmail;
    }

    public String getUsername() {
        return username;
    }
    
    public String getEmail(){
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public String getEmergencyEmail(){
        return emergencyEmail;
    }
    
}
