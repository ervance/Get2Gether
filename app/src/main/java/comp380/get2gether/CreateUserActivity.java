package comp380.get2gether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateUserActivity extends AppCompatActivity {
    //UI references
    private EditText mUserView; //user login
    private EditText mPasswordView; //user password
    private EditText mBirthdayView; //user birthday
    private EditText mFirstNameView; //user first name
    private EditText mLastNameView; //user last name
    private RadioButton mGenderView; //user gender
    private RadioGroup radioSexGroup;
    private TextView mTextView;
    //rotate logo to show loggin in progress
    private ImageView logo;
    private Animation fade;
    private Animation rotation;

    //Parse User for db check
    private ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create views
        setContentView(R.layout.login_activity);

        mUserView = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.pwd);
        mBirthdayView = (EditText) findViewById(R.id.birthday);
        mFirstNameView = (EditText) findViewById(R.id.fname);
        mLastNameView = (EditText) findViewById(R.id.lname);
        radioSexGroup = (RadioGroup) findViewById(R.id.sexGroup);
        // get selected radio button from radioGroup
        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        mGenderView = (RadioButton) findViewById(selectedId);

        mTextView = (TextView) findViewById(R.id.userNotFound);
        mTextView.setVisibility(View.INVISIBLE);

        //user to login/create
        user = ParseUser.getCurrentUser();
        if (user == null) {
            Log.d("login", "no user currently logged in");
            user = new ParseUser();
        }
        else {
            Log.d("login", "user "+ user.getUsername() + " is alreayd logged in, log them out");
            user.logOut();
            user = new ParseUser();
        }

        //Submit credentials
        Button newUserButton = (Button) findViewById(R.id.createBtn);

        //create user
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(rotation);
                attemptCreateUser();
            }
        });

    }

    private void attemptCreateUser() {
        boolean cancelLogin = false;
        View focusView = null;

        final String password = mPasswordView.getText().toString();
        final String userLogin = mUserView.getText().toString();
        final String birthday = mBirthdayView.getText().toString();
        final String firstname = mFirstNameView.getText().toString();
        final String lastname = mLastNameView.getText().toString();
        final String gender = mGenderView.getText().toString();

        // Check for a valid username.
        if (TextUtils.isEmpty(userLogin)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancelLogin = true;
        } else if (!isUserNameValid(userLogin)) {
            mUserView.setError(getString(R.string.error_invalid_user));
            focusView = mUserView;
            cancelLogin = true;
        }

        if(cancelLogin){
            focusView.requestFocus();
        }
        else {
            user.setUsername(userLogin);
            user.setPassword(password);
            user.put("firstName", firstname);
            user.put("lastName", lastname);
            user.put("birthday", birthday);
            user.put("gender", gender);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        sucessLoggingIn(userLogin);
                    } else {
                        errorLoggingIn();
                    }
                }
            });
        }
    }

    private boolean isPasswordValid(String password) {
        boolean isValid;
        //ToDo: Implement better password constraint
        isValid = password.length() > 4;
        return isValid;
    }

    private boolean isUserNameValid(String email) {
        boolean isValid;
        isValid = true;//TODO: Replace this with your own logic
        return isValid;
    }

    private void errorLoggingIn(){
        //stop the spinning
        logo.clearAnimation();
        mUserView.setError(getString(R.string.error_invalid_user));
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mTextView.setVisibility(View.VISIBLE);
    }

    private void sucessLoggingIn(String userLogin){
        logo.startAnimation(fade);
        finish();
        Intent intent = new Intent(CreateUserActivity.this, MapsActivity.class);
        startActivity(intent);
    }

}//end LoginAcvitity
