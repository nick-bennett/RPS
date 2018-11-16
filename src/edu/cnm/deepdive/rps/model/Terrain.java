package edu.cnm.deepdive.rps.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 *
 * @version 1.0
 * @author Nicholas Bennett &amp; Deep Dive Coding Java+Android Bootcamp, Cohort 5
 */
public class Terrain {

  // Grid is assumed to be square!!
  private Location bounds;
  private Enum[][] grid;
  private Random rng;
  private Neighborhood neighborhood = Neighborhood.VON_NEUMANN;
  private long iterations;
  private Class clazz;

  public <T extends Enum<T> & Competitive<T>> Terrain(Class<T> clazz, int size, Random rng) {
    grid = new Enum[size][size];
    bounds = new Location(size, size);
    this.rng = rng;
    this.clazz = clazz;
  }

  public void reset() {
    Enum[] values = new Enum[0];
    try {
      Method getValues = clazz.getMethod("values");
      values = (Enum[]) getValues.invoke(null);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      // Since clazz is an enum (subclass of Enum), should never happen.
      throw new RuntimeException(e);
    }
    for (Enum[] row : grid) {
      for (int col = 0; col < row.length; col++) {
        row[col] = values[rng.nextInt(values.length)];
      }
    }
    iterations = 0;
  }

  @SuppressWarnings("unchecked")
  public void step() {
    Location attackerLocation = new Location(rng.nextInt(grid.length), rng.nextInt(grid.length));
    Competitive attacker = (Competitive) grid[attackerLocation.getRow()][attackerLocation.getColumn()];
    Location defenderLocation = neighborhood.randomNeighbor(rng, attackerLocation, bounds);
    Competitive defender = (Competitive) grid[defenderLocation.getRow()][defenderLocation.getColumn()];
    int result = attacker.getReferee().compare(attacker, defender);
    if (result < 0) {
      grid[attackerLocation.getRow()][attackerLocation.getColumn()] = (Enum) defender;
    } else if (result > 0) {
      grid[defenderLocation.getRow()][defenderLocation.getColumn()] = (Enum) attacker;
    }
    iterations++;
  }

  public void step(int numSteps) {
    for (int i = 0; i < numSteps; i++) {
      step();
    }
  }

  /**
   * Returns a reference to the terrain contents. <strong>Important!</strong>
   * This is <strong>not</strong> a safe copy.
   *
   * @return
   */
  public Enum[][] getGrid() {
    return grid;
  }

  public long getIterations() {
    return iterations;
  }

  public Neighborhood getNeighborhood() {
    return neighborhood;
  }

  public void setNeighborhood(Neighborhood neighborhood) {
    this.neighborhood = neighborhood;
  }

}
