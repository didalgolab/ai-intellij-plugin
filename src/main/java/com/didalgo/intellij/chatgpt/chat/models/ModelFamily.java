/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.models;

import com.didalgo.intellij.chatgpt.settings.GeneralSettings;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public interface ModelFamily {

    ModelFamily OPEN_AI = new OpenAiModelFamily();

    ModelFamily AZURE_OPENAI = new AzureOpenAiModelFamily();

    ModelFamily ANTHROPIC = new AnthropicModelFamily();

    ModelFamily GEMINI = new GeminiModelFamily();

    ModelFamily OLLAMA = new OllamaModelFamily();

    ChatModel createChatModel(GeneralSettings.AssistantOptions config);

    String getDefaultApiEndpointUrl();

    default List<String> getCompatibleApiEndpointUrls() {
        return List.of();
    }

    String getApiKeysHomepage();

    default boolean isApiKeyOptional() {
        return "".equals(getApiKeysHomepage());
    }

    static ModelFamily create(Class<? extends ModelFamily> clazz) {
        return Arrays.stream(ModelFamily.class.getFields())
                .filter(field -> field.getType().equals(clazz) && ReflectionUtils.isPublicStaticFinal(field))
                .findFirst()
                .map(field -> {
                    try {
                        return (ModelFamily) field.get(null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseGet(() -> {
                    try {
                        return clazz.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    static Consumer<RestClient.Builder> defaultTimeout() {
        return restClientBuilder -> {
            var reactorHttpClient = HttpClient.create()
                    .responseTimeout(
                            Duration.ofMillis(Integer.parseInt(GeneralSettings.getInstance().getReadTimeout())));
            restClientBuilder.requestFactory(new ReactorClientHttpRequestFactory(reactorHttpClient));
        };
    }
}
