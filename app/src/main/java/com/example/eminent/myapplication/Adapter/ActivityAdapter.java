package com.example.eminent.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eminent.myapplication.Activity.DescriptionActivity;
import com.example.eminent.myapplication.Activity.DetailsActivity;
import com.example.eminent.myapplication.Model.ActivityModel;
import com.example.eminent.myapplication.Model.Common;
import com.example.eminent.myapplication.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by eminent on 6/9/2017.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context mContext;
    private List<ActivityModel> activityModelList;

    public ActivityAdapter(DetailsActivity activityList, List<ActivityModel> activityModelList) {
        this.mContext=activityList;
        this.activityModelList=activityModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ActivityModel model = activityModelList.get(position);

        if(model.getActivity_completed().equalsIgnoreCase("1"))
        {
//            holder.imageView.setVisibility(View.VISIBLE);
//            holder.imageView.setImageResource(R.mipmap.ic_completed);
//            holder.linearLayout.setAlpha(0.5f);
            holder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorlist));
        }
        holder.title.setText(model.getActivity_title());
    }

    @Override
    public int getItemCount() {
        return activityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.titleTxt);
            imageView = (ImageView) itemView.findViewById(R.id.imageComplete);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.completedLl);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    ((Activity)mContext).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                    Intent intent = Common.createIntent(mContext,DescriptionActivity.class);
                    intent.putExtra("activity_desc",activityModelList.get(position).getActivity_desc());
                    intent.putExtra("activity_id",activityModelList.get(position).getActivity_id());
                    intent.putExtra("activity_completed",activityModelList.get(position).getActivity_completed());
                    intent.putExtra("activity_video",activityModelList.get(position).getActivity_video());
                    intent.putExtra("activity_image",activityModelList.get(position).getActivity_image());
                    intent.putExtra("activity_title",activityModelList.get(position).getActivity_title());
                    intent.putExtra("activity_audio",activityModelList.get(position).getActivity_audio());

                    System.out.println ("adapter position "+position);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }
            });
        }

    }


}
