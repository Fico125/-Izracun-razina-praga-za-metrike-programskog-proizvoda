package GUI;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/*
 * Cilj klase je na osnovu logistièke regresije,metode strojnog uèenja
 * dati koeficijente beta0, beta1 korištene u sigmoidi i na osnovu njih
 * cemo primijeniti Bender metodu
 * */

public class LogisticRegression {

	private double beta0;
	private double beta1;
	private String outputText = "";
	private Classifier classifier;
	
	public Classifier getClassifier() {
		return classifier;
	}

	public double getBeta0() {
		return beta0;
	}

	public void setBeta0(double beta0) {
		this.beta0 = beta0;
	}

	public double getBeta1() {
		return beta1;
	}

	public void setBeta1(double beta1) {
		this.beta1 = beta1;
	}

	public void process(Instances train, Instances test, Instances randData) throws Exception {
		
		Instances trainingDataSet = train;
		Instances testingDataSet = test;
		/** Classifier here is Logistic Regression */
		classifier = new weka.classifiers.functions.Logistic();
		
		classifier.buildClassifier(trainingDataSet);
		/**
		 * train the algorithm with the training data and evaluate the
		 * algorithm with testing data
		 */
		Evaluation eval = new Evaluation(trainingDataSet);
		eval.evaluateModel(classifier, testingDataSet);
		/** Print the algorithm summary */
		System.out.println("** Linear Regression Evaluation with Datasets **");
		outputTextPreparation("** Linear Regression Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		outputTextPreparation(eval.toSummaryString());
		System.out.print(" the expression for the input data as per algorithm is ");
		outputTextPreparation(" the expression for the input data as per algorithm is ");
		System.out.println(classifier);
		outputTextPreparation(classifier);

		Instance predicationDataSet = randData.lastInstance();
		double value = classifier.classifyInstance(predicationDataSet);
		
		/** Prediction Output */
		System.out.println("Prediction output: ");
		outputTextPreparation("Prediction output: ");
		System.out.println(value);
		outputTextPreparation(value);
		
		double[][] koef = ((Logistic) classifier).coefficients();
		setBeta0(koef[0][0]);
		setBeta1(koef[1][0]);
		for(int i = 0; i < koef.length; i++)
		{
			System.out.println("koeficijent: " + koef[i][0]);
			outputTextPreparation("koeficijent: " + koef[i][0]);
		}
		
		System.out.println("*************************************************************************");
		outputTextPreparation("*******************************************************************");
	}
	
	private void outputTextPreparation( Object text) {
		outputText += text + "\n";
	}
	
    public String getOutputText() 
    {
    	return outputText;
    }
}