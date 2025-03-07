/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.messages;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;

public class MessageSupport {

    private static final String ELLIPSIS = "[...]";

    public static Message substring(Message message, int fromIndex) {
        return setTextContent(message, ELLIPSIS + " " + message.getText().substring(fromIndex));
    }

    /**
     * Sets new textContent to a given {@link Message} and returns a new instance of the message with updated textContent.
     * This method supports updating textContent for {@link UserMessage}, {@link SystemMessage}, and {@link AssistantMessage}.
     * It preserves other properties of the message, such as media and metadata, where applicable.
     *
     * @param message The original message object whose textContent is to be updated. This object is not modified.
     * @param textContent The new textContent string to be set in the message.
     * @return A new instance of the message with the updated textContent. The specific type of the returned message
     *         corresponds to the type of the input message.
     * @throws IllegalArgumentException If the input message type is not supported by this method, an exception
     *                                  is thrown indicating the unsupported message type.
     */
    public static Message setTextContent(Message message, String textContent) {
        if (message instanceof UserMessage userMessage) {
            return new UserMessage(textContent, userMessage.getMedia(), userMessage.getMetadata());
        } else if (message instanceof SystemMessage) {
            return new SystemMessage(textContent);
        } else if (message instanceof AssistantMessage) {
            return new AssistantMessage(textContent, message.getMetadata());
        } else if (message instanceof ToolResponseMessage toolMessage) {
            return new ToolResponseMessage(toolMessage.getResponses(), toolMessage.getMetadata());
        } else {
            throw new IllegalArgumentException("Unsupported message type: " + message.getClass().getSimpleName());
        }
    }
}
