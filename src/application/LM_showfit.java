package application;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LM_showfit  implements Initializable{
	
	@FXML
	private Label l1;
	
	@FXML
	private Label variableSelectionWarn;
	
	@FXML 
	private TextField removeVariable;

	@FXML
	private Button exit;
	
	private Stage stage;
	private Scene scene;
	
	@Override 
	public void initialize(URL arg0,ResourceBundle arg1) {
		l1.setText(LM_Controller.model);
		variableSelectionWarn.setText(LM_Controller.warnMessage);
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
	
	@FXML
	public void refit(ActionEvent event) throws IOException
	{
		String variableToRemove = removeVariable.getText();
		String [] vList = variableToRemove.split(",");
		int [] removeList = new int[vList.length];
		for(int i=0;i<vList.length;i++)
		{
			System.out.println(vList[i]);
			removeList[i] = Arrays.asList(LM_Controller.variableArray).indexOf(vList[i]);
			System.out.println(removeList[i]);
		}
		double[][] data= LM_coreFunction.parseData(LM_Controller.f);
		
		double [] Y = LM_coreFunction.setY(data,LM_Controller.varY);
		double [][] X = LM_coreFunction.removeFeatures(data, LM_Controller.varY, removeList);	
		OLSMultipleLinearRegression regression = LM_coreFunction.returnRegresiionObject(X,Y);
		
		DecimalFormat df = new DecimalFormat("###.##");
		
		
		regression.newSampleData(Y, X);
		double[] temp = regression.estimateRegressionParameters();
		String[] est = new String[temp.length];
		
		int [] rList = Arrays.copyOf(removeList, removeList.length+1);
		rList[rList.length-1]=LM_Controller.varY;
		Arrays.sort(rList);
		int [] reserveList = new int[data[0].length-rList.length];
		int k = 0;
		for(int i = 0;i<data[0].length;i++)
		{
			if(Arrays.asList(Arrays.stream( rList ).boxed().toArray( Integer[]::new )).indexOf(i)==-1)
			{
				reserveList[k]=i;
				k++;
			}
		}
		
		
		for(int i =0;i<temp.length;i++)
		{
			est[i] = df.format(temp[i]);
		}
		String warnMessage = new String();
		String model = "y = ";
		model = model + est[0] +"+";
		
		double [] tval = LM_coreFunction.t_valueEstimation(regression,LM_Controller.varY);
		Boolean[] notSignificant = LM_coreFunction.significantCalculate(LM_coreFunction.p_valueCalculate(tval, 30));
		if(notSignificant[0]) warnMessage = warnMessage+"coefficient not significant\n";
		for(int i = 1;i<est.length;i++) {
			if(notSignificant[i]) warnMessage = warnMessage+LM_Controller.variableArray[reserveList[i-1]]+" not significant\n";
			model = model+ est[i]+LM_Controller.variableArray[reserveList[i-1]];
			
			if(i<est.length-1 && temp[i+1]>0) model = model + "+";
		}
		l1.setText(model);
		variableSelectionWarn.setText(warnMessage);
	}
	
	

	
}
