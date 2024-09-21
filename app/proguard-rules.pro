# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
# request, reponse data class which are used with Retrofit
-keep class com.ku_stacks.ku_ring.remote.department.request.** { *; }
-keep class com.ku_stacks.ku_ring.remote.department.response.** { *; }
-keep class com.ku_stacks.ku_ring.remote.notice.request.** { *; }
-keep class com.ku_stacks.ku_ring.remote.notice.response.** { *; }
-keep class com.ku_stacks.ku_ring.remote.notice.** { *; }
-keep class com.ku_stacks.ku_ring.remote.staff.response.** { *; }
-keep class com.ku_stacks.ku_ring.remote.user.request.** { *; }
-keep class com.ku_stacks.ku_ring.remote.util.** { *; }
# Room DAO, entity
-keep class com.ku_stacks.ku_ring.local.entity.** { *; }
-keep class com.ku_stacks.ku_ring.local.room.** { *; }
# domain classes
-keep class com.ku_stacks.ku_ring.domain.** { *; }
# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep interface retrofit2.Call
-keep class retrofit2.Response
# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep class kotlin.coroutines.Continuation
-dontwarn com.sendbird.android.shadow.**
-dontwarn com.google.firebase.messaging.TopicOperation$TopicOperations
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
