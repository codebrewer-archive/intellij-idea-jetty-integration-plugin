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

import com.intellij.javaee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.javaee.appServerIntegrations.ApplicationServerInfo;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentDataEditor;
import com.intellij.javaee.appServerIntegrations.CantFindApplicationServerJarsException;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyApplicationServerHelper implements ApplicationServerHelper
{
  @NonNls
  protected static final String JSP_API_2_1_JAR = "jsp-api-2.1.jar";

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

    if (jettyPersistentData.getJettyHome().length() > 0 && !jettyPersistentData.getJettyVersion().startsWith("6.1")) {
      throw new CantFindApplicationServerJarsException(
        JettyBundle.message("exception.text.unsupported.jetty.version", jettyPersistentData.getJettyVersion(), "6.1"));
    }

    final File jettyHome =
      new File(jettyPersistentData.getJettyHome().replace('/', File.separatorChar)).getAbsoluteFile();
    final File jettyLib = new File(jettyHome, JettyConstants.JETTY_LIB_DIRECTORY_NAME);

    if (!jettyLib.isDirectory()) {
      throw new CantFindApplicationServerJarsException(
        JettyBundle.message("message.text.cant.find.directory", jettyLib.getAbsolutePath()));
    }

    final List<File> files = new ArrayList<File>();
    final File[] filesInLib = jettyLib.listFiles();

    if (filesInLib != null) {
      for (final File file : filesInLib) {
        if (file.isFile()) {
          files.add(file);
        }
      }
    }

    final File jspApi21File =
      new File(new File(jettyLib, JettyConstants.JETTY_JSP_2_1_LIB_DIRECTORY_NAME), JSP_API_2_1_JAR);
    if (jspApi21File.isFile()) {
      files.add(jspApi21File);
    }

    final String version = jettyPersistentData.getJettyVersion();

    return new ApplicationServerInfo(files.toArray(new File[files.size()]),
      JettyBundle.message("default.application.server.name", version));
  }
}
