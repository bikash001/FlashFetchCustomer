package com.buyer.flashfetch;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buyer.flashfetch.Adapters.RequestsPagerAdapter;
import com.buyer.flashfetch.Animations.ZoomOutPageTransformer;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Objects.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    public static final String TAG = "MainActivity";
    private Context context;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TextView personName, personEmail;
    private int numberOfVisits;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = MainActivity.this;

        Utils.startPlayServices(this);

        numberOfVisits = UserProfile.getVisits(context);
        numberOfVisits++;
        UserProfile.setVisits(numberOfVisits,context);

        setContentView(R.layout.activity_main2);

        getContacts();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("FlashFetch");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.closeDrawers();
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        personName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_person_name);
        personEmail = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_person_email);

        personName.setText(UserProfile.getName(context));
        personEmail.setText(UserProfile.getEmail(context));

        RequestsPagerAdapter requestsPagerAdapter = new RequestsPagerAdapter(getSupportFragmentManager());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        if (mViewPager != null) {
            mViewPager.setAdapter(requestsPagerAdapter);
            mViewPager.setPageTransformer(false,new ZoomOutPageTransformer());
            tabLayout.setupWithViewPager(mViewPager);
        }

        if(numberOfVisits == 5){

            View view = getLayoutInflater().inflate(R.layout.experience_layout,null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view);

            ImageView imageView = (ImageView)view.findViewById(R.id.experience_clear_button);
            ImageView sadImageView = (ImageView)view.findViewById(R.id.experience_sad);
            ImageView neutralImageView = (ImageView)view.findViewById(R.id.experience_neutral);
            ImageView happyImageView = (ImageView)view.findViewById(R.id.experience_happy);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            sadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            neutralImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            happyImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
        }
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
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Utils.doLogout(this);
            return true;

        } else if (id == R.id.action_contact){

            Intent intent = new Intent(this,ContactUs.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_connect) {

            Dialog dialog = new Dialog(this);
            dialog.setTitle("Connect with Us");
            dialog.setContentView(R.layout.dialog_connect);

            LinearLayout fb = (LinearLayout)dialog.findViewById(R.id.fb);
            LinearLayout twitter = (LinearLayout)dialog.findViewById(R.id.twitter);
            LinearLayout whatsapp = (LinearLayout)dialog.findViewById(R.id.whatsapp);

            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FACEBOOK_URL));
                    startActivity(browserIntent);
                }
            });

            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TWITTER_URL));
                    startActivity(browserIntent);
                }
            });

            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, Constants.CONTACT_US)
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_deals) {
            Intent intent = new Intent(this,DealsNearByActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this,RewardsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(this,AccountInfoActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(this,NotificationsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_refer) {
            Intent intent = new Intent(this,ReferAndEarn.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.nav_help){
            startActivity(new Intent(this,FeedbackActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getContacts() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
        }else{
            //TODO:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                retrieveContacts();
            }
        }
    }

    private List<String> retrieveContacts() {

        ArrayList<String> phoneNumberList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if(cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);

                    while (phoneCursor.moveToNext()){
                        String contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneNumberList.add(contactNumber);
                    }
                    phoneCursor.close();
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        return phoneNumberList;
    }
}
