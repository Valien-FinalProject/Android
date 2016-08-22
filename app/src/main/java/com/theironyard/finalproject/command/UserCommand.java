package com.theironyard.finalproject.command;

/**
 * Created by vasantia on 8/19/16.
 */
public class UserCommand {

    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private boolean emailOptIn;
    private boolean phoneOptIn;

    public UserCommand() {
    }

    public UserCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCommand(String username, String password, String name, String email, String phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserCommand(String username, String password, String name, String email, String phone,
                       boolean emailOptIn, boolean phoneOptIn) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.emailOptIn = emailOptIn;
        this.phoneOptIn = phoneOptIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEmailOptIn() {
        return emailOptIn;
    }

    public void setEmailOptIn(boolean emailOptIn) {
        this.emailOptIn = emailOptIn;
    }

    public boolean isPhoneOptIn() {
        return phoneOptIn;
    }

    public void setPhoneOptIn(boolean phoneOptIn) {
        this.phoneOptIn = phoneOptIn;
    }
}
