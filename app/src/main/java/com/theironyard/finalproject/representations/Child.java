package com.theironyard.finalproject.representations;

/**
 * Created by vasantia on 8/19/16.
 */
public class Child {

    private String name;
    private String username;
    private int id;
    private int childPoint;
    private boolean tokenValid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChildPoint() {
        return childPoint;
    }

    public void setChildPoint(int childPoint) {
        this.childPoint = childPoint;
    }

    public boolean isTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }
}
