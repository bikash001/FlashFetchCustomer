package com.buyer.flashfetch;

/**
 * Created by bikash on 03-04-2016.
 */
public class QuotesObject {
    private String seller,time,distance,price;
    private boolean bargained, valid_layout, rejected;
    public QuotesObject(String s,String t,String d,String p,boolean bargained){
        seller = s;
        time = t;
        price = p;
        distance = d;
        valid_layout = true;
        rejected = false;
        this.bargained = bargained;
    }
    void setValid(boolean t){
        valid_layout = t;
    }
    boolean getValid(){
        return valid_layout;
    }
    void setRejected(boolean t){
        rejected = t;
    }
    boolean isRejected(){
        return rejected;
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
