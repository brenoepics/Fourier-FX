module io.github.brenoepics.fourierfx {
		requires javafx.controls;
		requires javafx.fxml;


		opens io.github.brenoepics.fourierfx to javafx.fxml;
		exports io.github.brenoepics.fourierfx;
		exports io.github.brenoepics.fourierfx.calculator;
		opens io.github.brenoepics.fourierfx.calculator to javafx.fxml;
		exports io.github.brenoepics.fourierfx.ui;
		opens io.github.brenoepics.fourierfx.ui to javafx.fxml;
}