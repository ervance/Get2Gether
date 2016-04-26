package comp380.get2gether;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Eric on 3/6/2016.
 */
public class ParseInit extends Application{
   @Override
    public void onCreate() {
       super.onCreate();
       //connect to the database
       Parse.initialize(new Parse.Configuration.Builder(this)
                       .applicationId("get2gether")
                       .server("http://159.203.253.5:6380/parse/")
                       .build()
       );

       ParseUser.enableAutomaticUser();
       ParseACL defaultACL = new ParseACL();

       ParseACL.setDefaultACL(defaultACL, true);

   }

}
