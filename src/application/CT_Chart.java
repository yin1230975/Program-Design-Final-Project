package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class CT_Chart implements Initializable {
	@FXML
	private ScatterChart<Number, Number> chart;
	@FXML
	private List<XYChart.Series<Number, Number>> clusteredSeries;// = new XYChart.Series<>();
	
	private Stage stage;
	private Scene scene;
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
	@FXML
	public void Exit(ActionEvent event) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("/application/MainScene.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
}
