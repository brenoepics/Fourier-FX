package io.github.brenoepics.fourierfx.ui;

import javafx.scene.paint.Color;

public class FourierColors {
		private Color waveColor = Color.rgb(0, 255, 0);
		private Color circleColor = Color.rgb(255, 0, 0);
		private Color lineColor = Color.rgb(255, 255, 32);
		private Color insideColor = Color.rgb(157, 46, 40, 0.3);

		public Color getWaveColor() {
				return waveColor;
		}

		public void setWaveColor(Color waveColor) {
				this.waveColor = waveColor;
		}

		public Color getCircleColor() {
				return circleColor;
		}

		public void setCircleColor(Color circleColor) {
				this.circleColor = circleColor;
		}

		public Color getLineColor() {
				return lineColor;
		}

		public void setLineColor(Color lineColor) {
				this.lineColor = lineColor;
		}

		public Color getInsideColor() {
				return insideColor;
		}

		public void setInsideColor(Color insideColor) {
				this.insideColor = insideColor;
		}
}
