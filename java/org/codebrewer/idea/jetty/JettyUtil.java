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

import com.intellij.util.EnvironmentUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

import java.io.File;

/**
 * @author Mark Scott
 * @version $Id:$
 */
public class JettyUtil
{
  private static final JettyVersionChecker[] JETTY_VERSION_CHECKERS =
      new JettyVersionChecker[]{ new Jetty6xVersionFileChecker(), new Jetty4x5xVersionFileChecker() };
  @NonNls private static final String JETTY_HOME_ENV_PROPERTY = "JETTY_HOME";

  public static String baseConfigDir(final String baseDirectoryPath)
  {
    return baseDirectoryPath + File.separator + JettyConstants.JETTY_CONFIG_DIRECTORY_NAME;
  }

  public static String getDefaultLocation()
  {
    final String result = EnvironmentUtil.getEnviromentProperties().get(JETTY_HOME_ENV_PROPERTY);

    if (result != null) {
      return result.replace(File.separatorChar, '/');
    }
    else {
      return "";
    }
  }

  public static String getVersion(final String homeDir)
  {
    for (int i = 0; i < JETTY_VERSION_CHECKERS.length; i++) {
      final JettyVersionChecker jettyVersionChecker = JETTY_VERSION_CHECKERS[i];
      final String jettyVersion = jettyVersionChecker.getVersion(homeDir);
      if (jettyVersion != null) {
        return jettyVersion;
      }
    }

    return null;
  }

  public static int getPort(final File serverXmlFile)
  {
    // Todo - parse the config file being used (which may or may not be named jetty.xml)

    return JettyConstants.DEFAULT_PORT;
  }

  private JettyUtil()
  {
    // Utility class
  }

  public static class ContextItem
  {
    private File myFile;
    private Element myElement;

    public ContextItem(final File file, final Element element)
    {
      myFile = file;
      myElement = element;
    }

    public File getFile()
    {
      return myFile;
    }

    public Element getElement()
    {
      return myElement;
    }
  }
}