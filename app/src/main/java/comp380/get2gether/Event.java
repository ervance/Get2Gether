package comp380.get2gether;

/**
 * Created by Eric on 4/17/2016.
 */
public class Event {

    String eventName;
    int eventTime;
    int eventType;
    String host;


    Event(){
    }

    Event(String eventName, int eventTime, String host){
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.host = host;
    }

    //ToDo: create more setters getters

}
