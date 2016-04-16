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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.nio.channels.AsynchronousCloseException;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {

    //UI references
    private EditText mEmailView; //email login
    private EditText mPasswordView; //password login
    private View mLoginFormView;
    private View mProgressView;
    //private View mEmailView;
    private boolean validLogin;
    private CredentialManager authManager = null; //object to handle auth task
    private static final String[] DUMMY_QUERY = new String[]{
            "testcred:password", "dev:password"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create views
        setContentView(R.layout.login_activity);

        mEmailView = (EditText) findViewById(R.id.emailLogin);
        mPasswordView = (EditText) findViewById(R.id.passwordLogin);

        //Submit credentials
        Button submitButton =(Button) findViewById(R.id.logInButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //password.setTransformationMethod(new AsteriskPasswordTransformationMethod()); //Dont think this works

                //ToDo: this is where you would do the check for proper credentials
                    attemptLogin();//right now it will always return true
                    //get id of user to use to populate all of the specifics of his/her account
                    //Maybe set a logedin flag to send the user back here when they go anywhere past here
                    //if they are not logged in
                    showProgress(true);
                    //CredentialManager.isValidUser(userEmail, password)
                if(validLogin) {
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
//                else{
//                    setContentView(R.layout.login_activity);
//                    mEmailView.setError(getString(R.string.error_invalid_email));
//                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progressBarLogin);

    }

    private boolean attemptLogin(){

        if(authManager != null){
            //do not do the login!!! the manager is not null
            return false;
        }

        boolean cancelLogin = false;
        View focusView = null;

        String password = mPasswordView.getText().toString();
        String emailLogin = mEmailView.getText().toString();

        //validate the password entered is ok ***NOTE DOES NOT VALIDATE AGAINST DB
        if(!TextUtils.isEmpty(password) && !isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancelLogin = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailLogin)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancelLogin = true;
        } else if (!isEmailValid(emailLogin)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancelLogin = true;
        }

        if(cancelLogin)
            focusView.requestFocus();
        else {
            showProgress(true);
            authManager = new CredentialManager(emailLogin, password);
            authManager.delegate = this;
            authManager.execute((Void) null);
        }

        return validLogin;
    }

    private boolean isPasswordValid(String password) {
        boolean isValid;
        //ToDo: Implement better password constraint
        isValid = password.length() > 4;
        return isValid;
    }

    private boolean isEmailValid(String email) {
        boolean isValid;
        isValid = true;//TODO: Replace this with your own logic
        return isValid;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void authFinished(boolean output) {
        validLogin = output;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    //worker to do password checking
    class CredentialManager extends AsyncTask<Void, Void, Boolean> {

        private final String mUserEmail;
        private final String mPassword;
        public AsyncResponse delegate = null;

        CredentialManager (String userEmail, String password){
            mUserEmail = userEmail;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean isValid = false;
            //Database check if user is valid
            try {
                //check for the password email combo hash
                //simulating getting trying to get the user
                Thread.sleep(2000);
            } catch (InterruptedException e){
                isValid = false;
                return isValid;
            }
            //check all the credendials, change it to the query from the dbase
            for(String credentials : DUMMY_QUERY ){
                String [] pieces = credentials.split(":");
                //check if a user name matches
                if(pieces[0].equals(mUserEmail))
                    isValid = pieces[1].equals(mPassword);
            }//end check

            //this return is going to onPostExecute and is represented as success
            return isValid;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //this is what is returned by the thread
            authManager = null;
            showProgress(false);

            if (success) {
                //supposed to return the value of success
                delegate.authFinished(success);
            } else {
                //success comes back false
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //what happens if thread is cancled
            authManager = null;
            showProgress(false);
        }

    }
}



interface AsyncResponse {
    void authFinished(boolean output);
}