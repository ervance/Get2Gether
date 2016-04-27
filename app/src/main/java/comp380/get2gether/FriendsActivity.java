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

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {


    private RelativeLayout mFriends;
    private ListView listView;
    private FriendAdapter fAdapter;
    private List<Friend> friendList;

    /*TEST DATA FOR FRIENDS!!!!! REMOVE ONCE YOU GET DATABASE HOOKED TO IT*/
    final private String[] FRIENDFIRSTNAMES = {"Dino", "Keith", "Olga", "Maroof", "TestFirst1", "TestFirst2", "TestFirst3", "TestFirst4","TestFirst5"};
    final private String[] FRIENDLASTTNAMES = {"Biel", "Johnson", "Kup", "....", "TestLast1", "TestLast2", "TestLast3", "TestLast4", "TestLast5"};
    final private int[] IMG = {R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic,
            R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);

        mFriends = (RelativeLayout) findViewById(R.id.friend_relative_layout);

        friendList = new ArrayList<>();

        for(int i = 0; i < FRIENDFIRSTNAMES.length; i++){
            Friend friend = new Friend();
            friend.firstName = FRIENDFIRSTNAMES[i];
            friend.lastName = FRIENDLASTTNAMES[i];
            friend.photo = IMG[i];
            friendList.add(friend);
        }

        listView = (ListView) mFriends.findViewById(R.id.friend_list_view);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("FriendClick", "FriendClickWorked Correctly on " +FRIENDFIRSTNAMES[position]);
                Intent intent = new Intent(FriendsActivity.this, FriendInfo.class);
                startActivity(intent);
            }
        });
        fAdapter = new FriendAdapter(FriendsActivity.this, friendList);
        listView.setAdapter(fAdapter);
    }

}
