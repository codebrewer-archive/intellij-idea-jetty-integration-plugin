/*
 * Copyright 2007 Mark Scott
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
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mark Scott
 * @version $Id:$
 */
public abstract class AbstractJettyVersionFileChecker implements JettyVersionChecker
{
  @NotNull public abstract String getVersionFileName();
  @NotNull public abstract String getVersionPattern();

  @Nullable public String getVersion(@NotNull final String jettyHomeDir)
  {
    final String pathToVersionFile = jettyHomeDir + File.separator + getVersionFileName();
    final File versionFile = new File(pathToVersionFile);

    if (versionFile.exists() && versionFile.isFile() && versionFile.canRead()) {
      BufferedReader br = null;

      try {
        try {
          br = new BufferedReader(new InputStreamReader(new FileInputStream(versionFile)));
          final Pattern pattern = Pattern.compile(getVersionPattern());
          String line;

          do {
            line = br.readLine();

            if (line != null) {
              final Matcher matcher = pattern.matcher(line);

              if (matcher.matches()) {
                final String version = matcher.group(1);
                return version;
              }
            }
          }
          while (line != null);

          // We've read the file and not matched on any line
          //
          return null;
        }
        finally {
          if (br != null) {
            br.close();
          }
        }
      }
      catch (IOException e) {
        return null;
      }
    }
    else {
      return null;
    }
  }
}