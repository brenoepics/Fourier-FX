package io.github.brenoepics.fourier.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Renderer-agnostic Fourier series engine. Each call to {@link #update(long)} advances the
 * animation and produces the epicycles for the current frame plus a ring buffer with the wave
 * history. It has no rendering or platform dependencies, so it runs unchanged on the JVM (JavaFX
 * app) and in the browser (compiled to WebAssembly).
 */
public class FourierSeries {
  public static final double TAU = Math.PI * 2.0;
  public static final int WAVE_START = 256;
  public static final double ORIGIN_X = 128.0;
  public static final double ORIGIN_Y = 128.0;
  public static final double MIN_SCALE = 1.0;
  public static final double MAX_SCALE = 64.0;
  public static final double SCALE_STEP = 2.0;
  private static final int LIMIT = 8192;

  private final double[] graph = new double[LIMIT];
  private final List<Epicycle> epicycles = new ArrayList<>();
  private final List<Epicycle> epicyclesView = Collections.unmodifiableList(epicycles);
  private int pointer = 0;
  private double x = ORIGIN_X;
  private double y = ORIGIN_Y;
  private double time = 0.0;
  private long lastTime = -1;
  private double scale;
  private WaveForm waveForm;
  private int order;
  private double frequency;

  public FourierSeries(double scale, WaveForm waveForm, int order, double frequency) {
    this.scale = scale;
    this.waveForm = waveForm;
    this.order = order;
    this.frequency = frequency;
  }

  /**
   * Computes the next animation frame. {@code nowMillis} is any monotonic-ish millisecond clock
   * (wall clock or animation timestamp); only deltas between calls matter.
   *
   * @return the epicycles of this frame, innermost first (read-only, reused between frames)
   */
  public List<Epicycle> update(long nowMillis) {
    x = ORIGIN_X;
    y = ORIGIN_Y;
    epicycles.clear();
    calculateNext();
    graph[pointer++ % graph.length] = y;
    if (lastTime >= 0) {
      time += (nowMillis - lastTime) * Math.pow(10.0, frequency);
    }
    lastTime = nowMillis;
    return epicyclesView;
  }

  private void calculateNext() {
    switch (this.waveForm) {
      case SQUARE:
        for (int o = 0; o <= order; o++) {
          harmonic((o << 1) + 1);
        }
        break;
      case SAWTOOTH:
        for (int o = 1; o <= order; o++) {
          harmonic(o << 1);
        }
        break;
      case TRIANGLE:
        for (int o = 1; o <= order; o++) {
          triangleHarmonic(o);
        }
        break;
      case SINE:
      default:
        for (int o = 1; o <= order; o++) {
          harmonic(o);
        }
        break;
    }
  }

  private void harmonic(int k) {
    addEpicycle(4.0 / (k * Math.PI) * scale, k * time * TAU);
  }

  private void triangleHarmonic(int o) {
    int k = 2 * o - 1;
    double radius = 8.0 * scale / Math.pow(k * Math.PI, 2);
    if (o % 2 == 0) {
      radius = -radius;
    }
    addEpicycle(radius, k * time * TAU);
  }

  private void addEpicycle(double radius, double phase) {
    double centerX = x;
    double centerY = y;
    x += Math.cos(phase) * radius;
    y += Math.sin(phase) * radius;
    epicycles.add(new Epicycle(centerX, centerY, radius, x, y));
  }

  /** Number of samples kept in the wave history ring buffer. */
  public int waveLength() {
    return graph.length;
  }

  /** Wave sample {@code i} frames ago; {@code i} starts at 1 for the most recent sample. */
  public double waveAt(int i) {
    return graph[Math.floorMod(pointer - i, graph.length)];
  }

  public void increaseScale() {
    if (scale < MAX_SCALE) {
      scale += SCALE_STEP;
    }
  }

  public void decreaseScale() {
    if (scale > MIN_SCALE) {
      scale -= SCALE_STEP;
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

  public WaveForm getWaveForm() {
    return waveForm;
  }

  /** X of the tip of the last epicycle for the current frame. */
  public double getX() {
    return x;
  }

  /** Y of the tip of the last epicycle for the current frame. */
  public double getY() {
    return y;
  }
}
