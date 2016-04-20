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

public class NotificationActivity extends AppCompatActivity {
    private RelativeLayout mNotifs;
    private ListView listView;
    private NotificationAdapter nAdapter;
    private List<Notif> nList;

    /*TEST DATA FOR NOTIFICATIONS!!!!! REMOVE ONCE YOU GET DATABASE HOOKED TO IT*/
    final private String[] data = {"Dino wants to be your friend", "Keith accepted your friend request", "Olga invited you to an event", "Basketball starting soon"};
    final private int [] times = {R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        mNotifs = (RelativeLayout) findViewById(R.id.notif_relative_layout);

        nList = new ArrayList<>();

        for(int i = 0; i < data.length; i++){
            Notif notif = new Notif();
            notif.data = data[i];
            notif.timeAdded = times[i];
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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Toast.makeText(this, "Click Successful", Toast.LENGTH_SHORT).show();
    }

}