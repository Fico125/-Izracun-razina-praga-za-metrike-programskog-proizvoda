package GUI;


import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import weka.core.Instance;
import weka.core.Instances;
import static java.util.Collections.reverseOrder;


public class DecisionTree {
	private Instances testDataSet;
	private Map<String, ArrayList<Double>> regressionResults;
	private Map<String, Double> thresholdPerMetric;
	private Map<String, String> thresholdBreachPerClass;
	private String breaches;
	private String resultText = "";
	
	public DecisionTree(Instances testDataSet_, Map<String, ArrayList<Double>> regressionResults_ ){
		setTestDataSet(testDataSet_);
		setRegressionResults(regressionResults_);
	}
	
	public Map<String, Double> getThresholdPerMetric() {
		return thresholdPerMetric;
	}


	public void setTestDataSet(Instances testDataSet) {
		this.testDataSet = testDataSet;
	}

	public void setRegressionResults(Map<String, ArrayList<Double>> regressionResults) {
		this.regressionResults = regressionResults;
	}
	
	public String getResultText() {
		return resultText.toString();
	}
	
	public void computeDecisionTree() {
		Map<String, Double> decisionTree = new LinkedHashMap<String, Double>();
		thresholdBreachPerClass = new LinkedHashMap<String, String>();
		for ( int iInstance = 0; iInstance < testDataSet.numInstances(); iInstance++) {
			double score = computeScore(testDataSet.instance(iInstance));
			String className = testDataSet.attribute(0).value(iInstance);
			thresholdBreachPerClass.put(className, breaches);
			decisionTree.put(className , score) ;
		}
	    
		decisionTree.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach( 
				e -> displayResult( e.getKey(), e.getValue()));
		
	}
	
	private void displayResult( String key, double value ) {
		System.out.println( key + " obtains a score of " + value + " " + thresholdBreachPerClass.get(key));
		resultText += key + " obtains a score of " + value + " " + thresholdBreachPerClass.get(key) + "\n";
	}
	
	private double computeScore(Instance instance) {
		double sum = 0.0;
		breaches = ":";
		for (int i = 0; i < instance.numAttributes(); i++) {
			ArrayList<Double> arrayResult = this.regressionResults.get(instance.attribute(i).name());
			if ( arrayResult != null ) {
				double varl = arrayResult.get(4);
				if ( instance.value(i) > varl ) {
					double error = arrayResult.get(3);
					double confidenceWeight = 1 / (1 + error); //jer sto je error manji, to ce biti veca pouzdanost metrike 
				    sum += confidenceWeight;
				    breaches += instance.attribute(i).name() + ",";
				}
			}
		}
		breaches = breaches.substring(0, breaches.length() - 1);
		return round(sum,4);
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
