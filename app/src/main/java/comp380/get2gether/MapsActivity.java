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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    /******Map fields******/
    private GoogleMap mMap;
    private ArrayList<LatLng> coordinateList = new ArrayList<>();  //collects coordinates
    private int listSize;  //size of coordinateList
    private LocationManager locManager;
    private LatLng currentLocale;
    private OnMapReadyCallback callback;

    /****End Map fields****/
    LatLng northRidge = new LatLng(34.2417, -118.5283);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
            //add marker and move camera
            mMap.addMarker(new MarkerOptions().position(currentLocale).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocale));
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
        ArrayList<String> forms = (ArrayList<String>) getIntent().getSerializableExtra("formVar");
        //currently .get(0) is Event Name
        //currently .get(1) is Event Type
        //currently .get(2) is Event Time

        //If forms == null that means we have not returned from FormActivity
        if (forms != null) {
            //Toast is a pop up message on screen could be useful later...right now not important.
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.makeText(MapsActivity.this, forms.get(0), toast.LENGTH_SHORT).show();
            //-------------------------------------------------------------------------------------

            //This is the marker that is being used to store the data from the form
            MarkerOptions marker = new MarkerOptions()
                    .title(forms.get(0))
                    .draggable(true)
                    .position(currentLocale);

            if(currentLocale!=null) {
                mMap.addMarker(marker);
            }else{
                toast.makeText(MapsActivity.this, "No Current Location.", toast.LENGTH_SHORT).show();
            }
        }//end if

    }

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

}
