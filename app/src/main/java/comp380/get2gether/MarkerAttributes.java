package comp380.get2gether;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

public class MarkerAttributes {
    //may want to make these private but leaving them for testing purposes as is
    String eventName;   //holds event name
    String eventType;   //holds event type
    String startTime;   //holds start time for event
    String endTime;     //holds end time for event
    double markerLat;   // holds the latitude of the event
    double markerLong;  // holds the longitude of the event
    ParseGeoPoint markerLL = new ParseGeoPoint(); // holds the LatLng for the class.
}
