package com.buyer.flashfetch.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Delivery;
import com.buyer.flashfetch.InputCheck;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.R;

import java.util.List;

/**
 * Created by KRANTHI on 13-08-2016.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder> {

    private Context context;
    private List<Quote> quoteList;

    public QuotesAdapter(Context context,List<Quote> quoteList) {
        this.context = context;
        this.quoteList = quoteList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productType,sellerName, productPrice, timer,sellerDeclined, distance, bargain, comment, more, bargainButton, acceptButton;
        private boolean bargained;
        private LinearLayout generalLayout, acceptanceLayout, buttonLayout;

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

        public void changeView(int pos) {
            int n = getItemCount();
            for (int i = 0; i < pos; i++) {
                quoteList.get(i).setValid(false);
            }
            for (int i = pos + 1; i < n; i++) {
                quoteList.get(i).setValid(false);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public QuotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_quotes, parent, false);
        return new ViewHolder(view);
    }

    public void removeAt(int position) {
        quoteList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, quoteList.size());
    }

    @Override
    public void onBindViewHolder(final QuotesAdapter.ViewHolder holder, int position) {

        Quote quote = quoteList.get(position);

        holder.sellerName.setText(quote.name);
        //holder.timer.setText(object.);
        holder.distance.setText(quote.distance);
        holder.productPrice.setText(quote.qprice);
        holder.bargained = quote.bargained > 0;

        if (quote.selcon > 0) {
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
        if (holder.bargained) {
            holder.bargain.setVisibility(View.GONE);
        }
        if (!quote.getValid()) {
            holder.layout.setVisibility(View.GONE);
        }

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Delivery.class);
                intent.putExtra("qid", object.qid);
                context.startActivity(intent);
            }
        });
        holder.bargainButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog);
                dialog.getWindow().setLayout((int) (width * 0.8), height);

                final EditText priceText = (EditText) dialog.findViewById(R.id.new_price);
                priceText.setFilters(new InputFilter[]{new InputCheck(0, 10000000)});
                final EditText hourText = (EditText) dialog.findViewById(R.id.hour);
                hourText.setFilters(new InputFilter[]{new InputCheck(0, 23)});
                final EditText minText = (EditText) dialog.findViewById(R.id.min);
                minText.setFilters(new InputFilter[]{new InputCheck(0, 59)});
                Button button1 = (Button) dialog.findViewById(R.id.ok_dialog);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String price = priceText.getText().toString();
                        String hour = hourText.getText().toString();
                        String min = minText.getText().toString();
                        boolean error = false;
                        if (hour.length() == 0) {
                            error = true;
                            hourText.setError("Set Hour");
                        }
                        if (min.length() == 0) {
                            error = true;
                            minText.setError("Set Min");
                        }
                        if (price.length() == 0) {
                            error = true;
                            priceText.setError("Set Price");
                        }
                        if (!error) {
                            BargainTask bt = new BargainTask(object, priceText.getText().toString());
                            bt.execute();
                            holder.bargainButton.setVisibility(View.GONE);
                            holder.bargain.setText(String.format("%s %s", "Bargained for ₹", price));
                            holder.bargain.setVisibility(View.VISIBLE);
                        }
                    }
                });
                // set the custom dialog components - text, image and button
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
