/*
 * Copyright 2010 Mark Scott
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
import org.codebrewer.idea.jetty.JettyException;
import org.codebrewer.idea.jetty.versionsupport.JettyVersionHelper;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * <code>JettyVersionHelperProvider</code> defines an extension point for helper
 * providers, objects that provide helpers for various versions of Jetty.
 * </p>
 *
 * @author Mark Scott
 * @version $Id$
 */
public interface JettyVersionHelperProvider
{
  ExtensionPointName<JettyVersionHelperProvider> EXTENSION_POINT_NAME =
    ExtensionPointName.create("org.codebrewer.idea.jetty.jettyVersionHelperProvider");

  @NotNull
  JettyVersionHelper getJettyVersionHelper(@NotNull String versionString) throws JettyException;
}
