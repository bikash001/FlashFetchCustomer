package com.buyer.flashfetch.Objects;

import java.io.Serializable;

/**
 * Created by KRANTHI on 28-06-2016.
 */
public class NearByDealsDataModel implements Serializable {

    private String shopName,imageUrl,shopDistance,itemDescription;

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

    public String getShopDistance() {
        return shopDistance;
    }

    public void setShopDistance(String shopDistance) {
        this.shopDistance = shopDistance;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
