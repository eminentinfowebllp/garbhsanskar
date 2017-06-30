package com.example.eminent.myapplication.Model;

import java.util.ArrayList;

/**
 * Created by eminent on 6/9/2017.
 */

public class ActivityModel {

    private String activity_id;

    public ArrayList<String> getActivity_image() {
        return activity_image;
    }

    public void setActivity_image(ArrayList<String> activity_image) {
        this.activity_image = activity_image;
    }

    private ArrayList<String> activity_image;



    public String getActivity_completed() {
        return activity_completed;
    }

    public void setActivity_completed(String activity_completed) {
        this.activity_completed = activity_completed;
    }

    private String activity_completed;

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
    }

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getActivity_desc() {
        return activity_desc;
    }

    public void setActivity_desc(String activity_desc) {
        this.activity_desc = activity_desc;
    }

    public String getActivity_video() {
        return activity_video;
    }

    public void setActivity_video(String activity_video) {
        this.activity_video = activity_video;
    }

    public String getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(String activity_status) {
        this.activity_status = activity_status;
    }

    public String getActivity_added_date() {
        return activity_added_date;
    }

    public void setActivity_added_date(String activity_added_date) {
        this.activity_added_date = activity_added_date;
    }

    private String activity_date;
   private String activity_title;
   private String activity_desc;
   private String activity_video;
   private String activity_status;
   private String activity_added_date;

}
