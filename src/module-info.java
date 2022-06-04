module testfx {
	requires javafx.controls;
	requires javafx.fxml;
	requires commons.math3;
	requires weka;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
}
