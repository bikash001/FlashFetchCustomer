package com.example.bikash.flashfetchcustomer;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "fault: ";
    private SharedPreferences prefs;
    private static final String SIGNED = "SIGNED";
    private static final String NAME = "name";
    private static final String FILE = LoginActivity.FILE;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(FILE,MODE_PRIVATE);
        boolean signed = prefs.getBoolean(SIGNED,false);
        if(!signed){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //from other
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            prefs.edit().clear().apply();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_contact){
            Intent intent = new Intent(this,ContactUs.class);
            startActivity(intent);
            return true;
        }
        else if(id==R.id.action_connect)
        {
            Dialog dialog = new Dialog(this);
            dialog.setTitle("Connect with Us");
            dialog.setContentView(R.layout.dialog_connect);
            LinearLayout fb = (LinearLayout)dialog.findViewById(R.id.fb);
            LinearLayout twitter = (LinearLayout)dialog.findViewById(R.id.twitter);
            LinearLayout whatsapp = (LinearLayout)dialog.findViewById(R.id.whatsapp);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Flashfetch-140606842997095/"));//Insert FB page link
                    startActivity(browserIntent);
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/flashfetch"));//Insert twitter link
                    startActivity(browserIntent);
                }
            });
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, "+919940126973")
                            .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                            .putExtra(ContactsContract.Intents.Insert.NAME, "FlashFetch");
                    startActivity(intent);
                }
            });
            dialog.show();
            return true;
        }

        return false;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_deals) {
            Intent intent = new Intent(this,Offer.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this,Wallet.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(this,Account.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_refer) {
            Intent intent = new Intent(this,ReferAndEarn.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.nav_help){
            startActivity(new Intent(this,feedback.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //other starts here
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private int ACCEPTED=10;
        private int REQUESTED=10;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView ;
            if(Empty()) {
                rootView = inflater.inflate(R.layout.fragment_deals,container,false);
                LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.list_layout);
                TextView textView = new TextView(getContext());
                textView.setText("Empty");
                layout.addView(textView);
            }
            else {
                Bundle args = getArguments();
                int count = args.getInt(ARG_SECTION_NUMBER);
                if (count == 1) {
                    rootView = inflater.inflate(R.layout.fragment_deals, container, false);
                    FragmentManager fragmentManager = getFragmentManager();
                    for (int i = 1; i <=REQUESTED; i++) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FillList fillList = FillList.newInstance(i);
                        fragmentTransaction.add(R.id.list_layout, fillList);
                        fragmentTransaction.commit();
                    }
                } else {
                    rootView = inflater.inflate(R.layout.fragment_deals2, container, false);
                    FragmentManager fragmentManager = getFragmentManager();
                    for (int i = 1; i <=ACCEPTED; i++) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Accepted_list accepted_list = Accepted_list.newInstance(i);
                        fragmentTransaction.add(R.id.list_layout2, accepted_list);
                        fragmentTransaction.commit();
                    }
                }
            }
            return rootView;
        }
    }

    private static boolean Empty(){
        return false;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Placed";
                case 1:
                    return "Accepted";
                // case 2:
                //   return "SECTION 3";
            }
            return null;
        }
    }

    public static class FillList extends Fragment{
        private int rank=0;
        private String PRODUCT = "Product Name";
        private int PRICE = 0;
        private String url;

        public FillList(){}

        public static FillList newInstance(int index) {
            FillList f = new FillList();
            // Supply index input as an argument.
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
            url = "http://img6a.flixcart.com/image/computer/x/g/u/lenovo-notebook-400x400-imaef6hcdq9hjnqt.jpeg";
            TextView textView = (TextView) view.findViewById(R.id.product_name);
            textView.setText(PRODUCT);
            textView = (TextView) view.findViewById(R.id.set_price);
            textView.setText("" + PRICE);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_requested);
            new DownloadImageTask(imageView).execute(url);
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
                    Log.v(TAG, "clicked " + v.getTag());
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
                    startActivity(intent);
                }
            });
        }

    }

    // Class for orders that have been accepted
    public static class Accepted_list extends Fragment{
        private int rank=0;
        private long PHONE=1234567890;
        private String PRODUCT= "Product Name";
        private int PRICE = 0;
        private int PRICE_OFFERED = 0;
        private double LATITUDE = 12.9923;
        private double LONGITUDE = 80.2368;

        public Accepted_list(){}



        public static Accepted_list newInstance(int index) {
            Accepted_list f = new Accepted_list();
            // Supply index input as an argument.
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
            View view = inflater.inflate(R.layout.list_accepted,container,false);
            String url = "http://img5a.flixcart.com/image/book/4/5/1/advantage-india-from-challenge-to-opportunity-400x400-imaec4hqddggjjs7.jpeg";

            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_accepted);
            new DownloadImageTask(imageView).execute(url);
            ZoomImage zoomImage = new ZoomImage(url);
            imageView.setOnClickListener(zoomImage);
            TextView textView = (TextView) view.findViewById(R.id.product_name_accepted);
            textView.setText(PRODUCT);
            textView = (TextView)view.findViewById(R.id.price_from_store);
            textView.setText(String.format("Price: Rs %d",PRICE));
            textView = (TextView) view.findViewById(R.id.price_offered);
            textView.setText(String.format("Price Offered: %d",PRICE_OFFERED));
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button button_call = (Button) view.findViewById(R.id.call);
            CharSequence tag = "button_call"+rank;
            button_call.setTag(tag);
            button_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONE));
                    startActivity(intent);
                }
            });
            Button button_location = (Button) view.findViewById(R.id.location);
            tag = "button_location"+rank;
            button_location.setTag(tag);
            button_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(String.format("geo:%f,%f?z=16&q=%f,%f", LATITUDE, LONGITUDE, LATITUDE, LONGITUDE));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }


        // this is to zoom image
        public class ZoomImage implements View.OnClickListener{
            String url;
            public ZoomImage(String img){
                url = img;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        }

    }
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
