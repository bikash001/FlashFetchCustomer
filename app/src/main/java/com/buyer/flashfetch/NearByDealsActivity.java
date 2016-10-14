package com.buyer.flashfetch;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.buyer.flashfetch.Adapters.NearByDealsViewPagerAdapter;
import com.buyer.flashfetch.Animations.ZoomOutPageTransformer;
import com.buyer.flashfetch.CommonUtils.Toasts;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Constants.IEventConstants;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.IEvent;
import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.Objects.UserProfile;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class NearByDealsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NearByDealsActivity";

    private int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    String[] tabTitles = {"SHOPPING", "FOOD", "SERVICES"};

    public static int TAB_SHOPPING = 1000;
    public static int TAB_FOOD = 1001;
    public static int TAB_SERVICES = 1002;
//    public static int TAB_ENTERTAINMENT = 1003;
//    public static int TAB_TRENDING = 1004;

    private ProgressDialog progressDialog;
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NearByDealsViewPagerAdapter dealsViewPagerAdapter;
    private ArrayList<NearByDealsDataModel> deals;
    private ArrayList<String> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = NearByDealsActivity.this;

        setContentView(R.layout.deals_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nearby_deals_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("NearBy Deals");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        progressDialog = getProgressDialog(context);

        getContacts();
        setUpData();



        tabLayout = (TabLayout) findViewById(R.id.deal_nearby_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.deals_nearby_view_pager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.deals_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.closeDrawers();
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.deals_navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(NearByDealsActivity.this);
        }

        if (tabLayout != null) {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        dealsViewPagerAdapter = new NearByDealsViewPagerAdapter(getSupportFragmentManager(), tabTitles);

        if (viewPager != null) {
            viewPager.setAdapter(dealsViewPagerAdapter);
            viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
            tabLayout.setupWithViewPager(viewPager);
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

        } else if (id == R.id.action_contact) {

            Intent intent = new Intent(this, ContactUs.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_connect) {

            Dialog dialog = new Dialog(this);
            dialog.setTitle("Connect with Us");
            dialog.setContentView(R.layout.dialog_connect);

            LinearLayout fb = (LinearLayout) dialog.findViewById(R.id.fb);
            LinearLayout twitter = (LinearLayout) dialog.findViewById(R.id.twitter);
            LinearLayout whatsapp = (LinearLayout) dialog.findViewById(R.id.whatsapp);

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

        if (id == R.id.nav_account) {
            Intent intent = new Intent(this, AccountInfoActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_notification) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_refer) {
            Intent intent = new Intent(this, ReferAndEarn.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, FeedbackActivity.class));
        }

        return true;
    }

    private void setUpData() {
        if (Utils.isInternetAvailable(context)) {
            progressDialog.show();

            ServiceManager.callFetchDealsService(context, new UIListener() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(context);
                }

                @Override
                public void onFailure(int result) {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(context);
                }

                @Override
                public void onConnectionError() {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(context);
                }

                @Override
                public void onCancelled() {
                    progressDialog.dismiss();
                    Toasts.serverBusyToast(context);
                }
            });

        } else {
            Toasts.internetUnavailableToast(context);
        }
    }

    private void getContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            if (Utils.isInternetAvailable(context)) {

                ServiceManager.callUploadContactsService(context, UserProfile.getPhone(context), contactsList, new UIListener() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure() {
                    }

                    @Override
                    public void onFailure(int result) {
                    }

                    @Override
                    public void onConnectionError() {
                    }

                    @Override
                    public void onCancelled() {
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactsList = retrieveContacts();
            }
        }
    }

    private ArrayList<String> retrieveContacts() {

        ArrayList<String> phoneNumberList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    while (phoneCursor.moveToNext()) {
                        String contactNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneNumberList.add(contactNumber);
                    }
                    phoneCursor.close();
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return phoneNumberList;
    }
}
