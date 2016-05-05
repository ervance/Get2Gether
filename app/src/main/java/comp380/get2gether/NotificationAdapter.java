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

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notif>{
    private final Context context;
    private final List<Notif> notifList;

    public NotificationAdapter(Context context, List<Notif> notifList) {
        super(context, 0, notifList);
        this.context = context;
        this.notifList = notifList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.friend_list, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.friend_list_text_view);
            holder.accept = (Button) view.findViewById(R.id.add_btn);
            holder.delete = (Button) view.findViewById(R.id.delete_btn);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();

        holder.name.setText(notifList.get(position).data);

        return view;
    }

    private static class ViewHolder {
        private TextView name;
        private Button accept;
        private Button delete;
    }
}
