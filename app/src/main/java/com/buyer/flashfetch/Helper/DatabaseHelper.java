package com.buyer.flashfetch.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.buyer.flashfetch.Objects.NearByDealsDataModel;
import com.buyer.flashfetch.Objects.Notification;
import com.buyer.flashfetch.Objects.Quote;
import com.buyer.flashfetch.Objects.Request;

import java.util.ArrayList;

public class DatabaseHelper {

    private static String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "flash_fetch_buyer";
    private static final int DATABASE_VERSION = 1;

    private DbHelper dbHelper;
    private final Context context;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("CREATE TABLE " +
//                    Request.TABLE_NAME + "(" +
//                    Request.PRODUCT_ID + " VARCHAR PRIMARY KEY," + Request.PRODUCT_NAME + " VARCHAR ," +
//                    Request.PRODUCT_PRICE + " VARCHAR ," + Request.PRODUCT_IMAGE_URL+ " VARCHAR ," + Request.PRODUCT_CATEGORY + " BIGINT ," +
//                    Request.PRODUCT_REQUEST_EXP_TIME + " BIGINT DEFAULT 0 ," + Request.PRODUCT_DELIVERY_TYPE + " INT DEFAULT -1 ," +
//                    Request.PRODUCT_DELIVERY_LATITUDE + " VARCHAR ," + Request.PRODUCT_DELIVERY_LONGITUDE + " VARCHAR )");
//            Log.d("REQUEST", "DATABASE CREATED");
//
//            db.execSQL("CREATE TABLE " +
//                    Quote.TABLE_NAME + "(" +
//                    Quote.QUOTE_ID + " VARCHAR PRIMARY KEY," + Quote.PRODUCT_ID + " VARCHAR," + Quote.PRODUCT_NAME + " VARCHAR," +
//                    Quote.PRODUCT_CATEGORY + " BIGINT," + Quote.PRODUCT_TYPE + " INT," + Quote.QUOTE_PRICE + " INT," +
//                    Quote.SELLER_DELIVERY_TYPE + " INT DEFAULT -1," + Quote.BUYER_DELIVERY_TYPE + " INT DEFAULT -1," +
//                    Quote.COMMENTS + " VARCHAR," + Quote.LATITUDE + " VARCHAR," + Quote.LONGITUDE + " VARCHAR," +
//                    Quote.BARGAINED + " INT DEFAULT -1," + Quote.BARGAIN_PRICE + " INT," + Quote.BARGAIN_EXP_TIME + "BIGINT DEFAULT 0," +
//                    Quote.NUMBER_OF_BARGAINS + " INT DEFAULT 0," + Quote.SELLER_CONFIRMATION + " INT DEFAULT -1," +
//                    Quote.BUYER_CONFIRMATION + "INT DEFAULT -1," + Quote.SELLER_ID + " INT," + Quote.DISTANCE +" VARCHAR)");
//            Log.d("QUOTE", "DATABASE CREATED");

            db.execSQL("CREATE TABLE " + Notification.TABLE_NAME + "(" +
                    Notification.NOTIFICATION_ID + " INT NOT NULL UNIQUE," + Notification.NOTIFICATION_HEADING + " VARCHAR," +
                    Notification.NOTIFICATION_DESCRIPTION + " VARCHAR," + Notification.NOTIFICATION_IMAGE_URL + " VARCHAR," +
                    Notification.NOTIFICATION_EXP_TIME + " BIGINT DEFAULT 0" + ")");

            db.execSQL("CREATE TABLE " + NearByDealsDataModel.DEAL_TABLE_NAME + "(" +
                    NearByDealsDataModel.DEALS_ID + " VARCHAR NOT NULL UNIQUE," +
                    NearByDealsDataModel.DEAL_TYPE + " INT DEFAULT 0," +
                    NearByDealsDataModel.DEAL_CATEGORY + " INT DEFAULT 0," +
                    NearByDealsDataModel.IMAGE_URL + " VARCHAR," +
                    NearByDealsDataModel.DEAL_HEADING + " VARCHAR," +
                    NearByDealsDataModel.DEAL_DESCRIPTION + " VARCHAR," +
                    NearByDealsDataModel.DEAL_VALID_UPTO + " VARCHAR," +
                    NearByDealsDataModel.HOW_TO_AVAIL_DEAL + " VARCHAR," +
                    NearByDealsDataModel.ACTIVATED + " INT DEFAULT -1," +
                    NearByDealsDataModel.VOUCHER_ID + " VARCHAR," +
                    NearByDealsDataModel.SHOP_NAME + " VARCHAR," +
                    NearByDealsDataModel.SHOP_PHONE + " VARCHAR," +
                    NearByDealsDataModel.SHOP_LOCATION + " VARCHAR," +
                    NearByDealsDataModel.SHOP_ADDRESS + " VARCHAR," +
                    NearByDealsDataModel.SHOP_LATITUDE + " VARCHAR," +
                    NearByDealsDataModel.SHOP_LONGITUDE + " VARCHAR," +
                    NearByDealsDataModel.DEAL_WEIGHTAGE + " INT DEFAULT 1000" + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL(" DROP TABLE IF EXISTS " + Notification.TABLE_NAME);
            db.execSQL(" DROP TABLE IF EXISTS " + NearByDealsDataModel.DEAL_TABLE_NAME);
            onCreate(db);
        }
    }

