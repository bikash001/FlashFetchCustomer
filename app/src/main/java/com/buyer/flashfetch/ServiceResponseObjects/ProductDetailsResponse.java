package com.buyer.flashfetch.ServiceResponseObjects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import com.google.gson.Gson;
/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class ProductDetailsResponse implements Serializable{

    @SerializedName("result")
    public String data;

    public String toJson(){
        return new Gson().toJson(this);
    }
}
