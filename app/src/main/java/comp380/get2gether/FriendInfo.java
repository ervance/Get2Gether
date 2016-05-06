package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendInfo extends AppCompatActivity {

    //UI elements
    private RelativeLayout mRelativeFriendsInfo;
    private ListView eventListView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> eventList;
    private TextView mFriendName;
    private TextView mDescriptionText;
    private ImageView mFriendPhoto;
    private TabHost tabHost;


    //temp holder
    final private int[] IMG = {R.drawable.holderpic};
    final private String[] EVENTNAME = {"TestEvent1", "TestEvent2", "TestEvent3", "TestEvent4",
            "TestEvent5", "TestEvent6"};
    final private String[] HOST = {"TestHost1", "TestHost2", "TestHost3", "TestHost4",
            "TestHost5", "TestHost6"};
    final private String[] TIME = {"11:00","11:00","11:00","11:00","11:00","11:00"};
    private Friend testFriend = new Friend("TEST", "FRIEND");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_info_activity);

        /*NOTE!!! HIDE THE BIO AND ATTENDING EVENTS IF NOT A FRIEND AND SWITCH IT WITH A FRIEND
        REQUEST BUTTON AND MESSAGE IF WANTING*/
        mRelativeFriendsInfo = (RelativeLayout) findViewById(R.id.friend_info_relative_layout);
        mFriendName = (TextView) findViewById(R.id.friendName);
        mDescriptionText = (TextView) findViewById(R.id.descriptionText);
        mFriendPhoto = (ImageView) findViewById(R.id.friedPhoto);

        //test data retrieve from parse here
        testFriend.photo = IMG[0];
        testFriend.description = "THIS IS A TEST DESCRIPTION OF MY FRIEND. I LIKE BANANAS!";

        mFriendName.setText(testFriend.getFirstLast());
        mDescriptionText.setText(testFriend.description);
        mFriendPhoto.setImageResource(testFriend.photo);

        //create tabs
        tabHost = (TabHost)findViewById(R.id.tabHost);
        TabHost.TabSpec eventTab = tabHost.newTabSpec("Events");
        TabHost.TabSpec friendTab = tabHost.newTabSpec("Friends");

        eventTab.setIndicator("Events");
        friendTab.setIndicator("Friends");
        friendTab.setContent(new Intent(FriendInfo.this, FriendsActivity.class));


        //ToDo: THIS MAY NEED TO BE CHANGED TO FRIENDS EVENTS
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser.has("myEvents")) {
            eventList = (ArrayList<Event>) currentUser.get("myEvents");
        }

        eventListView = (ListView) mRelativeFriendsInfo.findViewById(R.id.hostingEvents);

        eventAdapter = new EventAdapter(FriendInfo.this, eventList);
        eventListView.setAdapter(eventAdapter);

        //tabHost.addTab(eventTab);
        tabHost.addTab(friendTab);


    }
}
