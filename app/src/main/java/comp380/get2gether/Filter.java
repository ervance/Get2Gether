package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

public class Filter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_form);


        final Spinner eventTypeSpinner = (Spinner) findViewById(R.id.type_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        final ArrayAdapter<CharSequence> eventTypeAdapter = ArrayAdapter
                .createFromResource(this, R.array.eventArray,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        eventTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        eventTypeSpinner.setAdapter(eventTypeAdapter);

        /*Spinner for choosing the distance*/
        final Spinner distanceChoiceSpinner = (Spinner) findViewById(R.id.distance_spinner);
        final String[] distanceChoices = new String[]{"Choose a Distance", "5 miles", "10 miles",
                "20 miles", "25 miles","30 miles", "50 miles"};

        final ArrayAdapter<String> distanceChoiceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, distanceChoices);

        distanceChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceChoiceSpinner.setAdapter(distanceChoiceAdapter);
        distanceChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //final Switch mySwitch = (Switch) findViewById(R.id.show_private_only_switch);

        final Button filterbutton = (Button) findViewById(R.id.filter_button);
        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterAndStart(distanceChoiceSpinner, eventTypeSpinner);
            }
        });

        final Button removeFilterbutton = (Button) findViewById(R.id.remove_filter);
        removeFilterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filter.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void filterAndStart(Spinner distanceChoiceSpinner, Spinner
            eventTypeSpinner){
        //attach choices and start the event
        //boolean privateEventsOnly = mySwitch.isChecked();
        int distance;
        //convert choice into an int to pass back to the query
        String distanceChoiceString = distanceChoiceSpinner.getSelectedItem().toString();
        if(distanceChoiceString.equals("Choose a Distance"))
            distance = 100;
        else {
            String[] distanceChoiceArray = distanceChoiceString.split(" ");
            distance = Integer.parseInt(distanceChoiceArray[0]);
        }
        //convert even type
        String eventType = eventTypeSpinner.getSelectedItem().toString();
        Intent intent = new Intent(Filter.this, MapsActivity.class);
        //intent.putExtra("privateEventOnly", privateEventsOnly);

        intent.putExtra("distance", distance);
        intent.putExtra("eventType", eventType);
        intent.putExtra("filter", true);
        startActivity(intent);

    }
}
