/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.models;

import com.didalgo.intellij.chatgpt.settings.GeneralSettings;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

public class GeminiModelFamily implements ModelFamily {

    public static final String STANDARD_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";

    public static final String OPENAI_COMPATIBLE_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    @Override
    public OpenAiChatModel createChatModel(GeneralSettings.AssistantOptions config) {
        var baseUrl = config.isEnableCustomApiEndpointUrl()? config.getApiEndpointUrl(): getDefaultApiEndpointUrl();
        if ("https://generativelanguage.googleapis.com".equals(baseUrl)) {
            baseUrl = OPENAI_COMPATIBLE_BASE_URL;
        }

        var api = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .completionsPath("/chat/completions")
                .embeddingsPath("/embeddings")
                .apiKey(config.getApiKey())
                .build();
        var options = OpenAiChatOptions.builder()
                .model(config.getModelName())
                .temperature(config.getTemperature())
                //.withStreamUsage(config.isEnableStreamOptions())
                .topP(config.getTopP())
                .N(1)
                .build();
        return new OpenAiChatModel(api, options);
    }

    @Override
    public String getDefaultApiEndpointUrl() {
        return OPENAI_COMPATIBLE_BASE_URL;
    }

    @Override
    public String getApiKeysHomepage() {
        return "https://aistudio.google.com/app/apikey";
    }
}
