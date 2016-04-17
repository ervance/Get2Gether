package comp380.get2gether;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
import java.util.List;


public class CreateActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private EditText chosenEventName; //holds the event name from the eventName text box
    private EditText chosenEventType; //Holds eventLocation ""
    private EditText chosenEventTime;  //holds the eventTime ""
    private static final int ERROR_DIALOG_REQUEST = 9001;

    /******Map fields******/
    private GoogleMap mMap;
    private ArrayList<LatLng> coordinateList = new ArrayList<>();  //collects coordinates
    private int listSize;  //size of coordinateList
    private LocationManager locManager;
    private LatLng currentLocale;
    private OnMapReadyCallback callback;
    private GoogleApiClient mLocationClient;
    private com.google.android.gms.location.LocationListener mListener;
    LatLng northRidge = new LatLng(34.2417, -118.5283);
    /****End Map fields****/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //---------------------------------------------------------------------------------------
       //if statement checks to see if we are connected to the map.
        //if we are go to Northridge first as current loc.
        if (servicesOK()) {
            if (initMap()) {
                gotoLocation(34.2417, -118.5283, 15);

                mLocationClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

                mLocationClient.connect();

//                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
            }

        }//end checking to see if map is working



            //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            //---------------------------------------------------------------------------------------
            //This code handles the spinner on the CreateActivity page
            final Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);

            // Create an ArrayAdapter using the string array and a default spinner
            ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                    .createFromResource(this, R.array.brew_array,
                            android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            staticAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            staticSpinner.setAdapter(staticAdapter);

            final Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

            String[] items = new String[]{"Select a Time", "1:00 am", "2:00 am", "3:00 am", "4:00 am", "5:00 am", "6:00 am",
                    "7:00 am", "8:00 am", "9:00 am", "10:00 am", "11:00 am", "12:00 pm", "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm", "5:00 pm", "6:00 pm",
                    "7:00 pm", "8:00 pm", "9:00 pm", "10:00 pm", "11:00 pm", "12:00 am"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, items);

            dynamicSpinner.setAdapter(adapter);

            dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            //---------------------------------------------------------------------------------------


            //Ties the completeForm button to the variable submitButton
            Button submitButton = (Button) findViewById(R.id.completeForm);

            //Create an onClickListener to save user input to variables and open MapActivity
            //Once the "Subtmit" button has been pressed save the event name "eName"
            //Save the event type "eType", and save the event time "eTime"
            submitButton.setOnClickListener(new View.OnClickListener() {

                //This is for for the submit button on the form page
                public void onClick(View v) {


                    //On button press, store text from the 3 fields
                    chosenEventName = (EditText) findViewById(R.id.eventName);
                    String eName = chosenEventName.getText().toString();
                    String eType = staticSpinner.getSelectedItem().toString();
                    String eTime = dynamicSpinner.getSelectedItem().toString();


                    //Built Arraylist to store variables from Form
                    ArrayList<String> formVariables = new ArrayList<>();
                    formVariables.add(eName);
                    formVariables.add(eType);
                    formVariables.add(eTime);

                /* unblock this when ready for parse server
                //Add it to parse test
                ParseObject inputForm = new ParseObject("InputForm");
                inputForm.put("name", eName);
                inputForm.put("type", eType);
                inputForm.put("time", eTime);
                inputForm.saveInBackground();
                */
                    //Toast is a pop up message on screen could be useful later...right now not important.
                    //Toast toast = new Toast(getApplicationContext());
                    //toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                    //toast.makeText(CreateActivity.this, eTime, toast.LENGTH_SHORT).show();
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

//------------------------------------
    //This section is the new code added for location services on the createActivity class
    //--------------------------------
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
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Create An Event!", Toast.LENGTH_SHORT).show();

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
                gotoLocation(location.getLatitude(), location.getLongitude(), 15);
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

    @Override
    protected void onPause(){
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, mListener);
    }
}

