package edu.cnm.deepdive.rps.model;

import java.util.Comparator;

public interface Competitive<T extends Competitive> {

  Comparator<T> getReferee();

}
