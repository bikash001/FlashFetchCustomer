package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.database.Cursor;

import com.buyer.flashfetch.Helper.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinselvaprasanna on 4/13/16.
 */
public class Ouote {
   /* public static final String TABLE_NAME = "Quotes" ;
    public String pname, pprice,pimg,url,id;
    public long exptime,cat;

    public Quote(JSONObject not) {
        try {
            this.pname = not.getString("email");
            this.pprice = not.getString("price");
            this.cat = not.getInt("category");
            this.exptime = not.getLong("time");
            this.id = not.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String[] columns = {"id", "url", "pname", "pprice", "pimg","cat","exptime"};

    public Request(String id, String url , String pname, String pprice, String pimg, long cat ,  long exptime) {
        this.id = id;
        this.url = url;
        this.pname = pname;
        this.pprice = pprice;
        this.pimg= pimg;
        this.cat = cat;
        this.exptime = exptime;
    }

    public static ArrayList<Request> getArrayList(Cursor c) {
        ArrayList<Request> arrayList = new ArrayList<>();
        //Gson gson = new Gson();
        while (c.moveToNext()) {
            arrayList.add(parseEvent(c));
        }
        return arrayList;
    }

    public static Request parseEvent(Cursor c) {
        //Gson gson = new Gson();
        Request not = new Request(c.getString(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4),c.getLong(5),c.getLong(6));
        return not;
    }

    public static ArrayList<Request> getAllRequests(Context context){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllRequests();
    }*/
}
