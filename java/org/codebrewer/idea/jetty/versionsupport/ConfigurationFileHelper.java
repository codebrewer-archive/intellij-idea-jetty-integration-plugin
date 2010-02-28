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

import org.codebrewer.idea.jetty.JettyException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * A <code>ConfigurationFileHelper</code> provides assistance with the XML
 * configuration files used by a particular version of Jetty.
 * </p>
 *
 * @author Mark Scott
 * @version $Id$
 */
public interface ConfigurationFileHelper
{
  /**
   * The name of the root element in a valid Jetty XML configuration file.
   */
  @NonNls
  String JETTY_DOCTYPE_ELEMENT_NAME = "Configure";

  /**
   * <p>
   * Gets the fully-qualified name of a class for a particular version of Jetty.
   * Assumes that names are unique even when not fully qualified.
   * </p>
   *
   * @param name the name of the class whose fully qualified name is required.
   *
   * @return the fully-qualified name of the requested class.
   *
   * @throws JettyException if the requested name cannot be provided.
   */
  @NotNull
  String getFullyQualifiedClassName(@NotNull String name) throws JettyException;

  /**
   * <p>
   * Gets the public ID of the DTD used by a particular version of Jetty.
   * </p>
   *
   * @return the public ID of the DTD used by a particular version of Jetty.
   */
  @NotNull
  String getDoctypePublicId();

  /**
   * <p>
   * Gets the system ID of the DTD used by a particular version of Jetty.
   * </p>
   *
   * @return the system ID of the DTD used by a particular version of Jetty.
   */
  @NotNull
  String getDoctypeSystemId();
}
