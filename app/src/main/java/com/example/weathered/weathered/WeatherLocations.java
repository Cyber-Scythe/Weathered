package com.example.weathered.weathered;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathered.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class WeatherLocations extends AppCompatActivity {

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    public static ArrayList<LocationModel> locations;

    public static RecyclerView rvLocations;

    static LocationsAdapter locationsAdapter;

    String newCoordinates;
    String newCity;

    public static ArrayList<Double> lats = new ArrayList<>();
    public static ArrayList<Double> lons = new ArrayList<>();
    public static ArrayList<String> cities = new ArrayList<>();

    ArrayList<Fragment> weatherFrags = new ArrayList<>();

    String time, city, temp, weather, dayNight;

    // [ START: backToMain() ]
    public void backToMain(View view) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }
    // [ END: backToMain() ]


    // [ START: onCreate() ]
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_locations);

        // Lookup the recyclerview in activity layout
        rvLocations = (RecyclerView) findViewById(R.id.rvLocations);
        //System.out.println("AFTER LOOK UP RECYCLER VIEW IN ACTIVITY LAYOUT");

        // Initialize locations
        locations = LocationModel.createLocationsList(LocationModel.cityNameArray.size());
        //System.out.println("AFTER INITIALIZING LOCATIONS");

        // Create adapter passing in the sample user data
        locationsAdapter = new LocationsAdapter(locations);
       // System.out.println("AFTER CREATING ADAPTER PASSING IN SAMPLE DATA");

        // Attach the adapter to the recyclerview to populate items
        rvLocations.setAdapter(locationsAdapter);
        //System.out.println("AFTER ATTACHING ADAPTER TO RECYCLERVIEW");

        // Set layout manager to position the items
        rvLocations.setLayoutManager(new LinearLayoutManager(this));


        rvLocations.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                                              rvLocations,
                                              new RecyclerItemClickListener.OnItemClickListener() {

        // [ START: onItemClick() ]
        @Override
        public void onItemClick(View view, int position) {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Bundle bundle = new Bundle();

            switch(position){

                case 0:

                    // Replace whatever is in the fragment_container view with this fragment
                    transaction.replace(R.id.container, CurrentLocationFragment.class, null);
                    transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();

                    break;

                case 1:

                    WeatherLocationFragment fragment = WeatherLocationFragment.newInstance(getLat(1),
                                                        getLon(1),
                                                        getCity(1));

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment).commit();

                    break;

                case 2:

                    WeatherLocationFragment fragment2 = WeatherLocationFragment.newInstance(getLat(2),
                            getLon(2),
                            getCity(2));

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment2).commit();

                    break;


                case 3:

                    WeatherLocationFragment fragment3 = WeatherLocationFragment.newInstance(getLat(3),
                            getLon(3),
                            getCity(3));

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment3).commit();

                    break;


                case 4:

                    WeatherLocationFragment fragment4 = WeatherLocationFragment.newInstance(getLat(4),
                            getLon(4),
                            getCity(4));

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment4).commit();

                    break;


                case 5:

                    WeatherLocationFragment fragment5 = WeatherLocationFragment.newInstance(getLat(5),
                            getLon(5),
                            getCity(5));

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment5).commit();

                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        }
        // [ END: onItemClick() ]


        // [ START: onLongItemClick() ]
        @Override public void onLongItemClick(View view, int position) {
            // do whatever
        }
        // [ END: onLongItemClick() ]
        }));
        // [ END: OnItemTouchListener ]

        /*
        * Initialize Places. For simplicity, the API key is hard-coded. In a production
        * environment we recommend using a secure mechanism to manage API keys.
        */

        //String placesApi = getString(R.string.placesAPI);
        //String API = "AIzaSyCyhFTpgtZB-JPjBKEHEEu4k3--UYkdxwk";
        String placesApi = "AIzaSyCLv4GpChbAn-C9HywCA3XWUJrV3z4oa9s";

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), placesApi);
        }

        // ---- NEW CODE ----
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // check autocompleteFragment is not null
        // Specify types of data to return
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response
        autocompleteFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {

                    @Override
                    public void onPlaceSelected(@NotNull Place place) {

                        newCoordinates = place.getLatLng().toString();
                        assert newCoordinates != null;

                        newCity = place.getName();
                        assert newCity != null;

                        // TODO: Get info about the selected place.
                        System.out.println("Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());


                        Intent placeIntent = new Intent(getApplicationContext(), NewLocation.class);

                        placeIntent.putExtra("city", place.getName());

                        double lat = place.getLatLng().latitude;
                        double lng = place.getLatLng().longitude;

                        placeIntent.putExtra("lat", lat);
                        placeIntent.putExtra("lng", lng);

                        startActivity(placeIntent);

                        // Save user's query
                        String query = findViewById(R.id.autocomplete_fragment).toString();

                        // Create new autocomplete session token.
                        // Pass token to FindAutocompletePredictionsRequest
                        // ??? and once again when user makes a selection
                        // (EX: calling fetchPlace()
                        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

                        // Use builder to create a FindAutcompletePredictionsRequest
                        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                                .setSessionToken(token)
                                .setQuery(query)
                                .build();

                        // Create a new Places client instance.
                        PlacesClient placesClient = Places.createClient(WeatherLocations.this);

                        placesClient.findAutocompletePredictions(request).addOnSuccessListener(
                                new OnSuccessListener<FindAutocompletePredictionsResponse>() {

                                    @Override
                                    public void onSuccess(FindAutocompletePredictionsResponse response) {

                                        for (AutocompletePrediction prediction :
                                                response.getAutocompletePredictions()) {

                                            System.out.println("Place Info: " + prediction
                                                    .getPrimaryText(null)
                                                    .toString());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {

                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        if (e instanceof ApiException) {

                                            ApiException apiException = (ApiException) e;
                                            System.out.println("Place not found: " +
                                                    apiException.getStatusCode());
                                        } // END if statement
                                    } // END onFailure method
                                }); // END onFailureListener method

                        } // END onPlaceSelected

                        public void onError(@NonNull Status status) {
                            System.out.println("Error occurred: " + status.toString());
                        }
                        // END onFailureListener

                    });
                    // END PlaceSelectionListener

    }
    // END onCreate method

    public void setLats(double lat){
        lats.add(lat);
    }

    public double getLat(int position){
        return lats.get(position);
    }

    public void setLons(double lon){
        lons.add(lon);
    }

    public double getLon(int position){
        return lons.get(position);
    }

    public void setCities(String city){
        cities.add(city);
    }

    public String getCity(int position){
        return cities.get(position);
    }

    public boolean containsCity(String city){
        return cities.contains(city);
    }
}
// END WeatherLocations class