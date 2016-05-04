package comp380.get2gether;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Eric on 4/29/2016.
 * This class will handle all specific database queries
 * It will be static because no objects need to be created.
 */
public class QueryDatabase {

//    private ParseQuery<ParseObject> query;
//    private List<ParseObject> queryList;
//    private final String LOGTAG = "QueryClass";
//
//
//    QueryDatabase(String objectName){
//        query = ParseQuery.getQuery(objectName);
//    }
//
//    public void newQuery(String objectName){
//        query = ParseQuery.getQuery(objectName);
//    }
//
//    public List<ParseObject> getQueryList(){//get the query
//        return queryList;
//    }
//
//    public void whereEqual(String key, String value){
//
//        query.whereEqualTo(key, value);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if (e == null)
//                    queryList = objects;
//                else
//                    Log.d(LOGTAG, "where equal fail");
//            }
//        });
//
//    }
//
//    public void orderByAscending(String key){
//        qu
//    }
//
//    public void closeToLocation(){
//
//    }
//
//
}
