package com.buyer.flashfetch.Objects;

import android.content.Context;
import android.database.Cursor;

import com.buyer.flashfetch.Helper.DatabaseHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class NearByDealsDataModel implements Serializable {

    public static final String DEAL_TABLE_NAME = "nearby_deals";

    public static final String DEALS_ID = "deals_id";
    public static final String DEAL_TYPE = "deal_type";
    public static final String DEAL_CATEGORY = "deal_category";
    public static final String IMAGE_URL = "image_url";
    public static final String DEAL_HEADING = "deal_heading";
    public static final String DEAL_DESCRIPTION = "deal_description";
    public static final String DEAL_VALID_UPTO = "deal_valid_upto";
    public static final String HOW_TO_AVAIL_DEAL = "how_to_avail_deal";
    public static final String ACTIVATED = "activated";
    public static final String VOUCHER_ID = "voucher_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_PHONE = "shop_phone";
    public static final String SHOP_LOCATION = "shop_location";
    public static final String SHOP_ADDRESS = "shop_address";
    public static final String SHOP_LATITUDE = "shop_latitude";
    public static final String SHOP_LONGITUDE = "shop_longitude";
    public static final String DEAL_WEIGHTAGE = "deal_weightage";

    public static String[] DEALS_COLUMN_NAMES = {"deals_id", "deal_type", "deal_category", "image_url", "deal_heading", "deal_description",
            "deal_valid_upto", "how_to_avail_deal", "activated", "voucher_id", "shop_name", "shop_phone", "shop_location",
            "shop_address", "shop_latitude", "shop_longitude", "deal_weightage"};

    private String imageUrl;
    private String dealId, itemHeading, itemDescription, validTo, howToAvailDeal, voucherId;
    private String shopName, shopPhone, shopLongitude, shopLatitude, shopAddress, shopLocation;
    private boolean isActivated;
    private int dealsCategory, dealsType, dealWeightage;

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

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
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

    public static ArrayList<NearByDealsDataModel> getArrayList(Cursor c) {
        ArrayList<NearByDealsDataModel> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            arrayList.add(parseDeal(c));
        }
        return arrayList;
    }

    public static ArrayList<NearByDealsDataModel> getAllDeals(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getAllDeals();
    }

    public static ArrayList<NearByDealsDataModel> getDeal(Context context, String dealId){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getDeal(dealId);
    }

    public static ArrayList<NearByDealsDataModel> getDeals(Context context, String dealCategory){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getDeals(dealCategory);
    }

    public static NearByDealsDataModel parseDeal(Cursor cursor){
        NearByDealsDataModel nearByDealsDataModel = new NearByDealsDataModel();

        nearByDealsDataModel.setDealId(cursor.getString(0));
        nearByDealsDataModel.setDealsType(cursor.getInt(1));
        nearByDealsDataModel.setDealsCategory(cursor.getInt(2));
        nearByDealsDataModel.setImageUrl(cursor.getString(3));
        nearByDealsDataModel.setItemHeading(cursor.getString(4));
        nearByDealsDataModel.setItemDescription(cursor.getString(5));
        nearByDealsDataModel.setValidTo(cursor.getString(6));
        nearByDealsDataModel.setHowToAvailDeal(cursor.getString(7));
        nearByDealsDataModel.setActivated(cursor.getInt(8));
        nearByDealsDataModel.setVoucherId(cursor.getString(9));
        nearByDealsDataModel.setShopName(cursor.getString(10));
        nearByDealsDataModel.setShopPhone(cursor.getString(11));
        nearByDealsDataModel.setShopLocation(cursor.getString(12));
        nearByDealsDataModel.setShopAddress(cursor.getString(13));
        nearByDealsDataModel.setShopLatitude(cursor.getString(14));
        nearByDealsDataModel.setShopLongitude(cursor.getString(15));

        return nearByDealsDataModel;
    }

    public int getDealWeightage() {
        return dealWeightage;
    }

    public void setDealWeightage(int dealWeightage) {
        this.dealWeightage = dealWeightage;
    }
}
