module io.github.brenoepics.fourier {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.github.brenoepics.fourier to javafx.fxml;
    exports io.github.brenoepics.fourier;
    exports io.github.brenoepics.fourier.calculator;
    opens io.github.brenoepics.fourier.calculator to javafx.fxml;
    exports io.github.brenoepics.fourier.ui;
    opens io.github.brenoepics.fourier.ui to javafx.fxml;
}