package com.cdac.readpanda;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cdac.readpanda.model.articles;

import java.util.List;

/**
 * Created by Frndzzz on 03-07-2016.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    String title,subtitle,content,image,author;
    int posi;

    List<articles> mArticles;
    public CardAdapter(List<articles> pArticles, Context context){
        super();
        //Getting all the articles
        this.mArticles = pArticles;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        articles article = mArticles.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(article.getImage(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, R.drawable.a));

        holder.imageView.setImageUrl(article.getImage(), imageLoader);
        holder.txtTitle.setText(article.getTitle());
        holder.txtSubtitle.setText(article.getSubtitle());

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView txtTitle;
        public TextView txtSubtitle;


        public ViewHolder(final View itemView) {
            super(itemView);
            articles article = mArticles.get(posi);
            txtTitle = (TextView)itemView.findViewById(R.id.card_title);
            txtSubtitle = (TextView)itemView.findViewById(R.id.card_text);
            imageView = (NetworkImageView)itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    posi = getAdapterPosition();
                    Log.v("POSITION_OF_CARD",""+posi);
                    articles article = mArticles.get(posi);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ScrollingActivity.class);
                    intent.putExtra(ScrollingActivity.EXTRA_TITLE, article.getTitle());
                    intent.putExtra(ScrollingActivity.EXTRA_SUBTITLE, article.getSubtitle());
                    intent.putExtra(ScrollingActivity.EXTRA_CONTENT,article.getContent());
                    intent.putExtra(ScrollingActivity.EXTRA_AUTHOR,article.getAuthor());
                    intent.putExtra(ScrollingActivity.EXTRA_IMAGE,article.getImage());
                    context.startActivity(intent);
                }
            });
        }
    }
}
