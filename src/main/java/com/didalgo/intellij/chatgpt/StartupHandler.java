/*
 * Copyright (c) 2024 Mariusz Bernacki <consulting@didalgo.com>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.didalgo.intellij.chatgpt;

import com.didalgo.intellij.chatgpt.settings.GeneralSettings;
import com.didalgo.intellij.chatgpt.ui.action.editor.ActionsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.model.ModelOptionsUtils;

public class StartupHandler implements StartupActivity {

    private @Getter static volatile boolean fullyStarted;


    @Override
    public void runActivity(@NotNull Project project) {
        try {
            initJacksonUtilsEagerly();
            GeneralSettings.getInstance();
            ActionsUtil.refreshActions();
        } finally {
            fullyStarted = true;
        }
    }

    private static void initJacksonUtilsEagerly() {
        ClassLoader previous = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(StartupHandler.class.getClassLoader());
            ModelOptionsUtils.OBJECT_MAPPER.getClass();
        } finally {
            Thread.currentThread().setContextClassLoader(previous);
        }
    }
}
