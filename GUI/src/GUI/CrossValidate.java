package GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Remove;


public class CrossValidate {
	
	private String resultText;
	private Input input;
	private double majorityClassPercentage;
	private Instances data;
	private Instances testDataSet; 
	private Map<String, ArrayList<Double>> regressionResults = new HashMap<String, ArrayList<Double>>();
	private int totalNumberOfOne;
	public int getTotalNumberOfOne() {
		return totalNumberOfOne;
	}

	public void setTotalNumberOfOne(int totalNumberOfOne) {
		this.totalNumberOfOne = totalNumberOfOne;
	}
	
	private void computerNumberOfElementsInEachCategory(Instances data) {
		int sumZero = 0;
		int sumOne = 0;
		int m = data.numAttributes()-1;
		for ( int i = 0; i < data.numInstances(); i++) {
			if ( data.instance(i).value(m) == 0 ) {
				sumZero++;
			}else {
				sumOne++;
			}
		}
		setTotalNumberOfOne(sumOne);
		setTotalNumberOfZero(sumZero);
	}
	
	private int totalNumberOfZero;
	
	public int getTotalNumberOfZero() {
		return totalNumberOfZero;
	}

	public void setTotalNumberOfZero(int totalNumberOfZero) {
		this.totalNumberOfZero = totalNumberOfZero;
	}

	public void printRegressionResults() {
		for ( String metric : regressionResults.keySet()) {
			ArrayList<Double> coefficients = regressionResults.get(metric);
			System.out.print(metric + " ");
			for (int i = 0; i < coefficients.size() ; i++) {
				System.out.print(coefficients.get(i) + "  ||  ");
			}
			System.out.println("");
			System.out.println("==============================");
		}
	}
	
	public Map<String, ArrayList<Double>> getRegressionResults() {
		return regressionResults;
	}

	public void setRegressionResults(Map<String, ArrayList<Double>> regressionResults) {
		this.regressionResults = regressionResults;
	}

	public double getMajorityClassPercentage() {
		return majorityClassPercentage;
	}
	
	public void computeMajorityClassPercentage() {
		int bugCountTrue = 0; 
		int bugCountFalse = 0;
		int numberOfClasses = data.numInstances();
		int lastColumnNumber = data.numAttributes() - 1;
		int maxNumber = 0;
		double pZeroValue = 0;
		
		for(int i = 0; i < numberOfClasses; i++)
		{
			if(data.instance(i).toString(lastColumnNumber).equals("0"))
			{
				bugCountFalse++;
			}
			else
			{
				bugCountTrue++;
			}
		}
		
		
		if(bugCountFalse > bugCountTrue)
		{
			maxNumber = bugCountFalse;
		}
		else
		{
			maxNumber = bugCountTrue;
		}
		
		pZeroValue = ((double) maxNumber / (double) numberOfClasses) * 100; // * 100 da dobijemo postotak
		
		majorityClassPercentage = pZeroValue;
	}

	public String getResultText() {
		return resultText.toString();
	}
	
	public CrossValidate(Input inputData) 
	{
		input = inputData;
	}
	
