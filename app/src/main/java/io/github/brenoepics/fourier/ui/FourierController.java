package io.github.brenoepics.fourier.ui;

import io.github.brenoepics.fourier.calculator.FourierUtility;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class FourierController {
  @FXML private GridPane debugGrid;
  @FXML private Button increaseScaleButton;
  @FXML private Button decreaseScaleButton;
  @FXML private HBox waveformContainer;
  @FXML private Canvas canvas;
  @FXML private Slider orderInput;
  @FXML private Slider frequencyInput;
  @FXML private ToggleGroup waveformToggleGroup;

  public void initialize() {
    FourierUtility.startDebug(debugGrid);
    waveformToggleGroup = FourierUtility.createWaveForm(waveformContainer);
    FourierUtility.createCalculator(
        orderInput,
        frequencyInput,
        waveformToggleGroup,
        increaseScaleButton,
        decreaseScaleButton,
        canvas,
        debugGrid);
  }
}
