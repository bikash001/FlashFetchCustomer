package com.buyer.flashfetch.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.buyer.flashfetch.Objects.Notification;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.Request;

import java.util.ArrayList;

public class DatabaseHelper {

    private static String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "FlashFetchBuyer";
    private static final int DATABASE_VERSION = 1;

    private DbHelper dbHelper;
    private final Context context;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " +
                    Request.TABLE_NAME + " ( " +
                    Request.PRODUCT_ID + " VARCHAR PRIMARY KEY," + Request.PRODUCT_NAME + " VARCHAR ," +
                    Request.PRODUCT_PRICE + " VARCHAR ," + Request.PRODUCT_IMAGE_URL+ " VARCHAR ," + Request.PRODUCT_CATEGORY + " BIGINT ," +
                    Request.PRODUCT_REQUEST_EXP_TIME + " BIGINT DEFAULT 0 ," + Request.PRODUCT_DELIVERY_TYPE + " INT DEFAULT -1 ," +
                    Request.PRODUCT_DELIVERY_LATITUDE + " VARCHAR ," + Request.PRODUCT_DELIVERY_LONGITUDE + " VARCHAR )");

            db.execSQL("CREATE TABLE " +
                    Quote.TABLE_NAME + " ( " +
                    Quote.QUOTE_ID + " VARCHAR PRIMARY KEY ," + Quote.PRODUCT_ID + " VARCHAR ," + Quote.PRODUCT_NAME + " VARCHAR ," +
                    Quote.PRODUCT_CATEGORY + " BIGINT ," + Quote.PRODUCT_TYPE + " INT ," + Quote.QUOTE_PRICE + " INT ," +
                    Quote.SELLER_DELIVERY_TYPE + " INT DEFAULT -1 ," + Quote.BUYER_DELIVERY_TYPE + " INT DEFAULT -1 ," +
                    Quote.COMMENTS + " VARCHAR ," + Quote.LATITUDE + " VARCHAR ," + Quote.LONGITUDE + " VARCHAR ," +
                    Quote.BARGAINED + " INT DEFAULT -1 ," + Quote.BARGAIN_PRICE + " INT ," + Quote.BARGAIN_EXP_TIME + "BIGINT DEFAULT 0 ," +
                    Quote.NUMBER_OF_BARGAINS + " INT DEFAULT 0 ," + Quote.SELLER_CONFIRMATION + " INT DEFAULT -1 ," +
                    Quote.BUYER_CONFIRMATION + "INT DEFAULT -1 ," + Quote.SELLER_ID + " INT ," + Quote.DISTANCE +" VARCHAR )");

            db.execSQL("CREATE TABLE " + Notification.TABLE_NAME + " ( " +
                    Notification.NOTIFICATION_ID + " INT PRIMARY KEY ," + Notification.NOTIFICATION_HEADING + " VARCHAR ," +
                    Notification.NOTIFICATION_DESCRIPTION + " VARCHAR ," + Notification.NOTIFICATION_IMAGE_URL + " VARCHAR ," +
                    Notification.NOTIFICATION_EXP_TIME + " BIGINT DEFAULT 0 )");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    public DatabaseHelper(Context c){
        context = c;
    }

    public DatabaseHelper open(){
        dbHelper = new DbHelper (context);
        ourDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long addRequest(ContentValues cv) {
        open();
        long id = ourDatabase.insert(Request.TABLE_NAME, null, cv);
        close();
        return id;
    }

    public ArrayList<Request> getRequest (String productId) {
        open();
        String[] columns = Request.columns;
        Cursor c = ourDatabase.query(Request.TABLE_NAME, columns, "id = ?" ,new String[]{productId}, null, null, null);
        ArrayList<Request> arrayList = Request.getArrayList(c);
        close();
        return arrayList;
    }

    public ArrayList<Request> getAllRequests () {
        open();
        String[] columns = Request.columns;
        Cursor c = ourDatabase.query(Request.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Request> arrayList = Request.getArrayList(c);
        close();
        return arrayList;
    }

    public long addQuote(ContentValues cv) {
        open();
        long id = ourDatabase.insert(Quote.TABLE_NAME, null, cv);
        close();
        return id;
    }

    public void updateQuote(String quoteId,ContentValues cv){
        open();
        ourDatabase.update(Quote.TABLE_NAME, cv, Quote.QUOTE_ID + " = ?", new String[]{quoteId});
        close();

    }
    public ArrayList<Quote> getAllQuotes (String productId) {
        open();
        String[] columns = Quote.columns;
        Cursor c = ourDatabase.query(Quote.TABLE_NAME, columns, Quote.PRODUCT_ID + " = ?" ,new String[]{productId}, null, null, null);
        ArrayList<Quote> arrayList = Quote.getArrayList(c);
        close();
        return arrayList;
    }

    public long addNotification(ContentValues cv) {
        open();
        long id = ourDatabase.insert(Notification.TABLE_NAME, null, cv);
        close();
        return id;
    }

    public ArrayList<Notification> getAllNotifications(){
        open();
        String[] columns = Notification.columns;
        Cursor c = ourDatabase.query(Notification.TABLE_NAME, columns, null, null, null, null, "time DESC");
        ArrayList<Notification> arrayList = Notification.getArrayList(c);
        close();
        return arrayList;
    }


}