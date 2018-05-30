package GUI;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.gui.visualize.*;

public class GenerateROC {
	
	private Input input;
	
	public GenerateROC(Input input_)
	{
		input = input_;
	}
	
	public void DrawRocGraph() throws Exception {
		
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
		
		data.setClassIndex(data.numAttributes() - 1);
		
		// train classifier
	     Classifier cl = new NaiveBayes();
	     Evaluation eval = new Evaluation(data);
	     eval.crossValidateModel(cl, data, 10, new Random(1));
	 
	     // generate curve
	     ThresholdCurve tc = new ThresholdCurve();
	     int classIndex = 0;
	     Instances result = tc.getCurve(eval.predictions(), classIndex);
	 
	     // plot curve
	     ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
	     vmc.setROCString("(Area under ROC = " +
	         Utils.doubleToString(tc.getROCArea(result), 4) + ")");
	     vmc.setName(result.relationName());
	     PlotData2D tempd = new PlotData2D(result);
	     tempd.setPlotName(result.relationName());
	     tempd.addInstanceNumberAttribute();
	     // specify which points are connected
	     boolean[] cp = new boolean[result.numInstances()];
	     for (int n = 1; n < cp.length; n++)
	       cp[n] = true;
	     tempd.setConnectPoints(cp);
	     // add plot
	     vmc.addPlot(tempd);
	 
	     // display curve
	     String plotName = vmc.getName();
	     final javax.swing.JFrame jf =
	       new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
	     jf.setSize(500,400);
	     jf.getContentPane().setLayout(new BorderLayout());
	     jf.getContentPane().add(vmc, BorderLayout.CENTER);
	     jf.addWindowListener(new java.awt.event.WindowAdapter() {
	       public void windowClosing(java.awt.event.WindowEvent e) {
	       jf.dispose();
	       }
	     });
	     jf.setVisible(true);
	}
	
}
