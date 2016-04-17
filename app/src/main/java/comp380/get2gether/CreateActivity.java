package comp380.get2gether;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends FragmentActivity {

    private EditText chosenEventName; //holds the event name from the eventName text box
    private EditText chosenEventType; //Holds eventLocation ""
    private EditText chosenEventTime;  //holds the eventTime ""
    private GoogleMap mMap;             //Map variable for map fragment
    private static final int DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //---------------------------------------------------------------------------------------
        //This code handles the map portion of the createActivity page



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

        String[] items = new String[] {"Select a Time", "1:00 am","2:00 am","3:00 am","4:00 am","5:00 am","6:00 am",
                "7:00 am","8:00 am","9:00 am","10:00 am","11:00 am","12:00 pm","1:00 pm","2:00 pm","3:00 pm","4:00 pm","5:00 pm","6:00 pm",
                "7:00 pm","8:00 pm","9:00 pm","10:00 pm","11:00 pm","12:00 am"};

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
        Button submitButton =(Button) findViewById(R.id.completeForm);

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
                String eTime =dynamicSpinner.getSelectedItem().toString();


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
    public boolean servicesOK() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this, DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.error_connect_to_services), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }

        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    LatLng latLng = marker.getPosition();
                    ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
                    return v;
                }
            });

        }

        return (mMap != null);

    }
}
