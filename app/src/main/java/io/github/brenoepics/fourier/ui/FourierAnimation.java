package io.github.brenoepics.fourier.ui;

import io.github.brenoepics.fourier.calculator.FourierCalculator;
import javafx.animation.AnimationTimer;

public class FourierAnimation extends AnimationTimer {

		private final FourierCalculator calculator;

		public FourierAnimation(FourierCalculator calculator) {
				this.calculator = calculator;
		}

		@Override
		public void handle(long now) {
				calculator.update();
		}

}