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
package org.codebrewer.idea.jetty;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * <p>
 * A version checker that attempts to read information from the manifest of a
 * Jetty installation's start.jar file.  The Implementation-Version attribute
 * has been used to store version information since some point in the life of
 * Jetty 6.
 * </p>
 *
 * @author Mark Scott
 * @version $Id$
 */
public class JettyStartJarVersionChecker implements JettyVersionChecker
{
  public static final String START_JAR_FILE_NAME = "start.jar";

  @Override
  public String getVersion(@NotNull String jettyHomeDir)
  {
    String version = null;
    final File startFile = new File(jettyHomeDir, START_JAR_FILE_NAME);

    if (startFile.exists() && startFile.canRead()) {
      try {
        final JarFile startJar = new JarFile(startFile);
        final Manifest manifest = startJar.getManifest();

        if (manifest != null) {
          final Attributes mainAttributes = manifest.getMainAttributes();
          version = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);

          if (version == null) {
            // Early use of this attribute used a lowercase representation
            //
            version = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION.toString().toLowerCase());
          }
        }
      }
      catch (IOException ignore) {
      }
    }

    return version;
  }
}
