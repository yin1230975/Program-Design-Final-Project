package application;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

//import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
//import javafx.scene.control.Button;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Clustering {
	private static final int NUMBER_OF_CLASSES = 3;
	private static final String DATA_SET = "src/application/iris.2D.arff";
//	private ScatterChart<Number, Number> clusteredChart;

//	private static int swapIndex = 0;
//	private int[][] swapColorsCombinations = { { 0, 1 }, { 0, 2 }, { 1, 2 } };
	public static Instances data;

//	public void start(Stage stage) throws Exception {
//		loadData();
//		clusteredChart = buildChart("Clustered", buildClusteredSeries());
//
////		Button btnRestore = new Button("Restore original data");
////		Button btnSwapColors = new Button("Swap clustered chart colors");
////
////		btnRestore.setOnAction(e -> {
////			loadData();
////			reloadSeries();
////		});
////		btnSwapColors.setOnAction(e -> swapClusteredChartSeriesColors());
//
//		GridPane gpRoot = new GridPane();
//		gpRoot.add(clusteredChart, 0, 0);
//
//		stage.setScene(new Scene(gpRoot));
//		stage.setTitle("Íris dataset clustering and visualization");
//		stage.show();
//	}

	public static void loadData() {
		BufferedReader datafile;
		try {
//			InputStream dataSetIs = getClass().getResource(DATA_SET).openStream();
//			datafile = new BufferedReader(new InputStreamReader(dataSetIs));
			datafile = new BufferedReader(new FileReader(DATA_SET));
			data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
		} catch (Exception e) {
			System.out.println("Exception loading data... Leaving");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static List<Series<Number, Number>> buildClusteredSeries() throws Exception {
		loadData();
		List<XYChart.Series<Number, Number>> clusteredSeries = new ArrayList<>();

		// to build the cluster we remove the class information
		Remove remove = new Remove();
		remove.setAttributeIndices("3");
		remove.setInputFormat(data);
		Instances dataToBeClustered = Filter.useFilter(data, remove);

		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setSeed(10);
		kmeans.setPreserveInstancesOrder(true);
		kmeans.setNumClusters(NUMBER_OF_CLASSES);
		kmeans.buildClusterer(dataToBeClustered);

		IntStream.range(0, 3).mapToObj(i -> {
			Series<Number, Number> newSeries = new XYChart.Series<>();
			newSeries.setName(String.valueOf(i));
			return newSeries;
		}).forEach(clusteredSeries::add);

		int[] assignments = kmeans.getAssignments();
		for (int i = 0; i < assignments.length; i++) {
			int clusterNum = assignments[i];
			clusteredSeries.get(clusterNum).getData().add(instancetoChartData(data.get(i)));
		}

		return clusteredSeries;
	}

	public static XYChart.Data<Number, Number> instancetoChartData(Instance i) {
		return new XYChart.Data<Number, Number>(i.value(0), i.value(1));
	}

	// this function is never called in the project
	public ScatterChart<Number, Number> buildChart(String chartName, List<XYChart.Series<Number, Number>> series) {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final ScatterChart<Number, Number> sc = new ScatterChart<Number, Number>(xAxis, yAxis);
		sc.setTitle(chartName);
		sc.setPrefHeight(450);
		sc.setPrefWidth(600);
		xAxis.getValueForDisplay(1);
		yAxis.getValueForDisplay(2);
		sc.getData().addAll(series);
		return sc;
	}

//	private void reloadSeries() {
//		try {
//			clusteredChart.getData().clear();
//			clusteredChart.getData().addAll(buildClusteredSeries());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void swapClusteredChartSeriesColors() {
//		List<Series<Number, Number>> clusteredSeries = new ArrayList<>();
//		// we have to copy the original data to swap the series
//		clusteredChart.getData().forEach(series -> {
//			Series<Number, Number> newSeries = new Series<>();
//			newSeries.setName(series.getName());
//			series.getData().stream().map(d -> new Data<>(d.getXValue(), d.getYValue())).forEach(series.getData()::add);
//			clusteredSeries.add(newSeries);
//		});
//		int i = swapColorsCombinations[swapIndex][0];
//		int j = swapColorsCombinations[swapIndex][1];
//		Collections.swap(clusteredSeries, i, j);
//		clusteredChart.getData().clear();
//		clusteredChart.getData().addAll(clusteredSeries);
//		swapIndex = swapIndex == NUMBER_OF_CLASSES - 1 ? 0 : swapIndex + 1;
//	}

}
