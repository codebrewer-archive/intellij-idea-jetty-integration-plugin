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

import com.intellij.facet.pointers.FacetPointersManager;
import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.facet.JavaeeFacet;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.Factory;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Editor to configure Jetty for run and debug sessions.  Not currently used.
 *
 * @author Mark Scott
 * @version $Id$
 */
public class JettyDeploymentSettingsEditor extends SettingsEditor<DeploymentModel>
{
  public JettyDeploymentSettingsEditor(final CommonModel configuration, final JavaeeFacet facet)
  {
    super(new Factory<DeploymentModel>()
    {
      public JettyModuleDeploymentModel create()
      {
        final FacetPointersManager manager = FacetPointersManager.getInstance(facet.getModule().getProject());
        return new JettyModuleDeploymentModel(configuration, manager.create(facet));
      }
    });
  }

  @Override
  protected void resetEditorFrom(final DeploymentModel s)
  {
  }

  @Override
  protected void applyEditorTo(final DeploymentModel s) throws ConfigurationException
  {
  }

  @NotNull
  @Override
  protected JComponent createEditor()
  {
//    return new JLabel("JettyDeploymentSettingsEditor");
    return new JLabel();
  }

  @Override
  protected void disposeEditor()
  {
  }
}
