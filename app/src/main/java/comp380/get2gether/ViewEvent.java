package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class ViewEvent extends AppCompatActivity {
    private float ratingBarRating;
    private int imageResource;
    private ParseObject event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);



        //get intent from map activity
        Intent intent = getIntent();
        //get intent from map activity
        String description = intent.getStringExtra("eDescription");
        String type = intent.getStringExtra("eType");
        String eventName = intent.getStringExtra("eName");

        ArrayList<ParseObject> eventList = null;

        ParseQuery query = new ParseQuery("Event");
        query.whereEqualTo("eName", eventName);
        try {
            eventList = (ArrayList<ParseObject>) query.find();
        }
        catch (ParseException e){
            Log.d("viewEvent", "error queirying");
            e.printStackTrace();
        }
        if (!eventList.isEmpty())
            event = eventList.get(0);

        /******Show Image for Full Window*********/
        imageResource = getResources().getIdentifier(type, "drawable",
                getPackageName());
        ImageView iv = (ImageView) findViewById(R.id.eventPic);
        iv.setImageResource(imageResource);
        /******Show Image for Full Window*********/

        final TextView eventNameText = (TextView) findViewById(R.id.eventNameLarge);
        eventNameText.setText(eventName);

        final TextView eventTypeText = (TextView) findViewById(R.id.eventTypeLarge);
        eventTypeText.setText(type);

        final TextView eventDescriptionBox = (TextView) findViewById(R.id.eventDescriptionDisplay);
        eventDescriptionBox.setText(description);




        //Rating bar button Section ---------------------------------------------------------------------
        final RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);

        //Click listener for Rating bar
        rating.setOnClickListener(new View.OnClickListener() {
            //Once Rating is chosen. set the Rating
            public void onClick(View v) {
                //Save Rating bar rating
                ratingBarRating = rating.getRating();

            }
        });


        //------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------

        //Ties the completeForm button to the variable submitButton
        final Button saveButton = (Button) findViewById(R.id.save);

        //Create an onClickListener to save user input to variables and open MapActivity
        //Once the "Subtmit" button has been pressed save the event name "eName"
        //Save the event type "eType", and save the event time "eTime"
        saveButton.setOnClickListener(new View.OnClickListener() {

            //This is for for the submit button on the form page
            public void onClick(View v) {
                //2 things need to passed
                event.put("rating", ratingBarRating);
                event.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.d("viewEvent", "error saving rating event");
                            e.printStackTrace();
                        }
                    }
                });
                //Create an Intent in order to pass info to "MapsAcitivity"
                Intent intent = new Intent(ViewEvent.this, MapsActivity.class);

                startActivity(intent);
            }
        });

    }//end onCreate Method

   // private Event queryEvent()
}
