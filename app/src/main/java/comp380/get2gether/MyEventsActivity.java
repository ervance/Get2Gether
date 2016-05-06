package comp380.get2gether;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {

    private final String TAG = "myevents";
    private RelativeLayout mEvents;
    private ListView listView;
    private EventAdapter eAdapter;
    private ArrayList<Event> eventList;
    private List<ParseObject> parseList;
    private final ParseUser CURRENTUSER = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(CURRENTUSER == null){
            Intent intent = new Intent(MyEventsActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.my_events_activity);

        mEvents = (RelativeLayout) findViewById(R.id.myevents_relative_layout);
        Log.d("queryEvents", "entered the events activity");
        parseList = queryEvents(CURRENTUSER.getUsername().toString());
        Log.d("queryEvents", "done querying events, its size is " + parseList.size() +" " +
                "starting " +
                "create event " +
                "list");
        eventList = Event.createEventList(CURRENTUSER, parseList);
        Log.d("queryEvents", "done creating list, its size " + eventList.size());
        for(int i = 0; i < eventList.size(); i++){
            Log.d("eventList", "name :" + eventList.get(i).geteName());
        }
//        for(int i = 0; i < EventNames.length; i++){
//            Event event = new Event();
//            event.name = EventNames[i];
//            event.photo = IMG[i];
//            eventList.add(event);
//        }

        listView = (ListView) mEvents.findViewById(R.id.myevents_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = attachIntentInfo(eventList,position);
                startActivity(intent);
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

    private Intent attachIntentInfo(ArrayList<Event> eventList, int position){

        Event event = eventList.get(position);
        Intent intent = new Intent(MyEventsActivity.this, ViewEvent.class);

        intent.putExtra("eName",event.geteName());
        intent.putExtra("eType", event.geteType());
        intent.putExtra("eStartTime", event.geteStartTime());
        intent.putExtra("eEndTime", event.geteEndTime());
        intent.putExtra("eDiscription", event.geteDescription());

        return intent;

    }

}
