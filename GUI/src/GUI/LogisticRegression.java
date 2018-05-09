import java.io.File;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class LogisticRegression {


	public static void process(Instances train, Instances test, Instances randData) throws Exception {
		
		Instances trainingDataSet = train;
		Instances testingDataSet = test;
		/** Classifier here is Linear Regression */
		Classifier classifier = new weka.classifiers.functions.Logistic();
		
		
		/** */
		classifier.buildClassifier(trainingDataSet);
	    
		
		/**
		 * train the alogorithm with the training data and evaluate the
		 * algorithm with testing data
		 */
		Evaluation eval = new Evaluation(trainingDataSet);
		eval.evaluateModel(classifier, testingDataSet);
		/** Print the algorithm summary */
		System.out.println("** Linear Regression Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(classifier);

		Instance predicationDataSet = randData.lastInstance();
		double value = classifier.classifyInstance(predicationDataSet);
		
		/** Prediction Output */
		System.out.println("Prediction output: ");
		System.out.println(value);
		
		
		double[][] koef = ((Logistic) classifier).coefficients();
		for(int i = 0; i < koef.length; i++)
		{
			System.out.println("koeficijent: " + koef[i][0]);
		}
		
		System.out.println("*************************************************************************");
	}

}
