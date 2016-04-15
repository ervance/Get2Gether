package comp380.get2gether;

/**
 * Created by Dino on 3/15/2016.
 * this class is made to hold the option list for the form activity page
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class ChoiceTime extends DialogFragment
{
    //Here is the list of events, if you need to add an event type or remove one make sure
    // to adjust the switch statement below accordingly.
    final CharSequence[] eventTimes = {"1:00 am","2:00 am","3:00 am","4:00 am","5:00 am","6:00 am",
            "7:00 am","8:00 am","9:00 am","10:00 am","11:00 am","12:00 pm","1:00 pm","2:00 pm","3:00 pm","4:00 pm","5:00 pm","6:00 pm",
            "7:00 pm","8:00 pm","9:00 pm","10:00 pm","11:00 pm","12:00 am"};
    String selection;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select a Time").setSingleChoiceItems(eventTimes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                //This is the switch statement which controls the choice list
                //if you need to add or remove choices do it from here.
                switch (choice){
                    case 0:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 1:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 2:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 3:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 4:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 5:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 6:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 7:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 8:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 9:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 10:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 11:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 12:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 13:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 14:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 15:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 16:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 17:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 18:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 19:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 20:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 21:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 22:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 23:
                        selection =  (String)eventTimes[choice];
                        break;

                    case 24:
                        selection =  (String)eventTimes[choice];
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            //Inside this method is where you will pass the selection to the variable in the other
            //form
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("timeChoice", selection);
            }
        });
        return builder.create();
    }
}
