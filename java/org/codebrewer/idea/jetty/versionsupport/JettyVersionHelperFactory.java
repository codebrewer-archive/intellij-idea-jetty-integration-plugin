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
package org.codebrewer.idea.jetty.versionsupport;

import com.intellij.openapi.extensions.Extensions;
import org.codebrewer.idea.jetty.JettyException;
import org.codebrewer.idea.jetty.versionsupport.extensions.JettyVersionChecker;
import org.codebrewer.idea.jetty.versionsupport.extensions.JettyVersionHelperProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * <p>
 * A factory that uses registered extension point implementations to find a
 * {@link JettyVersionHelper} for a Jetty installation.
 * </p>
 *
 * @author Mark Scott
 * @version $Id$
 */
public class JettyVersionHelperFactory
{
  public static final JettyVersionHelperFactory INSTANCE = new JettyVersionHelperFactory();

  @Nullable
  public JettyVersionHelper getJettyVersionHelper(@NotNull File jettyHomeDir)
  {
    final JettyVersionChecker[] versionCheckers =
      Extensions.getExtensions(JettyVersionChecker.EXTENSION_POINT_NAME);

    for (final JettyVersionChecker jettyVersionChecker : versionCheckers) {
      final String jettyVersion = jettyVersionChecker.getVersionString(jettyHomeDir);

      if (jettyVersion != null) {
        final JettyVersionHelperProvider[] versionHelperProviders =
          Extensions.getExtensions(JettyVersionHelperProvider.EXTENSION_POINT_NAME);

        for (final JettyVersionHelperProvider versionHelperProvider : versionHelperProviders) {
          try {
            return versionHelperProvider.getJettyVersionHelper(jettyVersion);
          }
          catch (JettyException ignore) {
          }
        }
      }
    }

    return null;
  }

  private JettyVersionHelperFactory()
  {
  }
}
