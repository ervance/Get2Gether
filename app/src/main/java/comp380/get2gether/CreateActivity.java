package comp380.get2gether;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CreateActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    /******FORM FIELDS********/
    private EditText chosenEventName; //holds the event name from the eventName text box
    ArrayList<String> formVariables = new ArrayList<>();
    String eName;
    String eType;
    String eStartTime;
    String eEndTime;
    /******FORM FIELDS********/

    /******Map fields******/
    private GoogleMap mMap;
    private LocationManager locManager;
    private GoogleApiClient mLocationClient;
    private com.google.android.gms.location.LocationListener mListener;
    private LatLng currentLocale;  //holds the current location throughout the activity
    private static final int ERROR_DIALOG_REQUEST = 9001;
    /****End Map fields****/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //This code handles the map checking and initialization

       //if statement checks to see if we are connected to the map.
        //if we are, go to Northridge first as current loc.
        if (servicesOK()) {
            if (initMap()) {
                gotoLocation(34.2417, -118.5283, 15);

                mLocationClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                mLocationClient.connect();
            } else {
                Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
            }
        }//end checking to see if map is working

        //The following code checks to see if the map marker has been moved
        //if it has been moved it will pass the lat and lng to the map activity class
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){
            @Override
            public void onMarkerDragStart(Marker marker){
            }
            @Override
            public void onMarkerDrag(Marker marker){
            }
            @Override
            public void onMarkerDragEnd(Marker marker){
                LatLng ll = marker.getPosition();
                currentLocale = ll;
            }
        });
            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            //---------------------------------------------------------------------------------------
            //This is the code for the spinner from the Strings array
            final Spinner eventTypeSpinner = (Spinner) findViewById(R.id.eventSpinner);

            // Create an ArrayAdapter using the string array and a default spinner
            final ArrayAdapter<CharSequence> eventTypeAdapter = ArrayAdapter
                    .createFromResource(this, R.array.eventArray,
                            android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            eventTypeAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            eventTypeSpinner.setAdapter(eventTypeAdapter);
        //------------------------------------------------------------------------------------------
            final Spinner startTimesSpinner = (Spinner) findViewById(R.id.startTimeSpinner);

            final String[] startTimes = new String[]{"Select a Start Time", "1:00 am", "2:00 am", "3:00 am", "4:00 am", "5:00 am", "6:00 am",
                    "7:00 am", "8:00 am", "9:00 am", "10:00 am", "11:00 am", "12:00 pm", "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm", "5:00 pm", "6:00 pm",
                    "7:00 pm", "8:00 pm", "9:00 pm", "10:00 pm", "11:00 pm", "12:00 am"};

            ArrayAdapter<String> start = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, startTimes);

            startTimesSpinner.setAdapter(start);

            startTimesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Log.v("item", (String) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });
            //-------------------------END SPINNER SECTION-------------------------------------------

        //---------------------------------------------------------------------------------------
        final Spinner stopTimeSpinner = (Spinner) findViewById(R.id.stopTimeSpinner);

        String[] stopTimes = new String[]{"Select an End Time", "1:00 am", "2:00 am", "3:00 am", "4:00 am", "5:00 am", "6:00 am",
                "7:00 am", "8:00 am", "9:00 am", "10:00 am", "11:00 am", "12:00 pm", "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm", "5:00 pm", "6:00 pm",
                "7:00 pm", "8:00 pm", "9:00 pm", "10:00 pm", "11:00 pm", "12:00 am"};

        ArrayAdapter<String> stop = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stopTimes);

        stopTimeSpinner.setAdapter(stop);

        stopTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //-------------------------END SPINNER SECTION-------------------------------------------


       /*****************Start Searh Section***********************/
        //button for search
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        //Click listener for search
        searchButton.setOnClickListener(new View.OnClickListener() {
           //Once search is clicked, pull search text and find location
            public void onClick(View v) {
                LatLng newLoc;
                //Save search data into variable
                EditText searchBox = (EditText) findViewById(R.id.search);
                String searchItem = searchBox.getText().toString();
                //use search method below to find string (if nothing found return null)
                newLoc= getLocationFromAddress(CreateActivity.this, searchItem);
                gotoLocation(newLoc.latitude, newLoc.longitude, 10);
                placeMapMarker(newLoc.latitude, newLoc.longitude);
            }
        });
        /*****************End Search Section***********************/

            //Ties the completeForm button to the variable submitButton
            final Button submitButton = (Button) findViewById(R.id.completeForm);

            //Create an onClickListener to save user input to variables and open MapActivity
            //Once the "Subtmit" button has been pressed save the event name "eName"
            //Save the event type "eType", and save the event time "eTime"
            submitButton.setOnClickListener(new View.OnClickListener() {

                //This is for for the submit button on the form page
                public void onClick(View v) {
                    //On button press, store text from the 3 fields
                    chosenEventName = (EditText) findViewById(R.id.eventName);
                    eName = chosenEventName.getText().toString();
                    eType = eventTypeSpinner.getSelectedItem().toString();
                    eStartTime = startTimesSpinner.getSelectedItem().toString();
                    eEndTime = stopTimeSpinner.getSelectedItem().toString();
                    //Here is your objective. Make sure that the current location is always saved
                    //to currentLocale, once it is saved to that then come into this section
                    //break up the currentLocale into two variables to be put into the string array
                    //then you need to go to map activity, unpack those locations and use them for
                    String eLat = Double.toString(currentLocale.latitude);
                    String eLng = Double.toString(currentLocale.longitude);

                    //the marker on the map.


                    //Built Arraylist to store variables from Form
                    formVariables.add(eName);
                    formVariables.add(eType);
                    formVariables.add(eStartTime);
                    formVariables.add(eEndTime);
                    formVariables.add(eLat);
                    formVariables.add(eLng);

                /* unblock this when ready for parse server
                //Add it to parse test
                ParseObject inputForm = new ParseObject("InputForm");
                inputForm.put("name", eName);
                inputForm.put("type", eType);
                inputForm.put("time", eTime);
                inputForm.saveInBackground();
                */
                    //----------------------------------------------------------------------------------------

                    //Create an Intent in order to pass info to "MapsAcitivity"
                    Intent intent = new Intent(CreateActivity.this, MapsActivity.class);


                    //Create Identifier for variable types in this .java file
                    //Make an arraylist instead of putExtra
                    //Testing parse so I am commenting this out.
                        intent.putExtra("formVar", formVariables);

                    //Start other Activity (MapsActivity) with pin
                        startActivity(intent);
                }
            });
        }
    //This code gets location from address passed in search bar
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    //------------------------------------------------------------------------------------
    //Checks to make sure the services are available and the map is initialized
    //------------------------------------------------------------------------------------
    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to mapping service", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    //checks to see if the map was successfully initialized
    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
        }
        return (mMap != null);
    }



    //This method updates the maps current location
    // Pass in a lat, lng, and zoom distance and it will move the camera to the desired location
    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        currentLocale = latLng;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }


    private void placeMapMarker(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        currentLocale =latLng;
        MarkerOptions marker = new MarkerOptions()
                .draggable(true)
                .position(currentLocale);

        if(currentLocale!=null) {
            //add marker and move camera to current location
            mMap.addMarker(marker);
        }else{
            Toast.makeText(CreateActivity.this, "No Current Location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "Create An Event!", Toast.LENGTH_SHORT).show();

        //checks for permissions to make sure we can use the map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }//end permission check

        //mListener is going to pull the current location
        mListener = new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gotoLocation(location.getLatitude(), location.getLongitude(), 15);  //updates the map to the current location
                placeMapMarker(location.getLatitude(), location.getLongitude());    //places movable marker on current location
            }
        };
        //This section updates the map
        //I used balanced power in order to get decent accuracy without sacrificing battery life
        //If we want we can change this to low power.
        LocationRequest request  = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setInterval(600000);
        request.setFastestInterval(600000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mLocationClient, request, mListener
        );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //This method is to save battery, when the app is paused (sent to the background)
    //it stops trying to update the map. If we need to add more battery saving things in, do it here
    @Override
    protected void onPause(){
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, mListener);
    }
}

