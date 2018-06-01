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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
 * import ce davat error, kliknite linkove, skinite jar fajlove i importajte ih u projekt
 * Postupak: desni klik projekt -> properties -> java build path -> libraries -> add external jar
 * http://www.java2s.com/Code/Jar/m/Downloadmtj0913jar.htm
 * http://www.java2s.com/Code/Jar/n/Downloadnetlibjava093jar.htm
 * */

public class pValueCalculation {
	
	/* Code that computes the p-values for all coefficients of a logistic regression model 
	 * It was originally written in Groovy code and team translated it to java. 
	 * Code source: http://weka.8497.n7.nabble.com/How-to-get-the-p-Values-from-the-logistic-regression-in-weka-td37743.html
	 * */
	
	private Map<String, ArrayList<Double>> result = new HashMap<String, ArrayList<Double>>();
	
	public void calculatepValue(Instances data_) throws Exception
	{
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
		
		
		System.out.println("\tEstimate\t\tStd. Error\tz value\tPr(>|z|)");
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
			if ( ( p > 0.5 ) && ( j > 0 ) ) 
			{
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

}
