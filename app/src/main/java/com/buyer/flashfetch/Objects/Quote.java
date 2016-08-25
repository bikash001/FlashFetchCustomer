package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.buyer.flashfetch.Helper.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Quote implements Serializable{

    public static final String TABLE_NAME = "Quotes" ;

    public String productId;
    public String quoteId;
    public String productName;
    public String comments;
    public String sellerName;
    public long productCategory;
    public long bargainExpTime;
    public int bargainPrice;
    public int quotePrice;
    public int productType;
    public int buyerDeliveryType;
    public int sellerDeliveryOptions;
    public boolean sellerConfirmation;
    public boolean customerConfirmation;
    public boolean bargained;
    public float latitude;
    public float longitude;

    public static String[] columns = {"quoteId","productId","productName","productCategory","productType","quotePrice","sellerDeliveryOptions",
            "buyerDeliveryType","comments","latitude","longitude","bargained","bargainPrice","bargainExpTime",
            "sellerConfirmation","customerConfirmation"};

    public Quote(String quoteId, String productId, String productName, long productCategory, int productType,int quotePrice, String sellerName, int sellerDeliveryOptions,
                    int buyerDeliveryType, String comments, float latitude, float longitude,
                        boolean bargained, int bargainPrice, long bargainExpTime, boolean sellerConfirmation, boolean customerConfirmation) {
        this.quoteId = quoteId;
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productType = productType;
        this.quotePrice = quotePrice;
        this.sellerName = sellerName;
        this.sellerDeliveryOptions = sellerDeliveryOptions;
        this.buyerDeliveryType = buyerDeliveryType;
        this.comments = comments;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bargained = bargained;
        this.bargainPrice = bargainPrice;
        this.bargainExpTime = bargainExpTime;
        this.sellerConfirmation = sellerConfirmation;
        this.customerConfirmation = customerConfirmation;
    }

    public static ArrayList<Quote> getArrayList(Cursor c) {
        ArrayList<Quote> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseEvent(c));
        }
        return arrayList;
    }

    public static ArrayList<Quote> getAllQuotes(Context context, String productId){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getAllQuotes(productId);
    }

    public static Quote parseEvent(Cursor c) {
        Quote quote = new Quote(c.getString(0), c.getString(1), c.getString(2),c.getLong(3), c.getInt(4),c.getInt(5),c.getString(6),c.getInt(7),c.getInt(8),c.getString(9), c.getFloat(10),c.getFloat(11),(c.getInt(12) == 1),c.getInt(13),c.getInt(14),(c.getInt(15) == 1),(c.getInt(16) == 1));
        return quote;
    }
}
