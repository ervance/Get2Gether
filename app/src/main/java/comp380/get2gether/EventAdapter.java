package comp380.get2gether;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eric on 4/17/2016.
 */
public class EventAdapter extends ArrayAdapter<Event>{

    private final Context context;
    private final List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        super(context, 0, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        View view = convertView;
        Event event;

        if(view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.event_list_adapter, parent, false);
            holder = new ViewHolder();

            holder.eventImg = (ImageView) view.findViewById(R.id.event_list_img_view);
            holder.eventName = (TextView) view.findViewById(R.id.event_list_text_view);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();

        event = eventList.get(position);

        holder.eventImg.setImageResource(event.eventType);
        holder.eventName.setText("" + event.eventName + "\nStarts at: " + event.eventTime +
                "\nHosted by: " + event.host);

        return view;

    }

    private static class ViewHolder {
        private TextView eventName;
        private ImageView eventImg;

    }
}
