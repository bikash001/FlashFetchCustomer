package com.buyer.flashfetch.Adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Objects.DealsDataModel;
import com.buyer.flashfetch.R;

import java.util.List;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.MyViewHolder> {

    private List<DealsDataModel> dealsDataModelList;

    public DealsAdapter(List<DealsDataModel> list){
        this.dealsDataModelList = list;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView shopName,shopDistance,description;
        public ImageView imageView;
        public Button button;
        public LinearLayout pickupLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView2);
            shopName = (TextView)itemView.findViewById(R.id.shop_name);
            shopDistance = (TextView)itemView.findViewById(R.id.shop_distance);
            description = (TextView)itemView.findViewById(R.id.description);
            button = (Button)itemView.findViewById(R.id.detail_button);
            pickupLayout = (LinearLayout)itemView.findViewById(R.id.deals_pickup_layout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_nearby_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DealsDataModel dataModel = dealsDataModelList.get(position);

        if(dataModel.isDelivered()){
            holder.pickupLayout.setVisibility(View.GONE);
            holder.button.setVisibility(View.VISIBLE);
        }else{
            holder.pickupLayout.setVisibility(View.VISIBLE);
            holder.button.setVisibility(View.GONE);
        }
        holder.imageView.setImageURI(Uri.parse(dataModel.getImageUrl()));
        holder.shopName.setText(dataModel.getShopName());
        holder.shopDistance.setText(dataModel.getShopDistance());
        holder.description.setText(dataModel.getItemDescription());
    }

    @Override
    public int getItemCount() {
        return dealsDataModelList.size();
    }
}
