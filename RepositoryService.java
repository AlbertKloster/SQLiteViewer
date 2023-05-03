package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RepositoryService {

    private final Repository repository;
    private String dbName;

    public RepositoryService() {
        repository = new Repository();
    }

    public void handleOpenDb(JTextField fileNameTextField, JComboBox<String> tablesComboBox, JTextArea queryTextArea, JButton executeQueryButton) throws SQLException {
        dbName = fileNameTextField.getText();

        tablesComboBox.removeAllItems();
        tablesComboBox.setEnabled(false);
        queryTextArea.setText("");
        queryTextArea.setEnabled(false);
        executeQueryButton.setEnabled(false);
        if (dbName.isEmpty())  return;

        List<String> tables = getAllTablesFromDb(fileNameTextField.getText());
        if (tables.isEmpty()) return;

        tablesComboBox.removeAllItems();
        tables.forEach(tablesComboBox::addItem);
        tablesComboBox.setSelectedIndex(0);
        handleChangeTable(tablesComboBox, queryTextArea);
        tablesComboBox.setEnabled(true);
        queryTextArea.setEnabled(true);
        executeQueryButton.setEnabled(true);
    }

    public void handleChangeTable(JComboBox<String> tablesComboBox, JTextArea queryTextArea) {
        queryTextArea.setText(String.format("SELECT * FROM %s;", tablesComboBox.getSelectedItem()));
    }

    private List<String> getAllTablesFromDb(String dbName) throws SQLException {
        return repository.getAllTablesFromDb(dbName);
    }

    public JTable handleExecuteQuery(JTextArea queryTextArea) throws SQLException {
        Table table = repository.executeQuery(queryTextArea.getText(), dbName);
        return new JTable(table.getData(), table.getColumnNames());
    }
}
