package com.buyer.flashfetch.Interfaces;

/**
 * Created by kranthikumar_b on 8/4/2016.
 */
public interface UIResponseListener<T> {

    public void onSuccess(T responseObj);

    public void onFailure();

    public void  onConnectionError();

    public void onCancelled();
}
