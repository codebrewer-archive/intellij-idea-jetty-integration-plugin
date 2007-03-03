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
package org.codebrewer.idea.jetty.components;

import org.codebrewer.idea.jetty.JettyBundle;
import org.codebrewer.idea.jetty.JettyPersistentData;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.intellij.openapi.options.ConfigurationException;

/**
 * @author Mark Scott
 * @version $Id$
 */
public class ConfigurationFileTableModel extends AbstractTableModel
{
  private static final String[] HEADERS = new String[]{
      JettyBundle.message("configuration.file.table.model.column.0"),
      JettyBundle.message("configuration.file.table.model.column.1") };

  @NonNls private static final String INVALID_ROW_INDEX_MESSAGE =
      "Invalid row index (should be in range {0} to {1}): {2}";
  @NonNls private static final String INVALID_COLUMN_INDEX_MESSAGE = "Invalid column index (should be 0 or 1): {0}";

  private final List<JettyPersistentData.JettyConfigurationFile> configurationFiles;

  public ConfigurationFileTableModel()
  {
    super();
    configurationFiles = new ArrayList<JettyPersistentData.JettyConfigurationFile>();
  }

  private String getConfigurationFilePath(final int rowIndex)
  {
    if (rowIndex < 0 || rowIndex >= configurationFiles.size()) {
      throw new IllegalArgumentException(
          MessageFormat.format(INVALID_ROW_INDEX_MESSAGE, 0, configurationFiles.size() - 1, rowIndex));
    }

    final String result = configurationFiles.get(rowIndex).getFile().getAbsolutePath();
    return result;
  }

  private boolean isConfigurationFileActive(final int rowIndex)
  {
    if (rowIndex < 0 || rowIndex >= configurationFiles.size()) {
      throw new IllegalArgumentException(
          MessageFormat.format(INVALID_ROW_INDEX_MESSAGE, 0, configurationFiles.size() - 1, rowIndex));
    }

    final boolean result = configurationFiles.get(rowIndex).isActive();
    return result;
  }

  public void addConfigurationFile(@NotNull final JettyPersistentData.JettyConfigurationFile configurationFile)
  {
    configurationFiles.add(configurationFile);
    fireTableDataChanged();
  }

  public @NotNull List<JettyPersistentData.JettyConfigurationFile> getConfigurationFiles()
  {
    return Collections.unmodifiableList(configurationFiles);
  }

  public void moveConfigurationFileDown(final int rowIndex)
  {
    if (rowIndex < 0 || rowIndex > configurationFiles.size() - 2) {
      throw new IllegalArgumentException(
          MessageFormat.format(INVALID_ROW_INDEX_MESSAGE, 0, configurationFiles.size() - 2, rowIndex));
    }

    final JettyPersistentData.JettyConfigurationFile configurationFile = configurationFiles.remove(rowIndex);

    configurationFiles.add(rowIndex + 1, configurationFile);
    fireTableDataChanged();
  }

  public void moveConfigurationFileUp(final int rowIndex)
  {
    if (rowIndex < 1 || rowIndex >= configurationFiles.size()) {
      throw new IllegalArgumentException(
          MessageFormat.format(INVALID_ROW_INDEX_MESSAGE, 1, configurationFiles.size() - 1, rowIndex));
    }

    final JettyPersistentData.JettyConfigurationFile configurationFile = configurationFiles.remove(rowIndex);

    configurationFiles.add(rowIndex - 1, configurationFile);
    fireTableDataChanged();
  }

  public void removeConfigurationFile(final int rowIndex)
  {
    if (rowIndex < 0 || rowIndex >= configurationFiles.size()) {
      throw new IllegalArgumentException(
          MessageFormat.format(INVALID_ROW_INDEX_MESSAGE, 0, configurationFiles.size() - 1, rowIndex));
    }

    configurationFiles.remove(rowIndex);
    fireTableDataChanged();
  }

  public void removeConfigurationFiles()
  {
    configurationFiles.clear();
    fireTableDataChanged();
  }

  public int getColumnCount()
  {
    return HEADERS.length;
  }

  @Override public String getColumnName(final int column)
  {
    if (column < 0 || column >= HEADERS.length) {
      throw new IllegalArgumentException(
          "Column number should be in range 0 to " + (HEADERS.length - 1) + ": " + column);
    }

    return HEADERS[column];
  }

  public int getRowCount()
  {
    return configurationFiles.size();
  }

  public Object getValueAt(final int rowIndex, final int columnIndex)
  {
    final Object result;

    switch (columnIndex) {
      case 0:
        result = getConfigurationFilePath(rowIndex);
        break;
      case 1:
        result = isConfigurationFileActive(rowIndex);
        break;
      default:
        throw new IllegalArgumentException(MessageFormat.format(INVALID_COLUMN_INDEX_MESSAGE, columnIndex));
    }

    return result;
  }

  @Override public boolean isCellEditable(final int rowIndex, final int columnIndex)
  {
    return columnIndex == 1;
  }

  public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
  {
    if (isCellEditable(rowIndex, columnIndex)) {
      final JettyPersistentData.JettyConfigurationFile configurationFile = configurationFiles.get(rowIndex);

      switch (columnIndex) {
        case 0:
          try {
            configurationFile.setPath((String) aValue);
          }
          catch (ConfigurationException e) {
            // Todo - post a dialog
          }
          break;
        case 1:
          configurationFile.setActive((Boolean) aValue);
          break;
        default:
          ;
      }

      fireTableRowsUpdated(rowIndex, rowIndex);
    }
  }
}