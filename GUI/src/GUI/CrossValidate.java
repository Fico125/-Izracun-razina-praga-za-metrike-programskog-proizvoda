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
	
	private String resultText = "";
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
		int bug_cnt_true = 0, bug_cnt_false = 0;
		int numberOfClasses = data.numInstances();
		int lastColumnNumber = data.numAttributes() - 1;
		int maxNumber = 0;
		double pZeroValue = 0;
		//System.out.println("Broj klasa: " + numberOfClasses);
		
		for(int i = 0; i < numberOfClasses; i++)
		{
			if(data.instance(i).toString(lastColumnNumber).equals("0"))
			{
				bug_cnt_false++;
			}
			else
			{
				bug_cnt_true++;
			}
		}
		
		//System.out.println("Broj bugova: " + bug_cnt_true);
		//System.out.println("Broj ispravnih klasa: " + bug_cnt_false);
		
		if(bug_cnt_false > bug_cnt_true)
		{
			maxNumber = bug_cnt_false;
		}
		else
		{
			maxNumber = bug_cnt_true;
		}
		
		//System.out.println("maxNumber: " + maxNumber);
		//System.out.println("numberOfClasses: " + numberOfClasses);
		
		pZeroValue = ((double) maxNumber / (double) numberOfClasses) * 100; // * 100 da dobijemo postotak
		
		//System.out.println("p0 value = " + pZeroValue);
		majorityClassPercentage = pZeroValue;
	}

	public String getResultText() {
		return resultText;
	}
	
	public CrossValidate(Input input_) 
	{
		input = input_;
	}
	

	
	public void CrossVal() throws Exception{
		
		System.out.println("getting data from the Input object");
		int iCount = 1;
		if (  input == null ){
			System.out.println("NULL input data object");
		} 
		do {
			data = input.getData();
			iCount ++;
		}
		//while( (data == null) &&  (iCount < 100 ));
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
		//System.out.println(data);
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
			
			if (randData.classAttribute().isNominal())
			{
				randData.stratify(fold);
			}
			
			double averagecorrect = 0;
			ArrayList<Double> coeffResults = new ArrayList<Double>();;
			double beta0 = 0;
			double beta1 = 0;
			for ( int i = 0; i < fold; i++ )
			{
				System.out.println("-------starting " + i + "  cross validation-------");
				Evaluation evaluation = new Evaluation(randData);
				Instances train = randData.trainCV(fold, i);
				Instances test = randData.testCV(fold, i);
				
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
			
			if ( pval < 0.05 ) {
				beta0 = beta0/fold;
				beta1 = beta1/fold;
				coeffResults.add(beta0);
				coeffResults.add(beta1);
				coeffResults.add(pval);
				double varlThreshold = (1/beta1) * (Math.log((majorityClassPercentage/100)/(1-majorityClassPercentage/100)) - beta0);
				coeffResults.add(varlThreshold);
				regressionResults.put(data.attribute(iAttribute).name(), coeffResults);
			}
			
			//double pval = pValCalc.calculatepValueBasedOnChiSquare(getExpectedResults(data), getObservedResults(data, iAttribute,  beta0, beta1));
			//double pval = pValCalc.calculatepValueBasedOnChiSquare(getExpectedResultsOnesAndZeros(data), getObservedResultsOnesAndZeros(data, iAttribute,  beta0, beta1));
			//coeffResults.add(pval);
		}
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

	private long[] getObservedResults(Instances dataset, int iAttribute, double a, double b) {
		long[] result = new long[dataset.numInstances()];
		long sum = 0;
		for ( int i = 0; i < dataset.numInstances(); i++) {
			result[i] = (long) sigmoid(dataset.instance(i).value(iAttribute), a, b);
			if (result[i] == 0) {
				result[i] = 1;
			}else result[i] = 2;
		}
		return result;
	}
	
	private long[] getObservedResultsOnesAndZeros(Instances dataset, int iAttribute, double a, double b) {
		long[] result = new long[2];
		long sumOnes = 0;
		for ( int i = 0; i < dataset.numInstances(); i++) {
			long modelResult = (long) sigmoid(dataset.instance(i).value(iAttribute), a, b);
			if (modelResult == 1) {
				sumOnes++;
			}
		}
		result[0] = dataset.numInstances() - sumOnes;
		result[1] = sumOnes;
		return result;
	}
	
	private double[] getExpectedResultsOnesAndZeros(Instances dataset) {
		double[] result = new double[2];
		result[1] = totalNumberOfOne;
		result[0] = dataset.numInstances() - totalNumberOfOne;
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