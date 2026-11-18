
package com.joinplus.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class MessageUtil {

    private static final MiniMessage MINI_MESSAGE =
            MiniMessage.miniMessage();

    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.legacyAmpersand();

    private MessageUtil() {
    }

    public static Component parse(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }

        if (message.contains("<") && message.contains(">")) {
            return MINI_MESSAGE.deserialize(message);
        }

        return LEGACY.deserialize(message);
    }
}
