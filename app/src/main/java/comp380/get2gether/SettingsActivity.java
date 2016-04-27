package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private EditText editText;
    private Button submitButton;
    private Switch mySwitch;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean privateMode;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        currentUser = ParseUser.getCurrentUser();

        Log.d("settings", "current user obid " + currentUser.getObjectId());
        if (currentUser != null)
            initSettings(currentUser);
        else{
            //not logged in send back to login
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        submitButton = (Button) findViewById(R.id.submitBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("settings", "call saveSettings()");
                saveSettings(currentUser);
            }
        });

    }

    //call to database to initialize button fields
    public void initSettings(ParseUser user){
        //TODO: call db and store info for variables
        //TODO: using placeholder values for now
        Log.d("settings", "init" + user.getObjectId());
        if(user != null) {
            Log.d("settings", "initSettings() key firstName " + user.has("firstName"));
            Log.d("settings", "initSettings() key lastName " + user.has("lastName"));
            Log.d("settings", "initSettings() key email " + user.has("email"));
            Log.d("settings", "initSettings() key phone " + user.has("phone"));
            Log.d("settings", "initSettings() key privateMode " + user.has("privateMode"));

            //get all db info if its there
            if (user.has("firstName"))
                firstName = user.getString("firstName");
            if (user.has("lastName"))
                lastName = user.getString("lastName");
            if (user.has("email"))
                email = user.getString("email");//email
            if (user.has("phone"))
                phone = user.getString("phone");//"8181234567"
            if (user.has("privateMode"))
                privateMode = user.getBoolean("privateMode"); //true or false
        }

        editText = (EditText) findViewById(R.id.firstName);
        if (firstName != null)
            editText.setText(firstName);
        editText = (EditText) findViewById(R.id.lastName);
        if (lastName != null)
            editText.setText(lastName);
        editText = (EditText) findViewById(R.id.emailField);
        if(email != null)
            editText.setText(email);
        editText = (EditText) findViewById(R.id.phoneField);
        if(phone != null)
            editText.setText(phone);
        mySwitch = (Switch) findViewById(R.id.privateSwitch);
        if(privateMode != null)
            mySwitch.setChecked(privateMode);
    }

    //save to database the changes
    public void saveSettings(ParseUser user){
        //TODO: store variables into db associated with user.
        editText = (EditText) findViewById(R.id.firstName);
        firstName = editText.getText().toString();

        editText = (EditText) findViewById(R.id.lastName);
        lastName = editText.getText().toString();

        editText = (EditText) findViewById(R.id.emailField);
        email = editText.getText().toString();

        editText = (EditText) findViewById(R.id.phoneField);
        phone = editText.getText().toString();

        mySwitch = (Switch) findViewById(R.id.privateSwitch);
        privateMode = mySwitch.isChecked();


        if (user != null) {
            Log.d("settings", "save" + user.getObjectId());
            Log.d("settings", "call saveSettings() put " + firstName);
            Log.d("settings", "call saveSettings() put " + lastName);
            Log.d("settings", "call saveSettings() put " + email);
            Log.d("settings", "call saveSettings() put " + phone);
            Log.d("settings", "call saveSettings() put " + privateMode);
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            user.put("email", email);
            user.put("phone", phone);
            user.put("privateMode", privateMode);
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Log.d("settings", "save worked");
                    }
                    else {
                        Log.d("settings", "save did not work");
                        e.printStackTrace();

                    }
                }
            });
        }

        //go back to maps
        Intent intent = new Intent(SettingsActivity.this, MapsActivity.class);
        startActivity(intent);
    }

}
