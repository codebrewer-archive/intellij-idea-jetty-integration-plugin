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
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.DocumentAdapter;
import org.codebrewer.idea.jetty.components.ConfigurationFileJTable;
import org.codebrewer.idea.jetty.components.ConfigurationFileTableModel;
import org.codebrewer.idea.jetty.components.TableWithARUMButtons;
import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class JettyDataEditor extends ApplicationServerPersistentDataEditor<JettyPersistentData>
{
  public static FileChooserDescriptor JETTY_CONFIGURATION_FILE_CHOOSER_DESCRIPTOR =
      new FileChooserDescriptor(true, false, false, false, false, false)
      {
        public boolean isFileVisible(final VirtualFile file, final boolean showHiddenFiles)
        {
          boolean result = super.isFileVisible(file, showHiddenFiles);

          if (!file.isDirectory()) {
            result &= JettyUtil.isJettyConfigurationFile(file.getPath());
          }

          return result;
        }
      };

  public static FileChooserDescriptor JETTY_CONFIGURATION_FILES_CHOOSER_DESCRIPTOR =
      new FileChooserDescriptor(true, false, false, false, false, true)
      {
        public boolean isFileVisible(final VirtualFile file, final boolean showHiddenFiles)
        {
          boolean result = super.isFileVisible(file, showHiddenFiles);

          if (!file.isDirectory()) {
            result &= JettyUtil.isJettyConfigurationFile(file.getPath());
          }

          return result;
        }
      };

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

  /**
   * Used to manage the configuration files to be used with the Jetty installation.
   */
  private final ServerConfigurationFileTable jettyConfigurationFileTable;

  public JettyDataEditor()
  {
    panel = new JPanel();
    jettyHomeField = new TextFieldWithBrowseButton();
    jettyVersionLabel = new JLabel();
    jettyConfigurationFileTable = new ServerConfigurationFileTable();
    build();
  }

  private void build()
  {
    panel.setLayout(new BorderLayout());

    final JPanel northPanel = new JPanel(new GridBagLayout());
    final Insets insets = new Insets(5, 0, 0, 0);

    // Label for the Jetty home directory chooser widget
    //
    final JLabel jettyHomeFieldLabel = new JLabel(JettyBundle.message("label.configuration.jetty.home"));
    jettyHomeFieldLabel.setLabelFor(jettyHomeField);
    northPanel.add(jettyHomeFieldLabel,
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
    northPanel.add(jettyHomeField,
        new GridBagConstraints(
            1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    // Label for the Jetty version label
    //
    final JLabel jettyVersionLabelLabel = new JLabel(JettyBundle.message("label.configuration.detected.jetty.version"));
    northPanel.add(jettyVersionLabelLabel,
        new GridBagConstraints(
            0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    // Label for displaying the Jetty version detected from the installation
    //
    northPanel.add(jettyVersionLabel,
        new GridBagConstraints(
            1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0));

    panel.add(northPanel, BorderLayout.NORTH);
    panel.add(jettyConfigurationFileTable.getComponent(), BorderLayout.CENTER);
  }

  private void update()
  {
    final String homeDir = jettyHomeField.getText();

    if (homeDir.length() > 0) {
      final String jettyVersion = JettyUtil.getVersion(homeDir);

      if (jettyVersion == null) {
        jettyVersionLabel.setText(JettyBundle.message("message.text.jetty.not.found"));
        jettyVersionLabel.setForeground(Color.RED);
//        clearConfigurationFileTable();
      }
      else if (jettyVersion.startsWith("6.1")) {
        jettyVersionLabel.setText(jettyVersion);
        jettyVersionLabel.setForeground(Color.BLACK);

        if (jettyConfigurationFileTable.getConfigurationFiles().size() == 0) {
          fillConfigurationFileTable(JettyUtil.baseConfigDir(homeDir));
        }
      }
      else {
        jettyVersionLabel.setText(jettyVersion);
        jettyVersionLabel.setForeground(Color.RED);
//        clearConfigurationFileTable();
      }
    }
    else {
      jettyVersionLabel.setText(null);
//      clearConfigurationFileTable();
    }
  }

//  private void clearConfigurationFileTable()
//  {
//    final ConfigurationFileTableModel model =
//        (ConfigurationFileTableModel) jettyConfigurationFileTable.table.getModel();
//    model.removeConfigurationFiles();
//  }

  private void fillConfigurationFileTable(final String configDirPath)
  {
    final File configDir = new File(configDirPath);

    if (configDir.isDirectory() && configDir.canRead()) {
      final String[] configFiles = configDir.list(new FilenameFilter()
      {
        public boolean accept(final File dir, final String name)
        {
          return JettyUtil.isJettyConfigurationFile(new File(dir, name).getAbsolutePath());
        }
      });

      if (configFiles != null && configFiles.length > 0) {
        final List<JettyPersistentData.JettyConfigurationFile> configFilesList =
            new ArrayList<JettyPersistentData.JettyConfigurationFile>(configFiles.length);

        for (int i = 0; i < configFiles.length; i++) {
          final String configFilePath = new File(configDirPath, configFiles[i]).getAbsolutePath();

          try {
            configFilesList.add(new JettyPersistentData.JettyConfigurationFile(configFilePath, true));
          }
          catch (ConfigurationException e) {
            // Shouldn't happen unless the file changes on disk in a small time window...
          }
        }

        Collections.sort(configFilesList, new Comparator<JettyPersistentData.JettyConfigurationFile>()
        {
          public int compare(
              final JettyPersistentData.JettyConfigurationFile o1, final JettyPersistentData.JettyConfigurationFile o2)
          {
            return o1.getFile().getAbsolutePath().compareTo(o2.getFile().getAbsolutePath());
          }
        });

        jettyConfigurationFileTable.setConfigurationFiles(configFilesList);
      }
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
    s.setJettyConfigurationFiles(jettyConfigurationFileTable.getConfigurationFiles());
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
    final List<JettyPersistentData.JettyConfigurationFile> configurationFiles = s.getJettyConfigurationFiles();
    jettyConfigurationFileTable.setConfigurationFiles(configurationFiles);
    update();
  }

  private class ServerConfigurationFileTable extends TableWithARUMButtons
  {
    VirtualFile[] chosenFiles = null;

    public ServerConfigurationFileTable()
    {
      super(new ConfigurationFileJTable(), null);
      getComponent().setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createEmptyBorder(5, 0, 0, 0),
          BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
              JettyBundle.message("data.editor.configuration.files.title"))));
    }

    protected void ensureRowVisible(final int row)
    {
      if (table.getParent() instanceof JViewport) {
        final JViewport viewport = (JViewport) table.getParent();
        final Rectangle rect = table.getCellRect(row, 0, true);
        final Point p = viewport.getViewPosition();

        rect.setLocation(rect.x - p.x, rect.y - p.y);
        viewport.scrollRectToVisible(rect);
      }
    }

    protected @NotNull List<JettyPersistentData.JettyConfigurationFile> getConfigurationFiles()
    {
      final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();
      final List<JettyPersistentData.JettyConfigurationFile> result = model.getConfigurationFiles();

      return result;
    }

    protected void setConfigurationFiles(
        @NotNull final List<JettyPersistentData.JettyConfigurationFile> configurationFiles)
    {
      final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();
      model.removeConfigurationFiles();

      for (final JettyPersistentData.JettyConfigurationFile configurationFile : configurationFiles) {
        model.addConfigurationFile(configurationFile);
      }
    }

    protected void doAdd()
    {
      final FileChooserDialog fileChooserDialog =
          FileChooserFactory.getInstance().createFileChooser(JETTY_CONFIGURATION_FILES_CHOOSER_DESCRIPTOR, project);
      final VirtualFile initialSelection;

      if (chosenFiles != null && chosenFiles.length > 0) {
        initialSelection = chosenFiles[chosenFiles.length - 1];
      }
      else if (jettyHomeField.getText().length() > 0) {
        initialSelection = VirtualFileManager.getInstance().findFileByUrl(
            JettyConstants.FILE_SCHEME + jettyHomeField.getText());
      }
      else {
        initialSelection = null;
      }

      chosenFiles = fileChooserDialog.choose(initialSelection, project);

      if (chosenFiles != null && chosenFiles.length > 0) {
        final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();
        String path;
        JettyPersistentData.JettyConfigurationFile file;

        for (int i = 0; i < chosenFiles.length; i++) {
          path = chosenFiles[i].getPath();

          try {
            file = new JettyPersistentData.JettyConfigurationFile(path, true);
            model.addConfigurationFile(file);
          }
          catch (ConfigurationException e) {
            // Can't happen unless the file changes on disk between being
            // selected in the chooser and checked here
          }

          final int lastRowIndex = model.getRowCount() - 1;
          table.setRowSelectionInterval(lastRowIndex, lastRowIndex);
        }
      }
    }

    protected void doMoveDown()
    {
      final int selectedRow = table.getSelectedRow();
      final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();

      model.moveConfigurationFileDown(selectedRow);
      table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
      ensureRowVisible(selectedRow + 1);
      moveDownButton.requestFocus();
    }

    protected void doMoveUp()
    {
      final int selectedRow = table.getSelectedRow();
      final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();

      model.moveConfigurationFileUp(selectedRow);
      table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
      ensureRowVisible(selectedRow + 1);
      moveUpButton.requestFocus();
    }

    protected void doRemove()
    {
      final int[] selectedRows = table.getSelectedRows();
      final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();

      for (int i = selectedRows.length; i > 0; i--) {
        final int selectedRow = selectedRows[i - 1];

        model.removeConfigurationFile(selectedRow);
      }

      final int rowCount = model.getRowCount();

      if (rowCount > 0) {
        final int rowToSelect = Math.min(rowCount - 1, selectedRows[selectedRows.length - 1]);
        table.setRowSelectionInterval(rowToSelect, rowToSelect);
      }
    }

    protected void doUpdate()
    {
      final int selectedRow = table.getSelectedRow();
      final String configurationFilePath = (String) table.getValueAt(selectedRow, 0);
      final VirtualFile initialSelection =
          VirtualFileManager.getInstance().findFileByUrl(JettyConstants.FILE_SCHEME + configurationFilePath);
      final FileChooserDialog fileChooserDialog =
          FileChooserFactory.getInstance().createFileChooser(JETTY_CONFIGURATION_FILE_CHOOSER_DESCRIPTOR, project);
      final VirtualFile[] chosenFile = fileChooserDialog.choose(initialSelection, project);

      if (chosenFile != null && chosenFile.length == 1) {
        final ConfigurationFileTableModel model = (ConfigurationFileTableModel) table.getModel();
        final String path = chosenFile[0].getPath();

        try {
          final JettyPersistentData.JettyConfigurationFile file =
              new JettyPersistentData.JettyConfigurationFile(path, true);
          model.setValueAt(file.getFile().getAbsolutePath(), selectedRow, 0);
        }
        catch (ConfigurationException e) {
          // Can't happen unless the file changes on disk between being
          // selected in the chooser and checked here
        }
      }
    }
  }
}