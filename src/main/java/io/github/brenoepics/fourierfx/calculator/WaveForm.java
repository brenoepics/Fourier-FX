package io.github.brenoepics.fourierfx.calculator;

import java.util.Optional;

public enum WaveForm {
  SQUARE("Square"),
  SINE("Sine"),
  SAWTOOTH("Sawtooth"),
  TRIANGLE("Triangle");

  private final String text;

  WaveForm(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public static Optional<WaveForm> fromString(String text) {
    for (WaveForm b : WaveForm.values()) {
      if (b.text.equalsIgnoreCase(text)) {
        return Optional.of(b);
      }
    }
    return Optional.empty();
  }
}
