package com.buyer.flashfetch;

import android.app.Activity;
import android.os.Bundle;
import android.test.ActivityTestCase;

/**
 * Created by kranthikumar_b on 7/1/2016.
 */
public class DealsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
    }
}
