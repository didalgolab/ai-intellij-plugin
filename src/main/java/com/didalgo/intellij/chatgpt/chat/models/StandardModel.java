/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.models;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum StandardModel implements ModelType {
    CLAUDE_3_7_SONNET("claude-3-7-sonnet-latest", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_7_SONNET_20250219("claude-3-7-sonnet-20250219", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_5_SONNET("claude-3-5-sonnet-latest", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_5_SONNET_20241022("claude-3-5-sonnet-20241022", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_5_SONNET_20240620("claude-3-5-sonnet-20240620", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_OPUS("claude-3-opus-20240229", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_SONNET("claude-3-sonnet-20240229", ModelFamily.ANTHROPIC, 200000),
    CLAUDE_3_HAIKU("claude-3-haiku-20240307", ModelFamily.ANTHROPIC, 200000),

    GEMINI_1_5_PRO_LATEST("gemini-1.5-pro-latest", ModelFamily.GEMINI, 2097152),
    GEMINI_1_5_FLASH_LATEST("gemini-1.5-flash-latest", ModelFamily.GEMINI, 1048576),
    GEMINI_2_0_FLASH("gemini-2.0-flash", ModelFamily.GEMINI, 1048576),

    GPT_3_5_TURBO("gpt-3.5-turbo", ModelFamily.OPEN_AI, 16385),
    GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301", ModelFamily.OPEN_AI, 4096),
    GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613", ModelFamily.OPEN_AI, 4096),
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k", ModelFamily.OPEN_AI, 16384),
    GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613", ModelFamily.OPEN_AI, 16384),
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106", ModelFamily.OPEN_AI, 16385),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125", ModelFamily.OPEN_AI, 16385),
    GPT_4_O("gpt-4o", ModelFamily.OPEN_AI, 128000),
    GPT_4_O_MINI("gpt-4o-mini", ModelFamily.OPEN_AI, 128000),
    GPT_4_TURBO("gpt-4-turbo", ModelFamily.OPEN_AI, 128000),
    GPT_4("gpt-4", ModelFamily.OPEN_AI, 8192),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview", ModelFamily.OPEN_AI, 128000),
    GPT_4_0314("gpt-4-0314", ModelFamily.OPEN_AI, 8192),
    GPT_4_0613("gpt-4-0613", ModelFamily.OPEN_AI, 8192),
    GPT_4_32K("gpt-4-32k", ModelFamily.OPEN_AI, 32768),
    GPT_4_32K_0314("gpt-4-32k-0314", ModelFamily.OPEN_AI, 32768),
    GPT_4_32K_0613("gpt-4-32k-0613", ModelFamily.OPEN_AI, 32768),
    GPT_4_1106_PREVIEW("gpt-4-1106-preview", ModelFamily.OPEN_AI, 128000),
    GPT_4_0125_PREVIEW("gpt-4-0125-preview", ModelFamily.OPEN_AI, 128000),

    O3_MINI("o3-mini", ModelFamily.OPEN_AI, 200000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O3_MINI_2025_01_31("o3-mini-2025-01-31", ModelFamily.OPEN_AI, 200000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O1("o1", ModelFamily.OPEN_AI, 200000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O1_2024_12_17("o1-2024-12-17", ModelFamily.OPEN_AI, 200000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O1_MINI("o1-mini", ModelFamily.OPEN_AI, 128000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O1_MINI_2024_09_12("o1-mini-2024-09-12", ModelFamily.OPEN_AI, 128000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O1_PREVIEW("o1-preview", ModelFamily.OPEN_AI, 128000, false, false, IncompatibleChatOptions.O1_OVERRIDE),
    O1_PREVIEW_2024_09_12("o1-preview-2024-09-12", ModelFamily.OPEN_AI, 128000, false, false, IncompatibleChatOptions.O1_OVERRIDE);

    private final ModelFamily family;
    private final String id;
    private final int inputTokenLimit;
    private final boolean supportsStreaming;
    private final boolean supportsSystemMessage;
    private final ChatOptions incompatibleChatOptionsOverride;

    StandardModel(String id, ModelFamily family, int inputTokenLimit) {
        this(id, family, inputTokenLimit, true, true, OVERRIDE_NONE);
    }

    StandardModel(String id, ModelFamily family, int inputTokenLimit, boolean supportsStreaming, boolean supportsSystemMessage, ChatOptions incompatibleChatOptionsOverride) {
        this.id = id;
        this.family = family;
        this.inputTokenLimit = inputTokenLimit;
        this.supportsStreaming = supportsStreaming;
        this.supportsSystemMessage = supportsSystemMessage;
        this.incompatibleChatOptionsOverride = incompatibleChatOptionsOverride;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int getInputTokenLimit() {
        return inputTokenLimit;
    }

    @Override
    public ModelFamily getFamily() {
        return family;
    }

    @Override
    public boolean supportsStreaming() {
        return supportsStreaming;
    }

    @Override
    public boolean supportsSystemMessage() {
        return supportsSystemMessage;
    }

    @Override
    public ChatOptions incompatibleChatOptionsOverride() {
        return incompatibleChatOptionsOverride;
    }

    public static List<ModelType> getAvailableModels() {
        return Arrays.asList(values());
    }

    public static List<String> getAvailableModelIds() {
        List<String> modelIds = new ArrayList<>();
        for (StandardModel model : StandardModel.values()) {
            modelIds.add(model.id());
        }
        return modelIds;
    }

    public static StandardModel of(String id) {
        for (StandardModel model : StandardModel.values()) {
            if (model.id().equals(id)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Model `" + id + "` not found");
    }

    public static Optional<ModelType> findFirstAvailableModelInFamily(ModelFamily family) {
        for (StandardModel model : StandardModel.values()) {
            if (model.getFamily().equals(family)) {
                return Optional.of(model);
            }
        }
        return Optional.empty();
    }

    private static final class IncompatibleChatOptions {

        private static final ChatOptions O1_OVERRIDE = ChatOptionsBuilder.builder()
                .withTemperature(1.0)
                .withTopP(1.0)
                .build();
    }
}
