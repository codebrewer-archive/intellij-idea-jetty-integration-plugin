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

import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyLocalRunConfigurationEditor extends SettingsEditor<CommonModel>
{
  protected void resetEditorFrom(final CommonModel s)
  {
  }

  protected void applyEditorTo(final CommonModel s) throws ConfigurationException
  {
  }

  @NotNull
  protected JComponent createEditor()
  {
//    return new JLabel("JettyLocalRunConfigurationEditor");
    return new JLabel();
  }

  protected void disposeEditor()
  {
  }
}