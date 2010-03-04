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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * A <code>ProcessHelper</code> can generate a process command line for starting
 * and stopping a Jetty server of a particular version.
 * </p>
 *
 * @author Mark Scott
 * @version $Id$
 */
public interface ProcessHelper
{
  /**
   * <p>
   * Gets the process command line needed to start a Jetty server.
   * </p>
   *
   * @return the process command line needed to start a Jetty server.
   */
  @NotNull
  String getStartCommand();

  /**
   * <p>
   * Gets a template for the process command line needed to stop a Jetty server.
   * The template should contain placeholders for the number of the TCP port on
   * which Jetty is listening for a shutdown command, and the key which Jetty
   * requires to authorize the shutdown request.
   * </p>
   *
   * @return the process command line needed to start a Jetty server.
   */
  @NotNull
  String getStopCommandTemplate();

  /**
   * <p>
   * Determines whether or not the given text (captured from Jetty's stdout
   * stream) indicates that the server has successfully started.
   * </p>
   *
   * @param text a line captured from Jetty's stdout stream.
   *
   * @return <code>true</code> if the text indicates that Jetty has successfully
   *         started, otherwise <code>false</code>.
   */
  boolean isStartingMessage(@Nullable String text);
}
