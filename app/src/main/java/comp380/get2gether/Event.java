package comp380.get2gether;

import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import bolts.Bolts;

public class Event {

    private ParseUser parseHost; //current user
    private String eHost;
    private String uniqueEventID;
    private String eName;
    private String eStartTime;
    private String eEndTime;
    private String eType;
    private String eDescription;
    private ParseGeoPoint eLocation;
    private ArrayList<ParseUser> attendingList = new ArrayList<ParseUser>();
    private ArrayList<ParseUser> invitedList = new ArrayList<ParseUser>();
    private boolean privateEvent;
    private int photo;

    //default
    final private String DEFAULTSTRING = "None";
    final private boolean DEFAULTBOOLEAN = false;
    final private int DEFAULTPIC = R.drawable.holderpic;

    Event(){
        eName = DEFAULTSTRING;
        uniqueEventID = DEFAULTSTRING;
        eStartTime = DEFAULTSTRING;
        eEndTime = DEFAULTSTRING;
        eType = DEFAULTSTRING;
        eHost = DEFAULTSTRING;
        photo = DEFAULTPIC;
        eDescription = DEFAULTSTRING;
        eLocation = null;

    }

    Event(ParseUser parseHost, String host, String uniqueEventID, String name, String startTime,
          String endTime, String
            type, String description, ParseGeoPoint eventLocation, boolean isPrivate){
        this.parseHost = parseHost;
        eHost = host;
        this.uniqueEventID = uniqueEventID;
        eName = name;
        eStartTime = startTime;
        eEndTime = endTime;
        eType = type;
        eDescription = description;
        eLocation = eventLocation;
        privateEvent = isPrivate;
        photo = DEFAULTPIC;
        attendingList.add(parseHost);

    }



    public int getPhoto(){
        return photo;
    }

    public void saveEvent(){
        ParseObject event = new ParseObject("Event");
        ParseACL publicACL = new ParseACL(parseHost);
        publicACL.setPublicReadAccess(true);
        event.setACL(publicACL);
        event.put("uniqueID", uniqueEventID);
        event.put("eName", eName);
        event.put("eType", eType);
        event.put("eStartTime", eStartTime);
        event.put("eEndTime", eEndTime);
        event.put("eLocation", eLocation);
        event.put("eDescription", eDescription);
        event.put("private", privateEvent);

        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.d("createActivity", "error saving public event");
                    e.printStackTrace();
                }
            }
        });
    }

    public String geteHost() {
        return eHost;
    }

    public void seteHost(String eHost) {
        this.eHost = eHost;
    }

    public ParseUser getParseHost() {
        return parseHost;
    }

    public void setParseHost(ParseUser parseHost) {
        this.parseHost = parseHost;
    }

    public String getUniqueEventID() {
        return uniqueEventID;
    }

    public void setUniqueEventID(String uniqueEventID) {
        this.uniqueEventID = uniqueEventID;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteStartTime() {
        return eStartTime;
    }

    public void seteStartTime(String eStartTime) {
        this.eStartTime = eStartTime;
    }

    public String geteEndTime() {
        return eEndTime;
    }

    public void seteEndTime(String eEndTime) {
        this.eEndTime = eEndTime;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String geteDescription() {
        return eDescription;
    }

    public void seteDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    public ParseGeoPoint geteLocation() {
        return eLocation;
    }

    public void seteLocation(ParseGeoPoint eLocation) {
        this.eLocation = eLocation;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
