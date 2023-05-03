package viewer;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        RepositoryService service = new RepositoryService();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");

        // Header
        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setPreferredSize(new Dimension(getWidth() - 100, 28));

        JButton openFileButton = new JButton();
        openFileButton.setName("OpenFileButton");
        openFileButton.setText("Open");

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());
        headerPanel.add(fileNameTextField);
        headerPanel.add(openFileButton);

        // Tables
        JComboBox<String> tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");
        tablesComboBox.setPreferredSize(new Dimension(getWidth() - 31, 28));
        tablesComboBox.setEnabled(true);
        JPanel tablesComboBoxPanel = new JPanel();
        tablesComboBoxPanel.setLayout(new FlowLayout());
        tablesComboBoxPanel.add(tablesComboBox);

        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setPreferredSize(new Dimension(getWidth() - 120, 120));
        queryTextArea.setEnabled(false);

        JButton executeQueryButton = new JButton();
        executeQueryButton.setName("ExecuteQueryButton");
        executeQueryButton.setText("Execute");
        executeQueryButton.setEnabled(false);

        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new FlowLayout());
        queryPanel.add(queryTextArea);

        JPanel executeQueryButtonBox = new JPanel();
        executeQueryButtonBox.setLayout(new FlowLayout());
        queryPanel.add(executeQueryButton);

        JScrollPane tablePane = new JScrollPane();
        tablePane.setPreferredSize(new Dimension(getWidth() - 24, 346));

        JTable table = new JTable();
        table.setName("Table");
        table.setPreferredSize(new Dimension(getWidth() - 24, 340));
        tablePane.setViewportView(table);
        queryPanel.add(tablePane);

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BorderLayout());
        tablesPanel.add(tablesComboBoxPanel, BorderLayout.NORTH);
        tablesPanel.add(queryPanel, BorderLayout.CENTER);

        openFileButton.addActionListener(e -> {
            tablePane.remove(table);
            JTable emptyTable = new JTable();
            emptyTable.setName("Table");
            emptyTable.setPreferredSize(new Dimension(getWidth() - 24, 340));
            tablePane.setViewportView(emptyTable);
            try {
                service.handleOpenDb(fileNameTextField, tablesComboBox, queryTextArea, executeQueryButton);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(new Frame(), ex.getMessage());
            }
            tablePane.remove(table);
            JTable newTable;
            try {
                newTable = service.handleExecuteQuery(queryTextArea);
            } catch (SQLException ex) {
                newTable = new JTable();
                JOptionPane.showMessageDialog(new Frame(), ex.getMessage());
            }
            newTable.setName("Table");
            newTable.setPreferredSize(new Dimension(getWidth() - 24, 340));
            tablePane.setViewportView(newTable);
        });
        tablesComboBox.addActionListener(e -> service.handleChangeTable(tablesComboBox, queryTextArea));
        executeQueryButton.addActionListener(e -> {
            tablePane.remove(table);
            JTable newTable;
            try {
                newTable = service.handleExecuteQuery(queryTextArea);
            } catch (SQLException ex) {
                newTable = new JTable();
                JOptionPane.showMessageDialog(new Frame(), ex.getMessage());
            }
            newTable.setName("Table");
            newTable.setPreferredSize(new Dimension(getWidth() - 24, 340));
            tablePane.setViewportView(newTable);
        });

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);

        setVisible(true);
    }

}
