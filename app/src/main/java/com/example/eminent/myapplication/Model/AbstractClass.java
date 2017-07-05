package com.example.eminent.myapplication.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eminent on 7/5/2017.
 */

public class AbstractClass{

    private static final String KEY_USERID = "user_id";
    private static final String KEY_DAY = "day";
    private Context context;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor userEditor;

    private String prgnancyDay,userId;

    String activity_title,activity_desc;

    public AbstractClass(Context context) {
        this.context = context;
    }

    public void languageAPICall(final int pos, final List<ActivityModel> activityModelList) {

        sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);

         prgnancyDay = sharedPreferences.getString(Config.ACTUAL_DAY, "");
         userId = sharedPreferences.getString(Config.USER_ID, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/getactivity.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        try {

                            JSONObject rootObject = new JSONObject(s);
                            int successCode = rootObject.getInt("success");
                            if (successCode == 1) {

                                JSONObject childObject = rootObject.getJSONObject("activitydata");
                                JSONArray jsonArray = childObject.getJSONArray("activities");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String activity_id = jsonObject.getString("activity_id");
                                    String activity_day = jsonObject.getString("activity_day");
                                    String activity_video = jsonObject.getString("activity_video");
                                    String activity_status = jsonObject.getString("activity_status");
                                    String activity_added_date = jsonObject.getString("activity_added_date");

                                    if (pos == 0)
                                    {
                                         activity_title = jsonObject.getString("activity_title_eng");
                                         activity_desc = jsonObject.getString("activity_desc_eng");

                                    }else if (pos == 1)
                                    {
                                         activity_title = jsonObject.getString("activity_title_hindi");
                                         activity_desc= jsonObject.getString("activity_desc_hindi");

                                    }else if (pos == 2)
                                    {
                                         activity_title= jsonObject.getString("activity_title_guj");
                                         activity_desc = jsonObject.getString("activity_desc_guj");

                                    }
                                    ActivityModel model = new ActivityModel();
                                    ArrayList<String> imagList = new ArrayList<String>();

                                    if (jsonObject.has("activity_image")) {

                                        JSONArray genreArry = jsonObject.getJSONArray("activity_image");
                                        ArrayList<String> genre = new ArrayList<String>();

                                        for (int j = 0; j < genreArry.length(); j++)
                                        {
                                            genre.add((String) genreArry.get(j));
                                        }
                                        model.setActivity_image(genre);

                                    }

                                    String activity_completed = jsonObject.getString("completed");

                                    model.setActivity_id(activity_id);
                                    model.setActivity_date(activity_day);
                                    model.setActivity_video(activity_video);
                                    model.setActivity_status(activity_status);
                                    model.setActivity_added_date(activity_added_date);
                                    model.setActivity_completed(activity_completed);
                                    model.setActivity_title(activity_title);
                                    model.setActivity_desc(activity_desc);
                                    activityModelList.add(model);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley_Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(KEY_DAY, String.valueOf(prgnancyDay));
                params.put(KEY_USERID, userId);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
