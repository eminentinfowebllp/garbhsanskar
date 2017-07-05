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

    public String getActivity_title_eng() {
        return activity_title_eng;
    }

    public void setActivity_title_eng(String activity_title_eng) {
        this.activity_title_eng = activity_title_eng;
    }

    public String getActivity_title_hindi() {
        return activity_title_hindi;
    }

    public void setActivity_title_hindi(String activity_title_hindi) {
        this.activity_title_hindi = activity_title_hindi;
    }

    public String getActivity_title_guj() {
        return activity_title_guj;
    }

    public void setActivity_title_guj(String activity_title_guj) {
        this.activity_title_guj = activity_title_guj;
    }

    public String getActivity_desc_eng() {
        return activity_desc_eng;
    }

    public void setActivity_desc_eng(String activity_desc_eng) {
        this.activity_desc_eng = activity_desc_eng;
    }

    public String getActivity_desc_hindi() {
        return activity_desc_hindi;
    }

    public void setActivity_desc_hindi(String activity_desc_hindi) {
        this.activity_desc_hindi = activity_desc_hindi;
    }

    public String getActivity_desc_guj() {
        return activity_desc_guj;
    }

    public void setActivity_desc_guj(String activity_desc_guj) {
        this.activity_desc_guj = activity_desc_guj;
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

    private String activity_date;
   private String activity_title_eng,activity_title_hindi,activity_title_guj;
   private String activity_desc_eng,activity_desc_hindi,activity_desc_guj;
   private String activity_video;
   private String activity_status;
   private String activity_added_date;
   private String activity_title;
   private String activity_desc;

}
