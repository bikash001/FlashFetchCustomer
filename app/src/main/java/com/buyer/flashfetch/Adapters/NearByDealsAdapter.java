package com.buyer.flashfetch.Adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.R;

import java.util.List;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class NearByDealsAdapter extends RecyclerView.Adapter<NearByDealsAdapter.MyViewHolder> implements RecyclerView.OnClickListener {

    public List<NearByDealsDataModel> nearByDealsDataModelList;

    public NearByDealsAdapter(List<NearByDealsDataModel> list){
        this.nearByDealsDataModelList = list;
    }

    @Override
    public void onClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView shopName,shopDistance,description;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView2);
            shopName = (TextView)itemView.findViewById(R.id.shop_name);
            shopDistance = (TextView)itemView.findViewById(R.id.shop_distance);
            description = (TextView)itemView.findViewById(R.id.description);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_nearby_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NearByDealsDataModel dataModel = nearByDealsDataModelList.get(position);

        holder.imageView.setImageURI(Uri.parse(dataModel.getImageUrl()));
        holder.shopName.setText(dataModel.getShopName());
        holder.shopDistance.setText(dataModel.getShopDistance());
        holder.description.setText(dataModel.getItemDescription());
    }

    @Override
    public int getItemCount() {
        return nearByDealsDataModelList.size();
    }
}
