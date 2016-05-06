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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {


    private RelativeLayout mFriends;
    private ListView listView;
    private FriendAdapter fAdapter;
    private List<Friend> friendList;
    private String friendUsernames;
    private ParseUser CURRENTUSER = ParseUser.getCurrentUser();
    private EditText friendSearch;

    /*TEST DATA FOR FRIENDS!!!!! REMOVE ONCE YOU GET DATABASE HOOKED TO IT*/
  //  final private String[] FRIENDFIRSTNAMES = {"Dino", "Keith", "Olga", "Maroof", "TestFirst1", "TestFirst2", "TestFirst3", "TestFirst4","TestFirst5"};
 //   final private String[] FRIENDLASTTNAMES = {"Biel", "Johnson", "Kup", "Haque", "TestLast1", "TestLast2", "TestLast3", "TestLast4", "TestLast5"};
   // final private int[] IMG = {R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic,
   //         R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic, R.drawable.holderpic};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);

        mFriends = (RelativeLayout) findViewById(R.id.friend_relative_layout);

        friendList = new ArrayList<>();

        friendUsernames = CURRENTUSER.getString("friends");

        if(friendUsernames!=null) {
            String[] usernames = friendUsernames.split(" ");

            for (int i = 0; i < usernames.length; i++) {
                Friend friend = new Friend();
                friend.username = usernames[i];
                //friend.firstName = FRIENDFIRSTNAMES[i];
                // friend.lastName = FRIENDLASTTNAMES[i];
                //     friend.photo = IMG[i];
                friendList.add(friend);
            }

            listView = (ListView) mFriends.findViewById(R.id.friend_list_view);

            fAdapter = new FriendAdapter(FriendsActivity.this, friendList);
            listView.setAdapter(fAdapter);
        }

        friendSearch = (EditText)findViewById(R.id.search_friends);
    }

    //handles friendrequests from search feature
    public void sendFriendRequest(View view){
        //get username from search field
        String searchedUser = friendSearch.getText().toString();

        List<ParseObject> queryList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        //checks if request already exist
        query.whereEqualTo("sender", CURRENTUSER);
        query.whereEqualTo("recipient", searchedUser);
        try{
            queryList = query.find();
        }
        catch (ParseException e){
            Log.d("queryFriendRequests", "problem with FriendRequest Query");
            e.printStackTrace();
        }
        if(queryList.size() < 1) { //run only if request object doesn't exist in db

            ParseObject friendRequest = new ParseObject("FriendRequest");
            friendRequest.put("sender", CURRENTUSER); //after search made, sender will be CURRENTUSER
            friendRequest.put("recipient", searchedUser); //after search made, recipient will be searched user

            Toast toast = new Toast(getApplicationContext());
            toast.makeText(this, "Friend Request Sent!", toast.LENGTH_LONG).show();

            friendRequest.saveInBackground();
        }

    }

}
