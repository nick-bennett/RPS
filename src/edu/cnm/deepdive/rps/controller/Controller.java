package edu.cnm.deepdive.rps.controller;

import edu.cnm.deepdive.rps.model.Neighborhood;
import edu.cnm.deepdive.rps.model.RpslzBreed;
import edu.cnm.deepdive.rps.model.Terrain;
import edu.cnm.deepdive.rps.view.TerrainView;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

/**
 *
 */
public class Controller {

  private static final Neighborhood DEFAULT_NEIGHBORHOOD = Neighborhood.VON_NEUMANN;
  private static final int TERRAIN_SIZE = 80;
  private static final int STEPS_PER_ITERATION = 250;
  private static final int MAX_SLEEP_PER_ITERATION = 11;
  private static final int MIX_THRESHOLD = 10;
  private static final int PAIRS_TO_MIX = 8;
  private static final double HEX_VERT_SCALE = Math.sqrt(3) / 2;

  @FXML
  private TerrainView terrainView;
  @FXML
  private CheckBox fitCheckbox;
  @FXML
  private Text iterationsLabel;
  @FXML
  private ScrollPane viewScroller;
  @FXML
  private Slider speedSlider;
  @FXML
  private Slider mixingSlider;
  @FXML
  private ChoiceBox<Neighborhood> neighborhood;
  @FXML
  private Button start;
  @FXML
  private Button stop;
  @FXML
  private Button reset;

  private double unscaledWidth;
  private String iterationFormat;
  private Terrain terrain;
  private boolean running = false;
  private final Object lock = new Object();
  private Timer timer;

  @FXML
  private void initialize() {
    terrain = new Terrain(RpslzBreed.class, TERRAIN_SIZE, new Random());
    terrainView.setBreedCount(RpslzBreed.values().length);
    iterationFormat = iterationsLabel.getText();
    speedSlider.setMax(MAX_SLEEP_PER_ITERATION - 1);
    neighborhood.getItems().setAll(Neighborhood.values());
    timer = new Timer();
    reset(null);
    neighborhood.setValue(DEFAULT_NEIGHBORHOOD);
  }

  @FXML
  private void fitView(ActionEvent actionEvent) {
    if (fitCheckbox.isSelected()) {
      unscaledWidth = terrainView.getWidth();
      terrainView.setWidth(viewScroller.getWidth() - 2);
      terrainView.setHeight(viewScroller.getHeight() - 2);
    } else {
      terrainView.setWidth(unscaledWidth);
      terrainView.setHeight(
          unscaledWidth * (neighborhood.getValue().isHexagonal() ? HEX_VERT_SCALE : 1));
    }
    if (!running) {
      draw();
    }
  }

  @FXML
  private void changeNeighborhood(ActionEvent actionEvent) {
    Neighborhood prevNeighborhood = terrain.getNeighborhood();
    Neighborhood neighborhood = this.neighborhood.getValue();
    boolean wasHexagonal = prevNeighborhood.isHexagonal();
    boolean isHexagonal = neighborhood.isHexagonal();
    boolean changeHex = (wasHexagonal != isHexagonal);
    terrain.setNeighborhood(neighborhood);
    terrainView.setHexagonal(isHexagonal);
    if (!fitCheckbox.isSelected() && changeHex) {
      terrainView.setHeight(terrainView.getWidth() * (isHexagonal ? HEX_VERT_SCALE : 1));
    }
    if (!running) {
      draw();
    }
  }

  @FXML
  private void start(ActionEvent actionEvent) {
    running = true;
    start.setDisable(true);
    stop.setDisable(false);
    reset.setDisable(true);
    timer.start();
    new Runner().start();
  }

  /**
   * Signals simulation thread (if one is running) to stop.
   *
   * @param actionEvent   UI control event (may be null).
   */
  @FXML
  public void stop(ActionEvent actionEvent) {
    running = false;
  }

  @FXML
  private void reset(ActionEvent actionEvent) {
    terrain.reset();
    terrainView.setHexagonal(DEFAULT_NEIGHBORHOOD.isHexagonal());
    terrainView.setHeight(
        terrainView.getWidth() * (DEFAULT_NEIGHBORHOOD.isHexagonal() ? HEX_VERT_SCALE : 1));
    start.setDisable(false);
    draw();
  }

  private void draw() {
    synchronized (lock) {
      terrainView.draw(terrain.getGrid());
      iterationsLabel.setText(String.format(iterationFormat, terrain.getIterations()));
    }
  }

  private void cleanAfterStop() {
    start.setDisable(false);
    stop.setDisable(true);
    reset.setDisable(false);
    timer.stop();
  }

  private class Timer extends AnimationTimer {

    @Override
    public void handle(long now) {
      draw();
    }

  }

  private class Runner extends Thread {

    @Override
    public void run() {
      while (running) {
        int sleep = MAX_SLEEP_PER_ITERATION - (int) speedSlider.getValue();
        synchronized (lock) {
          terrain.step(STEPS_PER_ITERATION);
          // TODO Mixing?
        }
        try {
          Thread.sleep(sleep);
        } catch (InterruptedException e) {
          // DO NOTHING!
        }
      }
      Platform.runLater(() -> cleanAfterStop());
    }

  }

}
