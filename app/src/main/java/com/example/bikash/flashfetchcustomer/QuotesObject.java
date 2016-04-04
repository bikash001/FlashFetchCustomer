package com.example.bikash.flashfetchcustomer;

/**
 * Created by bikash on 03-04-2016.
 */
public class QuotesObject {
    private String seller,time,distance,price;
    private boolean bargained;
    public QuotesObject(String s,String t,String d,String p,boolean bargained){
        seller = s;
        time = t;
        price = p;
        distance = d;
        this.bargained = bargained;
    }
    String getSeller(){
        return seller;
    }
    String getTime(){
        return time;
    }
    String getDistance(){
        return distance;
    }
    String getPrice(){
        return price;
    }
    boolean isBargained(){
        return bargained;
    }
    void setBargained(boolean b){
        bargained = b;
    }
}
