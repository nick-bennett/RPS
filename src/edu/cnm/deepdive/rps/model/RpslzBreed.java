package edu.cnm.deepdive.rps.model;

import java.util.Comparator;

public enum RpslzBreed implements Competitive<RpslzBreed> {
  ROCK,
  PAPER,
  SCISSORS,
  LIZARD,
  SPOCK;

  private static final int[][] DOMINANCE = {
                        // ROCK,  PAPER, SCISSORS,  LIZARD,   SPOCK
      /* ROCK     */ {        0,     -1,        1,       1,      -1},
      /* PAPER    */ {        1,      0,       -1,      -1,       1},
      /* SCISSORS */ {       -1,      1,        0,       1,      -1},
      /* LIZARD   */ {       -1,      1,       -1,       0,       1},
      /* SPOCK    */ {        1,     -1,        1,      -1,       0},
  };

  private static final Comparator<RpslzBreed> REFEREE =
      (rps1, rps2) -> DOMINANCE[rps1.ordinal()][rps2.ordinal()];

  @Override
  public Comparator<RpslzBreed> getReferee() {
    return REFEREE;
  }

}
