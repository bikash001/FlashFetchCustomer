package com.buyer.flashfetch.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Fragments.AcceptedListFragment;
import com.buyer.flashfetch.Fragments.RequestedFragment;
import com.buyer.flashfetch.Objects.Request;
import com.buyer.flashfetch.R;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public static ArrayList<Request> reqs;

    private String[] titles = {"Requested", "Accepted"};

    private static boolean list_is_empty;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new RequestedFragment();
            case 1:
                return new AcceptedListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

//    public static class PlaceholderFragment extends Fragment {
//        private int ACCEPTED=10;
//        private int REQUESTED=0;
//
//        /** The fragment argument representing the section number for this fragment **/
//
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /** Returns a new instance of this fragment for the given section number **/
//
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//            View rootView ;
//            Bundle args = getArguments();
//            int count = args.getInt(ARG_SECTION_NUMBER);
//
//            if (count == 1){
//                reqs = Request.getAllRequests(getContext());
//                REQUESTED = reqs.size();
//                setEmpty(REQUESTED);
//            }
//            else{
//
//            }
//            if(Empty()) {
//                rootView = inflater.inflate(R.layout.fragment_deals,container,false);
//                LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.list_layout);
//                TextView textView = new TextView(getContext());
//                textView.setText("Empty");
//                layout.addView(textView);
//            }
//            else {
//                if (count == 1) {
//                    rootView = inflater.inflate(R.layout.fragment_deals, container, false);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    for (int i = 1; i <=REQUESTED; i++) {
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        RequestedFragment fillList = RequestedFragment.newInstance(i);
//                        fragmentTransaction.add(R.id.list_layout, fillList);
//                        fragmentTransaction.commit();
//                    }
//                } else {
//                    rootView = inflater.inflate(R.layout.fragment_deals2, container, false);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    for (int i = 1; i <=ACCEPTED; i++) {
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        AcceptedListFragment accepted_list = AcceptedListFragment.newInstance(i);
//                        fragmentTransaction.add(R.id.list_layout2, accepted_list);
//                        fragmentTransaction.commit();
//                    }
//                }
//            }
//            return rootView;
//        }
//    }
//
//    private static boolean Empty(){
//        return list_is_empty;
//    }
//    private static void setEmpty(int size){
//        if(size > 0){
//            list_is_empty = false;
//        }
//    }
}
