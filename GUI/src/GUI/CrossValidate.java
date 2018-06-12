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

/*
 * Cilj klase jest izraditi cross validaciju i koristi klasu LogisticRegression
 * koja obuhvaæa model logistièke regresije koji je potreban zbog klasifikacije.
 * */

public class CrossValidate {
	
	private String resultText = "";
	private Input input;
	private double majorityClassPercentage;
	private Instances data;
	private Instances testDataSet;
	// regressionResults je struktura podataka koja sadrži rezultate cross validacije
	private Map<String, ArrayList<Double>> regressionResults = new HashMap<String, ArrayList<Double>>();
	
	public void printRegressionResults() {
		int j = 0;
		for ( String metric : regressionResults.keySet()) {
			
			j++;
			ArrayList<Double> coefficients = regressionResults.get(metric);
			System.out.print(j + " : " + metric + " ");
			for (int i = 0; i < coefficients.size() ; i++) {
				System.out.print(coefficients.get(i) + "  ||  ");
			}
			System.out.println("");
			System.out.println("==============================");
		}
	}
	
	public Instances getTestDataSet(Instances randData) {
		return randData.testCV(10,0);
	}
	
	public Instances getTestDataSet() {
		return testDataSet;
	}

	public void setTestDataSet(Instances testDataSet) {
		this.testDataSet = testDataSet;
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

	public String getResultText() {
		return resultText.toString();
	}
	
	/* Metoda za izraèun p(0), odnosno postotka veæinske klase(0,1) koja je potrebna
	 * za izraèun VARL (Bender) metode */
	public void computeMajorityClassPercentage(Instances data) {
		int bugCountTrue = 0; 
		int bugCountFalse = 0;
		int numberOfClasses = data.numInstances();
		int lastColumnNumber = data.numAttributes() - 1;
		int maxNumber = 0;
		double pZeroValue = 0;
		
		for(int i = 0; i < numberOfClasses; i++)
		{
			if(data.instance(i).toString(lastColumnNumber).equals("0")) {
				bugCountFalse++;
			}
			else {
				bugCountTrue++;
			}
		}
		
		
		if(bugCountFalse > bugCountTrue) {
			maxNumber = bugCountFalse;
		}
		else {
			maxNumber = bugCountTrue;
		}
		
		pZeroValue = ((double) maxNumber / (double) numberOfClasses) * 100; // * 100 da dobijemo postotak
		
		majorityClassPercentage = pZeroValue;
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
			computeMajorityClassPercentage(data);
		}
		
		Instances testData = data.testCV(10, 0); //ovdje uzimamo prvih 240 klasa za kasnije izracunavati njihov score
		setTestDataSet(testData);
		System.out.println("Total number of attributes : " + data.numAttributes() );
		String rejectedMetrics = "List of rejected metrics :";
		
		/* Sadržaj for petlje radi sljedeæe:
		 * za svaku metriku, odnosno attribute vrši cross validaciju */
		for ( int iAttribute = 1; iAttribute < data.numAttributes() - 1;  iAttribute++) {
			
			System.out.println("validation of attribute : " + data.attribute(iAttribute).name() ); 
			/* Pocinjemo prvo odvajati od pocetnog seta podataka metriku koja nas zanima (attribute)
			 * tako da imamo dataset koji ima samo 2 stupca : metrika i bug_cnt*/
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
			
			/* Priprema za cross-validaciju : process ce uciti na manjim skupinama podataka (instances
			 * tako da ce od pocetnih 2400 podataka uzeti stalno 240 (= 2400/fold) za test, 2400 - 240 za ucenje */
			int fold = 10;
			int seed = 1;
			
			/* pomjesamo podatke putem niz slucajnih brojeva */
			Random rand = new Random(seed);
			Instances randData = new Instances(dataset);
			randData.randomize(rand);
			randData.stratify(fold);
			
			if (randData.classAttribute().isNominal()) {
				randData.stratify(fold);
			}
			
			//coeffResults sadrži vrijednosti beta0, beta1, pval, errorMetric, varlThreshold
			ArrayList<Double> coeffResults = new ArrayList<Double>();
			double beta0 = 0;
			double beta1 = 0;
			int i = 0;
			/* averageCorrect raèuna koliko je puta u svakoj cross validaciji za dan dataset
			 * od 10% podataka model toèno procijenio da li æe u klasi biti bug ili ne*/
			double averageCorrect = 0.0;
			
			for ( i = 0; i < fold; i++ )
			{
				/* Weka uzima 10 puta razliciti skup test podatka od 240 instanci, i na takav
				 * nacin ce train dataset i test dataset biti razliciti, èime se smanjuje
				 * rizik vezan za overfitting */
				System.out.println("-------starting " + i + "  cross validation-------");
				resultText += "----------------- starting " + i + "  cross validation-----------------\n";
				Instances train = randData.trainCV(fold, i); // 2400 - 240 podataka = 90%
				Instances test = randData.testCV(fold, i); // 240 podataka = 10% 
				
				Evaluation evaluation = new Evaluation(randData);
				
				LogisticRegression logisticRegressionEngine = new LogisticRegression();
				logisticRegressionEngine.process(train, test, randData); // model uèi s danim podacima
				
				evaluation.evaluateModel(logisticRegressionEngine.getClassifier(), test);
				double correct = evaluation.correct();
				double incorrect = evaluation.incorrect();
				/* Izracun greske tokom ucenja preko i-tog folda, gdje se Weka bazira na 
				 * koliko je tocan model bio tokom primjene na test podacima  */
				averageCorrect += correct / ( incorrect + correct );
				/* beta0 (intercept) i beta1 su koeficijenti u sigmoidi u logistièkoj regresiji */
				beta0 += logisticRegressionEngine.getBeta0();
				beta1 += logisticRegressionEngine.getBeta1();

				resultText += logisticRegressionEngine.getOutputText() + "\n";
			}
			averageCorrect /= fold;
			String finalText = "the average correction rate of "+fold+" cross validation: " + averageCorrect;
			System.out.println(finalText);
			resultText += finalText;
			
			pValueCalculation pValCalc = new pValueCalculation();
			pValCalc.calculatepValue(dataset);
			double pval = pValCalc.getP_();
			/* greska metrike je 100 posto manje prosjek postotka tocnih klasifikacija u test podacima 
			   kojih smo dobili prije tokom ucenja */
			double errorMetric = 1 - averageCorrect; 
			double minorityClassPercentage = 1 - majorityClassPercentage / 100;
			
			if ( errorMetric < minorityClassPercentage ) { 
				/* za svaku metriku uzimamo najnizi postotak od 1 ili 0 u bug_cnt,
				 * na ovakav nacin filtiramo sve pre-optimisticne i pre-pesimisticne metrike
				 * koje bi dale za bilo koju klasu stalno 0 ili 1 bez obzira na vrijednost metrike!*/
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
			} else rejectedMetrics += data.attribute(iAttribute).name() + "(" + errorMetric + "),";
		}
		System.out.println(rejectedMetrics); //ispis metrika koje su dale los rezultat u klasifikaciji
	}

}