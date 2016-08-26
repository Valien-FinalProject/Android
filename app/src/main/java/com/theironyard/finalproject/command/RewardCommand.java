package com.theironyard.finalproject.command;

/**
 * Created by vasantia on 8/22/16.
 */
public class RewardCommand {

    private String name;
    private String description;
    private int points;
    private String url;

    public RewardCommand() {
    }

    public RewardCommand(int points) {
        this.points = points;
    }

    public RewardCommand(String name, String description, int points) {
        this.name = name;
        this.description = description;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
