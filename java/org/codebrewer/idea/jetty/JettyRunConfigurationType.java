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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.javaee.run.configuration.J2EEConfigurationFactory;
import com.intellij.javaee.run.configuration.J2EEConfigurationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

/**
 * @author Mark Scott
 * @version $Id:$
 */
public class JettyRunConfigurationType extends J2EEConfigurationType
{
  protected RunConfiguration createJ2EEConfigurationTemplate(
      final ConfigurationFactory factory, final Project project, final boolean isLocal)
  {
    final JettyModel jettyModel = new JettyModel();
    final JettyStartupPolicy jettyStartupPolicy = new JettyStartupPolicy();

    return J2EEConfigurationFactory.getInstance().createJ2EERunConfiguration(factory,
        project,
        jettyModel,
        JettyManager.getInstance(),
        isLocal,
        jettyStartupPolicy);
  }

  public String getDisplayName()
  {
    return JettyBundle.message("run.config.tab.title.jetty");
  }

  public String getConfigurationTypeDescription()
  {
    return JettyBundle.message("run.config.tab.description.jetty");
  }

  public Icon getIcon()
  {
    return JettyManager.getIcon();
  }

  @NonNls @NotNull public String getComponentName()
  {
    return getClass().getName();
  }
}