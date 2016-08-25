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
            // TODO Auto-generated method stub _id INTEGER PRIMARY KEY
            db.execSQL("CREATE TABLE Requests( id VARCHAR PRIMARY KEY, url VARCHAR, pname VARCHAR, pprice VARCHAR , pimg VARCHAR,cat BIGINT,exptime BIGINT, cuscon INT DEFAULT 0,del INT DEFAULT 0)");
            db.execSQL("CREATE TABLE Quotes( id VARCHAR , qid VARCHAR PRIMARY KEY,name VARCHAR, qprice VARCHAR, type INT, deltype INT, comment VARCHAR ,lat DECIMAL(9,6),long DECIMAL(9,6),distance VARCHAR,bargained INT, bgprice VARCHAR DEFAULT '', bgexptime BIGINT DEFAULT 0,selcon INT DEFAULT 0,cuscon INT DEFAULT 0,del INT DEFAULT 0 )");
            db.execSQL("CREATE TABLE" + Notification.TABLE_NAME + "(notificationId VARCHAR PRIMARY KEY, heading VARCHAR, description VARCHAR, imageURL VARCHAR, time BIGINT DEFAULT 0)");
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
        Log.d("ADD_REQUEST","ADD_REQUEST");
        close();
        return id;
    }

    public ArrayList<Request> getAllRequests () {
        open();
        String[] columns = Request.columns;
        Cursor c = ourDatabase.query(Request.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Request> arrayList = Request.getArrayList(c);
        close();
        return arrayList;
    }
    public ArrayList<Request> getRequest (String id) {
        open();
        String[] columns = Request.columns;
        Cursor c = ourDatabase.query(Request.TABLE_NAME, columns, "id = ?" ,new String[]{id}, null, null, null);
        ArrayList<Request> arrayList = Request.getArrayList(c);
        close();
        return arrayList;
    }

    public long addQuote(ContentValues cv) {
        open();
        long id = ourDatabase.insert(Quote.TABLE_NAME, null, cv);
        Log.d("dmydb","QUOTE ADDED");
        close();
        return id;
    }
    public void updateQuote(String id,ContentValues cv){
        open();
        ourDatabase.update(Quote.TABLE_NAME, cv, "id" + " = ?", new String[]{id});
        close();

    }
    public ArrayList<Quote> getAllQuotes (String productId) {
        open();
        String[] columns = Quote.columns;
        Cursor c = ourDatabase.query(Quote.TABLE_NAME, columns,"productId = ?" ,new String[]{productId}, null, null, null);
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