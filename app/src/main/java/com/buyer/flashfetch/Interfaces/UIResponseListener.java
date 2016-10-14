package com.buyer.flashfetch.Interfaces;

public interface UIResponseListener<T> {

    public void onSuccess(T result);

    public void onFailure();

    public void onConnectionError();

    public void onCancelled();
}
