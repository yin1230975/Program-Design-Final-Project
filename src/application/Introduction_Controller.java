package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.io.FileReader;
import java.io.BufferedReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.math3.stat.inference.TTest;

import java.lang.Math;
public class Introduction_Controller implements Initializable{
	private Set<String> checktwogroup = new LinkedHashSet <>();
	private int groupindex=1,obsindex=2;
	public File f;
	public String[] variables;
	public ArrayList<String[]> Data;
	NumberAxis xaxis = new NumberAxis();
	NumberAxis yaxis = new NumberAxis();
	LineChart linechart = new LineChart(xaxis,yaxis);
	@FXML
	private Button testbutton,columnbutton,executebutton,clearbutton,csvbutton,restartbutton,backbutton;
	@FXML
	private RadioButton twosample,pair;
	@FXML
	private TextField grouptextbox,obstextbox,meanbox,sigmabox;
	@FXML
	private Label problem,statement,col_label1,col_label2;
	@FXML
	private FlowPane chartinscene;
	
	public static double normalDistribution(double x, double mu, double seta) {
		return 1.0/(Math.sqrt(2.0*Math.PI)*seta)*Math.exp(-0.5*Math.pow((x-mu)/seta, 2));
	}
	
	public XYChart.Series<Double, Double> createSeries(double mean,double sigma){
		Series series = new LineChart.Series<>();
		series.setName("");
		for (double i=-15;i<=15;i+=0.1) {
			series.getData().add(new XYChart.Data(i,normalDistribution(i,mean,sigma)));
		}
		return series;
	}
	
	@Override
	public void initialize(URL arg0,ResourceBundle arg1)
	{
		csvbutton.setDisable(true);
		grouptextbox.setDisable(true);
		obstextbox.setDisable(true);
		columnbutton.setDisable(true);
		linechart.getData().add(createSeries(0,1));
		linechart.setCreateSymbols(false);
		linechart.setTitle("Normal Distribution");
		linechart.setPrefHeight(330);
		linechart.setPrefWidth(330);
		chartinscene.getChildren().add(linechart);
		grouptextbox.setText("");;
		obstextbox.setText("");
		problem.setText("");
		statement.setText("");
		executebutton.setDisable(true);
	}
	@FXML
	void restartall(ActionEvent event) throws IOException{
		columnbutton.setDisable(true);
		checktwogroup = new LinkedHashSet <>();
		groupindex=1;
		obsindex=2;
		f = null;
		variables = null;
		Data = null;
		NumberAxis xaxis = new NumberAxis();
		NumberAxis yaxis = new NumberAxis();
		LineChart linechart = new LineChart(xaxis,yaxis);
		chartinscene.getChildren().clear();
		csvbutton.setDisable(true);
		grouptextbox.setDisable(true);
		obstextbox.setDisable(true);
		linechart.getData().add(createSeries(0,1));
		linechart.setCreateSymbols(false);
		linechart.setTitle("Normal Distribution");
		linechart.setPrefHeight(330);
		linechart.setPrefWidth(330);
		chartinscene.getChildren().add(linechart);
		grouptextbox.setText("");;
		obstextbox.setText("");
		problem.setText("");
		statement.setText("");
		executebutton.setDisable(true);
	}
	
	@FXML
	void backtomain(ActionEvent event) throws IOException{
	}
	
	
	@FXML	
	void changemean(ActionEvent event) throws IOException{
		linechart.getData().add(createSeries(Double.parseDouble(meanbox.getText()),Double.parseDouble(sigmabox.getText())));
		linechart.setCreateSymbols(false);
		linechart.setTitle("Normal Distribution");
		linechart.setPrefHeight(330);
		linechart.setPrefWidth(330);
		chartinscene.getChildren().add(linechart);
	}
	
	@FXML	
	void changesigma(ActionEvent event) throws IOException{
		linechart.getData().add(createSeries(Double.parseDouble(meanbox.getText()),Double.parseDouble(sigmabox.getText())));
		linechart.setCreateSymbols(false);
		linechart.setTitle("Normal Distribution");
		linechart.setPrefHeight(330);
		linechart.setPrefWidth(330);
		chartinscene.getChildren().add(linechart);
	}
	@FXML	
	void clearpane(ActionEvent event) throws IOException{
		xaxis = new NumberAxis();
		yaxis = new NumberAxis();
		linechart = new LineChart(xaxis,yaxis);
		chartinscene.getChildren().clear();
	}
	
