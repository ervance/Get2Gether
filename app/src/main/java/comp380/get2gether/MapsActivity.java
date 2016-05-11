package comp380.get2gether;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemClickListener{


    /******Map fields******/
    private GoogleMap mMap;
    private ArrayList<LatLng> coordinateList = new ArrayList<>();  //collects coordinates
    private int listSize;  //size of coordinateList
    private LocationManager locManager;
    private LatLng currentLocale;
    private OnMapReadyCallback callback;
    private GoogleApiClient mLocationClient;
    private com.google.android.gms.location.LocationListener mListener;
    private int imageResource;
    /****End Map fields****/
    LatLng northRidge = new LatLng(34.2417, -118.5283);

    /*Parse Query Items*/
    private List<ParseObject> events;
    private ArrayList<MarkerAttributes> mapMarkers;
    private List<ParseObject> personalEvents;
    boolean filter;

    /****Drawer*****/
    private DrawerLayout drawerLayout;
    private RelativeLayout mDrawer;
    private ListView listView;
    private DrawerAdapter adapter;
    private List<NavigationIcon> drawerString;
    final private String[] NAVIGATION = {"Create Event","My Events", "Filter", "QR Code", "Settings"};
    final private int[] IMG = {R.drawable.holderpic,R.drawable.holderpic,R.drawable.holderpic,R.drawable.holderpic,R.drawable.holderpic};
    /***End Drawer***/

    //for notifications
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    //int oldSize = 5; //will use database to find old size of

    //LogID
    private final String LOGID = "mapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);//this was activity_maps

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /****Set up drawer for slider from left side***********/
        mDrawer = (RelativeLayout) findViewById(R.id.relative_drawer);

        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawer.getLayoutParams();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.5); //set width of drawer
        //create the listener for drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //what happens on slide
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //what happens when opend
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        }); //end drawer listener

        //create drawerString
        drawerString = new ArrayList<>();
        // will need to populate the list with something meaningful to what we want on our drawer
        //this creates the drawer
        for(int i = 0; i < NAVIGATION.length; i++){
            //Note: had this problem before, it was just changing the same objects
            //attributes so the whole drawer was the same info
            NavigationIcon n = new NavigationIcon();
            n.title = NAVIGATION[i];
            n.imgId = IMG[i];
            drawerString.add(n);
        }
        listView = (ListView) drawerLayout.findViewById(R.id.drawer_list); //in drawer.xml
        listView.setOnItemClickListener(this);
        adapter = new DrawerAdapter(MapsActivity.this, drawerString);
        listView.setAdapter(adapter);

        startTimer(); //notifications
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**********Change UI settings****************/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true); //permisions need to be set
        mMap.getUiSettings().setMyLocationButtonEnabled(true); //false to disable
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        // Add a marker in Northridge and move the camera
        /*Consider putting the following code into a method to streamline the marker placing
        and make it more reuseable
        */


        //Get an updtated location *--Eric if you Delete this I will kill you :)
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //If a location is found move the marker and camera to the current location
        if ( location != null )
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //create the location
            currentLocale = new LatLng(latitude,longitude);
            gotoLocation(latitude,longitude, 10); //center camera on current location
        }//end if

        /*TODO ERIC****** Here is where I left off.-----------------------------------------------------
         TODO  My goal here was to create a loop to gather
         TODO everything from the parse server, but I don't think it is right.
         */
        Intent intent = getIntent();

        Log.d("filter", "" + intent.toString() + " has filter " + intent.hasCategory("filter"));
        if (intent != null && intent.hasExtra("filter")) {
                filter = true;//only becomes true if filter is put onto the intent
                events = filterQuery(intent);
        }
        else {
            filter = false;
            queryEvents();
        }
        if (events == null)//just in case the filterQuery fails
            queryEvents();
        //array list to hold marker attributes
        mapMarkers = createMarkerAttList(events, events.size(),0);
        makeCustomInfoWindow(mapMarkers);

