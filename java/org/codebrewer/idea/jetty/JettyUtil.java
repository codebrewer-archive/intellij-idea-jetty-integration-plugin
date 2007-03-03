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

import com.intellij.openapi.util.JDOMUtil;
import com.intellij.util.EnvironmentUtil;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyUtil
{
  private static final JettyVersionChecker[] JETTY_VERSION_CHECKERS =
      new JettyVersionChecker[]{ new Jetty6xVersionFileChecker(), new Jetty4x5xVersionFileChecker() };
  private static final String EXCEPTION_TEXT_CANNOT_LOAD_FILE = "exception.text.cannot.load.file.bacause.of.1";

  public static String baseConfigDir(final String baseDirectoryPath)
  {
    return baseDirectoryPath + File.separator + JettyConstants.JETTY_CONFIG_DIRECTORY_NAME;
  }

  public static String getDefaultLocation()
  {
    final String result = EnvironmentUtil.getEnviromentProperties().get(JettyConstants.JETTY_HOME_ENV_VAR);

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

  public static int getPort(final File[] serverConfigurationFiles)
  {
    // Todo - parse the config files being used

    return JettyConstants.DEFAULT_PORT;
  }

  public static boolean isJettyConfigurationFile(final String path)
  {
    boolean result = false;

    try {
      final Document document = loadXMLFile(path);
      final DocType docType = document.getDocType();

      if (docType != null) {
        final boolean isValidElementName = docType.getElementName().equals(JettyConstants.JETTY_DOCTYPE_ELEMENT_NAME);
        final boolean isValidPublicID = docType.getPublicID().equals(JettyConstants.JETTY_DOCTYPE_PUBLIC_ID);
        final boolean isValidSystemID = docType.getSystemID().equals(JettyConstants.JETTY_DOCTYPE_SYSTEM_ID);

        result = isValidElementName && isValidPublicID && isValidSystemID;
      }
    }
    catch (JettyException e) {
      result = false;
    }

    return result;
  }

  public static @NotNull Document loadXMLFile(final String xmlPath) throws JettyException
  {
    try {
      final Document xmlDocument = JDOMUtil.loadDocument(new File(xmlPath));

      if (xmlDocument == null) {
        throw new JettyException(JettyBundle.message("exception.text.cannot.find.file", xmlPath));
      }

      return xmlDocument;
    }
    catch (JDOMException e) {
      throw new JettyException(JettyBundle.message(EXCEPTION_TEXT_CANNOT_LOAD_FILE, xmlPath, e.getMessage()));
    }
    catch (IOException e) {
      throw new JettyException(JettyBundle.message(EXCEPTION_TEXT_CANNOT_LOAD_FILE, xmlPath, e.getMessage()));
    }
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