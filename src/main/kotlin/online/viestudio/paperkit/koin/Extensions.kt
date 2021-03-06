package online.viestudio.paperkit.koin

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.getScopeId
import org.koin.core.component.inject
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.StringQualifier
import org.koin.mp.KoinPlatformTools
import kotlin.properties.ReadOnlyProperty

inline fun <reified T : Any> KoinComponent.config(): ReadOnlyProperty<Any?, T> = ReadOnlyProperty { _, _ -> get() }

inline fun <reified T : Any> KoinComponent.plugin(mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode()): Lazy<T> {
    return inject(qualifier = pluginQualifier, mode = mode)
}

val Any.pluginQualifier: Qualifier get() = StringQualifier(this::class.java.classLoader.getScopeId())
