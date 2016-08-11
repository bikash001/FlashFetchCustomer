package com.buyer.flashfetch.ServiceResponseObjects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import com.google.gson.Gson;
/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class ProductDetailsResponse implements Serializable{

    @SerializedName("result")
    public String productName;

    @SerializedName("category")
    public long productCategory;

    @SerializedName("price")
    public String productPrice;

    @SerializedName("img")
    public String imageURL;

    public String toJson(){
        return new Gson().toJson(this);
    }
}
