package com.cdac.readpanda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    TextView txtTnc,tv_login;
    EditText edtName,edtEmail,edtPass;
    Session session;
    ProgressDialog pDialog;
    FloatingActionButton btnSup;
    String name,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        session = new Session(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getSupportActionBar().hide();
        txtTnc = (TextView)findViewById(R.id.txtTnc);
        txtTnc.setText(Html.fromHtml("<html><center><font align=\"center\">By Signing in you confirm that you have read &amp; agree to our <br><u><b>Terms &amp; Conditions</b></u> and <u><b>Privacy Policy</b></u></font></center></html>"));
        tv_login = (TextView)findViewById(R.id.tv_signin);

        edtName = (EditText)findViewById(R.id.sup_name);
        edtEmail = (EditText)findViewById(R.id.sup_email);
        edtPass = (EditText)findViewById(R.id.sup_pass);
        btnSup = (FloatingActionButton)findViewById(R.id.btnSup);

        btnSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPass.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty())
                {
                    registerUser(name, email, password);
                }
                else
                {
                    Snackbar.make(v, "All Fields are Compulsory",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        tv_login.setText(Html.fromHtml("<html>Already have an account?<u>Sign In</u></html>"));
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void registerUser(final String name, final String email, final String password)
    {
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();

        final StringRequest strReq = new StringRequest(Request.Method.POST, AppURLs.URL_USERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if(!error)
                    {
                        Intent i = new Intent(SignUp.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq ,tag_string_req);
    }
    private void showDialog()
    {
        if(!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog()
    {
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
}
