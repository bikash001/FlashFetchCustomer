package com.buyer.flashfetch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Objects.Notification;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

/**
 * Created by KRANTHI on 21-08-2016.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationArrayList;

    public NotificationsAdapter(Context context, ArrayList<Notification> notificationArrayList){
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView heading,description,timer;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.notifications_image_view);
            heading = (TextView)itemView.findViewById(R.id.notifications_heading);
            description = (TextView)itemView.findViewById(R.id.notifications_description);
            timer = (TextView)itemView.findViewById(R.id.notifications_timer);
        }
    }

    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = notificationArrayList.get(position);

        if(notification.getImageURL() != null){
            Glide.with(context).load(notification.getImageURL()).centerCrop().into(holder.imageView);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }

        holder.heading.setText(notification.getHeading());
        holder.description.setText(notification.getDescription());

        long time = System.currentTimeMillis();

        int difference = (int) ((notification.getTimeInMillis() - time)/60000);

        if(difference < 60){
            holder.timer.setText(difference + "m ago");
        }else{
            holder.timer.setText((difference/60) + "h ago");
        }
    }

    @Override
    public int getItemCount() {
        if(notificationArrayList == null){
            return 0;
        }
        return notificationArrayList.size();
    }
}
