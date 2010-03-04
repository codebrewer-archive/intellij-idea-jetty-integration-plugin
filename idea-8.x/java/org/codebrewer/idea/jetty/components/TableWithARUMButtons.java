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

import com.intellij.javaee.ui.DisposableComponent;
import com.intellij.openapi.project.Project;
import com.intellij.util.xml.ui.CommittablePanel;
import org.codebrewer.idea.jetty.JettyBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A class that wraps a table and provides add, remove, update (edit), move up
 * and move down buttons that may be used to control table entries and
 * positions.  Inspired by the Jetbrains
 * {@link com.intellij.javaee.ui.TableWithCRUDButtons TableWithCRUDButtons}
 * class.
 *
 * @author Mark Scott
 * @version $Id$
 */
public abstract class TableWithARUMButtons extends DisposableComponent implements CommittablePanel
{
  protected final JButton addButton;
  protected final JButton removeButton;
  protected final JButton updateButton;
  protected final JButton moveUpButton;
  protected final JButton moveDownButton;
  protected final JTable table;
  private final JPanel mainPanel;
  private final JScrollPane scrollPane;
  protected final Project project;

  protected TableWithARUMButtons(@NotNull final JTable table, @Nullable final Project project)
  {
    this.table = table;
    this.project = project;
    addButton = new JButton(JettyBundle.message("action.name.add"));
    removeButton = new JButton(JettyBundle.message("action.name.remove"));
    updateButton = new JButton(JettyBundle.message("action.name.update"));
    moveDownButton = new JButton(JettyBundle.message("action.name.move.down"));
    moveUpButton = new JButton(JettyBundle.message("action.name.move.up"));
    mainPanel = new JPanel(new BorderLayout());
    scrollPane = new JScrollPane(this.table);

    build();
    addListeners();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        final Component component = scrollPane.getViewport().getView();

        if (component != null) {
          scrollPane.getViewport().setBackground(component.getBackground());
        }

        selectionChanged();
      }
    });
  }

  private void selectionChanged()
  {
    final int[] selectedRows = table.getSelectedRows();

    moveUpButton.setEnabled(selectedRows.length == 1 && selectedRows[0] != 0);
    moveDownButton.setEnabled(selectedRows.length == 1 && selectedRows[0] != table.getRowCount() - 1);
    removeButton.setEnabled(selectedRows.length != 0);
    updateButton.setEnabled(selectedRows.length == 1);
  }

  protected void addListeners()
  {
    addButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        doAdd();
      }
    });
    removeButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        doRemove();
      }
    });
    updateButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        doUpdate();
      }
    });
    moveUpButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        doMoveUp();
      }
    });
    moveDownButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        doMoveDown();
      }
    });

    table.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(final MouseEvent e)
      {
        if (e.getClickCount() == 2) {
          doUpdate();
        }
      }
    });

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(final ListSelectionEvent e)
      {
        selectionChanged();
      }
    });
  }

  protected void build()
  {
    resizeButtons(new JButton[]{ addButton, removeButton, updateButton, moveDownButton, moveUpButton });

    // Table for the configuration files
    //
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    final JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

    // Button to add a new file
    //
    buttonPanel.add(addButton);
    buttonPanel.add(Box.createVerticalStrut(6));

    // Button to remove a file
    //
    buttonPanel.add(removeButton);
    buttonPanel.add(Box.createVerticalStrut(6));

    // Button to update a file
    //
    buttonPanel.add(updateButton);
    buttonPanel.add(Box.createVerticalStrut(6));

    // Button to move a file up
    //
    buttonPanel.add(moveUpButton);
    buttonPanel.add(Box.createVerticalStrut(6));

    // Button to move a file down
    //
    buttonPanel.add(moveDownButton);
    buttonPanel.add(Box.createVerticalGlue());

    mainPanel.add(buttonPanel, BorderLayout.EAST);
    scrollPane.setMinimumSize(new Dimension(480, buttonPanel.getPreferredSize().height));
  }

  protected void resizeButtons(@NotNull final JButton[] buttons)
  {
    int maxDefaultWidth = Integer.MIN_VALUE;

    for (final JButton button : buttons) {
      maxDefaultWidth = Math.max(maxDefaultWidth, button.getPreferredSize().width);
    }

    for (final JButton button : buttons) {
      final Dimension uniformSize = new Dimension(maxDefaultWidth, button.getPreferredSize().height);

      button.setMaximumSize(uniformSize);
      button.setMinimumSize(uniformSize);
      button.setPreferredSize(uniformSize);
    }
  }

  public void commit()
  {
  }

  public JComponent getComponent()
  {
    return mainPanel;
  }

  public final void reset()
  {
  }

  protected abstract void doAdd();

  protected abstract void doMoveDown();

  protected abstract void doMoveUp();

  protected abstract void doRemove();

  protected abstract void doUpdate();
}