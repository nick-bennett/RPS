package edu.cnm.deepdive.rps.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TerrainView extends Canvas {

  private static final double HEX_VERT_OFFSET = Math.sqrt(3) / 2;
  private static final double HEX_HORIZ_OFFSET = 1f / 2;
  private static final double MAX_HUE = 360;

  private boolean hexagonal;
  private int breedCount = 3;
  private Color[] colors;

  public TerrainView() {
    computeColors();
  }

  public void draw(Enum[][] grid) {
    GraphicsContext context = getGraphicsContext2D();
    double cellHeight = getHeight() / grid.length / (hexagonal ? HEX_VERT_OFFSET : 1);
    double cellWidth = getWidth() / grid.length;
    context.clearRect(0, 0, getWidth(), getHeight());
    double verticalSpacing = cellHeight * (hexagonal ? HEX_VERT_OFFSET : 1);
    for (int row = 0; row < grid.length; row++) {
      if (hexagonal && row % 2 != 0) {
        double shift = cellWidth * HEX_HORIZ_OFFSET;
        for (int col = 0; col < grid.length; col++) {
          context.setFill(colors[grid[row][col].ordinal()]);
          context.fillOval(col * cellWidth + shift, row * verticalSpacing, cellWidth, cellHeight);
        }
        context.fillOval(-shift, row * verticalSpacing, cellWidth, cellHeight);
      } else {
        for (int col = 0; col < grid.length; col++) {
          context.setFill(colors[grid[row][col].ordinal()]);
          context.fillOval(col * cellWidth, row * verticalSpacing, cellWidth, cellHeight);
        }
      }
    }
  }

  public int getBreedCount() {
    return breedCount;
  }

  public void setBreedCount(int breedCount) {
    this.breedCount = breedCount;
    computeColors();
  }

  public boolean isHexagonal() {
    return hexagonal;
  }

  public void setHexagonal(boolean hexagonal) {
    this.hexagonal = hexagonal;
  }

  private void computeColors() {
    colors = new Color[breedCount];
    for (int i = 0; i < breedCount; i++) {
      colors[i] = Color.hsb(i * MAX_HUE / breedCount, 1, 0.9);
    }
  }

}
