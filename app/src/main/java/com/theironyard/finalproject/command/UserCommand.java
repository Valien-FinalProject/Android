package com.theironyard.finalproject.command;

/**
 * Created by vasantia on 8/19/16.
 */
public class UserCommand {

    private String username;
    private String password;

    public UserCommand() {
    }

    public UserCommand(String username, String password) {
        this.username = username;
        this.password = password;
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
}
