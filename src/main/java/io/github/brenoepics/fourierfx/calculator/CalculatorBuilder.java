package io.github.brenoepics.fourierfx.calculator;

import io.github.brenoepics.fourierfx.ui.FourierColors;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;

public class CalculatorBuilder {
  private GraphicsContext context;
  private Double scale;
  private WaveForm waveForm;
  private int order;
  private double frequency;
  private FourierColors colors;
  private GridPane debugGrid;

  public CalculatorBuilder setContext(GraphicsContext context) {
    this.context = context;
    return this;
  }

  public CalculatorBuilder setScale(Double scale) {
    this.scale = scale;
    return this;
  }

  public CalculatorBuilder setWaveForm(WaveForm waveForm) {
    this.waveForm = waveForm;
    return this;
  }

  public CalculatorBuilder setOrder(int order) {
    this.order = order;
    return this;
  }

  public CalculatorBuilder setFrequency(double frequency) {
    this.frequency = frequency;
    return this;
  }

  public CalculatorBuilder setColors(FourierColors colors) {
    this.colors = colors;
    return this;
  }

  public CalculatorBuilder setDebugGrid(GridPane debugGrid) {
    this.debugGrid = debugGrid;
    return this;
  }

  public FourierCalculator build() {
    return new FourierCalculator(context, scale, waveForm, order, frequency, colors, debugGrid);
  }
}
