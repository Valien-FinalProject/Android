package com.theironyard.finalproject.command;

import java.sql.Timestamp;

/**
 * Created by vasantia on 8/22/16.
 */
public class ChoreCommand {

    private Timestamp startDate;
    private Timestamp endDate;
    private String name;
    private String description;
    private int value;
    private int id;
    private boolean pending;
    private boolean complete;

    public ChoreCommand() {
    }

    public ChoreCommand(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
