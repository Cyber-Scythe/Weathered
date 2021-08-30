package com.example.weathered.weathered.Response;

public class Alert{
    private String sender_name;
    private String event;
    private int start;
    private int end;
    private String description;

    public String getSenderName() {
        return sender_name;
    }

    public void setSenderName(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
