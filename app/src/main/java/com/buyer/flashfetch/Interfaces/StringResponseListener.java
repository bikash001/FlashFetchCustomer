package com.buyer.flashfetch.Interfaces;

public interface StringResponseListener<T> {

    public void onSuccess(T result);

    public void onFailure();

    public void onConnectionError();
}
