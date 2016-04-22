package comp380.get2gether;

import bolts.Bolts;

public class Event {
    //needs attributes
    public String name;
    public String time;
    public String type;
    public Boolean isAttending;
    public Boolean isInvited;
    public int photo;

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
