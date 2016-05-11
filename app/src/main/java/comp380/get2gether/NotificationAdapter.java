package comp380.get2gether;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notif>{
    private final Context context;
    private final List<Notif> notifList;
    private ParseUser CURRENTUSER = ParseUser.getCurrentUser();

    public NotificationAdapter(Context context, List<Notif> notifList) {
        super(context, 0, notifList);
        this.context = context;
        this.notifList = notifList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.notification_list, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.friend_list_text_view);
            holder.accept = (Button) view.findViewById(R.id.add_btn);
            holder.accept.setTag(position);
            holder.delete = (Button) view.findViewById(R.id.delete_btn);
            holder.delete.setTag(position);
            Log.d("acceptOnClick", "view was null ");
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
            Log.d("acceptOnClick", "view was NOT null ");
        }

        holder.name.setText(notifList.get(position).data);

        Log.d("acceptOnClick", " " + holder.toString());
        holder.accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Test functionality after saving/querrying works
                CURRENTUSER.put("friends", holder.name);//add user to current user's friends string
                //delete view from list view
                notifList.remove(position);
                notifyDataSetChanged();
                //delete from data base
                ParseQuery query = new ParseQuery("FriendRequest");
                query.whereEqualTo("recipient", CURRENTUSER.getUsername().toString());
                query.whereEqualTo("sender", holder.name.getText().toString());
                List<ParseObject> queryList = null;
                try{
                    queryList = query.find();
                }
                catch (com.parse.ParseException e){
                    Log.d("queryFriendRequests", "problem with FriendRequest Query");
                    e.printStackTrace();
                }
                for (int i = 0; i < queryList.size(); i++) {
                    ParseObject tempTest = queryList.get(i);
                    tempTest.deleteInBackground();
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Test functionality after saving/querrying works
                //delete view from list view
                notifList.remove(position);
                notifyDataSetChanged();
                //delete from data base
                ParseQuery query = new ParseQuery("FriendRequest");
                query.whereEqualTo("recipient", CURRENTUSER.getUsername().toString());
                query.whereEqualTo("sender", holder.name.getText().toString());
                List<ParseObject> queryList = null;
                try{
                    queryList = query.find();
                }
                catch (com.parse.ParseException e){
                    Log.d("queryFriendRequests", "problem with FriendRequest Query");
                    e.printStackTrace();
                }
                for (int i = 0; i < queryList.size(); i++) {
                    ParseObject tempTest = queryList.get(i);
                    tempTest.deleteInBackground();
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        private TextView name;
        private Button accept;
        private Button delete;
    }
}
