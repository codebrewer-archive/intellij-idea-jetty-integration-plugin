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

import com.intellij.facet.FacetTypeId;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.javaee.appServerIntegrations.AppServerIntegration;
import com.intellij.javaee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.javaee.deployment.DeploymentProvider;
import com.intellij.javaee.facet.JavaeeFacet;
import com.intellij.javaee.facet.JavaeeFacetUtil;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.Collection;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyManager extends AppServerIntegration
{
  private static final Icon ICON_JETTY = IconLoader.getIcon("/images/jetty16x16.png");
  private static final Icon ICON_JCOLON = IconLoader.getIcon("/images/jcolon16x16.png");
  @NonNls
  private static final String JETTY_6_1_X = "6.1.x";

  @NonNls
  public static final String PLUGIN_NAME = "JettyIntegration";

  public static Icon getIcon()
  {
    return ICON_JCOLON;
  }

  public static JettyManager getInstance()
  {
    return ApplicationManager.getApplication().getComponent(JettyManager.class);
  }

  private ApplicationServerHelper applicationServerHelper = new JettyApplicationServerHelper();
  private final DeploymentProvider deploymentProvider = new JettyDeploymentProvider();

  public DeploymentProvider getDeploymentProvider()
  {
    return deploymentProvider;
  }

  @Nullable
  @Override
  public ApplicationServerHelper getApplicationServerHelper()
  {
    return applicationServerHelper;
  }

  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor()
  {
    final FileTemplateGroupDescriptor group =
      new FileTemplateGroupDescriptor(JettyBundle.message("label.version", JETTY_6_1_X), null);

    final FileTemplateGroupDescriptor root =
      new FileTemplateGroupDescriptor(JettyBundle.message("templates.group.title"), getIcon());

    group.addTemplate(new FileTemplateDescriptor("jetty-context.xml", StdFileTypes.XML.getIcon()));
    root.addTemplate(group);

    return root;
  }

  @Override
  public String getPresentableName()
  {
    return JettyBundle.message("jetty.server.presentable.name");
  }

  @NotNull
  @Override
  public Collection<FacetTypeId<? extends JavaeeFacet>> getSupportedFacetTypes()
  {
    return JavaeeFacetUtil.getInstance().getSingletonCollection(WebFacet.ID);
  }

  public void disposeComponent()
  {
  }

  @NonNls
  @NotNull
  public String getComponentName()
  {
    return getClass().getName();
  }

  public void initComponent()
  {
    JettyStartupPolicy.ensureExecutable();
  }
}
