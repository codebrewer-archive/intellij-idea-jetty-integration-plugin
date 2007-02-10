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

import com.intellij.javaee.appServerIntegrations.ApplicationServerPersistentDataEditor;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyDataEditor extends ApplicationServerPersistentDataEditor<JettyPersistentData>
{
  private static void checkIsDirectory(final File file) throws ConfigurationException
  {
    if (!file.isDirectory()) {
      throw new ConfigurationException(JettyBundle.message("message.text.cant.find.directory", file.getAbsolutePath()));
    }
  }

  /**
   * The editor UI.
   */
  private final JPanel panel;

  /**
   * Used to choose Jetty's home directory.
   */
  private final TextFieldWithBrowseButton jettyHomeField;

  /**
   * Displays the Jetty version number determined by examining the Jetty installation.
   */
  private final JLabel jettyVersionLabel;

  public JettyDataEditor()
  {
    panel = new JPanel();
    jettyHomeField = new TextFieldWithBrowseButton();
    jettyVersionLabel = new JLabel();
    build();
  }

  private void build()
  {
    panel.setLayout(new GridBagLayout());

    final Insets insets = new Insets(5, 0, 0, 0);

    // Label for the Jetty home directory chooser widget
    //
    final JLabel jettyHomeFieldLabel = new JLabel(JettyBundle.message("label.configuration.jetty.home"));
    jettyHomeFieldLabel.setLabelFor(jettyHomeField);
    panel.add(jettyHomeFieldLabel,
        new GridBagConstraints(
            0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    // Widget for choosing the Jetty installation directory
    //
    jettyHomeField.setPreferredSize(new Dimension(Integer.MAX_VALUE, jettyHomeField.getPreferredSize().height));
    jettyHomeField.setText(JettyUtil.getDefaultLocation());
    jettyHomeField.getTextField().setEditable(true);
    jettyHomeField.addBrowseFolderListener(JettyBundle.message("chooser.title.jetty.home.directory"),
        JettyBundle.message("chooser.description.jetty.home.directory"),
        null,
        new FileChooserDescriptor(false, true, false, false, false, false));
    jettyHomeField.getTextField().getDocument().addDocumentListener(new DocumentAdapter()
    {
      public void textChanged(final DocumentEvent event)
      {
        update();
      }
    });
    panel.add(jettyHomeField,
        new GridBagConstraints(
            1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    // Label for the Jetty version label
    //
    final JLabel jettyVersionLabelLabel = new JLabel(JettyBundle.message("label.configuration.detected.jetty.version"));
    panel.add(jettyVersionLabelLabel,
        new GridBagConstraints(
            0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    // Label for displaying the Jetty version detected from the installation
    //
    panel.add(jettyVersionLabel,
        new GridBagConstraints(
            1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    // Component to absorb vertical space
    //
    panel.add(Box.createVerticalGlue(),
        new GridBagConstraints(
            0, 2, 2, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));
  }

  private void update()
  {
    final String homeDir = jettyHomeField.getText();

    if (homeDir.length() > 0) {
      final String jettyVersion = JettyUtil.getVersion(homeDir);

      if (jettyVersion == null) {
        jettyVersionLabel.setText(JettyBundle.message("message.text.jetty.not.found"));
        jettyVersionLabel.setForeground(Color.RED);
      }
      else if (jettyVersion.startsWith("6.1")) {
        jettyVersionLabel.setText(jettyVersion);
        jettyVersionLabel.setForeground(Color.BLACK);
      }
      else {
        jettyVersionLabel.setText(jettyVersion);
        jettyVersionLabel.setForeground(Color.RED);
      }
    }
    else {
      jettyVersionLabel.setText(null);
    }
  }

  protected void applyEditorTo(final JettyPersistentData s) throws ConfigurationException
  {
    final File home = new File(jettyHomeField.getText()).getAbsoluteFile();
    checkIsDirectory(home);
    checkIsDirectory(new File(home, JettyConstants.JETTY_CONFIG_DIRECTORY_NAME));
    checkIsDirectory(new File(home, JettyConstants.JETTY_LIB_DIRECTORY_NAME));

    s.setJettyHome(home.getAbsolutePath().replace(File.separatorChar, '/'));
    s.setJettyVersion(jettyVersionLabel.getText());
  }

  @NotNull
  protected JComponent createEditor()
  {
    return panel;
  }

  protected void disposeEditor()
  {
  }

  protected void resetEditorFrom(final JettyPersistentData s)
  {
    jettyHomeField.setText(s.getJettyHome().replace('/', File.separatorChar));
    update();
  }
}