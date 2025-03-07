/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt.chat.metadata;

import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.ai.chat.metadata.Usage;

import java.util.function.Consumer;

/**
 * Aggregates {@link Usage} instances into a single {@link Usage} object.
 */
@Setter
public class UsageAggregator implements Usage, Consumer<Usage> {

    /**
     *  The number of prompt tokens.
     */
    private int promptTokens;

    /**
     *  The number of generation tokens.
     */
    private int completionTokens;

    /**
     * Creates a new {@code UsageAggregator} with zero prompt and generation tokens.
     */
    public UsageAggregator() {
        this(0, 0);
    }

    /**
     * Creates a new {@code UsageAggregator} with the given prompt and generation tokens.
     *
     * @param promptTokens        the number of prompt tokens
     * @param completionTokens    the number of generation tokens
     */
    public UsageAggregator(int promptTokens, int completionTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
    }

    @Override
    public Integer getPromptTokens() {
        return promptTokens;
    }

    @Override
    public Integer getCompletionTokens() {
        return completionTokens;
    }

    @Override
    public Object getNativeUsage() {
        return null;
    }

    @Override
    public void accept(@Nullable Usage source) {
        if (source != null) {
            setPromptTokens(getMaxOrDefault(getPromptTokens(), source.getPromptTokens()));
            setCompletionTokens(getMaxOrDefault(getCompletionTokens(), source.getCompletionTokens()));
        }
    }

    private static int getMaxOrDefault(int first, @Nullable Integer second) {
        return second != null ? Math.max(first, second) : first;
    }

    /**
     * Creates an immutable usage instance (snapshot) from this object.
     *
     * @return the immutable copy
     */
    public ImmutableUsage toImmutableUsage() {
        return new ImmutableUsage(promptTokens, completionTokens, null);
    }
}