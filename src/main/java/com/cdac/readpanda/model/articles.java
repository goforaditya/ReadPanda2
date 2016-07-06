package com.cdac.readpanda.model;

import com.cdac.readpanda.AppURLs;

/**
 * Created by Frndzzz on 03-07-2016.
 */
public class articles {
    private String title;
    private String subtitle;
    private String content;
    private String image;
    private String author;
    int artid;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return AppURLs.URL_BASE+"readable/img/"+image;
    }

    public void setId(int i) {
        this.artid = i;
    }

    public int getId() {
        return artid;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public articles(){}

    public articles(String title,String subtitle,String content,String image, String author)
    {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.image = image;
        this.author = author;
    }
}
