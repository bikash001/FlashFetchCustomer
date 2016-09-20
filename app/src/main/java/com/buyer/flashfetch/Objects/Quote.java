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
    public String sellerId;
    public String distance;
    public long productCategory;
    public long bargainExpTime;
    public int bargainPrice;
    public int quotePrice;
    public int productType;
    public int noOfBargains;
    public int buyerDeliveryType;
    public int sellerDeliveryType;
    public boolean sellerConfirmation;
    public boolean customerConfirmation;
    public boolean bargained;
    public String latitude;
    public String longitude;

    public static final String QUOTE_ID = "quoteId";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_CATEGORY = "productCategory";
    public static final String PRODUCT_TYPE = "productType";
    public static final String QUOTE_PRICE = "quotePrice";
    public static final String SELLER_DELIVERY_TYPE = "sellerDeliveryType";
    public static final String BUYER_DELIVERY_TYPE = "buyerDeliveryType";
    public static final String COMMENTS = "comments";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String BARGAINED = "bargained";
    public static final String BARGAIN_PRICE = "bargainPrice";
    public static final String BARGAIN_EXP_TIME = "bargainExpTime";
    public static final String NUMBER_OF_BARGAINS = "noOfBargains";
    public static final String SELLER_CONFIRMATION = "sellerConfirmation";
    public static final String BUYER_CONFIRMATION = "customerConfirmation";
    public static final String SELLER_ID = "sellerId";
    public static final String DISTANCE = "distance";

    public static String[] columns = {"quoteId","productId","productName","productCategory","productType","quotePrice","sellerDeliveryType",
            "buyerDeliveryType","comments","latitude","longitude","bargained","bargainPrice","bargainExpTime","noOfBargains",
            "sellerConfirmation","customerConfirmation","sellerId","distance"};

    public Quote(String quoteId, String productId, String productName, long productCategory, int productType,int quotePrice,
                    String sellerName, int sellerDeliveryType, int buyerDeliveryType, String comments, String latitude,
                        String longitude, boolean bargained, int bargainPrice, long bargainExpTime,int noOfBargains,
                            boolean sellerConfirmation, boolean customerConfirmation, String sellerId, String distance) {
        this.quoteId = quoteId;
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productType = productType;
        this.quotePrice = quotePrice;
        this.sellerName = sellerName;
        this.sellerDeliveryType = sellerDeliveryType;
        this.buyerDeliveryType = buyerDeliveryType;
        this.comments = comments;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bargained = bargained;
        this.bargainPrice = bargainPrice;
        this.bargainExpTime = bargainExpTime;
        this.noOfBargains = noOfBargains;
        this.sellerConfirmation = sellerConfirmation;
        this.customerConfirmation = customerConfirmation;
        this.sellerId = sellerId;
        this.distance = distance;
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
        Quote quote = new Quote(String.valueOf(c.getDouble(0)), c.getString(1), c.getString(2),c.getLong(3), c.getInt(4),c.getInt(5),c.getString(6),
                                    c.getInt(7),c.getInt(8),c.getString(9), c.getString(10),c.getString(11),(c.getInt(12) == 1),
                                        c.getInt(13),c.getInt(14),c.getInt(15),(c.getInt(16) == 1),(c.getInt(17) == 1),
                                            c.getString(18),c.getString(19));
        return quote;
    }
}
