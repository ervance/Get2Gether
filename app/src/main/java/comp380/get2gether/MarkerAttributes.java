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

//    public void dropMarker(MarkerAttributes m){
//
//
//        final LatLng target = new LatLng(m.markerLat, m.markerLong);
//
//        final long duration = 400;
//        final Handler handler = new Handler();
//        final long start = SystemClock.uptimeMillis();
//        Projection proj = map.getProjection();
//
//        Point startPoint = proj.toScreenLocation(target);
//        startPoint.y = 0;
//        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//
//        final Interpolator interpolator = new LinearInterpolator();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                long elapsed = SystemClock.uptimeMillis() - start;
//                float t = interpolator.getInterpolation((float) elapsed / duration);
//                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
//                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
//                if (t < 1.0) {
//                    // Post again 10ms later.
//                    handler.postDelayed(this, 10);
//                } else {
//                    // animation ended
//                }
//            }
//        });
//
//    }


}
