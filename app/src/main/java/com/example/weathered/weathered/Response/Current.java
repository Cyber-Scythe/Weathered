package com.example.weathered.weathered.Response;

public class Current {
    private long dt;
    private Long sunrise;
    private Long sunset;
    private double temp;
    private double feels_like;
    private double pressure;
    private double humidity;
    private double dew_point;
    private double uvi;
    private double clouds;
    private double visibility;
    private double wind_speed;
    private double wind_deg;
    private Weather[] weather;
    private double pop;

    public long getDt() {
        return dt;
    }

    public void setDt(long value) {
        this.dt = value;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long value) {
        this.sunrise = value;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long value) {
        this.sunset = value;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double value) {
        this.temp = value;
    }

    public double getFeelsLike() {
        return feels_like;
    }

    public void setFeelsLike(double value) {
        this.feels_like = value;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(long value) {
        this.pressure = value;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(long value) {
        this.humidity = value;
    }

    public double getDewPoint() {
        return dew_point;
    }

    public void setDewPoint(double value) {
        this.dew_point = value;
    }

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double value) {
        this.uvi = value;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(long value) {
        this.clouds = value;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(long value) {
        this.visibility = value;
    }

    public double getWindSpeed() {
        return wind_speed;
    }

    public void setWindSpeed(double value) {
        this.wind_speed = value;
    }

    public double getWindDeg() {
        return wind_deg;
    }

    public void setWindDeg(long value) {
        this.wind_deg = value;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] value) {
        this.weather = value;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double value) {
        this.pop = value;
    }
}
