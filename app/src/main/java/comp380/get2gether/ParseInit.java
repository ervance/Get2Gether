package comp380.get2gether;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by mhaque1 on 3/15/16.
 */
public class ParseInit extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("get2gether")
                .server("159.203.253.5:6380/parse/")

                .build()
        );
    }
}
