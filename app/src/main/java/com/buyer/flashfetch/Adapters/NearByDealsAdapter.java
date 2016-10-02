package com.buyer.flashfetch.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Constants.NearByDealsConstants;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NearByDealsAdapter extends RecyclerView.Adapter<NearByDealsAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<NearByDealsDataModel> nearByDealsDataModelList;

    public NearByDealsAdapter(Context context, ArrayList<NearByDealsDataModel> list){
        this.context = context;
        this.nearByDealsDataModelList = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView shopName,knowMore,heading, buyNow, storeDetails;
        public ImageView imageView;
        public LinearLayout pickUpLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            pickUpLayout = (LinearLayout)itemView.findViewById(R.id.deals_pickup_layout);

            imageView = (ImageView)itemView.findViewById(R.id.imageView2);
            shopName = (TextView)itemView.findViewById(R.id.shop_name);
            knowMore = (TextView)itemView.findViewById(R.id.know_more);
            heading = (TextView)itemView.findViewById(R.id.heading);
            buyNow = (TextView)itemView.findViewById(R.id.buy_now_button);
            storeDetails = (TextView)itemView.findViewById(R.id.store_details);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_nearby_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NearByDealsDataModel dataModel = nearByDealsDataModelList.get(position);

        Glide.with(context).load(dataModel.getImageUrl()).centerCrop().into(holder.imageView);

        holder.shopName.setText(dataModel.getShopName() + " | " + dataModel.getShopLocation());
        holder.heading.setText(dataModel.getItemHeading());

        if(dataModel.getDealsType() == NearByDealsConstants.PICKUP_DEALS){
            holder.buyNow.setText("ACTIVATE DEAL");
            holder.buyNow.setBackgroundColor(context.getResources().getColor(R.color.ff_green));
        }else if(dataModel.getDealsType() == NearByDealsConstants.INVENTORY_DEALS){
            holder.buyNow.setText("BUY NOW");
            holder.buyNow.setBackgroundColor(context.getResources().getColor(R.color.ff_black));
        }

        holder.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataModel.getDealsType() == NearByDealsConstants.PICKUP_DEALS){
                    holder.buyNow.setVisibility(View.GONE);
                    holder.pickUpLayout.setVisibility(View.VISIBLE);
                }else if(dataModel.getDealsType() == NearByDealsConstants.INVENTORY_DEALS){

                }
            }
        });

        holder.storeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView storeName, storeAddress, storePhone, storeDetailsOkButton;

                final Dialog dialog = new Dialog(context);

                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.deal_store_details);

                storeName = (TextView)dialog.findViewById(R.id.deals_store_name);
                storeAddress = (TextView)dialog.findViewById(R.id.deals_store_address);
                storePhone = (TextView)dialog.findViewById(R.id.deals_store_phone);
                storeDetailsOkButton = (TextView)dialog.findViewById(R.id.store_details_ok_button);

                storePhone.setPaintFlags(storePhone.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                storeName.setText(dataModel.getShopName());
                storeAddress.setText(dataModel.getShopAddress());

                if(!TextUtils.isEmpty(dataModel.getShopPhone())){
                    storePhone.setText(dataModel.getShopPhone());
                }else{
                    storePhone.setVisibility(View.GONE);
                }

                storePhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataModel.getShopPhone()));
                        context.startActivity(intent);
                    }
                });

                storeDetailsOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        holder.knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView code, codeDescription, validUpTo, knowMoreDone, availDeal;

                final Dialog dialog = new Dialog(context);

                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.know_more_dialog);

                codeDescription = (TextView)dialog.findViewById(R.id.deal_description_text);
                validUpTo = (TextView)dialog.findViewById(R.id.valid_up_to_text);
                knowMoreDone = (TextView)dialog.findViewById(R.id.know_more_dialog_ok);
                availDeal = (TextView)dialog.findViewById(R.id.how_to_avail_deal);

                codeDescription.setText(dataModel.getItemDescription());
                validUpTo.setText(dataModel.getValidTo());
                availDeal.setText(dataModel.getHowToAvailDeal());

                knowMoreDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return nearByDealsDataModelList.size();
    }
}
