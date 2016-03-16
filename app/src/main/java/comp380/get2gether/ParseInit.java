package comp380.get2gether;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by Eric on 3/6/2016.
 */
public class ParseInit extends Application{
   @Override
    public void onCreate() {
       super.onCreate();
       //connect to the database
      // Parse.initialize(this, "get2gether", "123456");
       Parse.initialize(new Parse.Configuration.Builder(this)
               .applicationId("get2gether")
               .server("http://159.203.253.5:6380/parse/")
               .build()
       );
   }

}
