package comp380.get2gether;

import 	android.app.Notification.Builder;
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Maroof on 4/14/16.
 */

public class Notification {
    Notification noti = new Notification.Builder(mContext)
            .setContentTitle("New mail from " + sender.toString())
            .setContentText(subject)
            .setSmallIcon(R.drawable.new_mail)
            .setLargeIcon(aBitmap)
            .build();
}
