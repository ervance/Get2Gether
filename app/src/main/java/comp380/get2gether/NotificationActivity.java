package comp380.get2gether;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
//TODO Add functionality to accept/delete button
public class NotificationActivity extends AppCompatActivity {
    private RelativeLayout mNotifs;
    private ListView listView;
    private NotificationAdapter nAdapter;
    private List<Notif> nList;
    private List<ParseObject> queryList;
    private final ParseUser CURRENTUSER = ParseUser.getCurrentUser();

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        mNotifs = (RelativeLayout) findViewById(R.id.notif_relative_layout);

        nList = new ArrayList<>();

        //run friend requests query
        queryList = getFriendRequests();

        data = new String[queryList.size()];
        //render into string array for adapter for list view
        setData(queryList);

        for(int i = 0; i < data.length; i++){
            Notif notif = new Notif();
            notif.data = data[i];
            nList.add(notif);
        }

        listView = (ListView) mNotifs.findViewById(R.id.notif_list_view);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("NotifClick", "NotifClickWorked Correctly on " + data[position]);
            }
        });
        nAdapter = new NotificationAdapter(NotificationActivity.this, nList);
        listView.setAdapter(nAdapter);
    }

    //TODO Not returning FriendRequests
    private List<ParseObject> getFriendRequests(){

        List<ParseObject> queryList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRQ");
        //queries all friend requests of current User
        Log.d("username", CURRENTUSER.getUsername().toString());//CURRENTUSER.getUsername().toString()
        String user = CURRENTUSER.getUsername().toString();
        query.whereEqualTo("recipient", "eric");

        try{
            Log.d("queryList", "searching....");
            queryList = query.find();
        }
        catch (ParseException e){
            Log.d("queryFriendRequests", "problem with FriendRequest Query");
            e.printStackTrace();
        }
        Log.d("queryList", ""+ queryList.size());
        for(int i = 0; i < queryList.size(); i++){
            Log.d("queryList", "" + (String) queryList.get(i).getString("recipient"));
        }
        return queryList;
    }

    private void setData(List<ParseObject> list){
        for(int i=0; i<list.size(); i++){
            data[i] = list.get(i).getString("sender") + " ";
        }
    }
}