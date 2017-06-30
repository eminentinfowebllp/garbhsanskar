package com.example.eminent.myapplication.Model;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;


/**
 * Created by Android on 6/22/2017.
 */

public class Common
{
    public static final String BASE_URL = "http://gs.mobileapplicationchamps.com/";

    public static final String LOGIN_API = "API/login.php";
    public static final String ACTIVITY_API = "";
    public static final String COMPLETE_STATUS_API = "API/complete-activity.php";


    public static Intent createIntent(Context context, Class activityClass)
    {
        return new Intent(context,activityClass);
    }


}
