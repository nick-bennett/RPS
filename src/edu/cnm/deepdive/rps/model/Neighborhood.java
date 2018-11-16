package edu.cnm.deepdive.rps.model;

import java.util.Random;

/**
 * Enumerated set of neighborhoods, where each neighborhood is characterized by
 * the locations of neighboring cells, relative to some reference cell. Each
 * enumerated value can be interrogated for a randomly selected neighbor,
 * expressed either as a relative offset, an offset applied to a base location,
 * or an offset applied to a base location and normalized (i.e. wrapped around a
 * torus).
 *
 * @version 1.0
 * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android Bootcamp, Cohort 5
 */
public enum Neighborhood {

  /** A cell's neighbors are directly adjacent to the cell. */
  VON_NEUMANN("Von Neumann") {
    {
      offsets = new Location[] {
                                new Location(-1, 0),
          new Location( 0, -1),                      new Location(0, 1),
                                new Location( 1, 0)
      };
    }
  },
  /** A cell's neighbors are directly or diagonally adjacent to the cell. */
  MOORE("Moore") {
    {
      offsets = new Location[] {
          new Location(-1, -1), new Location(-1, 0), new Location(-1, 1),
          new Location( 0, -1), new Location( 0, 0), new Location( 0, 1),
          new Location( 1, -1), new Location( 1, 0), new Location( 1, 1)
      };
    }
  },
  /** A cell's neighbors are diagonally adjacent to the cell. */
  DIAGONAL("Diagonal") {
    {
      offsets = new Location[] {
          new Location(-1, -1),                      new Location(-1, 1),

          new Location( 1, -1),                      new Location( 1, 1)
      };
    }
  },
  /** A cell's neighbors are directly adjacent on a hexagonal grid. */
  HEXAGONAL("Hexagonal") {
    {
      offsets = new Location[] {
                    new Location(-1, -1), new Location(-1, 0),
          new Location(0, -1),                      new Location(0, 1),
                    new Location( 1, -1), new Location( 1, 0)
      };
    }

    @Override
    public Location randomNeighbor(Random rng, Location base) {
      Location location = super.randomNeighbor(rng, base);
      if ((base.getRow() & 1) == 1 && base.getRow() != location.getRow()) {
        return new Location(location.getRow(), location.getColumn() + 1);
      }
      return location;
    }

    @Override
    public boolean isHexagonal() {
      return true;
    }
  };

  private final String name;
  protected Location[] offsets;

  Neighborhood(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  /**
   *
   * @return
   */
  public Location[] offsets() {
    return offsets;
  }

  /**
   * Returns a flag indicating whether the neighborhood is applicable to a
   * hexagonal grid or lattice.
   *
   * @return  true if neighborhood is suitable for use on a hexagonal grid.
   */
  public boolean isHexagonal() {
    return false;
  }

  /**
   * Selects and returns a random element from the offsets defining the
   * neighboring cells in the neighborhood.
   *
   * @param rng   source of randomness.
   * @return      offset (location with row and column both in {-1, 0, 1}).
   */
  protected Location randomOffset(Random rng) {
    return offsets[rng.nextInt(offsets.length)];
  }

  /**
   * Computes and returns the non-normalized location of a cell that is a
   * randomly selected neighbor of <code>base</code>.
   *
   * @param rng   source of randomness.
   * @param base  cell for which a random neighbor will be selected.
   * @return      neighboring cell.
   */
  public Location randomNeighbor(Random rng, Location base) {
    Location offset = randomOffset(rng);
    return new Location(base.getRow() + offset.getRow(), base.getColumn() + offset.getColumn());
  }

  /**
   * Computes and returns the location of a cell that is a randomly selected
   * neighbor of <code>code</code>. The location is normalized within
   * <code>bounds</code> by wrapping any overflow or underflow to the opposite
   * side of a grid of size <code>bounds</code>.
   *
   * @param rng     source of randomness.
   * @param base    cell for which a random neighbor will be selected.
   * @param bounds  grid height (number of rows) and width (number of columns).
   * @return        neighboring cell.
   */
  public Location randomNeighbor(Random rng, Location base, Location bounds) {
    Location location = randomNeighbor(rng, base);
    return new Location((location.getRow() + bounds.getRow()) % bounds.getRow(),
        (location.getColumn() + bounds.getColumn()) % bounds.getColumn());
  }

}
