package GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import weka.core.Instance;
import weka.core.Instances;

public class DecisionTree {
	private Instances testDataSet;
	private Map<String, ArrayList<Double>> regressionResults;
	private Map<String, Double> decisionTreeResult;
	private Map<String, Double> thresholdPerMetric;
	
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
	
	public void computeDecisionTree() {
		Map<String, Double> unsortedDecisionTree = new TreeMap<String, Double>();
		for ( int iInstance = 0; iInstance < testDataSet.numInstances(); iInstance++) {
			double score = computeScore(testDataSet.instance(iInstance));
			String className = testDataSet.attribute(0).value(iInstance);
			unsortedDecisionTree.put(className , score) ;
		}
	    
		Map<String, Double> decisionTreeResult = sortByValue(unsortedDecisionTree);
		
		
		for (Map.Entry<String, Double> entry : decisionTreeResult.entrySet()) {
			System.out.println(entry.getKey() + " obtains a score of " + entry.getValue());
		}
		
	}
	
	
	private double computeScore(Instance instance) {
		double sum = 0.0;
		for (int i = 0; i < instance.numAttributes(); i++) {
			ArrayList<Double> arrayResult = this.regressionResults.get(instance.attribute(i).name());
			if ( arrayResult != null ) {
				double varl = arrayResult.get(4);
				if ( instance.value(i) > varl ) {
					double error = arrayResult.get(3);
					double confidenceWeight = 1 / (1 + error); //jer sto je error manji, to ce biti veca pouzdanost metrike 
				    sum += confidenceWeight;
				}
			}
		}
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
