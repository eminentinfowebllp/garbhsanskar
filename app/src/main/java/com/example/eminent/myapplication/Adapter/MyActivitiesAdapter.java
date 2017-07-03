package com.example.eminent.myapplication.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eminent.myapplication.Model.MyActivitiesModel;
import com.example.eminent.myapplication.NavigationPanel.MyActivities;
import com.example.eminent.myapplication.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.List;

/**
 * Created by eminent on 6/27/2017.
 */

public class MyActivitiesAdapter extends RecyclerView.Adapter<MyActivitiesAdapter.ViewHolder> {

    private Context context;
    private List<MyActivitiesModel> activitiesModels;

    public MyActivitiesAdapter(Context myActivities, List<MyActivitiesModel> activityModelList) {

        this.context = myActivities;
        this.activitiesModels = activityModelList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myactivities_list_item, viewGroup,false);
        MyActivitiesAdapter.ViewHolder viewHolder = new MyActivitiesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        MyActivitiesModel myActivitiesModel = activitiesModels.get(i);

        Double completed_activities = Double.valueOf(myActivitiesModel.getCompleted_activities());
        Double total_activities = Double.valueOf(myActivitiesModel.getTotal_activities());
        Double percentage = ((completed_activities * 100)/ total_activities);
        String.format("%.2f", percentage);

//        int Average = myActivitiesModel
        viewHolder.activityName.setText(myActivitiesModel.getActivities());
        viewHolder.activityAvg.setText(myActivitiesModel.getCompleted_activities()+"/"+myActivitiesModel.getTotal_activities());
        viewHolder.arcProgress.setProgress((int) Float.parseFloat(String.format("%.1f", percentage)));

        viewHolder.progressBar.setMax(Integer.parseInt(myActivitiesModel.getTotal_activities()));
        viewHolder.progressBar.setProgress(Integer.parseInt(myActivitiesModel.getCompleted_activities()));
    }

    @Override
    public int getItemCount() {
        return activitiesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView activityName, activityAvg;
//                , activityPercentage;
        private ArcProgress arcProgress;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            activityName = (TextView) itemView.findViewById(R.id.titleTxt);
            activityAvg = (TextView) itemView.findViewById(R.id.textvw_scoreAverage);
            arcProgress = (ArcProgress) itemView.findViewById(R.id.arc_progress);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
//            activityPercentage = (TextView) itemView.findViewById(R.id.textvw_scorePercentage);
        }
    }
}
