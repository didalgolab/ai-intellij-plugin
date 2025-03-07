/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.models;

import com.didalgo.intellij.chatgpt.settings.GeneralSettings;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi;

public class AnthropicModelFamily implements ModelFamily {

    @Override
    public AnthropicChatModel createChatModel(GeneralSettings.AssistantOptions config) {
        var baseUrl = config.isEnableCustomApiEndpointUrl() ? config.getApiEndpointUrl() : getDefaultApiEndpointUrl();
        var apiKey = config.getApiKey();
        var api = new AnthropicApi(baseUrl, apiKey);
        var options = AnthropicChatOptions.builder()
                .model(config.getModelName())
                .temperature(config.getTemperature())
                .topP(config.getTopP())
                .maxTokens(4096)
                .build();
        return new AnthropicChatModel(api, options);
    }

    @Override
    public String getDefaultApiEndpointUrl() {
        return AnthropicApi.DEFAULT_BASE_URL;
    }

    @Override
    public String getApiKeysHomepage() {
        return "https://console.anthropic.com/settings/keys";
    }
}
