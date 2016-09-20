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

public class Request implements Serializable{

    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_NAME = "productName";
    public static final String PRODUCT_PRICE = "productPrice";
    public static final String PRODUCT_IMAGE_URL = "imageURL";
    public static final String PRODUCT_CATEGORY = "productCategory";
    public static final String PRODUCT_DELIVERY_TYPE = "buyerDeliveryType";
    public static final String PRODUCT_REQUEST_EXP_TIME = "expiryTime";
    public static final String PRODUCT_DELIVERY_LATITUDE = "deliveryLatitude";
    public static final String PRODUCT_DELIVERY_LONGITUDE = "deliveryLongitude";

    public static final String TABLE_NAME = "Requests" ;
    public static String[] columns = {"productId", "productName", "productPrice","imageURL","productCategory","expiryTime","buyerDeliveryType","deliveryLatitude","deliveryLongitude"};

    public String productName, productPrice,imageURL,productId, buyerDeliveryType, deliveryLatitude, deliveryLongitude;
    public long expiryTime;
    public long productCategory;

    //TODO: need to set this after getting params from the response
    public Request(JSONObject request) {
        try {
            this.productId = request.getString("product_id");
            this.productName = request.getString("product_name");
            this.productPrice = request.getString("product_price");
            this.productCategory = request.getInt("product_category");
            this.expiryTime = request.getLong("exp_time");
            this.imageURL = request.getString("image_url");
            this.buyerDeliveryType = request.getString("delivery_type");
            this.deliveryLatitude = String.valueOf(request.getDouble("latitude"));
            this.deliveryLongitude = String.valueOf(request.getDouble("longitude"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Request(String productId, String imageURL , String productName, String productPrice, long productCategory, long expiryTime,
                        String buyerDeliveryType, String deliveryLatitude, String deliveryLongitude) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.expiryTime = expiryTime;
        this.imageURL = imageURL;
        this.buyerDeliveryType = buyerDeliveryType;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryLongitude = deliveryLongitude;
    }

    public static ArrayList<Request> getArrayList(Cursor c) {

        ArrayList<Request> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseEvent(c));
        }
        return arrayList;
    }

    public static Request parseEvent(Cursor c) {
        Request request = new Request(c.getString(0), c.getString(1), c.getString(2), c.getString(3),c.getLong(4),c.getLong(5),c.getString(6),String.valueOf(c.getDouble(7)),String.valueOf(c.getDouble(8)));
        return request;
    }

    public static ArrayList<Request> getAllRequests(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllRequests();
    }

    public static ArrayList<Request> getRequest(Context context, String productId){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getRequest(productId);
    }
}
