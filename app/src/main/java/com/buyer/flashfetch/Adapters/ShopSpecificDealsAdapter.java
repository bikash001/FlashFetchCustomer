package com.buyer.flashfetch.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buyer.flashfetch.Objects.SpecificDealsDataModel;
import com.buyer.flashfetch.R;

import java.util.List;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class ShopSpecificDealsAdapter extends RecyclerView.Adapter<ShopSpecificDealsAdapter.MyViewHolder> {

    private Context context;
    private List<SpecificDealsDataModel> specificDealsDataModelList;

    public ShopSpecificDealsAdapter(Context context, List<SpecificDealsDataModel> list){
        this.specificDealsDataModelList = list;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView productName,productOriginalPrice, productNewPrice, moreDetails, buyNow;
        public ImageView imageView;
        public LinearLayout specificDealsLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            specificDealsLayout = (LinearLayout)itemView.findViewById(R.id.specific_deals_layout);
            imageView = (ImageView)itemView.findViewById(R.id.specific_deals_product_image_view);
            productName = (TextView)itemView.findViewById(R.id.specific_deals_product_text);
            productOriginalPrice = (TextView)itemView.findViewById(R.id.specific_deals_product_original_price);
            productNewPrice = (TextView)itemView.findViewById(R.id.specific_deals_product_new_price);
            moreDetails = (TextView)itemView.findViewById(R.id.specific_deals_product_more_details);
            buyNow = (TextView)itemView.findViewById(R.id.specific_deals_product_but_now);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specfic_deals_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SpecificDealsDataModel specificDealsDataModel = specificDealsDataModelList.get(position);

        Glide.with(context).load(specificDealsDataModel.getImageURL()).centerCrop().into(holder.imageView);

        holder.productName.setText(specificDealsDataModel.getProductName());
        holder.productOriginalPrice.setText(specificDealsDataModel.getProductOriginalPrice());
        holder.productNewPrice.setText(specificDealsDataModel.getProductNewPrice());

        holder.productOriginalPrice.setPaintFlags(holder.productOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return specificDealsDataModelList.size();
    }
}
