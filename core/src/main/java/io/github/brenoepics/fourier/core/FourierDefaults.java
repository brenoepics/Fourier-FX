package io.github.brenoepics.fourier.core;

public final class FourierDefaults {
  private FourierDefaults() {
    throw new IllegalStateException("Utility class");
  }

  public static final WaveForm WAVEFORM = WaveForm.SQUARE;
  public static final double SCALE = 64.0;
  public static final double FREQUENCY = -3.5;
  public static final int ORDER = 4;
  public static final double MIN_FREQUENCY = -4.0;
  public static final double MAX_FREQUENCY = -3.0;
  public static final int MAX_ORDER = 16;
}
