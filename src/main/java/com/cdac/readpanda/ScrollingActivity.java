package com.cdac.readpanda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cdac.readpanda.model.articles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScrollingActivity extends AppCompatActivity {
    ActionBar actionBar;
    TextView txtSubtitle,txtContent,txtAuthor;
    NetworkImageView imgScr;
    int position;
    CollapsingToolbarLayout collapsingToolbar;
    public static final String EXTRA_CONTENT = "ocontent";
    public static final String EXTRA_IMAGE = "oimage";
    public static final String EXTRA_AUTHOR = "oauthor";
    public static final String EXTRA_TITLE = "otitle";
    public static final String EXTRA_SUBTITLE = "osubtitle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layoutscr);

        txtSubtitle = (TextView)findViewById(R.id.subtitle_readScr);
        txtContent = (TextView)findViewById(R.id.content_readScr);
        txtAuthor = (TextView)findViewById(R.id.author_readScr);
        imgScr = (NetworkImageView)findViewById(R.id.image_readscr);

        Intent i = getIntent();
                txtSubtitle.setText(i.getStringExtra(EXTRA_SUBTITLE));
                txtContent.setText(i.getStringExtra(EXTRA_CONTENT));
                txtAuthor.setText(i.getStringExtra(EXTRA_AUTHOR));
                collapsingToolbar.setTitle(i.getStringExtra(EXTRA_TITLE));
                ImageLoader imageLoader = CustomVolleyRequest.getInstance(ScrollingActivity.this).getImageLoader();
                imageLoader.get(i.getStringExtra(EXTRA_IMAGE), ImageLoader.getImageListener(imgScr, R.mipmap.ic_launcher, R.drawable.a));

                imgScr.setImageUrl(EXTRA_IMAGE, imageLoader);
        position++;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    }