	@FXML	
	void confirmtest(ActionEvent event) throws IOException
	{
		csvbutton.setDisable(false);
		if (twosample.isSelected()) {
			col_label1.setText("group column");
			col_label2.setText("observation column");
		}
		if (pair.isSelected()) {
			col_label1.setText("group 1");
			col_label2.setText("group2");
		}
	}
	
	@FXML
	public void choosecsv(ActionEvent event) throws IOException
	{
		grouptextbox.setDisable(false);
		obstextbox.setDisable(false);
		variables = null;
		Data = new ArrayList<String[]>();
		Window window = ((Node) (event.getSource())).getScene().getWindow();
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fc.getExtensionFilters().add(extFilter);
		f = fc.showOpenDialog(window);
		if (f==null) {
			Alert nofile = new Alert(AlertType.ERROR);
			nofile.setContentText("Please choose file");
			nofile.setHeaderText("");
			nofile.setTitle("Error");
			nofile.show();
			grouptextbox.setDisable(true);
    		obstextbox.setDisable(true);
			return;
		}
		FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        line = br.readLine();
        variables = line.split(",");
        String[] tempArr;
        while((line = br.readLine()) != null) {
            tempArr = line.split(",");
            Data.add(tempArr);
         }
        br.close();
        columnbutton.setDisable(false);
        grouptextbox.setText(variables[1]);
    	obstextbox.setText(variables[2]);
        System.out.println(variables[0]+variables[1]+variables[2]);
	}
	@FXML
	public void confirmdata(ActionEvent event) throws IOException{
		boolean havegroupcol=false;
		boolean haveobscol=false;
		boolean twogroup=true;
		boolean obsisdouble=true;
		boolean group1double = true;
		boolean group2double = true;
		for (int i=0;i<variables.length;i++) {
			if (variables[i].equals(grouptextbox.getText())) {
				groupindex = i;
				havegroupcol=true;
			}
			if (variables[i].equals(obstextbox.getText())) {
				obsindex=i;
				haveobscol=true;
			}
		}
		if (!havegroupcol || !haveobscol) {
			Alert cannotfound_error = new Alert(AlertType.ERROR);
			if (!havegroupcol && !haveobscol) {
				cannotfound_error.setContentText(grouptextbox.getText() + "&" + obstextbox.getText()+" can not found");
			}
			else if (!havegroupcol ) {
				cannotfound_error.setContentText(grouptextbox.getText()+" can not found");
			}
			else if (!haveobscol ) {
				cannotfound_error.setContentText(obstextbox.getText()+" can not found");
			}
			cannotfound_error.setTitle("ERROR");
			cannotfound_error.setHeaderText("");
			cannotfound_error.show();
			grouptextbox.setText(variables[1]);
        	obstextbox.setText(variables[2]);
			return;
		}
		if (twosample.isSelected()) {
			checktwogroup = new LinkedHashSet <>();
        	for (int i=0;i<Data.size();i++) {
        		if (checktwogroup.size() > 2) {
        			twogroup=false;
        			break;
        		}
        		checktwogroup.add(Data.get(i)[groupindex]);
        	}
        	for (int i=0;i<Data.size();i++) {
        		try {
        			Double checkdouble=Double.parseDouble(Data.get(i)[obsindex]);
        		}
        		catch(Exception e){
        			obsisdouble=false;
        		}
        		if (!obsisdouble) {
        			break;
        		}
        	}
        	if (!twogroup || !obsisdouble) {
        		Alert group_or_obs_error = new Alert(AlertType.ERROR);
    			if (!twogroup && !obsisdouble) {
    				group_or_obs_error.setContentText("There are over two group" + "&" + obstextbox.getText()+" variable is not double");
    			}
    			else if (!twogroup) {
    				group_or_obs_error.setContentText("There are over two group");
    			}
    			else if (!obsisdouble ) {
    				group_or_obs_error.setContentText(obstextbox.getText()+" variable is not double");
    			}
    			group_or_obs_error.setTitle("ERROR");
    			group_or_obs_error.setHeaderText("");
    			group_or_obs_error.show();
    			grouptextbox.setText(variables[1]);
            	obstextbox.setText(variables[2]);
            	return;
        	}
        	executebutton.setDisable(false);
        	problem.setText("Check if there is difference between" + " mean " + (String) checktwogroup.toArray()[0]
    				+ " of " + obstextbox.getText() + " and mean "+ (String) checktwogroup.toArray()[1]
    						+ " of " + obstextbox.getText() + ".");
		}
        	if (pair.isSelected()) {
            	for (int i=0;i<Data.size();i++) {
            		try {
            			Double checkdouble=Double.parseDouble(Data.get(i)[groupindex]);
            		}
            		catch(Exception e){
            			group1double=false;
            		}
            		try {
            			Double checkdouble=Double.parseDouble(Data.get(i)[obsindex]);
            		}
            		catch(Exception e){
            			group2double=false;
            		}
            		if (!group1double || !group2double) {
            			break;
            		}
            	}
            	if (!group1double || !group2double) {
            		Alert nodouble_error = new Alert(AlertType.ERROR);
        			if (!group1double && !group2double) {
        				nodouble_error.setContentText(grouptextbox.getText()+" variable is not double" + "&" + obstextbox.getText()+" variable is not double");
        			}
        			else if (!group1double) {
        				nodouble_error.setContentText(grouptextbox.getText()+" variable is not double");
        			}
        			else if (!group2double) {
        				nodouble_error.setContentText(obstextbox.getText()+" variable is not double");
        			}
        			nodouble_error.setTitle("ERROR");
        			nodouble_error.setHeaderText("");
        			nodouble_error.show();
        			grouptextbox.setText(variables[1]);
                	obstextbox.setText(variables[2]);
                	return;
            	}
            	executebutton.setDisable(false);
        		problem.setText("Check if there is difference between" + " mean " + grouptextbox.getText()
        				+ " and mean "+ obstextbox.getText() + ".");
        }
		problem.setWrapText(true);
	}
	@FXML	
	void execute(ActionEvent event) throws IOException{
		ArrayList<Double> group1 = new ArrayList<>();
		ArrayList<Double> group2 = new ArrayList<>();
		if (twosample.isSelected()) {
			for (int i=0;i<Data.size();i++) {
				if (Data.get(i)[groupindex].equals((String) checktwogroup.toArray()[0])) {
					group1.add(Double.parseDouble(Data.get(i)[obsindex]));
				}
				if (Data.get(i)[groupindex].equals((String) checktwogroup.toArray()[1])) {
					group2.add(Double.parseDouble(Data.get(i)[obsindex]));
				}
			}
			double[] arr1 = group1.stream().mapToDouble(v -> v.doubleValue()).toArray();
			double[] arr2 = group2.stream().mapToDouble(v -> v.doubleValue()).toArray();
			TTest t = new TTest();
			if (t.tTest(arr1,arr2)>0.05) {
				statement.setText(" Mean " + (String) checktwogroup.toArray()[0]
						+ " of " + obstextbox.getText() + " and mean "+ (String) checktwogroup.toArray()[1]
								+ " of " + obstextbox.getText() + " doesn't differ significantly.");
			}
			if (t.tTest(arr1,arr2)<=0.05) {
				statement.setText(" Mean " + (String) checktwogroup.toArray()[0]
						+ " of " + obstextbox.getText() + " and mean "+ (String) checktwogroup.toArray()[1]
								+ " of " + obstextbox.getText() + " differs significantly.");
			}
		}
		if (pair.isSelected()) {
			for (int i=0;i<Data.size();i++) {
				group1.add(Double.parseDouble(Data.get(i)[groupindex]));
				group2.add(Double.parseDouble(Data.get(i)[obsindex]));
			}
			double[] arr1 = group1.stream().mapToDouble(v -> v.doubleValue()).toArray();
			double[] arr2 = group2.stream().mapToDouble(v -> v.doubleValue()).toArray();
			TTest t = new TTest();
			if (t.pairedTTest(arr1,arr2)>0.05) {
				statement.setText( " Mean " + grouptextbox.getText()
				+ " and mean "+ obstextbox.getText() + " doesn't differ significantly.");
			}
			if (t.pairedTTest(arr1,arr2)<=0.05) {
				statement.setText( " Mean " + grouptextbox.getText()
				+ " and mean "+ obstextbox.getText() + " differs significantly.");
			}
		}
		statement.setWrapText(true);
	}
	
	
	
	
	
	

}
