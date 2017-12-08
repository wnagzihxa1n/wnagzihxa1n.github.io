package com.wnagzihxain.sourceapk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.ArrayMap;
import android.util.Log;

import dalvik.system.DexClassLoader;

public class ProtectApplication extends Application {
    Context context = ProtectApplication.this;
    ApplicationInfo applicationInfo = null;
    private String apkPath;
    private String odexPath;
    private String libPath;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        applicationInfo = ProtectApplication.this.getApplicationInfo();

        File odex = this.getDir("targetOdex", MODE_PRIVATE);
        File libs = this.getDir("targetLib", MODE_PRIVATE);
        apkPath = odex.getAbsolutePath() + "/targetAPK.apk";
        odexPath = odex.getAbsolutePath();
        libPath = libs.getAbsolutePath();

        Log.i("toT0C", "apkPath  : " + apkPath);
        Log.i("toT0C", "odexPath : " + odexPath);
        Log.i("toT0C", "libPath  : " + libPath);

        releaseDexFile();

        try {
            File apkFile = new File(apkPath);
            if (!apkFile.exists()) {
                Log.i("toT0C", "Top miss, Mid miss, Bot miss, All miss");
                return;
            }
            Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
            String packageName = this.getPackageName();
            ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mPackages");
            WeakReference wr = (WeakReference) mPackages.get(packageName);
            DexClassLoader dLoader = new DexClassLoader(apkPath, odexPath, libPath, (ClassLoader) RefInvoke.getFieldOjbect("android.app.LoadedApk", wr.get(), "mClassLoader"));
            RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader", wr.get(), dLoader);
            try {
                Object actObj = dLoader.loadClass("com.wnagzihxain.sourceapk.MainActivity");
                Log.i("toT0C", "ActObj : " + actObj);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("toT0C", "Activity : " + Log.getStackTraceString(e));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("toT0C", "Error : " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("toT0C", "onCreate()");
//        String appClassName = null;
//        try {
//            ApplicationInfo ai = this.getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
//            Bundle bundle = ai.metaData;
//            if (bundle != null && bundle.containsKey("APPLICATION_CLASS_NAME")) {
//                appClassName = bundle.getString("APPLICATION_CLASS_NAME");
//            } else {
//                Log.i("demo", "have no application class name");
//                return;
//            }
//        } catch (NameNotFoundException e) {
//            Log.i("demo", "error:" + Log.getStackTraceString(e));
//            e.printStackTrace();
//        }
//        Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
//        Object mBoundApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mBoundApplication");
//        Object loadedApkInfo = RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");
//        RefInvoke.setFieldOjbect("android.app.LoadedApk", "mApplication", loadedApkInfo, null);
//        Object oldApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mInitialApplication");
//        ArrayList<Application> mAllApplications = (ArrayList<Application>) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mAllApplications");
//        mAllApplications.remove(oldApplication);
//        ApplicationInfo appinfo_In_LoadedApk = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.LoadedApk", loadedApkInfo, "mApplicationInfo");
//        ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "appInfo");
//        appinfo_In_LoadedApk.className = appClassName;
//        appinfo_In_AppBindData.className = appClassName;
//        Application app = (Application) RefInvoke.invokeMethod("android.app.LoadedApk", "makeApplication", loadedApkInfo, new Class[]{boolean.class, Instrumentation.class}, new Object[]{false, null});
//        RefInvoke.setFieldOjbect("android.app.ActivityThread", "mInitialApplication", currentActivityThread, app);
//        ArrayMap mProviderMap = (ArrayMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mProviderMap");
//        Iterator it = mProviderMap.values().iterator();
//        while (it.hasNext()) {
//            Object providerClientRecord = it.next();
//            Object localProvider = RefInvoke.getFieldOjbect("android.app.ActivityThread$ProviderClientRecord", providerClientRecord, "mLocalProvider");
//            RefInvoke.setFieldOjbect("android.content.ContentProvider", "mContext", localProvider, app);
//        }
//        Log.i("demo", "app:" + app);
//        app.onCreate();
    }

    private byte[] decrypt(byte[] srcdata) {
        for (int i = 0; i < srcdata.length; i++) {
            srcdata[i] = (byte) (0xFF ^ srcdata[i]);
        }
        return srcdata;
    }

    public void releaseDexFile() {
        byte xor_key = 0x66;
        try {
            InputStream inputStream = context.getAssets().open("a", MODE_PRIVATE);
            File dexFile_save = new File(apkPath);
            FileOutputStream fileOutputStream = new FileOutputStream(dexFile_save);
            int myDexlength = inputStream.available();
            Log.i("toT0C", "DexLength : " + myDexlength);
            byte[] buffer_temp = new byte[myDexlength];
            inputStream.read(buffer_temp);
//            for (int i = 0; i < myDexlength; i++) {
////                buffer_temp[i] = (byte) (buffer_temp[i] ^ xor_key);
//                buffer_temp[i] = (byte) (buffer_temp[i]);
//            }

            fileOutputStream.write(buffer_temp);
            fileOutputStream.flush();
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("toT0C", "Releasing myAPK.apk failed\n" + e);
        }
    }

    protected AssetManager mAssetManager;
    protected Resources mResources;
    protected Theme mTheme;

    protected void loadResources(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            Log.i("inject", "loadResource error:" + Log.getStackTraceString(e));
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

}
