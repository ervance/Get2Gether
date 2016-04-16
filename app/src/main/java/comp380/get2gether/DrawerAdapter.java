package comp380.get2gether;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eric on 4/9/2016.
 */
public class DrawerAdapter extends ArrayAdapter<NavigationIcon> {
    //class to populate and create drawer

    private final Context context;
    private final List<NavigationIcon> drawerString;

    public DrawerAdapter(Context context, List<NavigationIcon> drawerString) {
        super(context, 0, drawerString);
        this.context = context;
        this.drawerString = drawerString;
        //this.alMarkers = alMarkers;
        //debugging log data
//        for (int i = 0; i < alMarkers.size(); i++) {
//            Log.i("MENU", "" + alMarkers.get(i).title + " " + alMarkers.get(i).imgId);
//        }
    }//end DrawerAdapter constructor

    //@Override
//    public int getCount() {
//        return alMarkers.size();
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;

        //create the drawer and the view of it
        if (view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.drawer_list, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) view.findViewById(R.id.drawer_list_img_view);
            holder.title = (TextView) view.findViewById(R.id.drawer_list_text_view);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

//        //debugging log data
//        Log.e("MENU", "" + alMarkers.get(position).title + " Position: " + position);
//        for (int i = 0; i < alMarkers.size(); i++) {
//            Log.e("MENU", "" + alMarkers.get(position).title + " " + alMarkers.get(position).imgId);
//        }
//        //end log
        holder.img.setImageResource(drawerString.get(position).imgId);
        holder.title.setText(drawerString.get(position).title);
        return view;
    }//end getView


    private static class ViewHolder{
        private TextView title;
        private ImageView img;
    }//end ViewHolder class
}//end DrawerAdapter Class