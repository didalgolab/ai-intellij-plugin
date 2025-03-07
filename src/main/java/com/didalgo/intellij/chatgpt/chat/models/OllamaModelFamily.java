/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.models;

import com.didalgo.intellij.chatgpt.settings.GeneralSettings;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.observation.ChatModelObservationConvention;
import org.springframework.ai.chat.observation.DefaultChatModelObservationConvention;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;

public class OllamaModelFamily implements ModelFamily {

    private final static String DEFAULT_BASE_URL = "http://localhost:11434";

    private static final ChatModelObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultChatModelObservationConvention();

    private static final ToolCallingManager DEFAULT_TOOL_CALLING_MANAGER = ToolCallingManager.builder().build();

    @Override
    public OllamaChatModel createChatModel(GeneralSettings.AssistantOptions config) {
        var baseUrl = config.isEnableCustomApiEndpointUrl()? config.getApiEndpointUrl(): getDefaultApiEndpointUrl();
        var api = new OllamaApi(baseUrl);
        var options = OllamaOptions.builder()
                .model(config.getModelName())
                .temperature(config.getTemperature())
                .topP(config.getTopP())
                .build();
        return new OllamaChatModel(api, options, DEFAULT_TOOL_CALLING_MANAGER, ObservationRegistry.NOOP, ModelManagementOptions.defaults());
    }

    @Override
    public String getDefaultApiEndpointUrl() {
        return DEFAULT_BASE_URL;
    }

    @Override
    public String getApiKeysHomepage() {
        return "";
    }
}
