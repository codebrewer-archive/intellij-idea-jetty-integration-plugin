/*
 * Copyright 2007, 2010 Mark Scott, Peter Niederwieser
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

import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.Factory;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactPointerManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;

/**
 * Editor to configure Jetty for run and debug sessions.
 *
 * @author Mark Scott
 * @author Peter Niederwieser
 * @version $Id$
 */
public class JettyDeploymentSettingsEditor extends SettingsEditor<DeploymentModel>
{
  private JPanel panel;
  private JTextField contextPath;

  public JettyDeploymentSettingsEditor(final CommonModel configuration, final Artifact artifact)
  {
    super(new Factory<DeploymentModel>()
    {
      public JettyModuleDeploymentModel create()
      {
        final ArtifactPointerManager manager = ArtifactPointerManager.getInstance(configuration.getProject());
        return new JettyModuleDeploymentModel(configuration, manager.createPointer(artifact));
      }
    });

    build();
  }

  private void build()
  {
    panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder(JettyBundle.message("label.deployment.context.path")));
    contextPath = new JTextField();
    panel.add(contextPath, BorderLayout.NORTH);
  }

  @NotNull
  private String getSelectedContextPath()
  {
    final String item = contextPath.getText();

    return (item == null) ? "" : item;
  }

  private void setSelectedContextPath(@NotNull final String contextPath)
  {
    this.contextPath.setText(contextPath);
  }

  @Override
  protected void resetEditorFrom(final DeploymentModel s)
  {
    setSelectedContextPath(((JettyModuleDeploymentModel) s).getContextPath());
  }

  @Override
  protected void applyEditorTo(final DeploymentModel s) throws ConfigurationException
  {
    ((JettyModuleDeploymentModel) s).setContextPath(getSelectedContextPath());
  }

  @NotNull
  @Override
  protected JComponent createEditor()
  {
    return panel;
  }

  @Override
  protected void disposeEditor()
  {
  }
}
