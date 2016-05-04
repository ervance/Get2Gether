package comp380.get2gether;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {


    private RelativeLayout mEvents;
    private ListView listView;
    private EventAdapter eAdapter;
    private List<Event> eventList;

    /*TEST DATA REMOVE ONCE YOU GET DATABASE HOOKED TO IT*/
    final private String[] EventNames = {"Party", "Restaurant", "Football", "Cooking", "Test1", "Test2", "Test3", "Test4","Test5"};
    final private int[] IMG = {R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic,
            R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(CURRENTUSER == null){
            Intent intent = new Intent(MyEventsActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.my_events_activity);

        mEvents = (RelativeLayout) findViewById(R.id.myevents_relative_layout);
        List<Event> eventList;
        Log.d("queryEvents", "error querying my events");
        parseList = queryEvents(CURRENTUSER.getUsername().toString());
        eventList = createEventList(CURRENTUSER, parseList);
        for(int i = 0; i < eventList.size(); i++){
            Log.d("eventList", "name :" + eventList.get(i).geteName());
        }

        listView = (ListView) mEvents.findViewById(R.id.myevents_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("EventClick", "EventClickWorked Correctly on " + EventNames[position]);
            }
        });
        
        if(eventList.size() > 0) {
            eAdapter = new EventAdapter(MyEventsActivity.this, eventList);
            listView.setAdapter(eAdapter);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(this, "Click Successful", Toast.LENGTH_SHORT).show();
    }

    private List<ParseObject> queryEvents(String userName){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        List<ParseObject> queryEvents = null;
        query.whereEqualTo("userName", CURRENTUSER.getUsername().toString());
        try{
            queryEvents = query.find();
        }
        catch (ParseException e){
            e.printStackTrace();
            Log.d("queryEvents", "error querying my events");
        }

        return queryEvents;

    }


}
