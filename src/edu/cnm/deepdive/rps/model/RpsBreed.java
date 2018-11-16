package edu.cnm.deepdive.rps.model;

import java.util.Comparator;

public enum RpsBreed implements Competitive<RpsBreed> {
  ROCK,
  PAPER,
  SCISSORS;

  private static final int[][] DOMINANCE = {
                        // ROCK,  PAPER, SCISSORS
      /* ROCK */     {        0,     -1,        1},
      /* PAPER */    {        1,      0,       -1},
      /* SCISSORS */ {       -1,      1,        0}
  };

  private static final Comparator<RpsBreed> REFEREE =
      (rps1, rps2) -> DOMINANCE[rps1.ordinal()][rps2.ordinal()];

  @Override
  public Comparator<RpsBreed> getReferee() {
    return REFEREE;
  }

}
