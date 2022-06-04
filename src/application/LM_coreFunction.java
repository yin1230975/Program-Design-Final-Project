package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.regression.*;
import org.apache.commons.math3.linear.*;




public class LM_coreFunction {
	private static File f = new File("/Users/yinziwei/Documents/成功大學/大四下/程式設計(二)/Term_project/mtcars.csv");

	public static double[][] parseData(File f) throws IOException
	{
		ArrayList<String[]> Data = new ArrayList<String[]>();
		FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        String[] tempArr;
        while((line = br.readLine()) != null) {
           tempArr = line.split(",");
           Data.add(tempArr);
        }
        br.close();
        int nrow = Data.size();
        int ncol = Data.get(1).length;
        double[][] DataToAnalysis = new double[nrow-1][ncol-1];
        for(int i =0;i<nrow-1;i++)
        {
        	for(int j=1;j<ncol;j++)
        	{
        		DataToAnalysis[i][j-1] = Double.parseDouble(Data.get(i+1)[j]);
        	}
        }
		return DataToAnalysis;
	}
	
	public static String[] getVariable(File f) throws IOException
	{
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String[] temp = br.readLine().split(",");
		String[] variable = Arrays.copyOfRange(temp, 1, temp.length);
		for(int i = 0;i<variable.length;i++)
		{
			variable[i] = variable[i].substring(1, variable[i].length() - 1);
		}
		
		return variable;
	}
	
	
	
	public static double[] setY(double[][] Data,int col) 
	{
		double [] Y = new double[Data.length];
		for(int i=0;i<Data.length;i++)
		{
			Y[i] = Data[i][col];
		}
		
		return Y;
	}
	public static double[][] setX(double[][] Data,int col) 
	{
		double [][] X = new double[Data.length][Data[0].length-1];
		for(int i=0;i<Data.length;i++)
		{
			for(int j=0;j<Data[0].length-1;j++)
			{
				if(j<col) X[i][j] = Data[i][j];
				else if(j>=col) X[i][j] = Data[i][j+1];
			}
		}
		
		return X;
	}
	
	public static double[][] removeFeatures(double[][] Data, int Y, int[] removeFeatureIndex)
	{
		double[][] X = new double[Data.length][Data[0].length-1-removeFeatureIndex.length];
		int [] removeList = Arrays.copyOf(removeFeatureIndex, removeFeatureIndex.length+1);
		removeList[removeList.length-1]=Y;
		Arrays.sort(removeList);
		int [] reserveList = new int[Data[0].length-removeList.length];
		int k = 0;
		for(int i = 0;i<Data[0].length;i++)
		{
			if(Arrays.asList(Arrays.stream( removeList ).boxed().toArray( Integer[]::new )).indexOf(i)==-1)
			{
				reserveList[k]=i;
				k++;
			}
		}
		for(int i=0;i<Data.length;i++)
		{
			for(int j=0;j<reserveList.length;j++)
			{
				X[i][j] = Data[i][reserveList[j]];
				System.out.print(X[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		return X;
	}
	public static OLSMultipleLinearRegression returnRegresiionObject(double[][] X,double[] Y)
	{
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		regression.newSampleData(Y, X);
		return regression;
	}
	
	public static String[] coefEstimation(File f,int indexY) throws IOException
	{
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		DecimalFormat df = new DecimalFormat("###.##");
		double[][] data= parseData(f);
		double [] Y = setY(data,indexY);
		double [][] X = setX(data,indexY);
		regression.newSampleData(Y, X);
		double[] temp = regression.estimateRegressionParameters();
		String[] beta = new String[temp.length];
		for(int i =0;i<temp.length;i++)
		{
			beta[i] = df.format(temp[i]);
		}
		return beta;
	}
	public static double[] t_valueEstimation(OLSMultipleLinearRegression regression,int indexY) throws IOException
	{	
		double[] beta = regression.estimateRegressionParameters();
		double[] stdError = regression.estimateRegressionParametersStandardErrors();
		double[] tval = new double[beta.length];
		for(int i = 0;i<beta.length;i++)
		{
			tval[i] = beta[i]/stdError[i];
		}
		return tval;
	}
	
	public static double[] p_valueCalculate(double[] tval,int degreeOfFreedom) throws IOException
	{
		TDistribution t = new TDistribution(degreeOfFreedom);
		double[] pval = new double[tval.length];
		
		for(int i=0;i<tval.length;i++)
		{
			pval[i] = 2*(1-t.cumulativeProbability(Math.abs(tval[i])));
		}
		return pval;
	}
	
	public static Boolean[] significantCalculate(double[] pval)
	{
		Boolean[] notSignificant = new Boolean[pval.length];
		for(int i=0;i<pval.length;i++)
		{
			if(pval[i]>0.05) notSignificant[i] = true;
			else notSignificant[i] = false;
			System.out.println(pval[i]);
		}
		return notSignificant;
	}
	
	public static void main(String[] args) throws IOException
	{
		double [][] Data = parseData(f);
		int [] r = {4,5};
		double [][] R = removeFeatures(Data,1,r);
		
//		String[] b = coefEstimation(f,0);
//		String[] var = getVariable(f);
//		for(int i = 0;i<b.length;i++) {
//			System.out.println(b[i]);
//		}
//		for(int i = 0;i<var.length;i++) {
//			System.out.println(var[i]);
//		}
		//System.out.println(data[2][3]);
		//System.out.println(data);
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		DecimalFormat df = new DecimalFormat("###.##");
		double[][] data= parseData(f);
		double [] Y = setY(data,0);
		double [][] X = setX(data,0);
		regression.newSampleData(Y, X);
		//double[] stdError = p_valueCalculate(f,0,30);
		//System.out.println(stdError);
//		for(int i = 0;i<stdError.length;i++) {
//			System.out.println(stdError[i]);
//		}
	}
	
}