    public DatabaseHelper(Context c){
        context = c;
    }

    public DatabaseHelper open(){
        if(dbHelper == null){
            dbHelper = new DbHelper (context);
        }
        ourDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(ourDatabase.isOpen()){
            dbHelper.close();
        }
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

    public void addDeal(ContentValues contentValues){
        if(getAllDeals() != null && getAllDeals().size() > 0){
            if(getDeal(contentValues.getAsString(NearByDealsDataModel.DEALS_ID)) != null && getDeal(contentValues.getAsString(NearByDealsDataModel.DEALS_ID)).size() > 0  && getDeal(contentValues.getAsString(NearByDealsDataModel.DEALS_ID)).get(0).getDealId().equalsIgnoreCase(contentValues.getAsString(NearByDealsDataModel.DEALS_ID))){
                updateDeal(contentValues, contentValues.getAsString(NearByDealsDataModel.DEALS_ID));
            }else{
                open();
                long id = ourDatabase.insert(NearByDealsDataModel.DEAL_TABLE_NAME, null, contentValues);
            }
        }else{
            open();
            long id = ourDatabase.insert(NearByDealsDataModel.DEAL_TABLE_NAME, null, contentValues);
        }
        close();
    }

    public void updateDeal(ContentValues contentValues, String dealId){
        open();
        long id = ourDatabase.update(NearByDealsDataModel.DEAL_TABLE_NAME,contentValues,NearByDealsDataModel.DEALS_ID + " = ? ", new String[]{dealId});
        close();
    }

    public ArrayList<NearByDealsDataModel> getAllDeals(){
        open();
        String[] columns = NearByDealsDataModel.DEALS_COLUMN_NAMES;
        Cursor cursor = ourDatabase.query(NearByDealsDataModel.DEAL_TABLE_NAME,columns,null,null,null,null, NearByDealsDataModel.DEAL_WEIGHTAGE + " DESC");
        ArrayList<NearByDealsDataModel> arrayList = NearByDealsDataModel.getArrayList(cursor);
        close();
        return arrayList;
    }

    public ArrayList<NearByDealsDataModel> getDeals(String dealCategory){
        open();
        String[] columns = NearByDealsDataModel.DEALS_COLUMN_NAMES;
        Cursor cursor = ourDatabase.query(NearByDealsDataModel.DEAL_TABLE_NAME,columns,NearByDealsDataModel.DEAL_CATEGORY + " = ? ", new String[]{dealCategory},null,null, NearByDealsDataModel.DEAL_WEIGHTAGE + " DESC");
        ArrayList<NearByDealsDataModel> arrayList = NearByDealsDataModel.getArrayList(cursor);
        close();
        return arrayList;
    }

    public ArrayList<NearByDealsDataModel> getDeal(String dealId){
        open();
        Cursor cursor = ourDatabase.query(NearByDealsDataModel.DEAL_TABLE_NAME, NearByDealsDataModel.DEALS_COLUMN_NAMES, NearByDealsDataModel.DEALS_ID + " = ?",new String[]{dealId},null, null, NearByDealsDataModel.DEAL_WEIGHTAGE + " DESC");
        ArrayList<NearByDealsDataModel> nearByDealsDataModel = NearByDealsDataModel.getArrayList(cursor);
        close();
        return nearByDealsDataModel;
    }
}