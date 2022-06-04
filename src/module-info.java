module testfx {
	requires javafx.controls;
	requires javafx.fxml;
	requires commons.math3;
	requires weka;
	requires com.github.haifengl.smile.math;
	requires com.github.haifengl.smile.core;
	requires com.github.haifengl.smile.data;
	requires javafx.graphics;
	requires JChart;
	opens application to javafx.graphics, javafx.fxml;
}
