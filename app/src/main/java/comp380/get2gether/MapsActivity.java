package comp380.get2gether;

import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //coordinate list
    private ArrayList<LatLng> coordinateList = new ArrayList<>();
    private int listSize;

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

        // Add a marker in Sydney and move the camera
        LatLng pdale = new LatLng(34.594812, -118.169495);
        mMap.addMarker(new MarkerOptions().position(pdale).title("This is our test marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pdale));
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
