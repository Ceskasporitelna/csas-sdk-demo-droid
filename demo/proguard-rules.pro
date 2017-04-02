# --- RECOMMENDED ANDROID CONFIG FOR ALL ANDROID APPS-----------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-keepattributes Signature
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licening.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Don't mess with classes with native methods

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-assumenosideeffects class android.util.Log {
    *;
}

# Core SDK (Embedded Retrofit)
# cscore
-keep class cz.csas.cscore.CoreRestService { *; }
-keep class cz.csas.cscore.RefreshTokenResponse { *; }
# client
-keep interface cz.csas.cscore.client.rest.* { *; }
-keep class cz.csas.cscore.client.rest.* { *; }
-keep class cz.csas.cscore.client.RestService { *; }
# utils
-keep class cz.csas.cscore.utils.csjson.* { *; }
# logger
-keep class cz.csas.cscore.logger.* { *; }
# webapi
-keep class cz.csas.cscore.webapi.WebApiService { *; }
-keep class cz.csas.cscore.webapi.ListResponse { *; }
-keep class cz.csas.cscore.webapi.PaginatedListResponse { *; }
-keep class cz.csas.cscore.webapi.WebApiEntity { *; }
-keep class cz.csas.cscore.webapi.WebApiRequest { *; }
# locker
-keep class cz.csas.cscore.locker.LockerRestService { *; }
-keep class cz.csas.cscore.locker.LockerRequest { *; }
-keep class cz.csas.cscore.locker.LockerRequestJson { *; }
-keep class cz.csas.cscore.locker.LockerResponse { *; }
-keep class cz.csas.cscore.locker.OneTimePasswordUnlockRequestJson { *; }
-keep class cz.csas.cscore.locker.PasswordRequestJson { *; }
-keep class cz.csas.cscore.locker.PasswordResponseJson { *; }
-keep class cz.csas.cscore.locker.RegistrationOrUnlockResponseJson { *; }
-keep class cz.csas.cscore.locker.RegistrationRequestJson { *; }
-keep class cz.csas.cscore.locker.UnlockRequestJson { *; }
-keep class cz.csas.cscore.locker.UnregisterRequestJson { *; }

# Uniforms SDK
-keep class cz.csas.uniforms.Attachment { *; }
-keep class cz.csas.uniforms.Branch { *; }
-keep class cz.csas.uniforms.FieldGroup { *; }
-keep class cz.csas.uniforms.FilledForm { *; }
-keep class cz.csas.uniforms.FilledFormField { *; }
-keep class cz.csas.uniforms.FilledFormFieldWithMessage { *; }
-keep class cz.csas.uniforms.FilledFormRequest { *; }
-keep class cz.csas.uniforms.Form { *; }
-keep class cz.csas.uniforms.FormField { *; }
-keep class cz.csas.uniforms.FormListing { *; }
-keep class cz.csas.uniforms.FormListResponse { *; }
-keep class cz.csas.uniforms.UpdateFilledFormRequest { *; }
-keep class cz.csas.uniforms.Validation { *; }

# Places SDK
-keep class cz.csas.places.AddressSuggestion { *; }
-keep class cz.csas.places.ATM { *; }
-keep class cz.csas.places.AutocompleteAddress { *; }
-keep class cz.csas.places.AutocompleteCity { *; }
-keep class cz.csas.places.AutocompletePostCode { *; }
-keep class cz.csas.places.Branch { *; }
-keep class cz.csas.places.CashWithdrawal { *; }
-keep class cz.csas.places.Location { *; }
-keep class cz.csas.places.NamedFlag { *; }
-keep class cz.csas.places.OpeningHours { *; }
-keep class cz.csas.places.Outage { *; }
-keep class cz.csas.places.Photo { *; }
-keep class cz.csas.places.Place { *; }
-keep class cz.csas.places.Product { *; }
-keep class cz.csas.places.Service { *; }
-keep class cz.csas.places.Specialist { *; }
-keep class cz.csas.places.SpecialistType { *; }
-keep class cz.csas.places.TimeInterval { *; }
-keep class cz.csas.places.ValidAddress { *; }

-keep class cz.csas.places.places.PlaceAroundListResponse { *; }
-keep class cz.csas.places.places.PlaceListResponse { *; }
-keep class cz.csas.places.places.PlaceWithinListResponse { *; }

-keep class cz.csas.places.branches.BranchAroundListResponse { *; }
-keep class cz.csas.places.branches.BranchWithinListResponse { *; }
-keep class cz.csas.places.branches.BranchListResponse { *; }
-keep class cz.csas.places.branches.BranchFlags { *; }
-keep class cz.csas.places.branches.PhotoListResponse { *; }
-keep class cz.csas.places.branches.ProductListResponse { *; }
-keep class cz.csas.places.branches.SpecialistListResponse { *; }

-keep class cz.csas.places.autocomplete.AutocompleteAddressListResponse { *; }
-keep class cz.csas.places.autocomplete.AutocompleteCityListResponse { *; }
-keep class cz.csas.places.autocomplete.AutocompletePostCodeListResponse { *; }

-keep class cz.csas.places.atms.ATMAroundListResponse { *; }
-keep class cz.csas.places.atms.ATMWithinListResponse { *; }
-keep class cz.csas.places.atms.ATMListResponse { *; }
-keep class cz.csas.places.atms.ATMsServicesListResponse { *; }
-keep class cz.csas.places.atms.ATMFlag { *; }
-keep class cz.csas.places.atms.ATMFlags { *; }

-keep class cz.csas.places.addresses.AddressSuggestionListResponse { *; }
-keep class cz.csas.places.addresses.AddressSuggestionRequest { *; }
-keep class cz.csas.places.addresses.AddressSuggestionResponse { *; }
-keep class cz.csas.places.addresses.ValidateAddressRequest { *; }
-keep class cz.csas.places.addresses.ValidateAddressResponse { *; }

# AppMenu SDK
-keep class cz.csas.appmenu.applications.ApplicationListResponse { *; }
-keep class cz.csas.appmenu.Application { *; }


-dontwarn cz.csas.cscore.**

# ButterKnife

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Picasso
-dontwarn com.squareup.okhttp.**