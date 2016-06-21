package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.buyer.flashfetch.Helper.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kevinselvaprasanna on 4/13/16.
 */
public class Quote implements Parcelable{
    public static final String TABLE_NAME = "Quotes" ;
    public String id,qid,qprice,comment,bgprice,name,distance;
    public long exptime,cat,bgexptime;
    Boolean valid_layout;
    public int type,deltype,bargained,cuscon,selcon,del;
    public float lat,longt;

   /* public Quote(JSONObject not) {
        try {
            this.pname = not.getString("email");
            this.pprice = not.getString("price");
            this.cat = not.getInt("category");
            this.exptime = not.getLong("time");
            this.id = not.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public static String[] columns = {"id", "qid","name", "qprice", "type", "deltype","comment","lat","long","distance","bargained","bgprice","bgexptime","selcon","cuscon","del"};

    public Quote(String id, String qid ,String name, String qprice, int type, int deltype, String comment, float lat, float longt, String distance, int bargained, String bgprice, long bgexptime, int selcon, int cuscon, int del) {
        this.id = id;
       this.qid = qid;
        this.valid_layout = true;
        this.qprice = qprice;
        this.type = type;
        this.deltype = deltype;
        this.comment = comment;
        this.lat = lat;
        this.longt = longt;
        this.distance = distance;
        this.bargained = bargained;
        this.bgprice = bgprice;
        this.bgexptime = bgexptime;
        this.selcon = selcon;
        this.cuscon = cuscon;
        this.del = del;
    }

    protected Quote(Parcel in) {
        id = in.readString();
        qid = in.readString();
        qprice = in.readString();
        comment = in.readString();
        bgprice = in.readString();
        name = in.readString();
        distance = in.readString();
        exptime = in.readLong();
        cat = in.readLong();
        bgexptime = in.readLong();
        type = in.readInt();
        deltype = in.readInt();
        bargained = in.readInt();
        cuscon = in.readInt();
        selcon = in.readInt();
        del = in.readInt();
        lat = in.readFloat();
        longt = in.readFloat();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public void setValid(boolean t){
        valid_layout = t;
    }
    public boolean getValid(){
        return valid_layout;
    }

    public static ArrayList<Quote> getArrayList(Cursor c) {
        ArrayList<Quote> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseEvent(c));
        }
        return arrayList;
    }

    public static Quote parseEvent(Cursor c) {
        //Gson gson = new Gson();
        Quote not = new Quote(c.getString(0), c.getString(1), c.getString(2),c.getString(3), c.getInt(4),c.getInt(5),c.getString(6),c.getFloat(7),c.getFloat(8), c.getString(9),c.getInt(10),c.getString(11),c.getLong(12),c.getInt(13),c.getInt(14),c.getInt(15));
        return not;
    }

    public static ArrayList<Quote> getAllQuotes(Context context, String id){
        DatabaseHelper data = new DatabaseHelper(context);
        return data.getAllQuotes(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(qid);
        parcel.writeString(qprice);
        parcel.writeString(comment);
        parcel.writeString(bgprice);
        parcel.writeString(name);
        parcel.writeString(distance);
        parcel.writeLong(exptime);
        parcel.writeLong(cat);
        parcel.writeLong(bgexptime);
        parcel.writeInt(type);
        parcel.writeInt(deltype);
        parcel.writeInt(bargained);
        parcel.writeInt(cuscon);
        parcel.writeInt(selcon);
        parcel.writeInt(del);
        parcel.writeFloat(lat);
        parcel.writeFloat(longt);
    }

    public float getLat(){
        return lat;
    }

    public float getLongt(){
        return longt;
    }
}
