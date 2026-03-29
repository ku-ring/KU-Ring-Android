# Keep all @Serializable NavKey implementations for Navigation 3 back stack serialization
-keep @kotlinx.serialization.Serializable class * implements androidx.navigation3.runtime.NavKey { *; }
-keepnames @kotlinx.serialization.Serializable class * implements androidx.navigation3.runtime.NavKey
