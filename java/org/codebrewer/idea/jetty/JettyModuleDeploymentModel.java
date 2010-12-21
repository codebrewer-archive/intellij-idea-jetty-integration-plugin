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
import com.intellij.javaee.deployment.DeploymentSource;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Scott
 * @author Peter Niederwieser
 */
public class JettyModuleDeploymentModel extends DeploymentModel
{
  private static final String CONTEXT_PATH_NAME = "CONTEXT_PATH";
  private static final String DEFAULT_CONTEXT_PATH = "/";

  private String contextPath = DEFAULT_CONTEXT_PATH;

  public JettyModuleDeploymentModel(CommonModel project, DeploymentSource source)
  {
    super(project, source);
  }

  @NotNull
  public String getContextPath()
  {
    return contextPath;
  }

  public void setContextPath(@NotNull String contextPath)
  {
    this.contextPath = contextPath;
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException
  {
    super.readExternal(element);

    for (Object o : element.getChildren("option")) {
      final Element e = (Element) o;
      final String fieldName = e.getAttributeValue("name");

      if (CONTEXT_PATH_NAME.equals(fieldName)) {
        final String value = e.getAttributeValue("value");

        setContextPath(value == null ? DEFAULT_CONTEXT_PATH : value);
      }
    }
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException
  {
    final Element contextPathElement = new Element("option");

    element.addContent(contextPathElement);
    contextPathElement.setAttribute("name", CONTEXT_PATH_NAME);
    contextPathElement.setAttribute("value", getContextPath());

    super.writeExternal(element);
  }
}
