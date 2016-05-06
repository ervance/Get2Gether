package comp380.get2gether;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final ArrayList<Event> eventList;

    public EventAdapter(Context context, ArrayList<Event> eventList) {
        super(context, 0, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.friend_list, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) view.findViewById(R.id.friend_list_img_view);
            holder.name = (TextView) view.findViewById(R.id.friend_list_text_view);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();

        Log.d("eventList", "name :" + eventList.get(position).geteName() + " photo: " + eventList.get(position).getPhoto());
        holder.img.setImageResource(eventList.get(position).getPhoto());
        holder.name.setText(eventList.get(position).geteName());

        return view;
    }

    private static class ViewHolder {
        private TextView name;
        private ImageView img;
    }
}
