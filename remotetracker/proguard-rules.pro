-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations
!code/simplification/arithmetic,!field/*,!class/merging/*
-keep public
class * extends android.app.Activity
-keep public class * extends
android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep
public class * extends android.content.ContentProvider
-keep public class
* extends android.app.backup.BackupAgentHelper
-keep public class *
extends android.preference.Preference
-keep public class
com.android.vending.licensing.ILicensingService
#keep all classes that might be used in XML layouts
-keep public class *
extends
android.view.View
-keep public class * extends
android.app.Fragment
-keep
public class * extends
android.support.v4.Fragment
#keep all public and protected methods that could be used by java reflection
-keepclassmembernames class * {
public protected
<methods>
 ;
 }
 -keepclasseswithmembernames class * {
 native
 <methods>
  ;
  }
  -keepclasseswithmembernames class * {
  public
  <init>
   (android.content.Context, android.util.AttributeSet);
   }
   -keepclasseswithmembernames class * {
   public
   <init>(android.content.Context, android.util.AttributeSet, int);
    }
    -keepclassmembers enum * {
    public static **[] values();
    public static
    ** valueOf(java.lang.String);
    }
    -keep class * implements
    android.os.Parcelable {
    public static final
    android.os.Parcelable$Creator *;
    }
 -dontwarn org.apache.http.**
 -dontwarn android.support.v4.app.**
 -dontwarn com.android.volley.**
 -dontwarn com.squareup.picasso.**
 -dontwarn com.activate.gcm.**
 -dontwarn com.google.android.gms.**

-ignorewarnings
