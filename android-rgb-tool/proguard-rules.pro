# Application specific
-keepattributes *Annotation*

# PhotoView
-keep class uk.co.senab.photoview.** { *; }
-keep interface uk.co.senab.photoview.** { *; }

# Picasso
-dontwarn com.squareup.okhttp3.**

# AndroidX Library
-keep class androidx.appcompat.widget.ShareActionProvider { *; }

# Greenbot EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# RxAndroid
-dontwarn rx.internal.util.unsafe.**

# Android Material Library
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }
-keep interface com.google.android.material.** { *; }

# Retrolambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
