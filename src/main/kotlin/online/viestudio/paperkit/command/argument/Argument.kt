package online.viestudio.paperkit.command.argument

import online.viestudio.paperkit.command.Arguments

typealias Validator = suspend (args: Arguments, input: String) -> String?
typealias Completer = suspend (args: Arguments, input: String) -> List<String>

interface Argument {

    val name: String
    val description: String
    val isRequired: Boolean
    val validator: Validator
    val completer: Completer

    interface Builder {

        var name: String
        var description: String
        var isRequired: Boolean
        var validator: Validator
        var completer: Completer

        fun name(name: String): Builder

        fun description(description: String): Builder

        fun isRequired(isRequired: Boolean): Builder

        fun required(): Builder

        fun validator(validator: Validator): Builder

        fun completer(completer: Completer): Builder

        fun build(): Argument
    }

    companion object {

        fun builder() = ArgumentImpl.Builder()
    }
}