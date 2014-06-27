package com.ManDraw.navi;

import static us.monoid.web.Resty.content;



import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import java.io.IOException;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class VitaLoginActivity extends Activity {
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */


    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    Utils loginUtils = new Utils(this);
    private static String server_ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_vita_login);
        initLinkButton();

        server_ip = loginUtils.getIP();
        // Set up the login form.
        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            attemptLogin();
                            return true;
                        }
                        return false;
                    }
                });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();
                    }
                }
        );
        //TextView registerScreen = (TextView) findViewById(R.id.Register);

        // Listening to register new account link
        //registerScreen.setOnClickListener(new View.OnClickListener() {

//			public void onClick(View v) {
//				// Switching to Register screen
//				Intent i = new Intent(getApplicationContext(), VitaRegisterActivity.class);
//				startActivity(i);
//			}
//		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.vita_login, menu);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Resty r = new Resty();
            String loginResponse=null;
            JSONObject loginJSON=null;
            try {
                loginJSON = new JSONObject().put("Pass", mPassword).put("Email", mEmail);
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                loginResponse = r.text(server_ip+ "/users/login", content(loginJSON)).toString();
                Log.d("The User Login response", loginResponse);
                String[] loginResponseParts = loginResponse.split(";");
                String loginResponseCode = loginResponseParts[0];
                if (loginResponseCode.equals("ok")) {
                    String loginResponseUid = loginResponseParts[1];
                    loginUtils.savePreferencesString("UserID",loginResponseUid);
                    return true;
                } else return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // If success, it will receive "ok", otherwise error info
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(VitaLoginActivity.this,MainActivity.class);
                startActivity(intent);
                loginUtils.savePreferencesBool("UserLogged", true);
                finish();

            } else {
                mPasswordView
                        .setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }



        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    private void initLinkButton(){
        TextView link = (TextView) findViewById(R.id.link_to_register);
        link.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(VitaLoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }

        });
    }

}
