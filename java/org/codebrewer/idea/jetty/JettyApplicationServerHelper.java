/*
 * Copyright 2007, 2010 Mark Scott, Chris Miller
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

import com.intellij.javaee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.javaee.appServerIntegrations.ApplicationServerInfo;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentDataEditor;
import com.intellij.javaee.appServerIntegrations.CantFindApplicationServerJarsException;
import org.codebrewer.idea.jetty.versionsupport.JettyVersionHelper;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mark Scott
 * @author Chris Miller
 * @version $Id$
 */
public class JettyApplicationServerHelper implements ApplicationServerHelper
{
  /**
   * <p>
   * The names of the possible JSP 2.1 lib directories, relative to the Jetty
   * installation's <code>lib</code> directory.
   * </p>
   */
  @NonNls
  private static final String[] JSP_API_LIB_DIRS = new String[]{ "jsp-2.1", "jsp" };

  /**
   * <p>
   * The start of the name of the JSP API jar file.
   * </p>
   */
  @NonNls
  private static final String JSP_API_2_1_JAR_PREFIX = "jsp-api-2.1";

  /**
   * <p>
   * The end of the name of the JSP API jar file.
   * </p>
   */
  @NonNls
  private static final String JAR_FILE_EXTENSION = ".jar";

  public ApplicationServerPersistentDataEditor createConfigurable()
  {
    return new JettyDataEditor();
  }

  public ApplicationServerPersistentData createPersistentDataEmptyInstance()
  {
    return new JettyPersistentData();
  }

  public ApplicationServerInfo getApplicationServerInfo(final ApplicationServerPersistentData persistentData)
    throws CantFindApplicationServerJarsException
  {
    final JettyPersistentData jettyPersistentData = (JettyPersistentData) persistentData;
    final JettyVersionHelper versionHelper = jettyPersistentData.getJettyVersionHelper();
    final String versionString;
    final List<File> files;

    if (versionHelper == null) {
      files = Collections.emptyList();
      versionString = "";
    }
    else {
      versionString = versionHelper.getVersionString();

      final File jettyHome =
        new File(jettyPersistentData.getJettyHome().replace('/', File.separatorChar)).getAbsoluteFile();
      final File jettyLib = new File(jettyHome, JettyConstants.JETTY_LIB_DIRECTORY_NAME);

      if (!jettyLib.isDirectory()) {
        throw new CantFindApplicationServerJarsException(
          JettyBundle.message("message.text.cant.find.directory", jettyLib.getAbsolutePath()));
      }

      files = new ArrayList<File>();
      final File[] filesInLib = jettyLib.listFiles();

      if (filesInLib != null) {
        for (final File file : filesInLib) {
          if (file.isFile()) {
            files.add(file);
          }
        }
      }

      final FilenameFilter filter = new FilenameFilter()
      {
        public boolean accept(File dir, String name)
        {
          return name.startsWith(JSP_API_2_1_JAR_PREFIX) && name.endsWith(JAR_FILE_EXTENSION);
        }
      };

      for (final String jspLibDir : JSP_API_LIB_DIRS) {
        final File dir = new File(jettyLib, jspLibDir);
        final String[] jspApiJars = dir.list(filter);

        if (jspApiJars != null && jspApiJars.length > 0) {
          files.add(new File(dir, jspApiJars[0]));
          break;
        }
      }
    }

    return new ApplicationServerInfo(files.toArray(new File[files.size()]), JettyBundle.message("default.application.server.name", versionString));
  }
}
