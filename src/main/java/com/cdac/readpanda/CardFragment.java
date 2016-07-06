package com.cdac.readpanda;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.readpanda.model.articles;
import com.cdac.readpanda.CardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    public List<articles> mArticles;

    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArticles = new ArrayList<>();
        getData();

        return recyclerView;
    }
    private void getData(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Loading Data", "Please wait...",false,false);

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
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                })
    {
        @Override
        protected Map<String, String> getParams()
        {
            Map<String, String> params = new HashMap<>();
            params.put("tag","getall");
            return params;
        }
    };

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
        adapter = new CardAdapter(mArticles, getActivity());

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
