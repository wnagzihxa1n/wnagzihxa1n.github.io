package com.wnagzihxa1n.obfuscatordemo;

import android.app.Activity;
import android.util.Log;

public class Activity01 extends Activity {
    public static void funcA() {
        Log.i("toT0C", Util.decStr(new byte[]{0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31}));//1111111111
    }
}
