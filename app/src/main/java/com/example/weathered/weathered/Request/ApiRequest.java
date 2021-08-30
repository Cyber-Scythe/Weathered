package com.example.weathered.weathered.Request;


public class ApiRequest {

    private double latitude, longitude;
    private MeasurementUnit units;
    private Language lang;

    private ApiRequest() { }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public MeasurementUnit getMeasurementUnits() {
        return units;
    }

    public Language getLanguage() {
        return lang;
    }


    public static class Builder{

        private double latitude, longitude;
        private MeasurementUnit units = MeasurementUnit.metric;
        private Language lang = Language.en;

        public Builder(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /** Sets the measurement units of the results. Default is metric.
         * @param units  The results measurement units
         * @return self
         */
        public Builder unit(MeasurementUnit units){
            this.units = units;

            return this;
        }

        /** Sets the language of the results. Default is english.
         * @param language  The results language
         * @return self
         */
        public Builder lang(Language language){
            this.lang =language;

            return this;
        }


        public ApiRequest build(){

            ApiRequest apiRequest = new ApiRequest();

            apiRequest.lang = this.lang;
            apiRequest.latitude = this.latitude;
            apiRequest.longitude = this.longitude;
            apiRequest.units = this.units;

            return apiRequest;
        }
    }
}

