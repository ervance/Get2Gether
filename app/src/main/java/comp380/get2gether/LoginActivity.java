package comp380.get2gether;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.nio.channels.AsynchronousCloseException;

public class LoginActivity extends AppCompatActivity {

    //UI references
    private EditText mUserView; //email login
    private EditText mPasswordView; //password login
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

        mUserView = (EditText) findViewById(R.id.userLogin);
        mPasswordView = (EditText) findViewById(R.id.passwordLogin);
        mTextView = (TextView) findViewById(R.id.userNotFound);
        mTextView.setVisibility(View.INVISIBLE);
        //deal with logo spinning
        logo = (ImageView) findViewById(R.id.loginLogo);
        rotation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        fade = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        //user to login/create
        user = new ParseUser();

        //Submit credentials
        Button submitButton =(Button) findViewById(R.id.logInButton);
        Button newUserButton = (Button) findViewById(R.id.newUser);


        //attempt to login
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(rotation);
                attemptLogin();//right now it will always return true
            }
        });

        //create user
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(rotation);
                attemptCreateUser();
            }
        });

    }

    private void attemptLogin(){
        boolean cancelLogin = false;
        View focusView = null;
        String password = mPasswordView.getText().toString();
        String userLogin = mUserView.getText().toString();

        //validate the password entered is ok ***NOTE DOES NOT VALIDATE AGAINST DB
        if(!TextUtils.isEmpty(password) && !isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancelLogin = true;
        }

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

        //try to log user in
        if(cancelLogin) {
                focusView.requestFocus();
        }
        else {
            ParseUser.logInInBackground(userLogin, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        //user is logged in
                        Log.d("Login", "success loggin in");
                        sucessLoggingIn();
                    } else {
                        Log.d("Login", "error loggin in");
                        errorLoggingIn();
                    }
                }
            });
        }
    }

    private void attemptCreateUser() {
        boolean cancelLogin = false;
        View focusView = null;

        String password = mPasswordView.getText().toString();
        String userLogin = mUserView.getText().toString();

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
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        sucessLoggingIn();
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

    private void sucessLoggingIn(){
        logo.startAnimation(fade);
        finish();
        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(intent);
    }

}//end LoginAcvitity
