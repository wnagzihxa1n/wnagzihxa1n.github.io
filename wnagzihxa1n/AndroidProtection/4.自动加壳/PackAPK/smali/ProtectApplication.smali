.class public Lcom/wnagzihxain/sourceapk/ProtectApplication;
.super Landroid/app/Application;
.source "ProtectApplication.java"


# instance fields
.field private apkPath:Ljava/lang/String;

.field applicationInfo:Landroid/content/pm/ApplicationInfo;

.field context:Landroid/content/Context;

.field private libPath:Ljava/lang/String;

.field private odexPath:Ljava/lang/String;


# direct methods
.method public constructor <init>()V
    .locals 1

    .prologue
    .line 25
    invoke-direct {p0}, Landroid/app/Application;-><init>()V

    .line 26
    iput-object p0, p0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->context:Landroid/content/Context;

    .line 27
    const/4 v0, 0x0

    iput-object v0, p0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->applicationInfo:Landroid/content/pm/ApplicationInfo;

    return-void
.end method


# virtual methods
.method protected attachBaseContext(Landroid/content/Context;)V
    .locals 17
    .param p1, "base"    # Landroid/content/Context;

    .prologue
    .line 34
    invoke-super/range {p0 .. p1}, Landroid/app/Application;->attachBaseContext(Landroid/content/Context;)V

    .line 35
    invoke-virtual/range {p0 .. p0}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v11

    move-object/from16 v0, p0

    iput-object v11, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->applicationInfo:Landroid/content/pm/ApplicationInfo;

    .line 36
    const-string v11, "targetApk"

    const/4 v12, 0x0

    move-object/from16 v0, p0

    invoke-virtual {v0, v11, v12}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getDir(Ljava/lang/String;I)Ljava/io/File;

    move-result-object v1

    .line 37
    .local v1, "apk":Ljava/io/File;
    const-string v11, "targetOdex"

    const/4 v12, 0x0

    move-object/from16 v0, p0

    invoke-virtual {v0, v11, v12}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getDir(Ljava/lang/String;I)Ljava/io/File;

    move-result-object v8

    .line 38
    .local v8, "odex":Ljava/io/File;
    const-string v11, "targetLib"

    const/4 v12, 0x0

    move-object/from16 v0, p0

    invoke-virtual {v0, v11, v12}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getDir(Ljava/lang/String;I)Ljava/io/File;

    move-result-object v6

    .line 39
    .local v6, "libs":Ljava/io/File;
    new-instance v11, Ljava/lang/StringBuilder;

    invoke-direct {v11}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v1}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v12

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v11

    const-string v12, "/targetDex.dex"

    invoke-virtual {v11, v12}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v11

    invoke-virtual {v11}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    move-object/from16 v0, p0

    iput-object v11, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->apkPath:Ljava/lang/String;

    .line 40
    invoke-virtual {v8}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v11

    move-object/from16 v0, p0

    iput-object v11, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->odexPath:Ljava/lang/String;

    .line 41
    invoke-virtual {v6}, Ljava/io/File;->getAbsolutePath()Ljava/lang/String;

    move-result-object v11

    move-object/from16 v0, p0

    iput-object v11, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->libPath:Ljava/lang/String;

    .line 43
    :try_start_0
    new-instance v2, Ljava/io/File;

    move-object/from16 v0, p0

    iget-object v11, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->apkPath:Ljava/lang/String;

    invoke-direct {v2, v11}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 44
    .local v2, "apkFile":Ljava/io/File;
    invoke-virtual {v2}, Ljava/io/File;->exists()Z

    move-result v11

    if-nez v11, :cond_0

    .line 45
    invoke-virtual/range {p0 .. p0}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->releaseDexFile()V

    .line 47
    :cond_0
    const-string v11, "android.app.ActivityThread"

    const-string v12, "currentActivityThread"

    const/4 v13, 0x0

    new-array v13, v13, [Ljava/lang/Class;

    const/4 v14, 0x0

    new-array v14, v14, [Ljava/lang/Object;

    invoke-static {v11, v12, v13, v14}, Lcom/wnagzihxain/sourceapk/RefInvoke;->invokeStaticMethod(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    .line 48
    .local v3, "currentActivityThread":Ljava/lang/Object;
    invoke-virtual/range {p0 .. p0}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getPackageName()Ljava/lang/String;

    move-result-object v9

    .line 49
    .local v9, "packageName":Ljava/lang/String;
    const-string v11, "android.app.ActivityThread"

    const-string v12, "mPackages"

    invoke-static {v11, v3, v12}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Landroid/util/ArrayMap;

    .line 50
    .local v7, "mPackages":Landroid/util/ArrayMap;
    invoke-virtual {v7, v9}, Landroid/util/ArrayMap;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v10

    check-cast v10, Ljava/lang/ref/WeakReference;

    .line 51
    .local v10, "wr":Ljava/lang/ref/WeakReference;
    new-instance v4, Ldalvik/system/DexClassLoader;

    move-object/from16 v0, p0

    iget-object v12, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->apkPath:Ljava/lang/String;

    move-object/from16 v0, p0

    iget-object v13, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->odexPath:Ljava/lang/String;

    move-object/from16 v0, p0

    iget-object v14, v0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->libPath:Ljava/lang/String;

    const-string v11, "android.app.LoadedApk"

    invoke-virtual {v10}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v15

    const-string v16, "mClassLoader"

    move-object/from16 v0, v16

    invoke-static {v11, v15, v0}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Ljava/lang/ClassLoader;

    invoke-direct {v4, v12, v13, v14, v11}, Ldalvik/system/DexClassLoader;-><init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V

    .line 52
    .local v4, "dLoader":Ldalvik/system/DexClassLoader;
    const-string v11, "android.app.LoadedApk"

    const-string v12, "mClassLoader"

    invoke-virtual {v10}, Ljava/lang/ref/WeakReference;->get()Ljava/lang/Object;

    move-result-object v13

    invoke-static {v11, v12, v13, v4}, Lcom/wnagzihxain/sourceapk/RefInvoke;->setFieldOjbect(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_1

    .line 54
    :try_start_1
    const-string v11, "com.wnagzihxain.sourceapk.MainActivity"

    invoke-virtual {v4, v11}, Ldalvik/system/DexClassLoader;->loadClass(Ljava/lang/String;)Ljava/lang/Class;
    :try_end_1
    .catch Ljava/lang/Exception; {:try_start_1 .. :try_end_1} :catch_0

    .line 61
    .end local v2    # "apkFile":Ljava/io/File;
    .end local v3    # "currentActivityThread":Ljava/lang/Object;
    .end local v4    # "dLoader":Ldalvik/system/DexClassLoader;
    .end local v7    # "mPackages":Landroid/util/ArrayMap;
    .end local v9    # "packageName":Ljava/lang/String;
    .end local v10    # "wr":Ljava/lang/ref/WeakReference;
    :goto_0
    return-void

    .line 55
    .restart local v2    # "apkFile":Ljava/io/File;
    .restart local v3    # "currentActivityThread":Ljava/lang/Object;
    .restart local v4    # "dLoader":Ldalvik/system/DexClassLoader;
    .restart local v7    # "mPackages":Landroid/util/ArrayMap;
    .restart local v9    # "packageName":Ljava/lang/String;
    .restart local v10    # "wr":Ljava/lang/ref/WeakReference;
    :catch_0
    move-exception v5

    .line 56
    .local v5, "e":Ljava/lang/Exception;
    :try_start_2
    invoke-virtual {v5}, Ljava/lang/Exception;->printStackTrace()V
    :try_end_2
    .catch Ljava/lang/Exception; {:try_start_2 .. :try_end_2} :catch_1

    goto :goto_0

    .line 58
    .end local v2    # "apkFile":Ljava/io/File;
    .end local v3    # "currentActivityThread":Ljava/lang/Object;
    .end local v4    # "dLoader":Ldalvik/system/DexClassLoader;
    .end local v5    # "e":Ljava/lang/Exception;
    .end local v7    # "mPackages":Landroid/util/ArrayMap;
    .end local v9    # "packageName":Ljava/lang/String;
    .end local v10    # "wr":Ljava/lang/ref/WeakReference;
    :catch_1
    move-exception v5

    .line 59
    .restart local v5    # "e":Ljava/lang/Exception;
    invoke-virtual {v5}, Ljava/lang/Exception;->printStackTrace()V

    goto :goto_0
.end method

.method public onCreate()V
    .locals 26

    .prologue
    .line 65
    const/4 v6, 0x0

    .line 67
    .local v6, "appClassName":Ljava/lang/String;
    :try_start_0
    invoke-virtual/range {p0 .. p0}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v20

    invoke-virtual/range {p0 .. p0}, Lcom/wnagzihxain/sourceapk/ProtectApplication;->getPackageName()Ljava/lang/String;

    move-result-object v21

    const/16 v22, 0x80

    invoke-virtual/range {v20 .. v22}, Landroid/content/pm/PackageManager;->getApplicationInfo(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;

    move-result-object v4

    .line 68
    .local v4, "ai":Landroid/content/pm/ApplicationInfo;
    iget-object v9, v4, Landroid/content/pm/ApplicationInfo;->metaData:Landroid/os/Bundle;

    .line 69
    .local v9, "bundle":Landroid/os/Bundle;
    if-eqz v9, :cond_1

    const-string v20, "APPLICATION_CLASS_NAME"

    move-object/from16 v0, v20

    invoke-virtual {v9, v0}, Landroid/os/Bundle;->containsKey(Ljava/lang/String;)Z

    move-result v20

    if-eqz v20, :cond_1

    .line 70
    const-string v20, "APPLICATION_CLASS_NAME"

    move-object/from16 v0, v20

    invoke-virtual {v9, v0}, Landroid/os/Bundle;->getString(Ljava/lang/String;)Ljava/lang/String;
    :try_end_0
    .catch Landroid/content/pm/PackageManager$NameNotFoundException; {:try_start_0 .. :try_end_0} :catch_0

    move-result-object v6

    .line 77
    .end local v4    # "ai":Landroid/content/pm/ApplicationInfo;
    .end local v9    # "bundle":Landroid/os/Bundle;
    :goto_0
    const-string v20, "android.app.ActivityThread"

    const-string v21, "currentActivityThread"

    const/16 v22, 0x0

    move/from16 v0, v22

    new-array v0, v0, [Ljava/lang/Class;

    move-object/from16 v22, v0

    const/16 v23, 0x0

    move/from16 v0, v23

    new-array v0, v0, [Ljava/lang/Object;

    move-object/from16 v23, v0

    invoke-static/range {v20 .. v23}, Lcom/wnagzihxain/sourceapk/RefInvoke;->invokeStaticMethod(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v10

    .line 78
    .local v10, "currentActivityThread":Ljava/lang/Object;
    const-string v20, "android.app.ActivityThread"

    const-string v21, "mBoundApplication"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v10, v1}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v16

    .line 79
    .local v16, "mBoundApplication":Ljava/lang/Object;
    const-string v20, "android.app.ActivityThread$AppBindData"

    const-string v21, "info"

    move-object/from16 v0, v20

    move-object/from16 v1, v16

    move-object/from16 v2, v21

    invoke-static {v0, v1, v2}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v13

    .line 80
    .local v13, "loadedApkInfo":Ljava/lang/Object;
    const-string v20, "android.app.LoadedApk"

    const-string v21, "mApplication"

    const/16 v22, 0x0

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    move-object/from16 v2, v22

    invoke-static {v0, v1, v13, v2}, Lcom/wnagzihxain/sourceapk/RefInvoke;->setFieldOjbect(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V

    .line 81
    const-string v20, "android.app.ActivityThread"

    const-string v21, "mInitialApplication"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v10, v1}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v18

    .line 82
    .local v18, "oldApplication":Ljava/lang/Object;
    const-string v20, "android.app.ActivityThread"

    const-string v21, "mAllApplications"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v10, v1}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v15

    check-cast v15, Ljava/util/ArrayList;

    .line 83
    .local v15, "mAllApplications":Ljava/util/ArrayList;, "Ljava/util/ArrayList<Landroid/app/Application;>;"
    move-object/from16 v0, v18

    invoke-virtual {v15, v0}, Ljava/util/ArrayList;->remove(Ljava/lang/Object;)Z

    .line 84
    const-string v20, "android.app.LoadedApk"

    const-string v21, "mApplicationInfo"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v13, v1}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Landroid/content/pm/ApplicationInfo;

    .line 85
    .local v8, "appinfo_In_LoadedApk":Landroid/content/pm/ApplicationInfo;
    const-string v20, "android.app.ActivityThread$AppBindData"

    const-string v21, "appInfo"

    move-object/from16 v0, v20

    move-object/from16 v1, v16

    move-object/from16 v2, v21

    invoke-static {v0, v1, v2}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Landroid/content/pm/ApplicationInfo;

    .line 86
    .local v7, "appinfo_In_AppBindData":Landroid/content/pm/ApplicationInfo;
    iput-object v6, v8, Landroid/content/pm/ApplicationInfo;->className:Ljava/lang/String;

    .line 87
    iput-object v6, v7, Landroid/content/pm/ApplicationInfo;->className:Ljava/lang/String;

    .line 88
    const-string v20, "android.app.LoadedApk"

    const-string v21, "makeApplication"

    const/16 v22, 0x2

    move/from16 v0, v22

    new-array v0, v0, [Ljava/lang/Class;

    move-object/from16 v22, v0

    const/16 v23, 0x0

    sget-object v24, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    aput-object v24, v22, v23

    const/16 v23, 0x1

    const-class v24, Landroid/app/Instrumentation;

    aput-object v24, v22, v23

    const/16 v23, 0x2

    move/from16 v0, v23

    new-array v0, v0, [Ljava/lang/Object;

    move-object/from16 v23, v0

    const/16 v24, 0x0

    const/16 v25, 0x0

    invoke-static/range {v25 .. v25}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v25

    aput-object v25, v23, v24

    const/16 v24, 0x1

    const/16 v25, 0x0

    aput-object v25, v23, v24

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    move-object/from16 v2, v22

    move-object/from16 v3, v23

    invoke-static {v0, v1, v13, v2, v3}, Lcom/wnagzihxain/sourceapk/RefInvoke;->invokeMethod(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;[Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/app/Application;

    .line 89
    .local v5, "app":Landroid/app/Application;
    const-string v20, "android.app.ActivityThread"

    const-string v21, "mInitialApplication"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v1, v10, v5}, Lcom/wnagzihxain/sourceapk/RefInvoke;->setFieldOjbect(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V

    .line 90
    const-string v20, "android.app.ActivityThread"

    const-string v21, "mProviderMap"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v10, v1}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v17

    check-cast v17, Landroid/util/ArrayMap;

    .line 91
    .local v17, "mProviderMap":Landroid/util/ArrayMap;
    invoke-virtual/range {v17 .. v17}, Landroid/util/ArrayMap;->values()Ljava/util/Collection;

    move-result-object v20

    invoke-interface/range {v20 .. v20}, Ljava/util/Collection;->iterator()Ljava/util/Iterator;

    move-result-object v12

    .line 92
    .local v12, "it":Ljava/util/Iterator;
    :goto_1
    invoke-interface {v12}, Ljava/util/Iterator;->hasNext()Z

    move-result v20

    if-eqz v20, :cond_0

    .line 93
    invoke-interface {v12}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v19

    .line 94
    .local v19, "providerClientRecord":Ljava/lang/Object;
    const-string v20, "android.app.ActivityThread$ProviderClientRecord"

    const-string v21, "mLocalProvider"

    move-object/from16 v0, v20

    move-object/from16 v1, v19

    move-object/from16 v2, v21

    invoke-static {v0, v1, v2}, Lcom/wnagzihxain/sourceapk/RefInvoke;->getFieldOjbect(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;

    move-result-object v14

    .line 95
    .local v14, "localProvider":Ljava/lang/Object;
    const-string v20, "android.content.ContentProvider"

    const-string v21, "mContext"

    move-object/from16 v0, v20

    move-object/from16 v1, v21

    invoke-static {v0, v1, v14, v5}, Lcom/wnagzihxain/sourceapk/RefInvoke;->setFieldOjbect(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V

    goto :goto_1

    .line 74
    .end local v5    # "app":Landroid/app/Application;
    .end local v7    # "appinfo_In_AppBindData":Landroid/content/pm/ApplicationInfo;
    .end local v8    # "appinfo_In_LoadedApk":Landroid/content/pm/ApplicationInfo;
    .end local v10    # "currentActivityThread":Ljava/lang/Object;
    .end local v12    # "it":Ljava/util/Iterator;
    .end local v13    # "loadedApkInfo":Ljava/lang/Object;
    .end local v14    # "localProvider":Ljava/lang/Object;
    .end local v15    # "mAllApplications":Ljava/util/ArrayList;, "Ljava/util/ArrayList<Landroid/app/Application;>;"
    .end local v16    # "mBoundApplication":Ljava/lang/Object;
    .end local v17    # "mProviderMap":Landroid/util/ArrayMap;
    .end local v18    # "oldApplication":Ljava/lang/Object;
    .end local v19    # "providerClientRecord":Ljava/lang/Object;
    :catch_0
    move-exception v11

    .line 75
    .local v11, "e":Landroid/content/pm/PackageManager$NameNotFoundException;
    invoke-virtual {v11}, Landroid/content/pm/PackageManager$NameNotFoundException;->printStackTrace()V

    goto/16 :goto_0

    .line 97
    .end local v11    # "e":Landroid/content/pm/PackageManager$NameNotFoundException;
    .restart local v5    # "app":Landroid/app/Application;
    .restart local v7    # "appinfo_In_AppBindData":Landroid/content/pm/ApplicationInfo;
    .restart local v8    # "appinfo_In_LoadedApk":Landroid/content/pm/ApplicationInfo;
    .restart local v10    # "currentActivityThread":Ljava/lang/Object;
    .restart local v12    # "it":Ljava/util/Iterator;
    .restart local v13    # "loadedApkInfo":Ljava/lang/Object;
    .restart local v15    # "mAllApplications":Ljava/util/ArrayList;, "Ljava/util/ArrayList<Landroid/app/Application;>;"
    .restart local v16    # "mBoundApplication":Ljava/lang/Object;
    .restart local v17    # "mProviderMap":Landroid/util/ArrayMap;
    .restart local v18    # "oldApplication":Ljava/lang/Object;
    :cond_0
    invoke-virtual {v5}, Landroid/app/Application;->onCreate()V

    .line 98
    .end local v5    # "app":Landroid/app/Application;
    .end local v7    # "appinfo_In_AppBindData":Landroid/content/pm/ApplicationInfo;
    .end local v8    # "appinfo_In_LoadedApk":Landroid/content/pm/ApplicationInfo;
    .end local v10    # "currentActivityThread":Ljava/lang/Object;
    .end local v12    # "it":Ljava/util/Iterator;
    .end local v13    # "loadedApkInfo":Ljava/lang/Object;
    .end local v15    # "mAllApplications":Ljava/util/ArrayList;, "Ljava/util/ArrayList<Landroid/app/Application;>;"
    .end local v16    # "mBoundApplication":Ljava/lang/Object;
    .end local v17    # "mProviderMap":Landroid/util/ArrayMap;
    .end local v18    # "oldApplication":Ljava/lang/Object;
    :cond_1
    return-void
.end method

.method public releaseDexFile()V
    .locals 11

    .prologue
    .line 101
    const/16 v7, 0x66

    .line 103
    .local v7, "xor_key":B
    :try_start_0
    iget-object v8, p0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->context:Landroid/content/Context;

    invoke-virtual {v8}, Landroid/content/Context;->getAssets()Landroid/content/res/AssetManager;

    move-result-object v8

    const-string v9, "encryptedDex"

    const/4 v10, 0x0

    invoke-virtual {v8, v9, v10}, Landroid/content/res/AssetManager;->open(Ljava/lang/String;I)Ljava/io/InputStream;

    move-result-object v5

    .line 104
    .local v5, "inputStream":Ljava/io/InputStream;
    new-instance v1, Ljava/io/File;

    iget-object v8, p0, Lcom/wnagzihxain/sourceapk/ProtectApplication;->apkPath:Ljava/lang/String;

    invoke-direct {v1, v8}, Ljava/io/File;-><init>(Ljava/lang/String;)V

    .line 105
    .local v1, "dexFile_save":Ljava/io/File;
    new-instance v3, Ljava/io/FileOutputStream;

    invoke-direct {v3, v1}, Ljava/io/FileOutputStream;-><init>(Ljava/io/File;)V

    .line 106
    .local v3, "fileOutputStream":Ljava/io/FileOutputStream;
    invoke-virtual {v5}, Ljava/io/InputStream;->available()I

    move-result v6

    .line 107
    .local v6, "myDexlength":I
    new-array v0, v6, [B

    .line 109
    .local v0, "buffer_temp":[B
    invoke-virtual {v5, v0}, Ljava/io/InputStream;->read([B)I

    .line 110
    const/4 v4, 0x0

    .local v4, "i":I
    :goto_0
    if-ge v4, v6, :cond_0

    .line 111
    aget-byte v8, v0, v4

    xor-int/2addr v8, v7

    int-to-byte v8, v8

    aput-byte v8, v0, v4

    .line 110
    add-int/lit8 v4, v4, 0x1

    goto :goto_0

    .line 113
    :cond_0
    invoke-virtual {v3, v0}, Ljava/io/FileOutputStream;->write([B)V

    .line 114
    invoke-virtual {v3}, Ljava/io/FileOutputStream;->flush()V

    .line 115
    invoke-virtual {v5}, Ljava/io/InputStream;->close()V

    .line 116
    invoke-virtual {v3}, Ljava/io/FileOutputStream;->close()V
    :try_end_0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_0} :catch_0

    .line 120
    .end local v0    # "buffer_temp":[B
    .end local v1    # "dexFile_save":Ljava/io/File;
    .end local v3    # "fileOutputStream":Ljava/io/FileOutputStream;
    .end local v4    # "i":I
    .end local v5    # "inputStream":Ljava/io/InputStream;
    .end local v6    # "myDexlength":I
    :goto_1
    return-void

    .line 117
    :catch_0
    move-exception v2

    .line 118
    .local v2, "e":Ljava/lang/Exception;
    invoke-virtual {v2}, Ljava/lang/Exception;->printStackTrace()V

    goto :goto_1
.end method
