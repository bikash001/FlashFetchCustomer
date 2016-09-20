package com.buyer.flashfetch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 8/5/2016.
 */
public class RequestedAdapter extends RecyclerView.Adapter<RequestedAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();
    private ArrayList<Quote> quotes = new ArrayList<>();

    public RequestedAdapter(Context context, ArrayList<Request> requests) {

        this.context = context;
        this.requests = requests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView productName, timer, productPrice, numberOfQuotes;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.image_view_requested);
            productName = (TextView)itemView.findViewById(R.id.requested_product_name);
            timer = (TextView)itemView.findViewById(R.id.requested_timer);
            productPrice = (TextView)itemView.findViewById(R.id.requested_price);
            numberOfQuotes = (TextView)itemView.findViewById(R.id.requested_quote_number);
        }
    }

    @Override
    public RequestedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_requested,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestedAdapter.ViewHolder holder, int position) {
        Request request = requests.get(position);

        Glide.with(context).load(request.imageURL).centerCrop().into(holder.imageView);

        holder.productName.setText(request.productName);
        holder.productPrice.setText(request.productPrice);
        //TODO: set timer and no of quotes here
    }

    @Override
    public int getItemCount() {
        //TODO: change this
//        return requests.size();
        return 0;
    }
}
