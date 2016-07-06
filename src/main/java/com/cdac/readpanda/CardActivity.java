package com.cdac.readpanda;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.readpanda.model.articles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardActivity extends AppCompatActivity {

    private List<articles> mArticles;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardAdapter adapter;

    public static final String EXTRA_CAT = "tag";
    St
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerVie);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        mArticles = new ArrayList<>();

        //Calling method to get data
        getData();
    }
    //This method will get data from the web api
    private void getData(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(CardActivity.this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        StringRequest strReq = new StringRequest(Request.Method.POST, AppURLs.URL_USERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Dismissing progress dialog
                loading.dismiss();

                //calling method to parse json array
                try {
                    parseData(new JSONArray(response));
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CardActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag","getArtById");
                params.put("id",);
                return params;
            }
        };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(CardActivity.this);

        //Adding request to the queue
        requestQueue.add(strReq);
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            articles article = new articles();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                article.setImage(json.getString(AppURLs.TAG_IMAGE_URL));
                article.setTitle(json.getString(AppURLs.TAG_TITLE));
                article.setSubtitle(json.getString(AppURLs.TAG_SUBTITLE));
                article.setContent(json.getString(AppURLs.TAG_CONTENT));
                article.setAuthor(json.getString(AppURLs.TAG_AUTHOR));
                article.setId(json.getInt(AppURLs.TAG_ID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mArticles.add(article);
        }

        //Finally initializing our adapter
        adapter = new CardAdapter(mArticles, CardActivity.this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
