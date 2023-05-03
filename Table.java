package viewer;

public class Table {
    String[] columnNames;
    Object[][] data;

    public Table(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public Object[][] getData() {
        return data;
    }
}
