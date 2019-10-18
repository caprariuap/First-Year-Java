package uk.ac.imperial.matrixmult;

public class MatrixMultiplier {

  public static Matrix multiply(Matrix a, Matrix b) throws Exception {
    Matrix result = new MatrixClass(a.getNumRows(), b.getNumColumns());
    return new MultiplyHelper(a, b, result)
          .multiply(
            new Submatrix(a, 0, 0, a.getNumRows(), a.getNumColumns()),
            new Submatrix(b, 0, 0, b.getNumRows(), b.getNumColumns()),
            new Submatrix(result, 0, 0, result.getNumRows(), result.getNumColumns()));
  }
}
