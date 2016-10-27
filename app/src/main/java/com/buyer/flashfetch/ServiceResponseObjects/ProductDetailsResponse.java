package com.buyer.flashfetch.ServiceResponseObjects;


import java.io.Serializable;
/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public class ProductDetailsResponse implements Serializable{

    public String result;

    public String productName;

    public String productCategory;

    public String productPrice;

//    @SerializedName("img")
//    public String imageURL;

}
