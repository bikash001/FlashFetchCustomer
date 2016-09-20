package com.buyer.flashfetch.Objects;

/**
 * Created by KRANTHI on 04-09-2016.
 */
public class PlaceRequestObject {

    private String productPrice, productName, imageUrl, customerLocation, bargainExpTime;
    private double productCategory, customerLatitude, customerLongitude;;

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getBargainExpTime() {
        return bargainExpTime;
    }

    public void setBargainExpTime(String bargainExpTime) {
        this.bargainExpTime = bargainExpTime;
    }

    public double getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(double productCategory) {
        this.productCategory = productCategory;
    }

    public double getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(double customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public double getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(double customerLongitude) {
        this.customerLongitude = customerLongitude;
    }
}
