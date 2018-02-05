package sample;

/**
 * Created by pawelnarolski on 24.05.2017.
 */
class PlayerCoordinates {

    private int rowIndex;
    private int columnIndex;

    PlayerCoordinates() {
        this.rowIndex = 0;
        this.columnIndex = 0;
    }

    PlayerCoordinates (int x, int y) {
        this.rowIndex = x;
        this.columnIndex = y;
    }

    int getRowIndex() {
        return rowIndex;
    }

    int getColumnIndex() {
        return columnIndex;
    }

    void setNewCoordinates(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

}
