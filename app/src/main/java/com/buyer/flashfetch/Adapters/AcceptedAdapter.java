package com.buyer.flashfetch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

/**
 * Created by kranthikumar_b on 8/5/2016.
 */

public class AcceptedAdapter extends RecyclerView.Adapter<AcceptedAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Request> requests = new ArrayList<>();

    public AcceptedAdapter(Context context, ArrayList<Request> requests){

        this.context = context;
        this.requests = requests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView productName, productPrice, priceOffered;

        public ViewHolder(View itemView) {
            super(itemView);

            productName = (TextView)itemView.findViewById(R.id.product_name_accepted);
            productPrice = (TextView)itemView.findViewById(R.id.price_from_store);
            priceOffered = (TextView)itemView.findViewById(R.id.price_offered);
        }
    }

    @Override
    public AcceptedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_accepted,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AcceptedAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
//        return requests.size();
        //TODO: edit here
        return 0;
    }
}
