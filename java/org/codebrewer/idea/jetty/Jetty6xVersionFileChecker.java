/*
 * Copyright 2007, 2008 Mark Scott
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
import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class Jetty6xVersionFileChecker extends AbstractJettyVersionFileChecker
{
  @NonNls private static final String VERSION_FILE_NAME = "VERSION.txt";
  @NonNls private static final String VERSION_PATTERN = "^jetty-(6\\.1\\.\\d+) .*";

  @NotNull public String getVersionFileName()
  {
    return VERSION_FILE_NAME;
  }

  @NotNull public String getVersionPattern()
  {
    return VERSION_PATTERN;
  }
}
