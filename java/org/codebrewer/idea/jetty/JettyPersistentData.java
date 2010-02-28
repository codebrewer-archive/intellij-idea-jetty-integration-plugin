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

import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import org.codebrewer.idea.jetty.versionsupport.JettyVersionHelper;
import org.codebrewer.idea.jetty.versionsupport.JettyVersionHelperFactory;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * A class that is responsible for persisting and restoring configuration
 * information about a single Jetty application server definition.  Persisted
 * data comprises the location of the Jetty installation, its version number and
 * the location and active status of any configuration files used.
 * </p>
 *
 * @author Mark Scott
 * @author Chris Miller
 * @version $Id$
 */
public class JettyPersistentData implements ApplicationServerPersistentData
{
  /**
   * Key used to persist the active state of a single Jetty configuration file.
   */
  @NonNls
  private static final String CONFIGURATION_ACTIVE_KEY = "active";

  /**
   * Key used to persist the set of Jetty configuration files.
   */
  @NonNls
  private static final String CONFIGURATIONS_KEY = "configurations";

  /**
   * Key used to persist the path to a single Jetty configuration file.
   */
  @NonNls
  private static final String FILE_KEY = "file";

  /**
   * Key used to persist the path to the Jetty installation.
   */
  @NonNls
  private static final String PATH_KEY = "path";

  private String jettyHome;
  private JettyVersionHelper versionHelper;
  private List<JettyConfigurationFile> jettyConfigurationFiles = new ArrayList<JettyConfigurationFile>();

  public JettyPersistentData()
  {
    jettyHome = JettyUtil.getDefaultLocation();
  }

  @NotNull
  public List<JettyConfigurationFile> getJettyConfigurationFiles()
  {
    return Collections.unmodifiableList(jettyConfigurationFiles);
  }

  @NotNull
  public String getJettyHome()
  {
    return jettyHome;
  }

  @Nullable
  public JettyVersionHelper getJettyVersionHelper()
  {
    return versionHelper;
  }

  public void setJettyConfigurationFiles(final List<JettyConfigurationFile> jettyConfigurationFiles)
  {
    if (jettyConfigurationFiles == null) {
      this.jettyConfigurationFiles.clear();
    }
    else {
      this.jettyConfigurationFiles = new ArrayList<JettyConfigurationFile>(jettyConfigurationFiles);
    }
  }

  public void setJettyHome(final String jettyHome)
  {
    this.jettyHome = jettyHome == null ? "" : jettyHome;
  }

  public void setJettyVersionHelper(final JettyVersionHelper jettyVersionHelper)
  {
    versionHelper = jettyVersionHelper;
  }

  public void readExternal(final Element element) throws InvalidDataException
  {
    final String persistedJettyHome = JDOMExternalizer.readString(element, PATH_KEY);

    if (persistedJettyHome != null) {
      jettyHome = persistedJettyHome;
      versionHelper = JettyVersionHelperFactory.INSTANCE.getJettyVersionHelper(new File(jettyHome));
    }

    final Element configurationFilesElement = element.getChild(CONFIGURATIONS_KEY);

    if (configurationFilesElement != null && versionHelper != null) {
      final List configurationFileElements = configurationFilesElement.getChildren(FILE_KEY);

      for (final Object configurationFileElement : configurationFileElements) {
        final String path = JDOMExternalizer.readString((Element) configurationFileElement, PATH_KEY);
        final boolean isActive = JDOMExternalizer.readBoolean((Element) configurationFileElement, CONFIGURATION_ACTIVE_KEY);

        try {
          final JettyConfigurationFile configurationFile = new JettyConfigurationFile(versionHelper, path, isActive);
          jettyConfigurationFiles.add(configurationFile);
        }
        catch (ConfigurationException e) {
          throw new InvalidDataException(
            JettyBundle.message(JettyUtil.EXCEPTION_TEXT_CANNOT_LOAD_FILE, path, e.getMessage()), e);
        }
      }
    }
  }

  public void writeExternal(final Element element) throws WriteExternalException
  {
    JDOMExternalizer.write(element, PATH_KEY, jettyHome);

    final Element configurationFilesElement = new Element(CONFIGURATIONS_KEY);

    for (final JettyConfigurationFile configurationFile : jettyConfigurationFiles) {
      final Element configurationFileElement = new Element(FILE_KEY);

      JDOMExternalizer.write(configurationFileElement, PATH_KEY, configurationFile.getFile().getAbsolutePath());
      JDOMExternalizer.write(configurationFileElement, CONFIGURATION_ACTIVE_KEY, configurationFile.isActive());

      configurationFilesElement.addContent(configurationFileElement);
    }

    element.addContent(configurationFilesElement);
  }

  /**
   * A class that holds information about an individual Jetty configuration
   * file.
   */
  public static class JettyConfigurationFile
  {
    @NonNls
    private static final String INVALID_CONFIGURATION_FILE_KEY = "exception.text.not.jetty.configuration.file";

    private File configurationFile;
    private boolean active;
    private final JettyVersionHelper versionHelper;

    /**
     * Creates an instance if the given path is that of a valid Jetty
     * configuration file (<em>i.e.</em> is a readable XML file with the correct
     * DOCTYPE declaration).
     *
     * @param versionHelper a representation of a particular version of Jetty.
     * @param path the path to a Jetty configuration file.
     * @param active whether or not the file identified by <code>path</code> is
     * to be used when starting Jetty.
     *
     * @throws ConfigurationException if <code>path</code> is not that of a
     * valid Jetty configuration file.
     */
    public JettyConfigurationFile(@NotNull final JettyVersionHelper versionHelper,
                                  @NotNull final String path,
                                  final boolean active) throws ConfigurationException
    {
      if (!JettyUtil.isJettyConfigurationFile(versionHelper, path)) {
        throw new ConfigurationException(JettyBundle.message(INVALID_CONFIGURATION_FILE_KEY, path));
      }

      this.versionHelper = versionHelper;
      configurationFile = new File(path);
      this.active = active;
    }

    public File getFile()
    {
      return configurationFile;
    }

    public boolean isActive()
    {
      return active;
    }

    public void setActive(final boolean active)
    {
      this.active = active;
    }

    public void setPath(final String path) throws ConfigurationException
    {
      if (JettyUtil.isJettyConfigurationFile(versionHelper, path)) {
        configurationFile = new File(path);
      }
      else {
        throw new ConfigurationException(JettyBundle.message(INVALID_CONFIGURATION_FILE_KEY, path));
      }
    }
  }
}
