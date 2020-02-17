package videojuegos;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

public class JTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final String[] columnNames;
    private final Class<?>[] columnTypes;
    private Object[][] data;
    
    public JTableModel(String[] columnNames, Class<?>[] columnTypes, Object[][] data){
    	this.columnNames = columnNames;
    	this.columnTypes = columnTypes;
    	this.data = data;
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    /*@Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }*/

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	Object dato = data[rowIndex][columnIndex];
    	if(dato instanceof JButton) {
            return dato;
    	}
    	return dato;
    }   
}