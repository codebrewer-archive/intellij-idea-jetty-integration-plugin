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
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * @author Mark Scott
 * @version $Id$
 */
public abstract class AbstractJettyVersionHelper implements JettyVersionHelper
{
  private final String versionString;

  public AbstractJettyVersionHelper(@NotNull String versionString,
                                    @NotNull Pattern versionPattern) throws JettyException
  {
    if (!versionPattern.matcher(versionString).matches()) {
      throw new JettyException(JettyBundle.message("exception.text.unsupported.jetty.version", versionString));
    }

    this.versionString = versionString;
  }

  public String getVersionString()
  {
    return versionString;
  }
}
