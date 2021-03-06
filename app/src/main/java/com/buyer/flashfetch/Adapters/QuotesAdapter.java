package com.buyer.flashfetch.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Helper.DatabaseHelper;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KRANTHI on 13-08-2016.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {

    private Context context;
    private List<Quote> quoteList;
    private String productId;

    public QuotesAdapter(Context context, String productId, List<Quote> quoteList) {
        this.context = context;
        this.productId = productId;
        this.quoteList = quoteList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productType,sellerName, productPrice, timer,sellerDeclined, distance, bargain, comment, more, bargainButton, acceptButton;
        public boolean bargained;
        public LinearLayout generalLayout, acceptanceLayout, buttonLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            generalLayout = (LinearLayout)itemView.findViewById(R.id.general_layout);
            acceptanceLayout = (LinearLayout)itemView.findViewById(R.id.acceptance_layout);
            buttonLayout = (LinearLayout) itemView.findViewById(R.id.button_layout);

            productType = (TextView)itemView.findViewById(R.id.product_type);
            sellerName = (TextView) itemView.findViewById(R.id.seller_name);
            productPrice = (TextView) itemView.findViewById(R.id.price_offered_quotes);
            distance = (TextView) itemView.findViewById(R.id.distance);
            timer = (TextView) itemView.findViewById(R.id.timer);
            comment = (TextView) itemView.findViewById(R.id.comment);
            more = (TextView) itemView.findViewById(R.id.more);
            timer = (TextView)itemView.findViewById(R.id.timer);
            sellerDeclined = (TextView)itemView.findViewById(R.id.decline_message);
            bargain = (TextView) itemView.findViewById(R.id.bargained);
            bargainButton = (TextView) itemView.findViewById(R.id.bargain);
            acceptButton = (TextView) itemView.findViewById(R.id.accept);

            // tt = new TimerClass(0,15,timer);
            //tt.start();
        }
    }

    @Override
    public QuotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quotes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuotesAdapter.ViewHolder holder, int position) {

        Quote quote = quoteList.get(position);

        ArrayList<Request> requestArrayList = Request.getRequest(context, productId);
        Request request = requestArrayList.get(0);

        ServiceManager.getRoadDistance(context,Double.parseDouble(quote.quoteId),Double.parseDouble(quote.latitude),
                Double.parseDouble(quote.longitude),Double.parseDouble(request.deliveryLatitude),Double.parseDouble(request.deliveryLongitude));

        switch (quote.productType){
            case 0:
                if(quote.sellerDeliveryType == 0){
                    holder.productType.setText("Same | Free Seller Delivery");
                }else{
                    holder.productType.setText("Same");
                }
                break;
            case 1:
                holder.productType.setText("Similar");
                break;
        }

        holder.sellerName.setText(quote.sellerName);
        holder.productPrice.setText(quote.quotePrice);

        holder.distance.setText(quote.distance);

        holder.bargained = quote.bargained;

        if (quote.sellerConfirmation) {
            holder.distance.setText("bargain accepted");
            holder.bargainButton.setVisibility(View.GONE);
        }

        Layout temp = holder.comment.getLayout();
        if (temp != null) {
            int lines = temp.getLineCount();
            if (lines > 0) {
                int ellipsisCount = temp.getEllipsisCount(lines - 1);
                if (ellipsisCount > 0) {
                    holder.more.setVisibility(View.VISIBLE);
                }
            }
        }
//        if (holder.bargained) {
//            holder.bargain.setVisibility(View.GONE);
//        }
//        if (!quote.getValid()) {
//            holder.layout.setVisibility(View.GONE);
//        }
//
//        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Delivery.class);
//                intent.putExtra("qid", object.qid);
//                context.startActivity(intent);
//            }
//        });
//        holder.bargainButton.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog = new Dialog(v.getContext());
//                dialog.setContentView(R.layout.dialog);
//                dialog.getWindow().setLayout((int) (width * 0.8), height);
//
//                final EditText priceText = (EditText) dialog.findViewById(R.id.new_price);
//                priceText.setFilters(new InputFilter[]{new InputCheck(0, 10000000)});
//                final EditText hourText = (EditText) dialog.findViewById(R.id.hour);
//                hourText.setFilters(new InputFilter[]{new InputCheck(0, 23)});
//                final EditText minText = (EditText) dialog.findViewById(R.id.min);
//                minText.setFilters(new InputFilter[]{new InputCheck(0, 59)});
//                Button button1 = (Button) dialog.findViewById(R.id.ok_dialog);
//                button1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String price = priceText.getText().toString();
//                        String hour = hourText.getText().toString();
//                        String min = minText.getText().toString();
//                        boolean error = false;
//                        if (hour.length() == 0) {
//                            error = true;
//                            hourText.setError("Set Hour");
//                        }
//                        if (min.length() == 0) {
//                            error = true;
//                            minText.setError("Set Min");
//                        }
//                        if (price.length() == 0) {
//                            error = true;
//                            priceText.setError("Set Price");
//                        }
//                        if (!error) {
//                            BargainTask bt = new BargainTask(object, priceText.getText().toString());
//                            bt.execute();
//                            holder.bargainButton.setVisibility(View.GONE);
//                            holder.bargain.setText(String.format("%s %s", "Bargained for ₹", price));
//                            holder.bargain.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//                // set the custom dialog components - text, image and button
//                dialog.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }
}
