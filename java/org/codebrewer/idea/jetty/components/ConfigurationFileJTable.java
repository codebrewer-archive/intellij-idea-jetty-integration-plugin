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

import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.ui.BooleanTableCellRenderer;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * A table that displays information about Jetty configuration files.  Two
 * columns are used: the first shows the absolute path to the configuration file
 * while the second indicates whether or not the file is active.  Only active
 * files are used when Jetty is launched.  The table prevents column re-ordering
 * and resizing, specifies a custom editor and a custom renderer for the second
 * column and enforces the class of table model used.
 *
 * @author Mark Scott
 * @version $Id$
 */
public class ConfigurationFileJTable extends JTable
{
  private static final int FILE_ACTIVE_COLUMN_INDEX = 1;

  private final TableCellEditor booleanCellEditor = new BooleanTableCellEditor(false);
  private final TableCellRenderer booleanCellRenderer = new BooleanTableCellRenderer();

  public ConfigurationFileJTable()
  {
    super();
  }

  @Override public TableCellEditor getCellEditor(final int row, final int column)
  {
    if (column == FILE_ACTIVE_COLUMN_INDEX) {
      return booleanCellEditor;
    }
    else {
      return super.getCellEditor(row, column);
    }
  }

  @Override public TableCellRenderer getCellRenderer(final int row, final int column)
  {
    if (column == FILE_ACTIVE_COLUMN_INDEX) {
      return booleanCellRenderer;
    }
    else {
      return super.getCellRenderer(row, column);
    }
  }

  @Override public void createDefaultColumnsFromModel()
  {
    super.createDefaultColumnsFromModel();

    final int fileActiveColumnWidth = 3 * new JCheckBox().getPreferredSize().width;
    final TableColumn fileActiveColumn = getColumn(getColumnName(FILE_ACTIVE_COLUMN_INDEX));

    fileActiveColumn.setMaxWidth(fileActiveColumnWidth);
    fileActiveColumn.setMinWidth(fileActiveColumnWidth);
    fileActiveColumn.setPreferredWidth(fileActiveColumnWidth);
  }

  @Override protected TableModel createDefaultDataModel()
  {
    return new ConfigurationFileTableModel();
  }

  @Override protected JTableHeader createDefaultTableHeader()
  {
    final JTableHeader defaultTableHeader = super.createDefaultTableHeader();

    defaultTableHeader.setReorderingAllowed(false);

    return defaultTableHeader;
  }

  @Override protected ListSelectionModel createDefaultSelectionModel()
  {
    final ListSelectionModel listSelectionModel = new DefaultListSelectionModel();

    listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    return listSelectionModel;
  }
}