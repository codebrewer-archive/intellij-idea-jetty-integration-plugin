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

import org.jetbrains.annotations.NonNls;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyConstants
{
  /**
   * The name of the Jetty lib directory, relative to the Jetty installation
   * directory.
   */
  @NonNls public static final String JETTY_CONFIG_DIRECTORY_NAME = "etc";

  /**
   * The name of the Jetty JSP 2.1 lib directory, relative to the Jetty
   * installation lib directory.
   */
  @NonNls public static final String JETTY_JSP_2_1_LIB_DIRECTORY_NAME = "jsp-2.1";

  /**
   * The name of the Jetty lib directory, relative to the Jetty installation
   * directory.
   */
  @NonNls public static final String JETTY_LIB_DIRECTORY_NAME = "lib";
  @NonNls public static final String JETTY_XML_FILE_NAME = "jetty.xml";
  public static final int DEFAULT_PORT = 8080;
  @NonNls protected static final String HTTP_PROTOCOL = "http://";

  private JettyConstants()
  {
    // Utility class
  }
}