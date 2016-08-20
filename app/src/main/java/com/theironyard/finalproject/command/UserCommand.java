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
}
