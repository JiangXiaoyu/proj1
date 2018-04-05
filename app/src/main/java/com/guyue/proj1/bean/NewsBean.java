package com.guyue.proj1.bean;

import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by huyun on 2018/3/21.
 */

public class NewsBean implements Serializable{
    private String id;
    private String title;
    private String from;
    private String imageUrl;
    private String contentUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
