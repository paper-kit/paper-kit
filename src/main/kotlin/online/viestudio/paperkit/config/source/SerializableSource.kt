package online.viestudio.paperkit.config.source

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient
import online.viestudio.paperkit.config.Comment
import online.viestudio.paperkit.util.lineSeparator
import org.bukkit.configuration.file.YamlConfiguration
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*

sealed class SerializableSource(
    private val clazz: KClass<out Any>,
) : Source {

    private val KProperty1<out Any, *>.path: String get() = findAnnotation<SerialName>()?.value ?: name

    @Suppress("UNCHECKED_CAST")
    protected fun Any.encodeToStringOrNull(): String? {
        val serializer = findSerializerOrNull() ?: return null
        return Yaml.default.encodeToString(serializer as KSerializer<Any>, this)
            .parseYamlConfiguration()
            .writeAnnotationComments(this)
    }

    private fun findSerializerOrNull(): KSerializer<*>? = with(clazz) {
        val serializer = companionObject?.memberFunctions?.find {
            it.returnType.isSubtypeOf(KSerializer::class.starProjectedType)
        } ?: return null
        return serializer.call(companionObjectInstance) as KSerializer<*>
    }

    private fun String.parseYamlConfiguration() =
        YamlConfiguration().apply { loadFromString(this@parseYamlConfiguration) }

    private fun YamlConfiguration.writeAnnotationComments(any: Any): String {
        any::class.memberProperties.filter { !it.hasAnnotation<Transient>() }.forEach {
            it.writeAnnotationComments(path = it.path, config = this, holder = any)
        }
        return saveToString()
    }

    private fun KProperty1<out Any, *>.writeAnnotationComments(
        path: String,
        holder: Any,
        config: YamlConfiguration,
    ) {
        val annotation = findAnnotation<Comment>()
        if (annotation != null) {
            config.setComments(path, annotation.content.trimIndent().split(lineSeparator))
        }
        val children = call(holder) ?: return
        val clazz = children::class
        if (!clazz.isData) return
        children::class.memberProperties.forEach {
            it.writeAnnotationComments(path = "$path.${it.path}", config = config, holder = children)
        }
    }
}