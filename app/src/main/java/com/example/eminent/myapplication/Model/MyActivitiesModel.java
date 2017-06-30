package com.example.eminent.myapplication.Model;

/**
 * Created by eminent on 6/27/2017.
 */

public class MyActivitiesModel {

    String activity_average,activity_percentage,total_activities, completed_activities;
    String activities;
    int total_activity_score, completed_activity_score;


    public MyActivitiesModel() {
    }


    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getActivity_average() {
        return activity_average;
    }

    public void setActivity_average(String activity_average) {
        this.activity_average = activity_average;
    }

    public String getActivity_percentage() {
        return activity_percentage;
    }

    public void setActivity_percentage(String activity_percentage) {
        this.activity_percentage = activity_percentage;
    }

    public String getTotal_activities() {
        return total_activities;
    }

    public void setTotal_activities(String total_activities) {
        this.total_activities = total_activities;
    }

    public String getCompleted_activities() {
        return completed_activities;
    }

    public void setCompleted_activities(String completed_activities) {
        this.completed_activities = completed_activities;
    }

    public int getTotal_activity_score() {
        return total_activity_score;
    }

    public void setTotal_activity_score(int total_activity_score) {
        this.total_activity_score = total_activity_score;
    }

    public int getCompleted_activity_score() {
        return completed_activity_score;
    }

    public void setCompleted_activity_score(int completed_activity_score) {
        this.completed_activity_score = completed_activity_score;
    }
}
