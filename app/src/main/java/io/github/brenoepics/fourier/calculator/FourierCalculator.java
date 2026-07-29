package io.github.brenoepics.fourier.calculator;

import static io.github.brenoepics.fourier.calculator.FourierUtility.switchDebug;

import io.github.brenoepics.fourier.core.Epicycle;
import io.github.brenoepics.fourier.core.FourierSeries;
import io.github.brenoepics.fourier.core.WaveForm;
import io.github.brenoepics.fourier.ui.FourierColors;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/** Draws the shared {@link FourierSeries} engine onto a JavaFX canvas. */
public class FourierCalculator {
  private final FourierSeries series;
  private final GraphicsContext context;
  private final FourierColors colors;
  private final GridPane debugGrid;

  public FourierCalculator(
      GraphicsContext context, FourierSeries series, FourierColors colors, GridPane debugGrid) {
    this.context = context;
    this.series = series;
    this.colors = colors;
    this.debugGrid = debugGrid;
  }

  public void update() {
    context.clearRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
    List<Epicycle> epicycles = series.update(System.currentTimeMillis());
    for (Epicycle epicycle : epicycles) {
      drawEpicycle(epicycle);
    }
    connect();
    drawWave();
    updateLabels();
  }

  private void drawEpicycle(Epicycle epicycle) {
    double radius = Math.abs(epicycle.radius());
    context.beginPath();
    context.setLineWidth(1.0);
    context.setStroke(colors.getCircleColor());
    context.strokeOval(
        epicycle.centerX() - radius, epicycle.centerY() - radius, radius * 2, radius * 2);
    context.stroke();
    context.setStroke(colors.getInsideColor());
    context.moveTo(epicycle.centerX(), epicycle.centerY());
    context.lineTo(epicycle.endX(), epicycle.endY());
    context.stroke();
  }

  private void connect() {
    context.beginPath();
    context.moveTo(series.getX() + 0.5, series.getY() + 0.5);
    context.lineTo(FourierSeries.WAVE_START + 0.5, series.getY() + 0.5);
    context.setStroke(colors.getLineColor());
    context.stroke();
  }

  private void drawWave() {
    context.beginPath();
    context.setStroke(colors.getWaveColor());
    context.moveTo(FourierSeries.WAVE_START, series.getY());
    for (int i = 1; i < series.waveLength(); i++) {
      context.lineTo((double) i + FourierSeries.WAVE_START, series.waveAt(i));
    }
    context.stroke();
  }

  public void updateLabels() {
    if (debugGrid != null) {
      debugGrid.getChildren().forEach(node -> switchDebug((Label) node, this));
    }
  }

  public void increaseScale() {
    series.increaseScale();
  }

  public void decreaseScale() {
    series.decreaseScale();
  }

  public void setWaveForm(WaveForm waveForm) {
    series.setWaveForm(waveForm);
  }

  public void setOrder(int order) {
    series.setOrder(order);
  }

  public void setFrequency(double frequency) {
    series.setFrequency(frequency);
  }

  public double getFrequency() {
    return series.getFrequency();
  }

  public int getOrder() {
    return series.getOrder();
  }

  public double getScale() {
    return series.getScale();
  }

  public Pair<Double, Double> getCords() {
    return new Pair<>(series.getX(), series.getY());
  }
}
