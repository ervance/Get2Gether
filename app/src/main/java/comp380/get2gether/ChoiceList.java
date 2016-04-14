package comp380.get2gether;

/**
 * Created by Dino on 3/15/2016.
 * this class is made to hold the option list for the form activity page
 */
import android.content.Intent;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class ChoiceList extends DialogFragment
{
    //Here is the list of events, if you need to add an event type or remove one make sure
    // to adjust the switch statement below accordingly.
    final CharSequence[] eventTypes = { "Sports", "Food", "Social", "Party", "Dance", "Relax", "Other"};
    String selection;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose a Event Type").setSingleChoiceItems(eventTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                //This is the switch statement which controls the choice list
                //if you need to add or remove choices do it from here.
                switch (choice){
                    case 0:
                        selection =  (String)eventTypes[choice];
                        break;

                    case 1:
                        selection =  (String)eventTypes[choice];
                        break;

                    case 2:
                        selection =  (String)eventTypes[choice];
                        break;

                    case 3:
                        selection =  (String)eventTypes[choice];
                        break;

                    case 4:
                        selection =  (String)eventTypes[choice];
                        break;

                    case 5:
                        selection =  (String)eventTypes[choice];
                        break;

                    case 6:
                        selection =  (String)eventTypes[choice];
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            //Inside this method is where you will pass the selection to the variable in the other
            //form
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.putExtra("eventChoice", selection);
            }
        });
        return builder.create();
    }
}
