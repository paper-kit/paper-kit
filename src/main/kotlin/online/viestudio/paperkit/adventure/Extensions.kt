package online.viestudio.paperkit.adventure

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage

fun hexColor(hex: String) = TextColor.fromHexString(hex)

inline fun text(block: TextComponent.Builder.() -> Unit) = Component.text().apply(block).build()

fun <T : Audience> T.message(text: String) {
    sendMessage(MiniMessage.miniMessage().deserialize(text))
}

inline fun <T : Audience> T.message(block: TextComponent.Builder.(T) -> Unit) {
    val component = Component.text()
    component.block(this)
    sendMessage(component.build())
}

inline fun TextComponent.Builder.showTextOnHover(block: TextComponent.Builder.() -> Unit) {
    hoverEvent(HoverEvent.showText(text(block)))
}

inline infix fun TextComponent.Builder.appendText(block: TextComponent.Builder.() -> Unit): TextComponent.Builder {
    append(text(block))
    return this
}

infix fun TextComponent.Builder.appendText(string: String): TextComponent.Builder {
    append(Component.text(string))
    return this
}