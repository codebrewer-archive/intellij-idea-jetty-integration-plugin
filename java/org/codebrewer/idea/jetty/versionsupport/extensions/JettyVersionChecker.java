/*
 * Copyright 2007, 2010 Mark Scott
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codebrewer.idea.jetty.versionsupport.extensions;

import com.intellij.openapi.extensions.ExtensionPointName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Mark Scott
 * @version $Id: JettyVersionChecker.java 6 2007-02-10 23:33:18Z mark $
 */
public interface JettyVersionChecker
{
  ExtensionPointName<JettyVersionChecker> EXTENSION_POINT_NAME =
    ExtensionPointName.create("org.codebrewer.idea.jetty.jettyVersionChecker");

  @Nullable
  String getVersionString(@NotNull final File jettyHomeDir);
}
