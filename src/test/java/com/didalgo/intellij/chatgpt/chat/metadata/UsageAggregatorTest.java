/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.metadata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.ai.chat.metadata.Usage;

import static org.junit.jupiter.api.Assertions.*;

class UsageAggregatorTest {

    private final UsageAggregator aggregator = new UsageAggregator();

    @Test
    void is_zero_initially() {
        assertUsageEquals(0, 0);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 20, 10, 20",
            "5, 30, 5, 30",
            "25, 15, 25, 15"
    })
    void accept_accumulates_usage(int promptTokens, int generationTokens, int expectedPrompt, int expectedGeneration) {
        aggregator.accept(usage(promptTokens, generationTokens));
        assertUsageEquals(expectedPrompt, expectedGeneration);
    }

    @Test
    void accept_accumulates_maximum_usage_from_multiple_values() {
        aggregator.accept(usage(25, 20));
        aggregator.accept(usage(0, 30));
        aggregator.accept(usage(0, 0));

        assertUsageEquals(25, 30);
    }

    @Test
    void accept_does_not_accumulate_null_values() {
        aggregator.accept(usage(null, 1));
        aggregator.accept(null);

        assertUsageEquals(0, 1);
    }

    private static Usage usage(Integer promptTokens, Integer completionTokens) {
        return new ImmutableUsage(promptTokens, completionTokens, null);
    }

    private void assertUsageEquals(int expectedPromptTokens, int expectedCompletionTokens) {
        assertAll(
                () -> assertEquals(expectedPromptTokens, aggregator.getPromptTokens()),
                () -> assertEquals(expectedPromptTokens, aggregator.toImmutableUsage().getPromptTokens()),
                () -> assertEquals(expectedCompletionTokens, aggregator.getCompletionTokens()),
                () -> assertEquals(expectedCompletionTokens, aggregator.toImmutableUsage().getCompletionTokens())
        );
    }
}