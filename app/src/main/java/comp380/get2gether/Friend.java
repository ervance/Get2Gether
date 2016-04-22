package comp380.get2gether;

import java.util.List;

/**
 * Created by Eric on 4/15/2016.
 */
public class Friend {

    //what type of attributes should it have?
    public String firstName;
    public String lastName;
    public List<String> hostingEvents;
    public List<String> attendingEvents;
    public int photo;
    public String description;

    Friend(){}

    Friend(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstLast() {
        String firstLast = firstName + " " + lastName;
        return firstLast;
    }

    public int getPhoto(){
        return photo;
    }

}
