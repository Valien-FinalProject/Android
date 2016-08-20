package com.theironyard.finalproject.representations;

/**
 * Created by vasantia on 8/19/16.
 */
public class Parent {

    private String email;
    private boolean emailOptIn;
    private String name;
    private String password;
    private String phone;
    private boolean phoneOptIn;
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailOptIn() {
        return emailOptIn;
    }

    public void setEmailOptIn(boolean emailOptIn) {
        this.emailOptIn = emailOptIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneOptIn() {
        return phoneOptIn;
    }

    public void setPhoneOptIn(boolean phoneOptIn) {
        this.phoneOptIn = phoneOptIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
