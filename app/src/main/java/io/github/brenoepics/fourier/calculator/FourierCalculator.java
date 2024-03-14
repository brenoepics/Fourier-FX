package io.github.brenoepics.fourier.calculator;

import static io.github.brenoepics.fourier.calculator.FourierUtility.switchDebug;

import io.github.brenoepics.fourier.ui.FourierColors;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class FourierCalculator {
  public static final double TAU = Math.PI * 2.0;
  public static final int START_FROM = 256;
  private static final int LIMIT = 8192;
  private final double[] graph = new double[LIMIT];
  private int pointer = 0;
  private Pair<Double, Double> cords = new Pair<>(128.0, 128.0);
  private double time = 0.0;
  private double scale;
  private long startTime;
  private final GraphicsContext context;
  private WaveForm waveForm;
  private int order;
  private double frequency;
  private final FourierColors colors;
  private final GridPane debugGrid;

  public FourierCalculator(
      GraphicsContext context,
      Double scale,
      WaveForm waveForm,
      int order,
      double frequency,
      FourierColors colors,
      GridPane debugGrid) {
    this.context = context;
    this.startTime = System.currentTimeMillis();
    this.scale = scale;
    this.waveForm = waveForm;
    this.order = order;
    this.frequency = frequency;
    this.colors = colors;
    this.debugGrid = debugGrid;
  }

  private void fourier(int order) {
    double phase = order * time * TAU;
    double radius = 4.0 / (order * Math.PI) * scale;
    drawFourier(radius, phase);
  }

  private void drawFourier(double radius, double phase) {
    context.beginPath();
    context.setLineWidth(1.0);
    context.setStroke(colors.getCircleColor());
    context.strokeOval(cords.getKey() - radius, cords.getValue() - radius, radius * 2, radius * 2);
    context.stroke();
    context.setStroke(colors.getInsideColor());
    context.moveTo(cords.getKey(), cords.getValue());
    cords =
        new Pair<>(
            cords.getKey() + Math.cos(phase) * radius, cords.getValue() + Math.sin(phase) * radius);
    context.lineTo(cords.getKey(), cords.getValue());
    context.stroke();

    updateLabels();
  }

  private void connect() {
    context.beginPath();
    context.moveTo(cords.getKey() + 0.5, cords.getValue() + 0.5);
    context.lineTo(START_FROM + 0.5, cords.getValue() + 0.5);
    context.setStroke(colors.getLineColor());
    context.stroke();
  }

  private void drawWave() {
    graph[pointer++ % graph.length] = cords.getValue();
    context.beginPath();
    context.setStroke(colors.getWaveColor());
    context.moveTo(START_FROM, cords.getValue());
    for (int i = 1; i < graph.length; i++) {
      context.lineTo((double) i + START_FROM, graph[(pointer - i + graph.length) % graph.length]);
    }
    context.stroke();
  }

  public void update() {
    context.clearRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
    cords = new Pair<>(128.0, 128.0);
    calculateNext();
    connect();
    drawWave();
    long now = System.currentTimeMillis();
    time += (now - startTime) * Math.pow(10.0, frequency);
    startTime = now;
  }

  private void calculateNext() {
    switch (this.waveForm) {
      case SQUARE:
        for (int o = 0; o <= order; o++) {
          fourier((o << 1) + 1);
        }
        break;
      case SAWTOOTH:
        for (int o = 1; o <= order; o++) {
          fourier(o << 1);
        }
        break;
      case TRIANGLE:
        for (int o = 1; o <= order; o++) {
          fourierTriangle(o);
        }
        break;
      case SINE:
      default:
        for (int o = 1; o <= order; o++) {
          fourier(o);
        }
        break;
    }
  }

  private void fourierTriangle(int o) {
    double phase = (2 * o - 1) * time * TAU;
    double radius = 8.0 * scale / Math.pow((2 * o - 1) * Math.PI, 2);
    if (o % 2 == 0) {
      radius = -radius;
    }
    drawFourier(radius, phase);
  }

  public void updateLabels() {
    if (debugGrid != null) {
      debugGrid.getChildren().forEach(node -> switchDebug((Label) node, this));
    }
  }

  public void increaseScale() {
    if (scale < 64.0) {
      scale += 2.0;
    }
  }

  public void decreaseScale() {
    if (scale > 1.0) {
      scale -= 2.0;
    }
  }

  public void setWaveForm(WaveForm waveForm) {
    this.waveForm = waveForm;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public void setFrequency(double frequency) {
    this.frequency = frequency;
  }

  public double getFrequency() {
    return frequency;
  }

  public int getOrder() {
    return order;
  }

  public double getScale() {
    return scale;
  }

  public Pair<Double, Double> getCords() {
    return cords;
  }
}
