package com.buyer.flashfetch.Objects;

import java.io.Serializable;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class NearByDealsDataModel implements Serializable {

    private String dealId,shopName, imageUrl, itemHeading, itemCode, itemDescription, shopLocation, validFrom, validTo, activateDeal, shopAddress, shopLatitude,
            shopLongitude, productURL, productName, productSubCategory, howToAvailDeal, shopPhone, voucherId;
    private boolean isDeliverable, isActivated;
    private int shopId, dealsCategory, dealsType, quantityOrdered;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public boolean isDeliverable() {
        return isDeliverable;
    }

    public void setDeliverable(boolean deliverable) {
        isDeliverable = deliverable;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getDealsCategory() {
        return dealsCategory;
    }

    public void setDealsCategory(int dealsCategory) {
        this.dealsCategory = dealsCategory;
    }

    public String getItemHeading() {
        return itemHeading;
    }

    public void setItemHeading(String itemHeading) {
        this.itemHeading = itemHeading;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getActivateDeal() {
        return activateDeal;
    }

    public void setActivateDeal(String activateDeal) {
        this.activateDeal = activateDeal;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopLatitude() {
        return shopLatitude;
    }

    public void setShopLatitude(String shopLatitude) {
        this.shopLatitude = shopLatitude;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getShopLongitude() {
        return shopLongitude;
    }

    public void setShopLongitude(String shopLongitude) {
        this.shopLongitude = shopLongitude;
    }

    public int getDealsType() {
        return dealsType;
    }

    public void setDealsType(int dealsType) {
        this.dealsType = dealsType;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getHowToAvailDeal() {
        return howToAvailDeal;
    }

    public void setHowToAvailDeal(String howToAvailDeal) {
        this.howToAvailDeal = howToAvailDeal;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(int activated) {
        if(activated == 1){
            isActivated = true;
        }else {
            isActivated = false;
        }
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }
}
