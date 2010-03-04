/*
 * Copyright 2010 Mark Scott
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
package org.codebrewer.idea.jetty.versionsupport;

import org.codebrewer.idea.jetty.JettyBundle;
import org.codebrewer.idea.jetty.JettyException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class Jetty6xConfigurationFileHelper implements ConfigurationFileHelper
{
  /**
   * The public ID of the DOCTYPE declaration in a valid Jetty 4.x, 5.x or 6.x
   * XML configuration file.
   */
  @NonNls
  private static final String DOCTYPE_PUBLIC_ID = "-//Mort Bay Consulting//DTD Configure//EN";

  /**
   * The system ID of the DOCTYPE declaration in a valid Jetty 4.x, 5.x or 6.x
   * XML configuration file.
   */
  @NonNls
  private static final String DOCTYPE_SYSTEM_ID = "http://jetty.mortbay.org/configure.dtd";

  @NotNull
  public String getFullyQualifiedClassName(@NotNull String name) throws JettyException
  {
    if ("ContextDeployer".equals(name)) {
      return "org.mortbay.jetty.deployer.ContextDeployer";
    }

    if ("Server".equals(name)) {
      return "org.mortbay.jetty.Server";
    }

    if ("WebAppContext".equals(name)) {
      return "org.mortbay.jetty.webapp.WebAppContext";
    }

    throw new JettyException(JettyBundle.message("exception.text.unknown.class.name", name));
  }

  @NotNull
  public String getDoctypePublicId()
  {
    return DOCTYPE_PUBLIC_ID;
  }

  @NotNull
  public String getDoctypeSystemId()
  {
    return DOCTYPE_SYSTEM_ID;
  }
}
