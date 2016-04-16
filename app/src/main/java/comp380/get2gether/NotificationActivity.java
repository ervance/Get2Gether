package comp380.get2gether;

import 	android.app.Notification.Builder;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.games.internal.constants.NotificationChannel;
import com.google.android.gms.maps.model.LatLng;
import java.lang.reflect.Array;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NotificationActivity extends AppCompatActivity {
    private ArrayList<String> notify = new ArrayList<>();
    private Context context;
    //placeholders
    //store data from database into here or make direct database calls
    private ArrayList<String> notif;
    private LatLng currentLocale;

    //initialize notifications
    protected void onCreate(Bundle savedInstanceState) {
        //standard
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //placeholder location
        LatLng northRidge = new LatLng(34.2417, -118.5283);

        //populate arraylist
        ArrayList<String> notif = new ArrayList<String>();
        notif = getNotif();

        //prepare for ListView
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notif);

        //pass ArrayAdapter into ListView
        ListView listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(itemsAdapter);

    }

    //makes call to database and finds object that has
    //timestamp/log of certain updates/pushes
    public ArrayList <String> getNotif(){
        //will pull from notification database object to here
        ArrayList<String> list = new ArrayList<String>();

        //placeholder
        //this section will populate list with database call
        list.add("X wants to be your friend");
        list.add("X accepted your friend request");
        list.add("X invited you to an event");
        list.add("X event starting soon");
        list.add("Y wants to be your friend");
        list.add("Z invited you to an event");

        return list;
    }

}