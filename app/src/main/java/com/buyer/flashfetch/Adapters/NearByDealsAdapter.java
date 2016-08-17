package com.buyer.flashfetch.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.R;

import java.util.List;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class NearByDealsAdapter extends RecyclerView.Adapter<NearByDealsAdapter.MyViewHolder> implements RecyclerView.OnClickListener {

    private Context context;
    private List<NearByDealsDataModel> nearByDealsDataModelList;

    public NearByDealsAdapter(Context context, List<NearByDealsDataModel> list){
        this.context = context;
        this.nearByDealsDataModelList = list;
    }

    @Override
    public void onClick(View view) {
        //TODO: handle item click make service call for shop specific deals and fetch data using the shopId
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView shopName,shopDistance,description, buyNow;
        public ImageView imageView;
        public LinearLayout pickUpLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            pickUpLayout = (LinearLayout)itemView.findViewById(R.id.deals_pickup_layout);

            imageView = (ImageView)itemView.findViewById(R.id.imageView2);
            shopName = (TextView)itemView.findViewById(R.id.shop_name);
            shopDistance = (TextView)itemView.findViewById(R.id.shop_distance);
            description = (TextView)itemView.findViewById(R.id.description);
            buyNow = (TextView)itemView.findViewById(R.id.buy_now_button);
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

        Glide.with(context).load(dataModel.getImageUrl()).centerCrop().into(holder.imageView);

        holder.shopName.setText(dataModel.getShopName());
        holder.shopDistance.setText(dataModel.getShopDistance());
        holder.description.setText(dataModel.getItemDescription());

        if(dataModel.isDeliverable()){
            holder.pickUpLayout.setVisibility(View.GONE);
            holder.buyNow.setVisibility(View.VISIBLE);
        }else{
            holder.pickUpLayout.setVisibility(View.VISIBLE);
            holder.buyNow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
//        return nearByDealsDataModelList.size();
        return 0;
    }
}
