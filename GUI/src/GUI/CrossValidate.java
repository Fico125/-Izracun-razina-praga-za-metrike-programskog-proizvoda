package GUI;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class CrossValidate {
	
	private String resultText = "";
	private Input input;
	

	public String getResultText() {
		return resultText;
	}
	
	public CrossValidate(Input input_) 
	{
		input = input_;
	}
	
	public void CrossVal() throws Exception{
		System.out.println("getting data from the Input object");
		Instances data;
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
		}
		
		//System.out.println(data);
		
		DataSource source = new DataSource(data);
		Instances dataset = source.getDataSet();
		dataset.setClassIndex(dataset.numAttributes()-1);
		
		NaiveBayes nb = new NaiveBayes();
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
		
		/*for ( int n = 0; n < fold; n++ )
		{
			Evaluation eval = new Evaluation(randData);
			Instances train = randData.trainCV(fold, n);
			Instances test = randData.testCV(fold, n);
			
			nb.buildClassifier(train);
			eval.evaluateModel(nb, test);
			
			double correct = eval.pctCorrect();
			
			averagecorrect = averagecorrect + correct;
			String intermediateText = "the "+n+"th cross validation:"+eval.toSummaryString();
			System.out.println(intermediateText);
			resultText += intermediateText + "\n";
			LogisticRegression logisticRegressionEngine = new LogisticRegression();
			logisticRegressionEngine.process(train, test, randData);
			resultText += logisticRegressionEngine.getOutputText() + "\n";
		}*/
		for ( int i = 0; i < fold; i++ )
		{
			System.out.println("-------starting " + i + "  cross validation-------");
			Evaluation eval = new Evaluation(randData);
			Instances train = randData.trainCV(fold, i);
			Instances test = randData.testCV(fold, i);
			
			LogisticRegression logisticRegressionEngine = new LogisticRegression();
			logisticRegressionEngine.process(train, test, randData);
			resultText += logisticRegressionEngine.getOutputText() + "\n";
		}
		String finalText = "the average correction rate of "+fold+" cross validation: "+averagecorrect/fold;
		System.out.println(finalText);
		resultText += finalText;
	}
}