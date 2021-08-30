package com.example.weathered.weathered.Response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Hourly implements Serializable, Parcelable {

    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("feels_like")
    @Expose
    private Double feelsLike;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("dew_point")
    @Expose
    private Double dewPoint;
    @SerializedName("uvi")
    @Expose
    private Integer uvi;
    @SerializedName("clouds")
    @Expose
    private Integer clouds;
    @SerializedName("visibility")
    @Expose
    private Integer visibility;
    @SerializedName("wind_speed")
    @Expose
    private Double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private Integer windDeg;
    @SerializedName("wind_gust")
    @Expose
    private Double windGust;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("pop")
    @Expose
    private Integer pop;
    public final static Creator<Hourly> CREATOR = new Creator<Hourly>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Hourly createFromParcel(Parcel in) {
            return new Hourly(in);
        }

        public Hourly[] newArray(int size) {
            return (new Hourly[size]);
        }

    }
            ;
    private final static long serialVersionUID = -547785811948721565L;

    protected Hourly(Parcel in) {
        this.dt = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.temp = ((Double) in.readValue((Double.class.getClassLoader())));
        this.feelsLike = ((Double) in.readValue((Double.class.getClassLoader())));
        this.pressure = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.humidity = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.dewPoint = ((Double) in.readValue((Double.class.getClassLoader())));
        this.uvi = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.clouds = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.visibility = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.windSpeed = ((Double) in.readValue((Double.class.getClassLoader())));
        this.windDeg = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.windGust = ((Double) in.readValue((Double.class.getClassLoader())));
        in.readList(this.weather, (Weather.class.getClassLoader()));
        this.pop = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Hourly() {
    }

    /**
     *
     * @param temp
     * @param visibility
     * @param windGust
     * @param uvi
     * @param pressure
     * @param clouds
     * @param dewPoint
     * @param dt
     * @param feelsLike
     * @param windDeg
     * @param pop
     * @param weather
     * @param humidity
     * @param windSpeed
     */
    public Hourly(Integer dt, Double temp, Double feelsLike, Integer pressure, Integer humidity, Double dewPoint, Integer uvi, Integer clouds, Integer visibility, Double windSpeed, Integer windDeg, Double windGust, List<Weather> weather, Integer pop) {
        super();
        this.dt = dt;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dewPoint = dewPoint;
        this.uvi = uvi;
        this.clouds = clouds;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.windGust = windGust;
        this.weather = weather;
        this.pop = pop;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Integer getUvi() {
        return uvi;
    }

    public void setUvi(Integer uvi) {
        this.uvi = uvi;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(Integer windDeg) {
        this.windDeg = windDeg;
    }

    public Double getWindGust() {
        return windGust;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Integer getPop() {
        return pop;
    }

    public void setPop(Integer pop) {
        this.pop = pop;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dt);
        dest.writeValue(temp);
        dest.writeValue(feelsLike);
        dest.writeValue(pressure);
        dest.writeValue(humidity);
        dest.writeValue(dewPoint);
        dest.writeValue(uvi);
        dest.writeValue(clouds);
        dest.writeValue(visibility);
        dest.writeValue(windSpeed);
        dest.writeValue(windDeg);
        dest.writeValue(windGust);
        dest.writeList(weather);
        dest.writeValue(pop);
    }

    public int describeContents() {
        return  0;
    }

}

