package com.cdac.readpanda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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

public class LoginActivity extends AppCompatActivity {
    TextView txtSignu;
    EditText edtEmail,edtPass;
    FloatingActionButton btnLogin;
    Session session;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();

        session = new Session(LoginActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        txtSignu = (TextView)findViewById(R.id.btnSupc);
        edtEmail = (EditText) findViewById(R.id.email_to_login);
        edtPass = (EditText) findViewById(R.id.password_to_login);
        btnLogin = (FloatingActionButton)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();

                if(email.trim().length() > 0 && password.trim().length()>0)
                {
                    checkLogin(email,password);
                }else
                {
                    Snackbar.make(v,"All Fields are compulsory",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        txtSignu.setText(Html.fromHtml("<html>Not on Readpanda yet?<u>Sign Up</u>"));
        txtSignu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUp.class);
                startActivity(i);
            }
        });

    }
    private void checkLogin(final String email, final String password)
    {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppURLs.URL_USERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    String userId = jObj.getString("id");
                    if(userId!=null)
                    {
                        session.setLogin(true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id",userId);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg,Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag","login");
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        //Adding request to queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog()
    {
        if(!progressDialog.isShowing())
        progressDialog.show();
    }
    private void  hideDialog()
    {
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
}
