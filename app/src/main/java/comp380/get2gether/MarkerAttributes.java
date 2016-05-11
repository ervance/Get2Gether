package comp380.get2gether;

import android.graphics.Point;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseGeoPoint;
import android.os.Handler;

import java.util.logging.LogRecord;

public class MarkerAttributes {
    //may want to make these private but leaving them for testing purposes as is
    String eventName;   //holds event name
    String eventType;   //holds event type
    String startTime;   //holds start time for event
    String endTime;     //holds end time for event
    String eventDescription; //holds event description
    double markerLat;   // holds the latitude of the event
    double markerLong;  // holds the longitude of the event
    String arrayPos;
    //ParseGeoPoint markerLL; // holds the LatLng for the class.

}
