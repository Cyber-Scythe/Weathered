package com.example.weathered.weathered.Response;

public class ApiResponse{

    private double lat;
    private double lon;
    private String timezone;
    private long timezone_offset;
    private Current current;
    private Minutely[] minutely;
    private Current[] hourly;
    private Daily[] daily;
    private Alert[] alerts;

    public double getLat() {
        return lat;
    }

    public void setLat(double value) {
        this.lat = value;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double value) {
        this.lon = value;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String value) {
        this.timezone = value;
    }

    public long getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(long value) {
        this.timezone_offset = value;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current value) {
        this.current = value;
    }

    public Minutely[] getMinutely() {
        return minutely;
    }

    public void setMinutely(Minutely[] value) {
        this.minutely = value;
    }

    public Current[] getHourly() {
        return hourly;
    }

    public void setHourly(Current[] value) {
        this.hourly = value;
    }

    public Daily[] getDaily() {
        return daily;
    }

    public void setDaily(Daily[] value) {
        this.daily = value;
    }

    public Alert[] getAlerts() {
        return alerts;
    }

    public void setAlerts(Alert[] alerts) {
        this.alerts = alerts;
    }
}

