package com.example.weathered.weathered.Response;

public class Minutely {
    private long dt;
    private double precipitation;

    public long getDt() {
        return dt;
    }

    public void setDt(long value) {
        this.dt = value;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(long value) {
        this.precipitation = value;
    }
}
