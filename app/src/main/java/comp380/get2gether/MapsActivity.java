package comp380.get2gether;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, AdapterView.OnItemClickListener {


    /******Map fields******/
    private GoogleMap mMap;
    private ArrayList<LatLng> coordinateList = new ArrayList<>();  //collects coordinates
    private int listSize;  //size of coordinateList
    private LocationManager locManager;
    private LatLng currentLocale;
    private OnMapReadyCallback callback;
    /****End Map fields****/
    LatLng northRidge = new LatLng(34.2417, -118.5283);

    /****Drawer*****/
    private DrawerLayout drawerLayout;
    private RelativeLayout mDrawer;
    private ListView listView;
    private DrawerAdapter adapter;
    private List<NavigationIcon> drawerString;
    final private String[] NAVIGATION = {"Create Event","My Events", "Filter", "Friends", "QR Codr", "Settings"};
    /***End Drawer***/

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
        NavigationIcon n = new NavigationIcon();
        // will need to populate the list with something meaningful to what we want on our drawer
        for(int i = 0; i < NAVIGATION.length; i++){
            n.title = NAVIGATION[i];
            drawerString.add(n);
        }
        listView = (ListView) drawerLayout.findViewById(R.id.drawer_list); //in drawer.xml
        listView.setOnItemClickListener(this);
        adapter = new DrawerAdapter(MapsActivity.this, drawerString);
        listView.setAdapter(adapter);

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


        //Get an updtated location
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
        }//end if
//        else
//        {
//            //try to get a location because none was found previously
//            updateWithNewLocation(location);
//        }

        //wrapper around a view of a map to automatically handle the necessary life cycle needs
        //Sets a callback object which will be triggered when the GoogleMap instance is ready to be used.
        //May or may not need to use the OnMapReadyCallback here
        //Turns out it was causing a problem so I took it out

        /****This Arraylist Holds the FormActivity Variables****/
        final ArrayList<String> forms = (ArrayList<String>) getIntent().getSerializableExtra("formVar");
        //currently forms.get(0) is Event Name
        //currently forms.get(1) is Event Type
        //currently forms.get(2) is Event Time

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
                    View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                    //This pulls the info from our form and populates the info window
                    TextView tvEname = (TextView) v.findViewById(R.id.eName);
                    TextView tvEType = (TextView) v.findViewById(R.id.eType);
                    TextView tvETime = (TextView) v.findViewById(R.id.eTime);
                    TextView tvOther = (TextView) v.findViewById(R.id.otherStuff);

                    //this locates the position of the marker in order to put the bubble in the
                    //correct location
                    LatLng ll = marker.getPosition();

                    //Make a parse query to get the data
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("InputForm");

                    query.whereEqualTo("name", forms.get(0));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> formData, ParseException e) {
                            if (e==null)
                                for(int i = 0; i< formData.size(); i++)
                                Log.e("name", "Retrieved" + formData.get(i) + " formData");
                            else
                                Log.e("name", "Error: " + e.getMessage());
                        }
                    });

                    //If form is not null then populate the info window
                    if(forms != null) {
                        tvEname.setText(forms.get(0));
                        tvEType.setText(forms.get(1));
                        tvETime.setText(forms.get(2));
                    }
                    return v;
                }
            });
        }//end if (and end custom info window section----------------------------------------------

        //If forms == null that means we have not returned from FormActivity
        if (forms != null) {
            //Toast is a pop up message on screen could be useful later...right now not important.
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.makeText(MapsActivity.this, forms.get(0), toast.LENGTH_SHORT).show();
            //-------------------------------------------------------------------------------------

            //This is the marker that is being used to store the data from the form
            MarkerOptions marker = new MarkerOptions()
                    .draggable(true)
                    .position(currentLocale);

            if(currentLocale!=null) {
                //add marker and move camera to current location
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocale));
            }else{
                toast.makeText(MapsActivity.this, "No Current Location.", toast.LENGTH_SHORT).show();
            }
        }//end if

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

            //THIS MAY NOT NEED TO BE HERE
            //ORIGNIAL CODE HAD REDUNCANCY
            //CHECK TO MAKE SURE

    }


    //Listener to start the view the event
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, ViewEvent.class);
        //ToDo: add the identifier of the event in order to query the data in the ViewEvent
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //ToDo: this is where you handle what happens when the item in the drawer is clicked
        Toast.makeText(this, "Click Successful", Toast.LENGTH_SHORT).show();
        //going to use the northridge location as a tester
        mMap.addMarker(new MarkerOptions()
                    .position(northRidge)
        );//end adding marker

    }
}

