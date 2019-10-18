package uk.ac.imperial.matrixmult;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MatrixClass implements Matrix {
  private double[][] values;
  private int nRows, nColumns;

  public MatrixClass() {
    this(256, 256);
  }

  public MatrixClass(int nRows, int nColumns) {
    this.nRows = nRows;
    this.nColumns = nColumns;
    values = new double[nRows][nColumns];
  }

  public MatrixClass(double[][] source) {
    this(source.length, source[0].length);
    for (int i = 0; i < nRows; i++) {
      values[i] = Arrays.copyOf(source[i], nColumns);
    }
  }

  @Override
  public double get(int row, int column) {
    return values[row][column];
  }

  @Override
  public void set(int row, int column, double value) {
    values[row][column] = value;
  }

  @Override
  public int getNumRows() {
    return nRows;
  }

  @Override
  public int getNumColumns() {
    return nColumns;
  }
}
