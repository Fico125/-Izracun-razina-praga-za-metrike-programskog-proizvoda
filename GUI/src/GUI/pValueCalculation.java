package GUI;

import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.Utils;
import no.uib.cipr.matrix.AbstractMatrix;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.UpperSymmDenseMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;

import org.netlib.lapack.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

/*
 * import ce davat error, kliknite link, skinite jar file i importajte ga u projekt
 * Postupak: desni klik projekt -> properties -> java build path -> libraries -> add external jar
 * http://www.java2s.com/Code/Jar/m/Downloadmtj0913jar.htm¸
 * http://commons.apache.org/proper/commons-math/download_math.cgi  ( skinite commons-math3-3.6.1-bin.zip, odmah na vrhu je )
 * */

public class pValueCalculation {
	
	/* Code that computes the p-values for all coefficients of a logistic regression model */
	private Map<String, ArrayList<Double>> result = new HashMap<String, ArrayList<Double>>();
	
	private double p_;
	
	
	public double getP_() {
		return p_;
	}


	public void setP_(double p_) {
		this.p_ = p_;
	}

	public double normalReducedDistributionCumulativeFunction(double x) {
		double x0 = -10;
		double dx = 1e-3;
		double x_i = x0;
		double sum = 0.0;
		while ( x_i < x ) {
			sum += gaussian(x_i)* dx;
			x_i += dx;
		}
		return sum;
	}
	
	private double gaussian(double x) {
		return Math.exp(-x*x/2);
	}

	public void calculatepValue(Instances data_) throws Exception
	{
		//System.out.println(BLAS.getInstance().getClass().getName());
		int index;
		double c, e, p, z;
		
		Instances data = data_;
		
		data.setClassIndex(data.numAttributes() - 1);
		
		Classifier classifier = new weka.classifiers.functions.Logistic();
		
		classifier.buildClassifier(data);
		
		double[][] koef = ((Logistic) classifier).coefficients();
		
		// Store number of instances and number of attributes 
		int n = data.numInstances();
		int m = data.numAttributes();
		
		// Establish required matrices 
		DenseMatrix X = new DenseMatrix(n, m);
		UpperSymmDenseMatrix V = new UpperSymmDenseMatrix(n);
		
		for(int i = 0; i < n; i++)
		{
			p = classifier.distributionForInstance(data.instance(i))[0];
			V.set(i, i, p * (1 - p));
			index = 0;
			X.set(i, index++, 1.0);
			
			for(int j = 0; j < m; j++)
			{
				if(j != data.classIndex())
				{
					X.set(i, index++, data.instance(i).value(j));
				}
			}
		}
		
		// Compute M = X'VX
		Matrix M = X.transpose(new DenseMatrix(m, n)).mult(V, new DenseMatrix(new DenseMatrix(m, n))).mult(X, new UpperSymmDenseMatrix(m));
		
		// Compute covariance matrix for parameters (inverse of M)
		AbstractMatrix I = Matrices.identity(m);
		Matrix C =  I.copy(); 
		C = M.solve(I, C);
		
		
		System.out.println("Metric name  Estimate\t\tStd. Error\t\tz value\t\tPr(>|z|)");
		System.out.println("Metric name  Beta_0\t\tBeta_1\t\tp_value\t\tVARL");
		for(int j = 0; j < m - 1; j++)
		{
			ArrayList<Double> resultVector = new ArrayList<Double>();
			if(j == 0)
			{
				System.out.print("Interc.");
			}
			else
			{
				System.out.println(data.attribute(j).name());
			}
			c = koef[j][0];
			e = Math.sqrt(C.get(j, j));
			z = (c / e);
			p = 2.0 * (1.0 - weka.core.Statistics.normalProbability(Math.abs(z)));
			setP_(p);
			if ( ( p > 0.5 ) && ( j > 0 ) ) {
				resultVector.add(c);
				resultVector.add(e);
				resultVector.add(z);
				resultVector.add(p);
				result.put(data.attribute(j).name(), resultVector);
			}
			
			System.out.print("\t" + Utils.doubleToString(c, 7));
			System.out.print("\t" + Utils.doubleToString(e, 7));
			System.out.print("\t" + Utils.doubleToString(z, 3));
			System.out.println("\t" + Utils.doubleToString(p, 6));
		}
	}


	public double calculatepValueBasedOnChiSquareAllInstance(ArrayList<Double> resultFromModel,
			ArrayList<Double> resultFromSample) {
		long[] arrayFromModel = new long[resultFromModel.size()];
		double[] arrayFromSample = new double[resultFromSample.size()];
		for (int i = 0; i < resultFromModel.size(); i++) {
			arrayFromSample[i] = resultFromSample.get(i);
			arrayFromModel[i] = Double.valueOf(resultFromModel.get(i)).longValue();
		}
		ChiSquareTest t = new ChiSquareTest();
        double pval = t.chiSquareTest(arrayFromSample, arrayFromModel);
		return pval;
	}


	public double calculatepValueBasedOnChiSquare(double[] expected, long[] observed) {
		ChiSquareTest t = new ChiSquareTest();
		return t.chiSquareTest(expected, observed);
	}



}
