package viewer;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Repository {

    public List<String> getAllTablesFromDb(String dbName) throws SQLException {
        File f = new File(dbName);
        if(!f.exists() || f.isDirectory()) throw new SQLException("File doesn't exist!");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        connection.setAutoCommit(true);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%'");

        List<String> tables = new Stack<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString(1));
        }

        resultSet.close();
        statement.close();
        connection.close();
        return tables;

    }

    public Table executeQuery(String query, String dbName) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        connection.setAutoCommit(true);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnName(i + 1);
        }

        List<String[]> rowList = new ArrayList<>();

        while (resultSet.next()) {
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = resultSet.getString(i + 1);
            }
            rowList.add(row);
        }

        String[][] data = rowList.toArray(new String[0][]);

        resultSet.close();
        statement.close();
        connection.close();
        return new Table(columnNames, data);

    }
}
