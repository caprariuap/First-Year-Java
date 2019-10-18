package uk.ac.imperial.matrixmult;

import java.io.BufferedReader;
import java.io.FileReader;

public class MatrixBuilder {

  public static Matrix build(double[][] source) {
    return new MatrixClass(source);
  }

  public static Matrix build(int nRows, int nCols) {
    return new MatrixClass(nRows, nCols);
  }
}
