package online.viestudio.paperkit.command.argument

import kotlin.reflect.KClass
import kotlin.reflect.full.staticFunctions

inline fun Argument.Builder.intValidator(crossinline onNotInt: suspend () -> String) {
    validator { _, input ->
        val int = input.toIntOrNull()
        if (int == null) {
            onNotInt()
        } else null
    }
}

inline fun Argument.Builder.decimalValidator(crossinline onNotDec: suspend () -> String) {
    validator { _, input ->
        val int = input.toDoubleOrNull()
        if (int == null) {
            onNotDec()
        } else null
    }
}

inline fun Argument.Builder.inputValidator(crossinline validator: suspend (String) -> String?) {
    validator { _, input -> validator(input) }
}

inline fun <T : Any> Argument.Builder.enumValidator(
    enum: KClass<T>,
    crossinline prepare: suspend (String) -> String = { it },
    crossinline onNotEnum: suspend () -> String,
) {
    validator { _, input ->
        val preparedInput = prepare(input)
        val enumInstance = runCatching {
            enum.staticFunctions.find { it.name == "valueOf" }!!.call(preparedInput)
        }.getOrNull()
        if (enumInstance == null) {
            onNotEnum()
        } else null
    }
}