//TODO THIS IS THE END OF WHAT I ADDED -----------------------------------------------------------------
        mMap.setOnInfoWindowClickListener(this);

    }//end onMapReady

    //Location listener
    //collect lat lngs and put into list
    private final LocationListener locationListener = new LocationListener(){
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
            LatLng temp = new LatLng(location.getLatitude(), location.getLongitude());
            coordinateList.add(temp);
            listSize++;
            Log.e("LocationListener", "Coordinates Added to list");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

    };

    //New Location Updater

    private void updateWithNewLocation(Location location) {
        String latLongString = "";

        if (location != null){
            //create a string with current lat long
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            //latLongString = "Lat:" + lat + "\nLong:" + lng;
        }

    }


    //Listener to start the view the event
    @Override
    public void onInfoWindowClick(Marker marker) {

        //ToDo: add the identifier of the event in order to query the data in the ViewEvent
        int pos = Integer.parseInt(marker.getSnippet());
        MarkerAttributes currentMarker = mapMarkers.get(pos);
        Intent intent = createEventInfoIntent(currentMarker);

        startActivity(intent);
    }

    private Intent createEventInfoIntent(MarkerAttributes currentMarker){
        Intent intent = new Intent(MapsActivity.this, ViewEvent.class);
        intent.putExtra("eName", currentMarker.eventName);
        intent.putExtra("eDescription", currentMarker.eventDescription);
        intent.putExtra("eType", currentMarker.eventType);

        return intent;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //ToDo: this is where you handle what happens when the item in the drawer is clicked
        //Toast.makeText(this, "Click Successful", Toast.LENGTH_SHORT).show();
        //going to use the northridge location as a tester
        Intent intent = switchActivity(position);
        startActivity(intent);

    }

    private Intent switchActivity(int activity){
        //0: create 1: my event 2: filter 3: QR 4: settings
        //defualts to back to map activity for now until figure out a better
        //solution
        Intent intent;
        switch (activity){
            case 0:
                intent = new Intent(MapsActivity.this, CreateActivity.class);
                break;
            case 1:
                intent = new Intent(MapsActivity.this, MyEventsActivity.class);
                break;
            case 2:
                intent = new Intent(MapsActivity.this, Filter.class);
                break;
            case 3:
                intent = new Intent(MapsActivity.this, QRGenerator.class);
                break;
            case 4:
                intent = new Intent(MapsActivity.this, SettingsActivity.class);
                break;
            default:
                intent = new Intent(MapsActivity.this, MapsActivity.class);
                break;
        }
        return intent;
    }

    //This method updates the maps current location
    // Pass in a lat, lng, and zoom distance and it will move the camera to the desired location
    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        currentLocale = latLng;
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }
  /*  public void notif(View view) {
        Button button = (Button) findViewById(R.id.notifs);
        button.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        Intent intent = new Intent(MapsActivity.this, NotificationActivity.class);
        startActivity(intent);
        //update oldSize with new size of current notification object in database
    }*/


    //TODO: find way to save old values db
    //running notifications service in background->currently every 5 secs
    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows when new item added for user in database for notification
                handler.post(new Runnable() {
                    public void run() {
                     /*   //comparing static number with random number between 1 and 10
                        int newSize = (int )(Math.random() * 10 + 1);
                        //here we have the saved size of the old notification object of the database
                        //and we compare with the size of the new notification object that we just queried
                        //if the new size is bigger, we will display a toast and change the button color
                        //the timer feature will run in the background, so even in another android activity,
                        //users will still get a toast message and the button will be a different color when they return
                        //to maps
                        if(oldSize < newSize) {
                            final String msg = "Kickit Notification!";
                            //show the toast
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
                            toast.show();
                            //change button color
                            Button button = (Button) findViewById(R.id.notifs);
                            button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        } */
                        Log.d("filter", "" + filter);
                        if (!filter) {//if a filter is not in place
                            List<ParseObject> newQueryList = queryEventInBackground();
                            Log.d("notification", "events and newQueryList is not empty");
                            if (newQueryList != null && events != null) {
                                //updating map icons
                                Log.d("notification", "events and newQueryList is not empty newQuery: " +
                                        "" + newQueryList.size() + " events: " + events.size());
                                if (events.size() != newQueryList.size()) {
                                    int oldSize = events.size();
                                    int currSize = newQueryList.size();
                                    Log.d("notification", "updating map with new notification size of" +
                                            " lists were old: " + oldSize + " new: " + currSize);
                                    int range;
                                    if (oldSize < currSize)
                                        range = (currSize - oldSize);
                                    else
                                        range = (oldSize - currSize);
                                    events = newQueryList;
                                    mapMarkers = createMarkerAttList(events, range, oldSize - 1);
                                }

                            }
                        }
                    }
                });
            }
        };
    }

    //This is the method for placing the marker
    private void placeMarker(MarkerAttributes mAtt){
        //If forms == null that means we have not returned from FormActivity

        LatLng newLL = new LatLng(mAtt.markerLat,mAtt.markerLong);
//            currentLocale = newLL;

        //This is the marker that is being used to store the data from the form
        MarkerOptions marker = new MarkerOptions()
                .draggable(false)       //we don't want the marker to move once event is set
                .position(newLL)
                .snippet(mAtt.arrayPos);
        Marker floatMarker = mMap.addMarker(marker);
        dropPinEffect(floatMarker);

    }

    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    marker.showInfoWindow();

                }
            }
        });
    }

    //This method is for making each custom info window.
    private void makeCustomInfoWindow(final ArrayList<MarkerAttributes> markerList){
        //This section of code works on adding custom info window.--------------------------------
        if(mMap != null){
            //setinfoWindowAdapter is what we use to override Androids default popup window
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                //This section of code locates the contents of our custom_info_window.xml
                //It is going to use that layout to structure our window
                @Override
                public View getInfoContents(Marker marker) {
                    int pos = Integer.parseInt(marker.getSnippet());
                    MarkerAttributes currentMarker = markerList.get(pos);
                    View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                    /******Show Image for Info Window*********/
                    imageResource = getResources().getIdentifier(currentMarker.eventType, "drawable",
                            getPackageName());
                    ImageView iv = (ImageView) v.findViewById(R.id.markerImage);
                    iv.setImageResource(imageResource);
                    /******Show Image for Info Window*********/

                    //This pulls the info from our form and populates the info window
                    TextView tvEname = (TextView) v.findViewById(R.id.eName);
                    TextView tvEType = (TextView) v.findViewById(R.id.eType);
                    TextView tvEStartTime = (TextView) v.findViewById(R.id.eStartTime);
                    TextView tvEStopTime = (TextView) v.findViewById(R.id.eStopTime);

                    //this locates the position of the marker in order to put the bubble in the
                    //correct location
                    LatLng ll = marker.getPosition();


                    //If form is not null then populate the info window
                    tvEname.setText(currentMarker.eventName);
                    tvEType.setText(currentMarker.eventType);
                    tvEStartTime.setText(currentMarker.startTime);
                    tvEStopTime.setText(currentMarker.endTime);

                    return v;
                }
            });
        }//end if (and end custom info window section----------------------------------------------
    }

    private void queryEvents(){
        //Date date = new Date();
        //this will update the publicEvents array list with events
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.orderByAscending("createdAt");
        //ToDO: this will need to be added when the correct end time and start time are put on
        // there, we need to have it be a string in the date format
        //query.whereLessThanOrEqualTo("eEndTime", date);
        try {
            events = query.find();
        }
        catch (ParseException e){
            Log.d(LOGID, "query failed for Event");
        }

    }

    private List<ParseObject> queryEventInBackground(){
        List<ParseObject> queryList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.orderByAscending("createdAt");
        try {
            queryList = query.find();
        }
        catch (ParseException e){
            Log.d("query", "problem with query orderByascending");
            e.printStackTrace();
        }

        return queryList;
    }

    private ArrayList<MarkerAttributes> createMarkerAttList(List<ParseObject> eventList,
                                     int range, int start){
        //parse object to obtain marker info.

        ArrayList<MarkerAttributes> mapMarkers = new ArrayList<MarkerAttributes>();
        for(int i = start; i < range; i++) {
            MarkerAttributes m = new MarkerAttributes();
            m.eventName = eventList.get(i).getString("eName");
            m.eventType = eventList.get(i).getString("eType");
            m.startTime = eventList.get(i).getString("eStartTime");
            m.endTime = eventList.get(i).getString("eEndTime");
            ParseGeoPoint location = eventList.get(i).getParseGeoPoint("eLocation");
            m.eventDescription = eventList.get(i).getString("eDescription");
            m.markerLat = location.getLatitude();
            m.markerLong = location.getLongitude();
            m.arrayPos = new Integer(i).toString();
            //adds m to an array list of marker objects
            mapMarkers.add(m);
            placeMarker(m);
            gotoLocation(location.getLatitude(),location.getLongitude(), 10);
        }
        return mapMarkers;//CHANGIN THIS TO RETURN INSTEAD OF CHANGE THE GLOBAL COULD RUIN IT...
    }

    private List<ParseObject> filterQuery(Intent intent){
        List<ParseObject> queryList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        if (currentLocale != null) {
            ParseGeoPoint location = new ParseGeoPoint(currentLocale.latitude, currentLocale
                    .longitude);
            //query.whereNear("eLocation", location); //dont think i need this wherWithin MIles
            // does the same?
            int distance;
            if (intent.hasExtra("distance")) {

                distance = intent.getIntExtra("distance", 20);
                Log.d("filterQuery", "" + intent.getStringExtra("distance"));

                //distance = Integer.parseInt(dist[0]);//distance is in the first position
            } else {
                distance = 20; //default distance of 20 miles
            }
            if (intent.hasExtra("eventType")) {
                query.whereEqualTo("eType", intent.getStringExtra("eventType"));
            }


            query.whereWithinMiles("eLocation", location, distance);
            try{
                queryList = query.find();
            }
            catch (ParseException e){
                Log.d("queryFilter", "problem with query filterQuery");
                e.printStackTrace();
            }
        }
        return queryList;
    }

}//end class

