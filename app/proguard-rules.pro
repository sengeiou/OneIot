# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ivan/Software/AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
# -ignorewarnings

-keepattributes SourceFile,LineNumberTable

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keep class **$Properties

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# iwds
-dontwarn android.bluetooth.client.**
-dontwarn com.ingenic.iwds.**
-keep class android.bluetooth.client.** { *; }
-keep class com.ingenic.iwds.** { *; }

#numberpicker
-dontwarn net.simonvt.numberpicker.**
-keep class net.simonvt.numberpicker.** { *; }

# fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

# baidu location
-dontwarn com.baidu.location.**
-keep class com.baidu.location.** { *; }

#leancloud
-dontwarn com.avos.avoscloud.**
-keep class com.avos.avoscloud.** { *; }

-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** { *; }

# greendao
-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.** { *; }

-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
public static java.lang.String TABLENAME;
}

#baidu Map
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-dontwarn com.viewpagerindicator.**

# volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
-keep class com.coband.cocoband.bean.LastVersion {*;}

-keep public class com.coband.watchassistant.R$*{
    public static final int *;
}

-keep class com.github.mikephil.charting.** { *; }

-keep class com.coband.cocoband.widget.chat.** {*;}

-keep class cn.leancloud.chatkit.** {*;}
-keep class com.sj.emoji.** {*;}
-keep class sj.keyboard.** {*;}

# add by ivan
-keep class com.coband.cocoband.bean.** {*;}

-keep class com.google.** {*;}
-keep class com.realsil.** {*;}
-keep class com.yc.** {*;}

# retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.fasterxml.**
-dontwarn retrofit2.**

-keep class com.coband.cocoband.mvp.model.bean.** {*;}
-keep class com.coband.cocoband.mvp.model.entity.** {*;}
-keep class com.coband.cocoband.me.viewholder.** {*;}
-keep class com.coband.cocoband.message.viewholder.** {*;}
-keep class com.coband.cocoband.guide.viewholder.** {*;}
-keep class com.coband.dfu.network.** {*;}

# UTE Library
-keep class com.yc.** {*;}
-keep class com.realsil.** {*;}
-keep class com.google.i18n.phonenumbers.** {*;}


# iMCO SDK
-keep class com.coband.dfu.** {*;}
-keep class com.coband.interactivelayer.** {*;}
-keep class com.coband.protocollayer.** {*;}
-keep class retrofit2.converter.gson.** {*;}


-keep class com.coband.cocoband.mvp.model.remote.device.UTEService
-keep class com.coband.cocoband.mvp.model.remote.device.K9Service

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

-keep class com.tencent.mm.opensdk.** { *; }

-keep class com.tencent.wxop.** { *; }

-keep class com.tencent.mm.sdk.** { *; }

-keep class com.owen.library.sharelib.model.** { *; }