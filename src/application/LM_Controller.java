package application;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class LM_Controller implements Initializable{
	private Stage stage;
	private Scene scene;
	public static File f;
	
	public static String model="y = ";
	public static String[] variables;
	public static String warnMessage ;
	
	@FXML
	private Label variableList;
	@FXML
	private TextField dependentVar;
	@FXML
	private Button sendDependentVar;
	@FXML
	private Button nextButton;
	@FXML
	private Button chooseCSVButton;
	@FXML
	private Text t1;
	@FXML
	private Text t2;
	
	public static int varY;
	public static String[] variableArray;
	
	@Override
	public void initialize(URL arg0,ResourceBundle arg1)
	{
		variableList.setVisible(false);
		dependentVar.setVisible(false);
		t1.setVisible(false);
		t2.setVisible(false);
		sendDependentVar.setVisible(false);
		nextButton.setVisible(false);
	}
	
	@FXML
	void chooseCSV(ActionEvent event) throws IOException
	{
		Window window = ((Node) (event.getSource())).getScene().getWindow();
		FileChooser fc = new FileChooser();
		f = fc.showOpenDialog(window);
		Scanner sc = new Scanner(f);
		sc.useDelimiter(",");
	    //setting comma as delimiter pattern
	    while (sc.hasNext()) {
	      System.out.print(sc.next());
	    }
	    sc.close();
	    variableArray = LM_coreFunction.getVariable(f);
	    String printVar = new String();
	    for(int i=0;i<variableArray.length;i++)
	    {
	    	printVar = printVar+variableArray[i]+"  ";
	    }
	    t1.setVisible(true);
	    variableList.setVisible(true);
	    t2.setVisible(true);
	    dependentVar.setVisible(true);
	    sendDependentVar.setVisible(true);
	    chooseCSVButton.setVisible(false);
	    variableList.setText(printVar);
	}
	
	@FXML
	public void setDependentVar (ActionEvent event)
	{
		
		varY = Arrays.asList(variableArray).indexOf(dependentVar.getText());
		System.out.println(dependentVar.getText());
		System.out.print(varY);
		nextButton.setVisible(true);
		
	}
	
	@FXML
	public void switchToFitted(ActionEvent event) throws IOException{
		String[] est = LM_coreFunction.coefEstimation(f, varY);
		double [][] data = LM_coreFunction.parseData(f);
		System.out.println("finish fitting LM");
		warnMessage = new String();
		model = model + est[0] +"+";
		OLSMultipleLinearRegression regression = LM_coreFunction.returnRegresiionObject(LM_coreFunction.setX(data,varY),LM_coreFunction.setY(data,varY));
		double [] tval = LM_coreFunction.t_valueEstimation(regression,varY);
		Boolean[] notSignificant = LM_coreFunction.significantCalculate(LM_coreFunction.p_valueCalculate(tval, 30));
		if(notSignificant[0]) warnMessage = warnMessage+"coefficient not significant\n";
		for(int i = 1;i<est.length;i++) {
			if(i<=varY) {
				if(notSignificant[i]) warnMessage = warnMessage+variableArray[i-1]+" not significant\n";
				model = model+ est[i]+variableArray[i-1];
			}else if(i>varY) {
				if(notSignificant[i]) warnMessage = warnMessage+variableArray[i]+" not significant\n";
				model = model+ est[i]+variableArray[i];
			}
			System.out.print(i);
			
			if(i<est.length-1 && Double.parseDouble(est[i+1])>0) model = model + "+";
		}
		System.out.print("f");
		Parent root = FXMLLoader.load(getClass().getResource("/application/LM_Scene2.fxml"));	
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}
	
}