	public void crossVal() throws Exception{
		
		System.out.println("getting data from the Input object");
		int iCount = 1;
		if (  input == null ){
			System.out.println("NULL input data object");
		} 
		do {
			data = input.getData();
			iCount ++;
		}
		while( ( iCount < 100 ) && ( data == null )  );
		
		if ( iCount == 100 ) {
			System.out.println("getting data from the Input object FAILED");
		}else {
			System.out.println("data for cross validation read");
			computeMajorityClassPercentage();
		}
		computerNumberOfElementsInEachCategory(data);
		Instances testData = data.testCV(10, 0);
		setTestDataSet(testData);
		double[] expectedResult = getExpectedResults( data);
		for ( int iAttribute = 1; iAttribute < data.numAttributes() - 1;  iAttribute++) {
			int[] indices = { iAttribute,  data.numAttributes() - 1};
			InfoGainAttributeEval eval = new InfoGainAttributeEval();
		    Ranker search = new Ranker();
			AttributeSelection attributeSelect = new AttributeSelection();
			attributeSelect.setEvaluator(eval);
			attributeSelect.setSearch(search);
			Remove removeFilter = new Remove();
			removeFilter.setAttributeIndicesArray(indices);
			removeFilter.setInvertSelection(true);
			removeFilter.setInputFormat(data);
			Instances newData = Filter.useFilter(data, removeFilter);
			DataSource source = new DataSource(newData);
			Instances dataset = source.getDataSet();
			dataset.setClassIndex(dataset.numAttributes()-1);
			
			int fold = 10;
			int seed = 1;
			
			Random rand = new Random(seed);
			Instances randData = new Instances(dataset);
			randData.randomize(rand);
			randData.stratify(fold);
			
			if (randData.classAttribute().isNominal())
			{
				randData.stratify(fold);
			}
			
			double averagecorrect = 0;
			ArrayList<Double> coeffResults = new ArrayList<Double>();
			double beta0 = 0;
			double beta1 = 0;
			int i = 0;
			Instances test = randData.testCV(10, i);
			for ( i = 1; i < fold; i++ )
			{
				System.out.println("-------starting " + i + "  cross validation-------");
				Instances train = randData.trainCV(fold, i);
				
				LogisticRegression logisticRegressionEngine = new LogisticRegression();
				logisticRegressionEngine.process(train, test, randData);
				beta0 += logisticRegressionEngine.getBeta0();
				beta1 += logisticRegressionEngine.getBeta1();
				resultText += logisticRegressionEngine.getOutputText() + "\n";
			}
			String finalText = "the average correction rate of "+fold+" cross validation: "+averagecorrect/fold;
			System.out.println(finalText);
			resultText += finalText;
			
			pValueCalculation pValCalc = new pValueCalculation();
			pValCalc.calculatepValue(dataset);
			double pval = pValCalc.getP_();
			long[] observedResult = getObservedResults(data, iAttribute, beta0, beta1);
			double errorMetric = getErrorPerMetric( observedResult, expectedResult);
			if ( errorMetric < ( 1 - majorityClassPercentage / 100 ) ) {
				beta0 = beta0/fold;
				beta1 = beta1/fold;
				coeffResults.add(beta0);
				coeffResults.add(beta1);
				coeffResults.add(pval);
				coeffResults.add(errorMetric);
				double varlThreshold = 0.0;
				if ( beta1 != 0 ) {
					varlThreshold = (1/beta1) * (Math.log((majorityClassPercentage/100)/(1-majorityClassPercentage/100)) - beta0);
				}
				coeffResults.add(varlThreshold);
				regressionResults.put(data.attribute(iAttribute).name(), coeffResults);	
			}
		}
	}
	
	public Instances getTestDataSet(Instances randData) {
		return randData.testCV(10,0);
	}
	
	private double getErrorPerMetric(long[] observedResult, double[] expectedResult) {
		double result = 0.0;
		
		for(int i = 0; i < observedResult.length; i++)
		{
			if(observedResult[i] != expectedResult[i])
			{
				result++;
			}
		}
		
		result /= observedResult.length;
		
		return result;
	}
	
	
	private double[] getExpectedResults(Instances dataset) {
		double[] result = new double[dataset.numInstances()];
		for ( int i = 0; i < dataset.numInstances(); i++) {
			result[i] = dataset.instance(i).value(dataset.numAttributes()-1);
			if (result[i] == 0) {
				result[i] = 1;
			}else result[i] = 2;
		}
		return result;
	}

	private long[] getObservedResults(Instances data, int iAttribute, double a, double b) { 
		System.out.println("iAttribute = " + iAttribute);
		long[] result = new long[data.numInstances()];
		for ( int i = 0; i < data.numInstances(); i++) {
			result[i] = (long) sigmoid(data.instance(i).value(iAttribute), a, b);
			if (result[i] == 0) {
				result[i] = 1;
			}else result[i] = 2;
		}
		return result;
	}
	
	private double sigmoid(double x, double a, double b) {
		return 1/(1+Math.exp(-(a*x+b)));
	}

	public Instances getTestDataSet() {
		return testDataSet;
	}

	public void setTestDataSet(Instances testDataSet) {
		this.testDataSet = testDataSet;
	}
}