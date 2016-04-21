package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private EditText editText;
    private Switch mySwitch;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private  Boolean privateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //populate button fields
        initSettings();
    }

    //call to database to initialize button fields
    public  void initSettings(){
        //TODO: call db and store info for variables
        //TODO: using placeholder values for now
        firstName = "Maroof";
        lastName = "Haque";
        email = "mh267314@email.edu";
        phone = "8181234567";
        privateMode = false;

        editText = (EditText) findViewById(R.id.firstName);
        editText.setText(firstName);
        editText = (EditText) findViewById(R.id.lastName);
        editText.setText(lastName);
        editText = (EditText) findViewById(R.id.emailField);
        editText.setText(email);
        editText = (EditText) findViewById(R.id.phoneField);
        editText.setText(phone);
        mySwitch = (Switch) findViewById(R.id.privateSwitch);
        mySwitch.setChecked(privateMode);

    }

    //save to database the changes
    public void saveSettings(View view){
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

        privateMode = false;

        //go back to maps
        Intent intent = new Intent(SettingsActivity.this, MapsActivity.class);
        startActivity(intent);
    }
}
