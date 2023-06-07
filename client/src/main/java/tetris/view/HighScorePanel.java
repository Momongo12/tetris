package tetris.view;

import tetris.dataAccessLayer.HighScoreDataAccessObject;
import tetris.dataAccessLayer.HighScoreTableModel;
import tetris.model.HighScore;

import static tetris.resource.ResourceManager.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HighScorePanel extends JPanel {
    private static final int TABLE_WIDTH = 500;
    private static final int TABLE_HEIGHT = 200;

    private HighScoreTableModel tableModel;

    private JTable table;

    public HighScorePanel() {
        setPreferredSize(new Dimension(1000, 700));
        setSize(getPreferredSize());
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createBevelBorder(0, Color.black, Color.black));
        setBackground(new Color(6, 6, 9));

        JPanel panelOffset = new JPanel();
        panelOffset.setOpaque(false);
        panelOffset.setPreferredSize(new Dimension(1000, 100));
        panelOffset.setSize(getPreferredSize());
        panelOffset.setLayout(new BorderLayout());

        JButton updateTableButton = new JButton("Update High Score");
        updateTableButton.setFont(new Font("Showcard Gothic", Font.PLAIN, 19));
        updateTableButton.setIcon(getImg("refresh.png", 30, 30));
        updateTableButton.setForeground(Color.WHITE);
        updateTableButton.setBackground(new Color(0, 0, 0, 0));
        updateTableButton.setOpaque(false);
        updateTableButton.setBorder(null);
        updateTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HighScore highScore = HighScoreDataAccessObject.getHighScoreDataFromDB();
                    tableModel.updateHighScoreData(highScore);
                    table.updateUI();
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
        });

        JPanel panelOffset2 = new JPanel();
        panelOffset2.setOpaque(false);
        panelOffset2.setPreferredSize(new Dimension(1000, 100));
        panelOffset2.setSize(getPreferredSize());
        panelOffset2.setLayout(new BorderLayout());

        JPanel interPanel = new JPanel();
        interPanel.setOpaque(false);
        interPanel.setLayout(new FlowLayout());
        interPanel.add(updateTableButton);

        panelOffset2.add(interPanel);


        JPanel nameHighScoreTableName = createHighScoreTableName();

        JScrollPane tableScrollPane = createTableScrollPane();

        add(panelOffset);
        add(nameHighScoreTableName);
        add(tableScrollPane);
        add(panelOffset2);
    }

    private JPanel createHighScoreTableName() {
        JLabel HighScoreTableName = new JLabel("High Scores");
        HighScoreTableName.setBackground(new Color(0, 0, 0, 0));
        HighScoreTableName.setOpaque(false);
        HighScoreTableName.setForeground(Color.WHITE);
        HighScoreTableName.setFont(new Font("Arial", Font.BOLD, 64));

        JPanel HighScoreTableNameBox = new JPanel();
        HighScoreTableNameBox.setPreferredSize(new Dimension(getWidth(), 80));
        HighScoreTableNameBox.setBackground(new Color(0, 0, 0, 0));
        HighScoreTableNameBox.setOpaque(false);
        HighScoreTableNameBox.add(HighScoreTableName, BorderLayout.CENTER);

        return HighScoreTableNameBox;
    }

    private JTable createTable() {
        HighScore highScore = new HighScore(new ArrayList<>());
        try {
            highScore = HighScoreDataAccessObject.getHighScoreDataFromDB();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        tableModel = new HighScoreTableModel(highScore);


        table = new JTable(tableModel);

        table.setShowGrid(false);
        table.setEnabled(false);
        table.setDragEnabled(false);
        table.setDropMode(DropMode.USE_SELECTION);
        table.setBackground(new Color(26, 26, 32));
        table.setSelectionBackground(new Color(0, 0, 0, 0));
        table.setPreferredScrollableViewportSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
        table.setRowHeight(30);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setEnabled(false);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);
        tableHeader.setOpaque(false);
        tableHeader.setBackground(new Color(0, 0, 0, 0));
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 21));

        TableColumnModel columnModel = table.getColumnModel();
        int columnCount = columnModel.getColumnCount();

        for (int i = 0; i < columnCount; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    JLabel component = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    component.setOpaque(false);
                    component.setBackground(new Color(26, 26, 32));
                    component.setForeground(Color.WHITE);
                    component.setFont(new Font("Arial", Font.PLAIN, 14));
                    component.setHorizontalAlignment(JLabel.CENTER);
                    return component;
                }
            });
        }
        return table;
    }

    private JScrollPane createTableScrollPane() {
        JTable table = createTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(26, 26, 32));

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                // установить цвет фона
                this.thumbColor = Color.GRAY;
                // установить цвет полосы прокрутки
                this.trackColor = Color.DARK_GRAY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return invisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return invisibleButton();
            }

            private JButton invisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
        scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(204, 204, 204));
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }
        });

        return scrollPane;
    }

}
