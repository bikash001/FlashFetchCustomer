package com.buyer.flashfetch.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.Adapters.SectionsPagerAdapter;
import com.buyer.flashfetch.Network.BackGroundTask.DownloadImageTask;
import com.buyer.flashfetch.Quotes;
import com.buyer.flashfetch.R;

public class RequestedFragment extends Fragment {

    private int rank=0;
    private String url;

    public static DownloadImageTask refTask;

    public RequestedFragment(){
    }

    public static RequestedFragment newInstance(int index) {
        RequestedFragment f = new RequestedFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rank = getArguments()!=null? getArguments().getInt("index"):1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_requested,container,false);
        url = SectionsPagerAdapter.reqs.get(rank-1).pimg;
        TextView textView = (TextView) view.findViewById(R.id.product_name);
        textView.setText(SectionsPagerAdapter.reqs.get(rank-1).pname);
        textView = (TextView) view.findViewById(R.id.set_price);
        textView.setText(SectionsPagerAdapter.reqs.get(rank-1).pprice);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view_requested);
        refTask = new DownloadImageTask(imageView);
        refTask.execute(url);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = (Button) getView().findViewById(R.id.button_detail);
        CharSequence tag = "button_detail"+rank;
        button.setTag(tag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getContext(), ""+v.getTag(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        Button button_quote = (Button) getView().findViewById(R.id.button_quote);
        tag = "button_quote"+rank;
        button_quote.setTag(tag);
        button_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Quotes.class);
                intent.putExtra("URL",url);
                   /* Bundle bundle = new Bundle();
                    bundle.putString("id", reqs.get(rank-1).id);*/
                intent.putExtra("id",SectionsPagerAdapter.reqs.get(rank-1).id);
                startActivity(intent);
            }
        });
    }

}

