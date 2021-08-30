package com.example.weathered.weathered.Response;

public class Daily {

    private long dt;
    private long sunrise;
    private long sunset;
    private Temp temp;
    private FeelsLike feels_like;
    private double pressure;
    private double humidity;
    private double dew_point;
    private double wind_speed;
    private double wind_deg;
    private Weather[] weather;
    private double clouds;
    private double pop;
    private double uvi;
    private Double rain;

    public long getDt() {
        return dt;
    }

    public void setDt(long value) {
        this.dt = value;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long value) {
        this.sunrise = value;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long value) {
        this.sunset = value;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp value) {
        this.temp = value;
    }

    public FeelsLike getFeelsLike() {
        return feels_like;
    }

    public void setFeelsLike(FeelsLike value) {
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

    public double getClouds() {
        return clouds;
    }

    public void setClouds(long value) {
        this.clouds = value;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double value) {
        this.pop = value;
    }

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double value) {
        this.uvi = value;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double value) {
        this.rain = value;
    }
}

