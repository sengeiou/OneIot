# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/mai/Android/Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
# -ignorewarnings　

-keepattributes SourceFile,LineNumberTable

#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.fasterxml.**
-dontwarn retrofit2.**

# imcosdk
-keep class com.coband.dfu.network.bean.** {*;}
-keep class com.coband.dfu.OtaCallback {*;}
-keep class com.coband.dfu.OtaProxy {*;}
-keep class com.coband.dfu.** {*;}
-keep class com.coband.interactivelayer.bean.** {*;}
-keep class com.coband.interactivelayer.manager.CommandManager {*;}
-keep class com.coband.interactivelayer.manager.CommandCallback {*;}
-keep class com.coband.interactivelayer.manager.ConnectManager {*;}
-keep class com.coband.interactivelayer.manager.ConnectCallback {*;}
-keep class com.coband.interactivelayer.Flags {*;}
-keep class java.lang.annotation.**　{*;}
-keep class com.coband.interactivelayer.manager.SendCommandCallback {*;}
#打印混淆信息
-verbose
#代码优化选项，不加该行会将没有用到的类删除，这里为了验证时间结果而使用，在实际生产环境中可根据实际需要选择是否使用
-dontshrink
-dontwarn android.support.annotation.Keep
#保留注解，如果不添加改行会导致我们的@Keep注解失效
-keepattributes *Annotation*
-keep @android.support.annotation.Keep class **{
@android.support.annotation.Keep *;
}
