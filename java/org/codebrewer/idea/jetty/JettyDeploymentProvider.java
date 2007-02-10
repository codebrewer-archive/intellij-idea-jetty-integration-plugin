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

import com.intellij.javaee.JavaeeModuleProperties;
import com.intellij.javaee.deployment.DeploymentManager;
import com.intellij.javaee.deployment.DeploymentMethod;
import com.intellij.javaee.deployment.DeploymentModel;
import com.intellij.javaee.deployment.DeploymentProvider;
import com.intellij.javaee.deployment.DeploymentStatus;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.serverInstances.J2EEServerInstance;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyDeploymentProvider implements DeploymentProvider
{
  public static void prepareServer(JettyModel jettyModel)
  {
    //To change body of created methods use File | Settings | File Templates.
  }

  public void doDeploy(final Project project, final J2EEServerInstance instance, final DeploymentModel model)
  {
//    final Module module = model.getModuleProperties().getModule();
//    final JettyModuleDeploymentModel jettyModel = (JettyModuleDeploymentModel) model;
//    try {
//      final JettyModel serverModel = (JettyModel)model.getServerModel();
//      final List<JettyUtil.ContextItem> contexts = JettyUtil.getContexts(serverModel);
//      for (final JettyUtil.ContextItem contextItem : contexts) {
//        final String docBase = contextItem.getElement().getAttributeValue(DOC_BASE_ATTR);
//        if (docBase != null && docBase.equals(JettyUtil.getDeploymentPath(model))) {
//          JettyUtil.removeContextItem(serverModel, contextItem);
//        }
//      }
//      addApplicationContext(jettyModel);
//      setDeploymentStatus(instance, jettyModel, DeploymentStatus.DEPLOYED);
//
//      if (!serverModel.versionHigher(JettyPersistentData.VERSION50) && instance.isConnected()) {
//        new Jetty4Deployer(serverModel).deploy(getContextPath(jettyModel));
//      }
//    }
//    catch (ExecutionException e) {
//      Messages.showErrorDialog(project, e.getMessage(), JettyBundle.message("message.text.error.deploying.module", module.getName()));
//      setDeploymentStatus(instance, jettyModel, DeploymentStatus.FAILED);
//    }
  }

  public DeploymentModel createNewDeploymentModel(CommonModel configuration, JavaeeModuleProperties j2eeModuleProperties)
  {
    return new JettyModuleDeploymentModel(configuration, j2eeModuleProperties);
  }

  public SettingsEditor<DeploymentModel> createAdditionalDeploymentSettingsEditor(CommonModel configuration, JavaeeModuleProperties moduleProperties)
  {
    return new JettyDeploymentSettingsEditor(configuration, moduleProperties);
  }

  public void startUndeploy(J2EEServerInstance activeInstance, DeploymentModel model)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void updateDeploymentStatus(J2EEServerInstance instance, DeploymentModel model)
  {
    // Todo - implement properly
    final CommonModel configuration = instance.getCommonModel();
    final JettyModel serverModel = ((JettyModel) configuration.getServerModel());
    final JavaeeModuleProperties item = model.getModuleProperties();
    final DeploymentManager deploymentManager = DeploymentManager.getInstance(serverModel.getProject());

    // Dummy for now
    //
    deploymentManager.setDeploymentStatus(item, DeploymentStatus.UNKNOWN, configuration, instance);
  }

  @NonNls public String getHelpId()
  {
    return null;
  }

  public DeploymentMethod[] getAvailableMethods()
  {
    return null;
  }
}