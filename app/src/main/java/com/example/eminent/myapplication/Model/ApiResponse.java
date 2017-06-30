package com.example.eminent.myapplication.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 6/21/2017.
 */

public class ApiResponse {

    @SerializedName("user_email")
    private String user_email;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }

    public String getUser_contactno() {
        return user_contactno;
    }

    public void setUser_contactno(String user_contactno) {
        this.user_contactno = user_contactno;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHomepage_title() {
        return homepage_title;
    }

    public void setHomepage_title(String homepage_title) {
        this.homepage_title = homepage_title;
    }

    public String getHomepage_tagline() {
        return homepage_tagline;
    }

    public void setHomepage_tagline(String homepage_tagline) {
        this.homepage_tagline = homepage_tagline;
    }

    public String getHomepage_desc() {
        return homepage_desc;
    }

    public void setHomepage_desc(String homepage_desc) {
        this.homepage_desc = homepage_desc;
    }

    public String getHomepage_image() {
        return homepage_image;
    }

    public void setHomepage_image(String homepage_image) {
        this.homepage_image = homepage_image;
    }

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("user_city")
    private String user_city;
    @SerializedName("user_address")
    private String user_address;
    @SerializedName("user_state")
    private String user_state;
    @SerializedName("user_country")
    private String user_country;
    @SerializedName("user_contact_no")
    private String user_contactno;
    @SerializedName("success")
    private Integer success;
    @SerializedName("message")
    private String message;
    @SerializedName("homepage_title")
    private String homepage_title;
    @SerializedName("homepage_tagline")
    private String homepage_tagline;
    @SerializedName("homepage_desc")
    private String homepage_desc;
    @SerializedName("homepage_image")
    private String homepage_image;


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }



    /**
     * No args constructor for use in serialization
     */
    public ApiResponse() {
    }

    /**
     * @param message
     * @param success
     */
    public ApiResponse(Integer success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
