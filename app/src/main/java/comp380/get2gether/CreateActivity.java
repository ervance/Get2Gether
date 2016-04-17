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


public class CreateActivity extends FragmentActivity {

    private EditText chosenEventName; //holds the event name from the eventName text box
    private EditText chosenEventType; //Holds eventLocation ""
    private EditText chosenEventTime;  //holds the eventTime ""
    private static final int DIALOG_REQUEST = 9001;

    /******Map fields******/
    private GoogleMap mMap;
    private ArrayList<LatLng> coordinateList = new ArrayList<>();  //collects coordinates
    private int listSize;  //size of coordinateList
    private LocationManager locManager;
    private LatLng currentLocale;
    private OnMapReadyCallback callback;
    /****End Map fields****/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //---------------------------------------------------------------------------------------

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**********Change UI settings****************/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true); //permisions need to be set
        mMap.getUiSettings().setMyLocationButtonEnabled(true); //false to disable
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        // Add a marker in Northridge and move the camera
        /*Consider putting the following code into a method to streamline the marker placing
        and make it more reuseable
        */


        //Get an updtated location
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //If a location is found move the marker and camera to the current location
        if ( location != null )
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //create the location
            currentLocale = new LatLng(latitude,longitude);
        }//end if
//        else
//        {
//            //try to get a location because none was found previously
//            updateWithNewLocation(location);
//        }

        //wrapper around a view of a map to automatically handle the necessary life cycle needs
        //Sets a callback object which will be triggered when the GoogleMap instance is ready to be used.
        //May or may not need to use the OnMapReadyCallback here
        //Turns out it was causing a problem so I took it out

        /****This Arraylist Holds the FormActivity Variables****/
        final ArrayList<String> forms = (ArrayList<String>) getIntent().getSerializableExtra("formVar");
        //currently forms.get(0) is Event Name
        //currently forms.get(1) is Event Type
        //currently forms.get(2) is Event Time

        //This section of code works on adding custom info window.--------------------------------
        if(mMap != null){
            //setinfoWindowAdapter is what we use to override Androids default popup window
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                //This section of code locates the contents of our custom_info_window.xml
                //It is going to use that layout to structure our window
                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    //This pulls the info from our form and populates the info window
                    TextView tvEname = (TextView) v.findViewById(R.id.eName);
                    TextView tvEType = (TextView) v.findViewById(R.id.eType);
                    TextView tvETime = (TextView) v.findViewById(R.id.eTime);
                    TextView tvOther = (TextView) v.findViewById(R.id.otherStuff);

                    //this locates the position of the marker in order to put the bubble in the
                    //correct location
                    LatLng ll = marker.getPosition();

                    //Make a parse query to get the data
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("InputForm");

                    query.whereEqualTo("name", forms.get(0));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> formData, ParseException e) {
                            if (e==null)
                                for(int i = 0; i< formData.size(); i++)
                                    Log.e("name", "Retrieved" + formData.get(i) + " formData");
                            else
                                Log.e("name", "Error: " + e.getMessage());
                        }
                    });

                    //If form is not null then populate the info window
                    if(forms != null) {
                        tvEname.setText(forms.get(0));
                        tvEType.setText(forms.get(1));
                        tvETime.setText(forms.get(2));
                    }
                    return v;
                }
            });
        }//end if (and end custom info window section----------------------------------------------

            //This is the marker that is being used to store the data from the form
            MarkerOptions marker = new MarkerOptions()
                    .draggable(true)
                    .position(currentLocale);

            if(currentLocale!=null) {
                //add marker and move camera to current location
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocale));

        }//end if

    }//end onMapReady

    //Location listener
    //collect lat lngs and put into list
    private final LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
            LatLng temp = new LatLng(location.getLatitude(), location.getLongitude());
            coordinateList.add(temp);
            listSize++;
            Log.e("LocationListener", "Coordinates Added to list");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

    };

    //New Location Updater

    private void updateWithNewLocation(Location location) {
        String latLongString = "";

        if (location != null){
            //create a string with current lat long
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            //latLongString = "Lat:" + lat + "\nLong:" + lng;
        }

        //THIS MAY NOT NEED TO BE HERE
        //ORIGNIAL CODE HAD REDUNCANCY
        //CHECK TO MAKE SURE

    }


}

