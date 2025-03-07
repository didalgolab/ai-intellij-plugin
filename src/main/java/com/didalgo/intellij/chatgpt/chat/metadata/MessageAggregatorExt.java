/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.didalgo.intellij.chatgpt.chat.metadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

/**
 * Helper that for streaming chat responses, aggregate the chat response messages into a
 * single AssistantMessage. Job is performed in parallel to the chat response processing.
 *
 * @author Christian Tzolov
 * @since 1.0.0
 */
public class MessageAggregatorExt extends MessageAggregator {

	private static final Logger logger = LoggerFactory.getLogger(MessageAggregatorExt.class);

	public Flux<ChatResponse> aggregate(Flux<ChatResponse> fluxChatResponse,
										Consumer<ChatResponse> onAggregationComplete) {

		AtomicReference<StringBuilder> stringBufferRef = new AtomicReference<>(new StringBuilder());
		AtomicReference<ChatResponseMetadataBuilder> responseMetadataRef = new AtomicReference<>();
		AtomicReference<Map<String, Object>> mapRef = new AtomicReference<>();

		return fluxChatResponse.doOnSubscribe(subscription -> {
			// logger.info("Aggregation Subscribe:" + subscription);
			stringBufferRef.set(new StringBuilder());
			mapRef.set(new HashMap<>());
			responseMetadataRef.set(new ChatResponseMetadataBuilder());
		}).doOnNext(chatResponse -> {
			// logger.info("Aggregation Next:" + chatResponse);
			if (chatResponse.getResult() != null) {
				if (chatResponse.getResult().getOutput().getText() != null) {
					stringBufferRef.get().append(chatResponse.getResult().getOutput().getText());
				}
				if (chatResponse.getResult().getOutput().getMetadata() != null) {
					mapRef.get().putAll(chatResponse.getResult().getOutput().getMetadata());
				}
				if (chatResponse.getMetadata() != null) {
					responseMetadataRef.get().accept(chatResponse.getMetadata());
				}
			}
		}).doOnComplete(() -> {
			// logger.debug("Aggregation Complete");
			onAggregationComplete
				.accept(
						new ChatResponse(List.of(
								new Generation(new AssistantMessage(stringBufferRef.get().toString(), mapRef.get()))), responseMetadataRef.get().build()));
			stringBufferRef.set(new StringBuilder());
			mapRef.set(new HashMap<>());
			responseMetadataRef.set(new ChatResponseMetadataBuilder());
		}).doOnError(e -> {
			logger.error("Aggregation Error", e);
		});
	}

}