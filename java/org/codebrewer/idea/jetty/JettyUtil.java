/*
 * Copyright 2007, 2008 Mark Scott
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

import com.intellij.javaee.deployment.DeploymentManager;
import com.intellij.javaee.deployment.DeploymentSource;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.EnvironmentUtil;
import static org.codebrewer.idea.jetty.JettyConstants.DEFAULT_PORT;
import static org.codebrewer.idea.jetty.JettyConstants.JETTY_CONFIG_DIRECTORY_NAME;
import static org.codebrewer.idea.jetty.JettyConstants.JETTY_CONTEXT_DEPLOYER_CONFIG_DIR_NAME;
import static org.codebrewer.idea.jetty.JettyConstants.JETTY_DOCTYPE_ELEMENT_NAME;
import static org.codebrewer.idea.jetty.JettyConstants.JETTY_DOCTYPE_PUBLIC_ID;
import static org.codebrewer.idea.jetty.JettyConstants.JETTY_DOCTYPE_SYSTEM_ID;
import static org.codebrewer.idea.jetty.JettyConstants.JETTY_HOME_ENV_VAR;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyUtil
{
  private static final JettyVersionChecker[] JETTY_VERSION_CHECKERS = new JettyVersionChecker[]{
    new Hightide6xVersionFileChecker(),
    new Jetty6xVersionFileChecker(),
    new Jetty4x5xVersionFileChecker() };
  private static final String EXCEPTION_TEXT_CANNOT_LOAD_FILE = "exception.text.cannot.load.file.bacause.of.1";

  private static final String EMPTY_STRING = "";
  private static final String SCRATCH_DIRECTORY_NAME_PREFIX = "jetty_";

  public static String baseConfigDir(final String baseDirectoryPath)
  {
    return baseDirectoryPath + File.separator + JETTY_CONFIG_DIRECTORY_NAME;
  }

  @NotNull
  public static Document getContextDeployerDocument(@NotNull final File scratchDirectory)
  {
    final DocType docType = new DocType(JETTY_DOCTYPE_ELEMENT_NAME, JETTY_DOCTYPE_PUBLIC_ID, JETTY_DOCTYPE_SYSTEM_ID);
    final Element rootElement = new Element(JETTY_DOCTYPE_ELEMENT_NAME);
    final Element callElement = new Element("Call");
    final Element argElement = new Element("Arg");
    final Element newElement = new Element("New");
    final Element setContextsElement = new Element("Set");
    final Element refElement = new Element("Ref");
    final Element setConfigurationDirElement = new Element("Set");
    final Element setScanIntervalElement = new Element("Set");

    rootElement.setAttribute("id", "Server"); // Todo - this is a reference to an element defined in an active config file so shouldn't be hard-coded
    rootElement.setAttribute("class", "org.mortbay.jetty.Server");
    callElement.setAttribute("name", "addLifeCycle");
    newElement.setAttribute("class", "org.mortbay.jetty.deployer.ContextDeployer");
    setContextsElement.setAttribute("name", "contexts");
    refElement.setAttribute("id", "Contexts"); // Todo - this is a reference to an element defined in an active config file so shouldn't be hard-coded
    setConfigurationDirElement.setAttribute("name", "configurationDir");
    setConfigurationDirElement.setText(scratchDirectory + File.separator + JETTY_CONTEXT_DEPLOYER_CONFIG_DIR_NAME);
    setScanIntervalElement.setAttribute("name", "scanInterval");
    setScanIntervalElement.setText("1");

    setContextsElement.addContent(refElement);
    newElement.addContent(setContextsElement);
    newElement.addContent(setConfigurationDirElement);
    newElement.addContent(setScanIntervalElement);
    argElement.addContent(newElement);
    callElement.addContent(argElement);
    rootElement.addContent(callElement);

    final Document result = new Document(rootElement, docType);
    return result;
  }

  @NotNull
  public static Document getContextDeploymentDocument(
    @NotNull final Project project, @NotNull final JettyModuleDeploymentModel model) throws JettyException
  {
    final DeploymentManager deploymentManager = DeploymentManager.getInstance(project);
    final File deploymentSource = deploymentManager.getDeploymentSource(model);     // full path to war file or exploded directory

    if (deploymentSource == null) {
      throw new JettyException(JettyBundle.message("exception.text.null.deployment.source"));
    }

    final DeploymentSource deploymentMethod = model.getSpecifiedDeploymentSource(); // DeploymentSource.FROM_JAR or DeploymentSource.FROM_EXPLODED
    final String contextPath = model.getContextPath();
    final DocType docType = new DocType(JETTY_DOCTYPE_ELEMENT_NAME, JETTY_DOCTYPE_PUBLIC_ID, JETTY_DOCTYPE_SYSTEM_ID);
    final Element rootElement = new Element(JETTY_DOCTYPE_ELEMENT_NAME);
    final Element setContextPathElement = new Element("Set");
    final Element setDeploymentSourceElement = new Element("Set");

    rootElement.setAttribute("class", "org.mortbay.jetty.webapp.WebAppContext");
    setContextPathElement.setAttribute("name", "contextPath");
    setContextPathElement.setText(contextPath);

    if (deploymentMethod == DeploymentSource.FROM_EXPLODED) {
      setDeploymentSourceElement.setAttribute("name", "resourceBase");
      setDeploymentSourceElement.setText(deploymentSource.getAbsolutePath());
    }
    else {
      setDeploymentSourceElement.setAttribute("name", "war");
      setDeploymentSourceElement.setText(deploymentSource.getAbsolutePath());
    }

    rootElement.addContent(setContextPathElement);
    rootElement.addContent(setDeploymentSourceElement);

    final Document result = new Document(rootElement, docType);

    return result;
  }

  public static String getDefaultLocation()
  {
    final String result = EnvironmentUtil.getEnviromentProperties().get(JETTY_HOME_ENV_VAR);

    if (result != null) {
      return result.replace(File.separatorChar, '/');
    }
    else {
      return "";
    }
  }

  public static int getPort(final File[] serverConfigurationFiles)
  {
    // Todo - parse the config files being used

    return DEFAULT_PORT;
  }

  @NotNull
  public static File getContextDeployerConfigurationDirectory(@NotNull final File scratchDirectory)
  {
    return new File(scratchDirectory, JettyConstants.JETTY_CONTEXT_DEPLOYER_CONFIG_DIR_NAME);
  }

  @NotNull
  public static File getScratchDirectory(@NotNull final Project project)
  {
    return new File(getScratchDirectoryPath(project));
  }

  @NotNull
  private static String getScratchDirectoryPath(@NotNull final Project project)
  {
    return ApplicationManager.getApplication().runReadAction(new Computable<String>()
    {
      public String compute()
      {
        String result;

        try {
          final File systemDir = new File(PathManager.getSystemPath());
          final String nameCandidate = SCRATCH_DIRECTORY_NAME_PREFIX + project.getLocationHash();
          final File file = FileUtil.findSequentNonexistentFile(systemDir, nameCandidate, EMPTY_STRING);

          result = SystemInfo.isWindows ? file.getCanonicalPath() : file.getAbsolutePath();
        }
        catch (IOException e) {
          Logger.getInstance(JettyModel.class.getName()).error(e);
          result = EMPTY_STRING;
        }

        return result;
      }
    });
  }

  @Nullable
  public static String getVersion(final String homeDir)
  {
    for (final JettyVersionChecker jettyVersionChecker : JETTY_VERSION_CHECKERS) {
      final String jettyVersion = jettyVersionChecker.getVersion(homeDir);

      if (jettyVersion != null) {
        return jettyVersion;
      }
    }

    return null;
  }

  public static boolean isJettyConfigurationFile(final String path)
  {
    boolean result = false;

    try {
      final Document document = loadXMLFile(path);
      final DocType docType = document.getDocType();

      if (docType != null) {
        final boolean isValidElementName = docType.getElementName().equals(JETTY_DOCTYPE_ELEMENT_NAME);
        final boolean isValidPublicID = docType.getPublicID().equals(JETTY_DOCTYPE_PUBLIC_ID);
        final boolean isValidSystemID = docType.getSystemID().equals(JETTY_DOCTYPE_SYSTEM_ID);

        result = isValidElementName && isValidPublicID && isValidSystemID;
      }
    }
    catch (JettyException ignore) {
      result = false;
    }

    return result;
  }

  @NotNull
  public static Document loadXMLFile(final String xmlPath) throws JettyException
  {
    try {
      final Document xmlDocument = JDOMUtil.loadDocument(new File(xmlPath));

      return xmlDocument;
    }
    catch (JDOMException e) {
      throw new JettyException(JettyBundle.message(EXCEPTION_TEXT_CANNOT_LOAD_FILE, xmlPath, e.getMessage()), e);
    }
    catch (IOException e) {
      throw new JettyException(JettyBundle.message(EXCEPTION_TEXT_CANNOT_LOAD_FILE, xmlPath, e.getMessage()), e);
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
