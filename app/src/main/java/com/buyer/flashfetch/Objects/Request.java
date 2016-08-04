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

/**
 * Created by kevinselvaprasanna on 4/9/16.
 */
public class Request implements Serializable{
    public static final String TABLE_NAME = "Requests" ;
    public static String[] columns = {"productId", "productName", "productPrice","imageURL","productCategory","expiryTime"};

    public String productName, productPrice,imageURL,productId;
    public long expiryTime, productCategory;

    public Request(JSONObject request) {
        try {

            this.productName = request.getString("email");
            this.productPrice = request.getString("price");
            this.productCategory = request.getInt("category");
            this.expiryTime = request.getLong("time");
            this.productId = request.getString("id");
            this.imageURL = request.getString("img");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Request(String productId, String imageURL , String productName, String productPrice, long productCategory, long expiryTime) {
        this.productId = productId;
        this.imageURL = imageURL;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.expiryTime = expiryTime;
    }

    public static ArrayList<Request> getArrayList(Cursor c) {

        ArrayList<Request> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseEvent(c));
        }
        return arrayList;
    }

    public static Request parseEvent(Cursor c) {
        Request request = new Request(c.getString(0), c.getString(1), c.getString(2), c.getString(3),c.getLong(5),c.getLong(6));
        return request;
    }

    public static ArrayList<Request> getAllRequests(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllRequests();
    }

    public static ArrayList<Request> getRequest(Context context, String id){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getRequest(id);
    }
}
