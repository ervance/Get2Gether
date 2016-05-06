package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class ViewEvent extends AppCompatActivity {
    private float ratingBarRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);

        //get intent from map activity

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
        final Button submitButton = (Button) findViewById(R.id.completeForm);

        //Create an onClickListener to save user input to variables and open MapActivity
        //Once the "Subtmit" button has been pressed save the event name "eName"
        //Save the event type "eType", and save the event time "eTime"
        submitButton.setOnClickListener(new View.OnClickListener() {

            //This is for for the submit button on the form page
            public void onClick(View v) {
                //2 things need to passed

                //Create an Intent in order to pass info to "MapsAcitivity"
                Intent intent = new Intent(ViewEvent.this, MapsActivity.class);

                startActivity(intent);
            }
        });

    }//end onCreate Method

    private Event queryEvent()
}
