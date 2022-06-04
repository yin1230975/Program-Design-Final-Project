package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class CT_Chart implements Initializable {
	@FXML
	private ScatterChart<Number, Number> chart;
	@FXML
	private List<XYChart.Series<Number, Number>> clusteredSeries;// = new XYChart.Series<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//    	chart.getData().clear();
		chart.setVisible(false);
		try {
			clusteredSeries = Clustering.buildClusteredSeries();
		} catch (Exception e) {
			e.printStackTrace();
		}

		chart.getData().addAll(clusteredSeries);
		chart.setTitle("Clustered Result");
		chart.setVisible(true);

	}

}
