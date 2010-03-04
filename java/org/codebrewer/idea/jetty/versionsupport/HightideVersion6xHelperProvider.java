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
import org.codebrewer.idea.jetty.versionsupport.extensions.JettyVersionHelperProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * <p>
 * Provider of a helper that handles Hightide 6.x.
 * </p>
 *
 * @author Mark Scott
 * @version $Id$
 */
public class HightideVersion6xHelperProvider implements JettyVersionHelperProvider
{
  @NotNull
  public JettyVersionHelper getJettyVersionHelper(@NotNull String versionString) throws JettyException
  {
    return new HightideVersion6xHelper(versionString);
  }

  public static class HightideVersion6xHelper extends AbstractJettyVersionHelper
  {
    @NonNls
    private static final Pattern VERSION_PATTERN = Pattern.compile("^hightide-(.*) - .*");

    public HightideVersion6xHelper(@NotNull String versionString) throws JettyException
    {
      super(versionString, VERSION_PATTERN);
    }

    @NotNull
    public ConfigurationFileHelper getConfigurationFileHelper()
    {
      return new Jetty6xConfigurationFileHelper();
    }

    @NotNull
    public ProcessHelper getProcessHelper()
    {
      return new Jetty6xProcessHelper();
    }
  }
}
