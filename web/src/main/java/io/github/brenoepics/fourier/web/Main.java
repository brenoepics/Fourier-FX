package io.github.brenoepics.fourier.web;

import io.github.brenoepics.fourier.core.Epicycle;
import io.github.brenoepics.fourier.core.FourierDefaults;
import io.github.brenoepics.fourier.core.FourierSeries;
import io.github.brenoepics.fourier.core.WaveForm;
import java.util.List;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

/** Browser entry point: renders the shared {@link FourierSeries} engine onto an HTML5 canvas. */
public final class Main {
  private static final String WAVE_COLOR = "rgb(0,255,0)";
  private static final String CIRCLE_COLOR = "rgb(255,0,0)";
  private static final String LINE_COLOR = "rgb(255,255,32)";
  private static final String INSIDE_COLOR = "rgba(157,46,40,0.3)";

  private final HTMLDocument document;
  private final HTMLCanvasElement canvas;
  private final CanvasRenderingContext2D context;
  private final FourierSeries series;
  private final HTMLElement frequencyLabel;
  private final HTMLElement orderLabel;
  private final HTMLElement scaleLabel;
  private final HTMLElement pairLabel;

  private Main() {
    document = Window.current().getDocument();
    canvas = (HTMLCanvasElement) document.getElementById("canvas");
    context = (CanvasRenderingContext2D) canvas.getContext("2d");
    series =
        new FourierSeries(
            FourierDefaults.SCALE,
            FourierDefaults.WAVEFORM,
            FourierDefaults.ORDER,
            FourierDefaults.FREQUENCY);
    frequencyLabel = document.getElementById("frequency");
    orderLabel = document.getElementById("order");
    scaleLabel = document.getElementById("scale");
    pairLabel = document.getElementById("pair");
  }

  public static void main(String[] args) {
    Main main = new Main();
    main.bindControls();
    main.start();
  }

  private void bindControls() {
    HTMLInputElement frequency = (HTMLInputElement) document.getElementById("frequencyInput");
    frequency.addEventListener(
        "input", e -> series.setFrequency(Double.parseDouble(frequency.getValue())));

    HTMLInputElement order = (HTMLInputElement) document.getElementById("orderInput");
    order.addEventListener("input", e -> series.setOrder(Integer.parseInt(order.getValue())));

    for (WaveForm waveForm : WaveForm.values()) {
      HTMLInputElement radio =
          (HTMLInputElement) document.getElementById("wf-" + waveForm.getText());
      radio.addEventListener("change", e -> series.setWaveForm(waveForm));
    }

    document
        .getElementById("increaseScaleButton")
        .addEventListener("click", e -> series.increaseScale());
    document
        .getElementById("decreaseScaleButton")
        .addEventListener("click", e -> series.decreaseScale());
  }

  private void start() {
    AnimationFrameCallback[] loop = new AnimationFrameCallback[1];
    loop[0] =
        timestamp -> {
          render((long) timestamp);
          Window.requestAnimationFrame(loop[0]);
        };
    Window.requestAnimationFrame(loop[0]);
  }

  private void render(long now) {
    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    List<Epicycle> epicycles = series.update(now);
    context.setLineWidth(1.0);
    for (Epicycle epicycle : epicycles) {
      drawEpicycle(epicycle);
    }
    connect();
    drawWave();
    updateLabels();
  }

  private void drawEpicycle(Epicycle epicycle) {
    double radius = Math.abs(epicycle.radius());
    context.setStrokeStyle(CIRCLE_COLOR);
    context.beginPath();
    context.arc(epicycle.centerX(), epicycle.centerY(), radius, 0, FourierSeries.TAU);
    context.stroke();

    context.setStrokeStyle(INSIDE_COLOR);
    context.beginPath();
    context.moveTo(epicycle.centerX(), epicycle.centerY());
    context.lineTo(epicycle.endX(), epicycle.endY());
    context.stroke();
  }

  private void connect() {
    context.setStrokeStyle(LINE_COLOR);
    context.beginPath();
    context.moveTo(series.getX() + 0.5, series.getY() + 0.5);
    context.lineTo(FourierSeries.WAVE_START + 0.5, series.getY() + 0.5);
    context.stroke();
  }

  private void drawWave() {
    context.setStrokeStyle(WAVE_COLOR);
    context.beginPath();
    context.moveTo(FourierSeries.WAVE_START, series.getY());
    int visible = Math.min(series.waveLength(), canvas.getWidth() - FourierSeries.WAVE_START);
    for (int i = 1; i < visible; i++) {
      context.lineTo((double) i + FourierSeries.WAVE_START, series.waveAt(i));
    }
    context.stroke();
  }

  private void updateLabels() {
    frequencyLabel.setInnerHTML("Frequency: " + round(series.getFrequency()));
    orderLabel.setInnerHTML("Order: " + series.getOrder());
    scaleLabel.setInnerHTML("Scale: " + series.getScale());
    pairLabel.setInnerHTML("Pair: " + round(series.getX()) + ", " + round(series.getY()));
  }

  private static double round(double value) {
    return Math.round(value * 100.0) / 100.0;
  }
}
