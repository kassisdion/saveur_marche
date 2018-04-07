#see https://github.com/square/okhttp#proguard
-dontwarn okhttp3.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Oki (see https://github.com/square/okio/issues/60)
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
