package com.wnagzihxain.setcomponent;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * DISABLE：COMPONENT_ENABLED_STATE_DISABLED or 2
        * ENABLE：COMPONENT_ENABLED_STATE_ENABLED or 1
        * DEFAULT：COMPONENT_ENABLED_STATE_DEFAULT or 0
        */
        PackageManager packagemanager = getPackageManager();
        ComponentName componentname = new ComponentName(this, MainActivity.class);
        packagemanager.setComponentEnabledSetting(componentname, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packagemanager.setComponentEnabledSetting(componentname, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        packagemanager.setComponentEnabledSetting(componentname, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
        packagemanager.setComponentEnabledSetting(componentname, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, 0);
    }
}






