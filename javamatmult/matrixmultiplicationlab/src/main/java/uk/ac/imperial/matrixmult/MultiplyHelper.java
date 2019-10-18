package uk.ac.imperial.matrixmult;

public class MultiplyHelper {
  Matrix realA, realB, realC;
  Thread[] threads = new Thread[8];

  public MultiplyHelper(Matrix realA, Matrix realB, Matrix realC) {
    this.realA = realA;
    this.realB = realB;
    this.realC = realC;
    for (int i = 0; i < 8; i++) threads[i] = null;
  }

  public Matrix multiply(Submatrix a, Submatrix b, Submatrix c) throws Exception {
    // max = maximum{a noRows, b noColumns, a noColumns}
    int max, min;
    if (a.getNumRows() > a.getNumColumns()) {
      if (a.getNumRows() > b.getNumColumns()) {
        max = a.getNumRows();
        min = b.getNumColumns() > a.getNumColumns() ? a.getNumColumns() : b.getNumColumns();
      } else {
        max = b.getNumColumns();
        min = a.getNumColumns() < a.getNumRows() ? a.getNumColumns() : a.getNumRows();
      }
    } else {
      if (a.getNumColumns() > b.getNumColumns()) {
        max = a.getNumColumns();
        min = b.getNumColumns() > a.getNumRows() ? a.getNumRows() : b.getNumColumns();
      } else {
        max = b.getNumColumns();
        min = a.getNumColumns() < a.getNumRows() ? a.getNumColumns() : a.getNumRows();
      }
    }
    if (max < 125 || min == 1) {
      for (int i = 0; i < a.getNumRows(); i++) {
        for (int j = 0; j < b.getNumColumns(); j++) {
          double sum = 0;
          for (int k = 0; k < a.getNumColumns(); k++) {
            sum += (a.get(i, k) * b.get(k, j));
          }
          c.set(i + a.startRow, j + b.startColumn, sum);
        }
      }
      return c;
    }

    if (max == a.getNumRows()) {
      int t1, t2;
      Submatrix a1, a2;
      int row1, row2, col;

      col = a.getNumColumns();
      row1 = a.getNumRows() / 2;
      row2 = a.getNumRows() - row1;
      a1 = new Submatrix(realA, a.startRow, a.startColumn, row1, col);
      a2 = new Submatrix(realA, a.startRow + row1, a.startColumn, row2, col);

      t1 = -1;
      t2 = -1;
      for (int i = 0; i < 8; i++) {
        if (threads[i] == null) {
          if (t1 == -1) {
            t1 = i;
          } else {
            t2 = i;
            break;
          }
        }
      }
      if (t1 != -1) {
        threads[t1] = new multiplier(a1, b, c);
        threads[t1].run();
        threads[t1].join();
        threads[t1] = null;
      } else {
        multiply(a1, b, c);
      }
      if (t2 != -1) {
        threads[t2] = new multiplier(a2, b, c);
        threads[t2].run();
        threads[t2].join();
        threads[t2] = null;
      } else {
        multiply(a2, b, c);
      }
      return c;
    }
    if (max == b.getNumColumns()) {
      int t1, t2;
      Submatrix b1, b2;
      int col1, col2, row;

      row = b.getNumRows();
      col1 = b.getNumColumns() / 2;
      col2 = b.getNumColumns() - col1;
      b1 = new Submatrix(realB, b.startRow, b.startColumn, row, col1);
      b2 = new Submatrix(realB, b.startRow, b.startColumn + col1, row, col2);

      t1 = -1;
      t2 = -1;
      for (int i = 0; i < 8; i++) {
        if (threads[i] == null) {
          if (t1 == -1) {
            t1 = i;
          } else {
            t2 = i;
            break;
          }
        }
      }
      if (t1 != -1) {
        threads[t1] = new multiplier(a, b1, c);
        threads[t1].run();
        threads[t1].join();
        threads[t1] = null;
      } else {
        multiply(a, b1, c);
      }
      if (t2 != -1) {
        threads[t2] = new multiplier(a, b2, c);
        threads[t2].run();
        threads[t2].join();
        threads[t2] = null;
      } else {
        multiply(a, b2, c);
      }
      return c;
    }
    Submatrix t =
        new Submatrix(
            new MatrixClass(c.getNumRows(), c.getNumColumns()),
            0,
            0,
            c.getNumRows(),
            c.getNumColumns());

    Submatrix a1, a2, b1, b2;
    int col1, col2, row;

    row = a.getNumRows();
    col1 = a.getNumColumns() / 2;
    col2 = a.getNumColumns() - col1;
    a1 = new Submatrix(realA, a.startRow, a.startColumn, row, col1);
    a2 = new Submatrix(realA, a.startRow, a.startColumn + col1, row, col2);

    int col, row1, row2;
    col = b.getNumColumns();
    row1 = b.getNumRows() / 2;
    row2 = b.getNumRows() - row1;
    b1 = new Submatrix(realB, b.startRow, b.startColumn, row1, col);
    b2 = new Submatrix(realB, b.startRow + row1, b.startColumn, row2, col);

    int t1, t2;
    t1 = -1;
    t2 = -1;
    for (int i = 0; i < 8; i++) {
      if (threads[i] == null) {
        if (t1 == -1) {
          t1 = i;
        } else {
          t2 = i;
          break;
        }
      }
    }
    if (t1 != -1) {
      threads[t1] = new multiplier(a1, b1, c);
      threads[t1].run();
      threads[t1].join();
      threads[t1] = null;
    } else {
      multiply(a1, b1, c);
    }
    if (t2 != -1) {
      threads[t2] = new multiplier(a2, b2, t);
      threads[t2].run();
      threads[t2].join();
      threads[t2] = null;
    } else {
      multiply(a2, b2, t);
    }
    /*
    int t3;
    t1 = -1;
    for (int i = 0; i < 8; i++) {
      if (threads[i] == null) {
        t1 = i;
        break;
      }
    }*/

    for (int i = 0; i < a.getNumRows(); i++) {
      for (int j = 0; j < b.getNumColumns(); j++) {
        c.set(
            i + a.startRow,
            j + b.startColumn,
            c.get(i + a.startRow, j + b.startColumn) + t.get(i + a.startRow, j + b.startColumn));
      }
    }
    return c;
  }

  private static void add(Submatrix a, Submatrix b) {
    for (int i = 0; i < a.getNumRows(); i++) {
      for (int j = 0; j < a.getNumColumns(); j++) {
        a.set(i, j, a.get(i, j) + b.get(i, j));
      }
    }
  }

  private class multiplier extends Thread {
    private Submatrix a, b, c;

    multiplier(Submatrix a, Submatrix b, Submatrix c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    @Override
    public void run() {
      try {
        multiply(a, b, c);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
