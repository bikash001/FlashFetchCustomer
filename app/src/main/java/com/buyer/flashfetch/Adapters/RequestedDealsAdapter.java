package com.buyer.flashfetch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 8/5/2016.
 */
public class RequestedDealsAdapter  extends RecyclerView.Adapter<RequestedDealsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();

    public RequestedDealsAdapter(Context context, ArrayList<Request> requests) {

        this.context = context;
        this.requests = requests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView productName, timer, productPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.image_view_requested);
            productName = (TextView)itemView.findViewById(R.id.requested_product_name);
            timer = (TextView)itemView.findViewById(R.id.requested_timer);
            productPrice = (TextView)itemView.findViewById(R.id.requested_price);
        }
    }

    @Override
    public RequestedDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_requested,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestedDealsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
//        return requests.size();
        return 1;
    }
}
