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

import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

/**
 * @author Mark Scott
 * @version $Id:$
 */
public class JettyPersistentData implements ApplicationServerPersistentData
{
  private static final String JETTY_HOME_KEY = "JETTY_HOME";
  private static final String JETTY_VERSION_KEY = "JETTY_VERSION";

  private String jettyHome = "";
  private String jettyVersion = "";

  public JettyPersistentData()
  {
    jettyHome = JettyUtil.getDefaultLocation();
  }

  public String getJettyHome()
  {
    return jettyHome;
  }

  public String getJettyVersion()
  {
    return jettyVersion;
  }

  public void setJettyHome(String jettyHome)
  {
    this.jettyHome = jettyHome;
  }

  public void setJettyVersion(String jettyVersion)
  {
    this.jettyVersion = jettyVersion;
  }

  public void readExternal(Element element) throws InvalidDataException
  {
    final String persistedJettyHome = JDOMExternalizer.readString(element, JETTY_HOME_KEY);

    if (persistedJettyHome != null) {
      jettyHome = persistedJettyHome;
    }

    final String persistedJettyVersion = JDOMExternalizer.readString(element, JETTY_VERSION_KEY);

    if (persistedJettyVersion != null) {
      jettyVersion = persistedJettyVersion;
    }
  }

  public void writeExternal(Element element) throws WriteExternalException
  {
    JDOMExternalizer.write(element, JETTY_HOME_KEY, jettyHome);
    JDOMExternalizer.write(element, JETTY_VERSION_KEY, jettyVersion);
  }
}