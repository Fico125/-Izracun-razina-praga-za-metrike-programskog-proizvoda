package GUI;

import java.util.ArrayList;
import java.util.Map;

import weka.core.Instances;

public class DecisionTree {
	
	private Instances testDataSet;
	private Map<String, ArrayList<Double>> regressionResults;
	
	public DecisionTree(Instances testDataSet_, Map<String, ArrayList<Double>> regressionResults_ ){
		setTestDataSet(testDataSet_);
		setRegressionResults(regressionResults_);
	}
	
	public void setTestDataSet(Instances testDataSet) {
		this.testDataSet = testDataSet;
	}

	public void setRegressionResults(Map<String, ArrayList<Double>> regressionResults) {
		this.regressionResults = regressionResults;
	}
	
	public void computeDecisionTree() {
		
		
	}
	
}
