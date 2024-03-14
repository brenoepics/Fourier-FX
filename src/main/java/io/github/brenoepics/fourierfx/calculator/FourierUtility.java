package io.github.brenoepics.fourierfx.calculator;

import io.github.brenoepics.fourierfx.ui.FourierAnimation;
import io.github.brenoepics.fourierfx.ui.FourierColors;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FourierUtility {
  FourierUtility() {
    throw new IllegalStateException("Utility class");
  }

  public static final WaveForm DEFAULT_WAVEFORM = WaveForm.SQUARE;
  public static final double DEFAULT_SCALE = 64.0;
  public static final double DEFAULT_FREQUENCY = -3.5;
  public static final int DEFAULT_ORDER = 4;

  public static WaveForm getWaveForm(String text) {
    return WaveForm.fromString(text).orElse(DEFAULT_WAVEFORM);
  }

  public static void addFrequencyListener(
      FourierCalculator calculator, DoubleProperty valueProperty) {
    valueProperty.addListener(
        (observable, oldValue, newValue) -> calculator.setFrequency(newValue.doubleValue()));
  }

  public static void addWaveListener(
      FourierCalculator calc, ReadOnlyObjectProperty<Toggle> selected) {
    selected.addListener(
        (observable, oldValue, newValue) ->
            calc.setWaveForm(
                FourierUtility.getWaveForm(
                    ((RadioButton) newValue.getToggleGroup().getSelectedToggle()).getText())));
  }

  public static void addOrderListener(FourierCalculator calculator, DoubleProperty valueProperty) {
    valueProperty.addListener(
        (observable, oldValue, newValue) -> calculator.setOrder(newValue.intValue()));
  }

  public static List<RadioButton> createWaveForms(ToggleGroup group) {
    List<RadioButton> radioButtons = new ArrayList<>();
    for (WaveForm waveForm : WaveForm.values()) {
      RadioButton radioButton = new RadioButton(waveForm.getText());
      radioButton.setCursor(javafx.scene.Cursor.HAND);
      radioButton.setToggleGroup(group);
      HBox.setMargin(radioButton, new Insets(0, 10, 0, 0));
      radioButtons.add(radioButton);
    }

    return radioButtons;
  }

  public static void startDebug(GridPane debugGrid) {
    Label frequencyLabel = new Label();
    Label orderLabel = new Label();
    Label scaleLabel = new Label();
    Label pairLabel = new Label();

    frequencyLabel.setId("frequency");
    orderLabel.setId("order");
    scaleLabel.setId("scale");
    pairLabel.setId("pair");

    debugGrid.add(frequencyLabel, 0, 0);
    debugGrid.add(orderLabel, 1, 0);
    debugGrid.add(scaleLabel, 0, 1);
    debugGrid.add(pairLabel, 1, 1);
  }

  public static ToggleGroup createWaveForm(HBox waveformContainer) {
    ToggleGroup waveformToggleGroup = new ToggleGroup();
    List<RadioButton> waves = createWaveForms(waveformToggleGroup);
    waveformContainer.getChildren().addAll(waves);

    if (!waveformToggleGroup.getToggles().isEmpty()) {
      waveformToggleGroup.getToggles().getFirst().setSelected(true);
    }

    return waveformToggleGroup;
  }

  public static void createCalculator(
      Slider order,
      Slider frequency,
      ToggleGroup waveGroup,
      Button increaseBtn,
      Button decreaseBtn,
      Canvas canvas,
      GridPane debugGrid) {
    FourierCalculator calculator = buildCalculator(canvas, debugGrid);
    addListeners(order, frequency, waveGroup, calculator);
    setScaleActions(increaseBtn, decreaseBtn, calculator);
    new FourierAnimation(calculator).start();
  }

  private static void addListeners(
      Slider orderInput,
      Slider frequencyInput,
      ToggleGroup waveformToggleGroup,
      FourierCalculator calculator) {
    FourierUtility.addOrderListener(calculator, orderInput.valueProperty());
    FourierUtility.addFrequencyListener(calculator, frequencyInput.valueProperty());
    FourierUtility.addWaveListener(calculator, waveformToggleGroup.selectedToggleProperty());
  }

  private static void setScaleActions(
      Button increaseScaleButton, Button decreaseScaleButton, FourierCalculator calculator) {
    increaseScaleButton.setOnAction(e -> calculator.increaseScale());
    decreaseScaleButton.setOnAction(e -> calculator.decreaseScale());
  }

  private static FourierCalculator buildCalculator(Canvas canvas, GridPane debugGrid) {
    GraphicsContext context = canvas.getGraphicsContext2D();

    return new CalculatorBuilder()
        .setContext(context)
        .setScale(DEFAULT_SCALE)
        .setWaveForm(DEFAULT_WAVEFORM)
        .setOrder(DEFAULT_ORDER)
        .setFrequency(DEFAULT_FREQUENCY)
        .setColors(new FourierColors())
        .setDebugGrid(debugGrid)
        .build();
  }

  public static void switchDebug(Label label, FourierCalculator calculator) {
    switch (label.getId()) {
      case "frequency":
        label.setText("Frequency: " + calculator.getFrequency());
        break;
      case "order":
        label.setText("Order: " + calculator.getOrder());
        break;
      case "scale":
        label.setText("Scale: " + calculator.getScale());
        break;
      case "pair":
        label.setText("Pair: " + calculator.getCords().getKey() + ", " + calculator.getCords().getValue());
        break;
      default:
        label.setText("Unknown");
        break;
    }
  }
}
