import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

fun PluginDependenciesSpec.kuringPrimitive(pluginName: String): PluginDependencySpec {
    return id("com.ku_stacks.ku_ring.buildlogic.primitive.$pluginName")
}

fun PluginDependenciesSpec.kuring(pluginName: String): PluginDependencySpec {
    return id("com.ku_stacks.ku_ring.buildlogic.$pluginName")
}
