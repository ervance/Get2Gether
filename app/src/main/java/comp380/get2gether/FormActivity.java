package comp380.get2gether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.FindCallback;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    private EditText chosenEventName; //holds the event name from the eventName text box
    private EditText chosenEventType; //Holds eventLocation ""
    private EditText chosenEventTime;  //holds the eventTime ""

    //lat lng test vars
    private EditText latitude;
    private EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ties the completeForm button to the variable submitButton
        Button submitButton =(Button) findViewById(R.id.completeForm);

        //Create an onClickListener to save user input to variables and open MapActivity
        submitButton.setOnClickListener(new View.OnClickListener() {

            //This is for for the submit button on the form page
            public void onClick(View v) {

                final ParseObject testObject = new ParseObject("TestObject");

                //On button press, store text from the 3 fields
                chosenEventName = (EditText) findViewById(R.id.eventName);
                chosenEventType = (EditText) findViewById(R.id.eventType);
                chosenEventTime = (EditText) findViewById(R.id.eventTime);

                //Get lat and long
                latitude = (EditText) findViewById(R.id.getLat);
                longitude = (EditText) findViewById(R.id.getLong);

                String eName = chosenEventName.getText().toString();
                String eType = chosenEventType.getText().toString();
                String eTime = chosenEventTime.getText().toString();

                //lat lngs to pass
                String lats = latitude.getText().toString();
                String longs = longitude.getText().toString();

                //put in database
                storeData(testObject, eName, eType, eTime);

                //Built Arraylist to store variables from Form
                final ArrayList<String> formVariables = new ArrayList<>();
                formVariables.add(eName);
                formVariables.add(eType);
                formVariables.add(eTime);
                formVariables.add(lats);
                formVariables.add(longs);

                /*ParseQuery<ParseObject> queryP = ParseQuery.getQuery("TestObject");
                queryP.whereEqualTo("Student", nameStudent);
                queryP.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> dataList, ParseException e) {
                        ArrayList<String> courses = null;
                        if (e == null) {
                            courses = new ArrayList<String>();
                            for (ParseObject course : dataList) {
                                String eName = course.getString("eName");
                                String eType = course.getString("eType");
                                String eTime = course.getString("eTime");
                                formVariables.add(eName);
                                formVariables.add(eType);
                                formVariables.add(eTime);
                            }
                        } else {
                            Log.d("Post retrieval", "Error: " + e.getMessage());
                        }
                    }
                });*/

                //Toast is a pop up message on screen could be useful later...right now not important.
                 //Toast toast = new Toast(getApplicationContext());
                 //toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
                 //toast.makeText(FormActivity.this, chosenEventName.getText(), toast.LENGTH_SHORT).show();
                //----------------------------------------------------------------------------------------

                //Create an Intent in order to pass info to "MapsAcitivity"
                Intent intent = new Intent(FormActivity.this, MapsActivity.class);


                //Create Identifier for variable types in this .java file
                //Make an arraylist instead of putExtra
                intent.putExtra("formVar", formVariables);

                //Start other Activity (MapsActivity) with pin
                startActivity(intent);
            }
        });

    }
    private void storeData(ParseObject testObject, String eName, String eType, String eTime){
        testObject.put("eName", "testName");
        testObject.put("eType", "testType");
        testObject.put("eTime", "testTime");
        //testObject.put("lats", lats);
        //testObject.put("longs", longs);
        testObject.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("parse", "success!");
                } else {
                    Log.d("parse", "someone goofed"+e);
                }
            }
        });
    }
}
