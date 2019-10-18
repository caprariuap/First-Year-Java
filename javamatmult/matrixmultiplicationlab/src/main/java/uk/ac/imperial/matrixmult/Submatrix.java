package uk.ac.imperial.matrixmult;

public class Submatrix implements Matrix {
  Matrix matrix;
  final int startRow, startColumn, noRows, noColumns;

  public Submatrix(Matrix matrix, int startRow, int startColumn, int noRows, int noColumns) {
    this.matrix = matrix;
    this.startRow = startRow;
    this.startColumn = startColumn;
    this.noColumns = noColumns;
    this.noRows = noRows;
  }

  @Override
  public double get(int row, int column) {
    return matrix.get(row + startRow, column + startColumn);
  }

  @Override
  public void set(int row, int column, double value) {
    matrix.set(row + startRow, column + startColumn, value);
  }

  @Override
  public int getNumRows() {
    return noRows;
  }

  @Override
  public int getNumColumns() {
    return noColumns;
  }
}
