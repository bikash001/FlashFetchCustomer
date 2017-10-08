package com.buyer.flashfetch;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyer.flashfetch.Adapters.NearByDealsViewPagerAdapter;
import com.buyer.flashfetch.Animations.ZoomOutPageTransformer;
import com.buyer.flashfetch.BroadcastReceivers.RegistrationReceiver;
import com.buyer.flashfetch.CommonUtils.Utils;
import com.buyer.flashfetch.Constants.Constants;
import com.buyer.flashfetch.Constants.GCMTaskServiceConstants;
import com.buyer.flashfetch.Constants.NearByDealsConstants;
import com.buyer.flashfetch.Constants.RegistrationConstants;
import com.buyer.flashfetch.Helper.DialogManager;
import com.buyer.flashfetch.Interfaces.UIListener;
import com.buyer.flashfetch.Network.ServiceManager;
import com.buyer.flashfetch.Objects.Notification;
import com.buyer.flashfetch.Objects.UserProfile;
import com.buyer.flashfetch.Services.CustomService;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.util.ArrayList;

import static com.google.android.gms.gcm.Task.NETWORK_STATE_CONNECTED;

public class NearByDealsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = NearByDealsActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 99;

    private static final int REGISTRATION_RECEIVER_REQUEST_CODE = PERMISSIONS_REQUEST_READ_CONTACTS + 1;

    String[] tabTitles = {NearByDealsConstants.TAB_SHOPPING, NearByDealsConstants.TAB_FOOD, NearByDealsConstants.TAB_SERVICES};

    private GcmNetworkManager gcmNetworkManager;

    private ProgressDialog progressDialog;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private NearByDealsViewPagerAdapter dealsViewPagerAdapter;
    private ArrayList<String> contactsList = new ArrayList<>();
    private int numberOfVisits;
    private AlertDialog feedbackAlertDialog;
    private TextView buyerName, buyerPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.startPlayServices(this);

        setContentView(R.layout.deals_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nearby_deals_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("NearBy Deals");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        gcmNetworkManager = GcmNetworkManager.getInstance(this);

        if(Utils.checkPlayServices(this)){
            scheduleTasks();
        }else {
            setUpData();
        }


        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getBoolean(RegistrationConstants.FROM_REGISTRATION_FLOW)) {
            Intent intent = new Intent(this, RegistrationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REGISTRATION_RECEIVER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +  5*60*1000, pendingIntent);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        progressDialog = getProgressDialog(this);

        if(!UserProfile.isContactsRetrieved(this)){
            getContacts();
        }

        setUpData();

        tabLayout = (TabLayout) findViewById(R.id.deal_nearby_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.deals_nearby_view_pager);

        drawer = (DrawerLayout) findViewById(R.id.deals_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.closeDrawers();
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.deals_navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(NearByDealsActivity.this);
            buyerName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_person_name);
            buyerPhone = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navigation_person_phone);
        }

        buyerName.setText(UserProfile.getName(this));
        buyerPhone.setText(UserProfile.getPhone(this));

        if (tabLayout != null) {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        dealsViewPagerAdapter = new NearByDealsViewPagerAdapter(getSupportFragmentManager(), tabTitles);
    }

    private void scheduleTasks(){
        Task task = new PeriodicTask.Builder()
                .setService(CustomService.class)
                .setPeriod(60*1000)
                .setFlex(30)
                .setPersisted(true)
                .setTag(GCMTaskServiceConstants.FETCH_DEALS_TASK)
                .setRequiredNetwork(NETWORK_STATE_CONNECTED)
                .build();

        if(gcmNetworkManager != null) {
            gcmNetworkManager.schedule(task);
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

        closeNavigationDrawer();

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
//            startActivity(new Intent(context, FeedbackActivity.class));
            DialogManager.showFeedbackDialog(this);
            return true;

        } else if (id == R.id.nav_rate_us) {

            showRatingPopup();
            return true;

        } else if (id == R.id.nav_contact_us) {
            DialogManager.showContactDialog(this);
            return true;
        }

        return true;
    }

    private void setUpData() {
        if (Utils.isInternetAvailable(this)) {
            progressDialog.show();

            ServiceManager.callFetchDealsService(this, new UIListener() {
                @Override
                public void onSuccess() {

                    numberOfVisits = UserProfile.getVisits(NearByDealsActivity.this);
                    numberOfVisits++;
                    UserProfile.setVisits(numberOfVisits, NearByDealsActivity.this);

                    progressDialog.dismiss();
                    if (viewPager != null) {
                        viewPager.setAdapter(dealsViewPagerAdapter);
                        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
                        tabLayout.setupWithViewPager(viewPager);
                    }

                    if (UserProfile.getVisits(NearByDealsActivity.this) == 7) {
                        showRatingPopup();
                    }
                }

                @Override
                public void onFailure() {
                    if(Notification.getAllNotifications(NearByDealsActivity.this) != null && Notification.getAllNotifications(NearByDealsActivity.this).size() > 0){
                        if (viewPager != null) {
                            viewPager.setAdapter(dealsViewPagerAdapter);
                            viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    }
                    progressDialog.dismiss();
                    DialogManager.showAlertDialog(NearByDealsActivity.this);
                }

                @Override
                public void onFailure(int result) {
//                    progressDialog.dismiss();
//                    DialogManager.showAlertDialog(NearByDealsActivity.this);
                }

                @Override
                public void onConnectionError() {
                    progressDialog.dismiss();
                    DialogManager.showAlertDialog(NearByDealsActivity.this);
                }

                @Override
                public void onCancelled() {
//                    progressDialog.dismiss();
//                    DialogManager.showAlertDialog(NearByDealsActivity.this);
                }
            });

        } else {
            if(Notification.getAllNotifications(this) != null && Notification.getAllNotifications(this).size() > 0){
                if (viewPager != null) {
                    viewPager.setAdapter(dealsViewPagerAdapter);
                    viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
                    tabLayout.setupWithViewPager(viewPager);
                }
            }
            Toast.makeText(this, "Connect to internet to avail latest deals", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeNavigationDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void showRatingPopup(){
        View view = getLayoutInflater().inflate(R.layout.experience_layout, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(NearByDealsActivity.this);
        builder.setView(view);

        final LinearLayout experienceLayout = (LinearLayout) view.findViewById(R.id.experience_layout);
        final LinearLayout experienceSadLayout = (LinearLayout) view.findViewById(R.id.experience_sad_layout);
        final LinearLayout experienceHappyLayout = (LinearLayout) view.findViewById(R.id.experience_happy_layout);

        final TextView ratingTitle = (TextView) view.findViewById(R.id.rating_title);
        final TextView ratingDescription = (TextView) view.findViewById(R.id.rating_description);

        ImageView imageView = (ImageView) view.findViewById(R.id.experience_clear_button);
        ImageView sadImageView = (ImageView) view.findViewById(R.id.experience_sad);
        ImageView happyImageView = (ImageView) view.findViewById(R.id.experience_happy);

        final EditText feedback = (EditText) view.findViewById(R.id.rating_edit_text_feedback);
        final TextView feedbackNo = (TextView) view.findViewById(R.id.feedback_cancel);
        TextView feedbackYes = (TextView) view.findViewById(R.id.feedback_send);

        TextView ratingCancel = (TextView) view.findViewById(R.id.rating_cancel);
        TextView ratingSend = (TextView) view.findViewById(R.id.rating_send);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackAlertDialog.dismiss();
            }
        });

        sadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                experienceLayout.setVisibility(View.GONE);
                experienceSadLayout.setVisibility(View.VISIBLE);
                ratingTitle.setText("Help us do better!");
                ratingDescription.setText("Mind giving us a quick feedback ?");
            }
        });

        happyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                experienceLayout.setVisibility(View.GONE);
                experienceHappyLayout.setVisibility(View.VISIBLE);
                ratingTitle.setText("Rate App");
                ratingDescription.setText("Mind giving us 5 star on Google Play ?");
            }
        });

        feedbackNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackAlertDialog.dismiss();
                Toast.makeText(NearByDealsActivity.this, "No worries. We will work harder", Toast.LENGTH_SHORT).show();
            }
        });

        feedbackYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackAlertDialog.dismiss();
                Toast.makeText(NearByDealsActivity.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(feedback.getText())) {
                    //TODO: need to integrate service call for feedback
                }
            }
        });

        ratingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackAlertDialog.dismiss();
                Toast.makeText(NearByDealsActivity.this, "No worries. We will work harder", Toast.LENGTH_SHORT).show();
            }
        });

        ratingSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackAlertDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_PLAY_STORE_URL));
                NearByDealsActivity.this.startActivity(intent);
            }
        });

        feedbackAlertDialog = builder.create();
        feedbackAlertDialog.show();
    }

    private void getContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            if (Utils.isInternetAvailable(this)) {

                ServiceManager.callUploadContactsService(this, UserProfile.getPhone(this), contactsList, new UIListener() {
                    @Override
                    public void onSuccess() {
                        UserProfile.setContactsRetrieved(true, NearByDealsActivity.this);
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
