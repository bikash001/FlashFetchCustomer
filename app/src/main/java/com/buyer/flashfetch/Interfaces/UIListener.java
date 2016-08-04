package com.buyer.flashfetch.Interfaces;

/**
 * Created by kranthikumar_b on 7/7/2016.
 */
public interface UIListener {

    public void onSuccess();

    public void onFailure();

    public void onFailure(int result);

    public void onConnectionError();

    public void onCancelled();
}
