package comp380.get2gether;

import bolts.Bolts;

public class Event {
    //needs attributes
    public String name;
    public String time;
    public String type;
    public boolean isAttending;
    public boolean isInvited;
    public int photo;
    public String host;

    //default
    final private String DEFAULTSTRING = "None";
    final private boolean DEFAULTBOOLEAN = false;
    final private int DEFAULTPIC = R.drawable.holderpic;

    Event(){
        name = DEFAULTSTRING;
        time = DEFAULTSTRING;
        type = DEFAULTSTRING;
        host = DEFAULTSTRING;
        isAttending = DEFAULTBOOLEAN;
        isInvited = DEFAULTBOOLEAN;
        photo = DEFAULTPIC;
    }

    Event(String name, String time, String host){
        this.name = name;
        this.time = time;
        this.host = host;
    }

    public Boolean getIsAttending(String user){
       //returns if user is attending
        isAttending = true;

        return isAttending;
    }

    public Boolean getIsInvited(String user){
        //returns if user is invited
        isInvited = false;

        return isInvited;
    }

    public int getPhoto(){
        return photo;
    }
}
