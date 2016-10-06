package com.buyer.flashfetch.ServiceResponseObjects;

import java.io.Serializable;

/**
 * Created by KRANTHI on 06-10-2016.
 */
public class DealsVoucherResponse implements Serializable {

    public String voucherID;

    public String getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }
}
