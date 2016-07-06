package com.cdac.readpanda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.InjectView;
import butterknife.ButterKnife;

public class FB_Login extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private CallbackManager callbackManager;
    //FB Login Button
    private LoginButton loginButton;
    private TextView info;
    //Google Sign in button
    private SignInButton signInButton;
    //Google sign in options
    private GoogleSignInOptions googleSignInOptions;
    //Google API Client
    private GoogleApiClient googleApiClient;
    //Sign in constant to check the activity result
    private int RC_Sign_In=100;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email)EditText _emailText;
    @InjectView(R.id.input_password)EditText _passwordText;
    @InjectView(R.id.btn_login)Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        // Initialising Google SignInOptions
        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //Initialising api
        googleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();

        setContentView(R.layout.activity_fb__login);

        ButterKnife.inject(this);
        loginButton =(LoginButton)findViewById(R.id.login_button);
        info=(TextView)findViewById(R.id.Info);
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);

        AppEventsLogger.activateApp(this);
        loginButton.setReadPermissions("email");

        signInButton.setOnClickListener((View.OnClickListener) this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                info.setText("User ID: "+ loginResult.getAccessToken().getUserId()+"\n"+"Auth Token: "+loginResult.getAccessToken().getToken());
            }
            @Override
            public void onCancel()
            {
                info.setText("Login Attempt Canceled By User");
            }
            @Override
            public void onError(FacebookException e)
            {
                info.setText("Login Attempt Failed");
            }

        });
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }
    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_Sign_In);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        signIn();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {info.setText("Connection Failed");}

    @Override
    public void onConnected(Bundle bundle)
    {info.setText("Connected");}

    @Override
    public void onConnectionSuspended(int i)
    {info.setText("Connection Suspended");    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(FB_Login.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
