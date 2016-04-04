package com.example.bikash.flashfetchcustomer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bikash on 03-04-2016.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    private List<QuotesObject> list;

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView sellerName, productPrice,timer,distance;
        protected Button bargainButton,acceptButton;
        public ViewHolder(View itemView) {
            super(itemView);
            sellerName = (TextView) itemView.findViewById(R.id.seller_name);
            productPrice = (TextView) itemView.findViewById(R.id.price_offered_quotes);
            timer = (TextView) itemView.findViewById(R.id.timer);
            distance = (TextView) itemView.findViewById(R.id.distance);
           // bargainButton = (Button) itemView.findViewById(R.id.button_bargain);
           // acceptButton = (Button) itemView.findViewById(R.id.accept_button);
        }
    }

    public ProductAdapter(List<QuotesObject> items){
        list = items;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quotes,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        QuotesObject object = list.get(position);
        holder.sellerName.setText(object.getSeller());
        holder.timer.setText(object.getTime());
        holder.distance.setText(object.getDistance());
        holder.productPrice.setText(object.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
