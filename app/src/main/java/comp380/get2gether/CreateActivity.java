package comp380.get2gether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateActivity extends FragmentActivity {

    private EditText chosenEventName; //holds the event name from the eventName text box
    private EditText chosenEventType; //Holds eventLocation ""
    private EditText chosenEventTime;  //holds the eventTime ""

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);



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
                //chosenEventType = (EditText) findViewById(R.id.eventType);
                //chosenEventTime = (EditText) findViewById(R.id.eventTime);


                String eName = chosenEventName.getText().toString();
                //changed to choice box
                //String eType = chosenEventType.getText().toString();
                String eType =getIntent().getStringExtra("eventChoice");
                //Changed to choice box
                String eTime =getIntent().getStringExtra("timeChoice");


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



    //These  methods are the buttons for event type and time
    public void selectEventType(View v){
        CreateActivityEventTypeScrollList choiceBox = new CreateActivityEventTypeScrollList();
        choiceBox.show(getSupportFragmentManager(), "choiceBox");
    }

    public void selectEventTime(View v){
        CreateActivityEventTimeScrollList timeBox = new CreateActivityEventTimeScrollList();
        timeBox.show(getSupportFragmentManager(),"timeBox");
    }
}
