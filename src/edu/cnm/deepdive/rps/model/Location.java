package edu.cnm.deepdive.rps.model;

/**
 * The class encapsulates an immutable (row, column) cell location, useful for
 * referencing an element in a 2-dimensional array.
 *
 * @version 1.0
 * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android Bootcamp, Cohort 5
 */
public class Location {

  private final int row;
  private final int column;

  /**
   * Initializes the location using the specified <code>row</code> &amp;
   * <code>column</code>.
   *
   * @param row     row index of cell.
   * @param column  column index of cell.
   */
  public Location(int row, int column) {
    this.row = row;
    this.column = column;
  }

  /**
   * Returns row index of cell.
   *
   * @return  cell row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns column index of cell.
   *
   * @return  cell column.
   */
  public int getColumn() {
    return column;
  }

}
