#-----------------------------------
# Common Configuration
#-----------------------------------
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, Exceptions
-dontwarn kotlin.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { <fields>; }
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

#-----------------------------------
# Project / Models
#-----------------------------------
-keep class co.jatri.domain.** { *; }
-keep class co.jatri.domain.roomentity.** { *; }
-keep class com.jatri.entity.** { *; }
-keep class co.jatri.apiresponse.** { *; }

#-----------------------------------
# Gson
#-----------------------------------
-keep class com.google.gson.** { *; }
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.*

#-----------------------------------
# Retrofit / OkHttp / RxJava
#-----------------------------------
-dontwarn retrofit.**, okhttp3.**, okio.**, rx.**, sun.misc.**, org.conscrypt.**, org.bouncycastle.**, org.openjsse.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keepclassmembers class * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation interface retrofit2.Call
-keep,allowobfuscation class retrofit2.Response

#-----------------------------------
# Firebase
#-----------------------------------
-dontwarn com.google.firebase.**
-keep class com.google.firebase.** { *; }

#-----------------------------------
# AndroidX / Lifecycle
#-----------------------------------
-keep class androidx.lifecycle.** { *; }
-keepclassmembers class * {
    @androidx.lifecycle.OnLifecycleEvent *;
}
-dontwarn androidx.lifecycle.**

#-----------------------------------
# Timber / Logging
#-----------------------------------
-dontwarn org.jetbrains.annotations.**

#-----------------------------------
# Image Libraries (Picasso / Okio)
#-----------------------------------
-dontwarn okio.**
-keep class androidx.appcompat.widget.** { *; }

#-----------------------------------
# Pusher Java Client
#-----------------------------------
-dontwarn org.slf4j.impl.StaticLoggerBinder
