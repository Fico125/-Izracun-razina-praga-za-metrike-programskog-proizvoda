package GUI;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class Input {
	
	private  Instances data;
	private  Instances new_data;

	
	public  void readFile(String filename, String filepath) throws Exception {

		String fullFilePath = filepath + "\\" + filename;
		BufferedReader reader = null;
  	  	try {
  	  		
  	  		if(fullFilePath.endsWith(".csv"))
  	  		{
  	  		CSVLoader loader = new CSVLoader();
	  			loader.setSource(new File(fullFilePath));
	  			data = loader.getDataSet();//get instances object
  	  		}
  	  		
  	  		else
  	  		{
  	  			reader = new BufferedReader(new FileReader(filepath + "/" + filename));
  	  			try {
  	  				data = new Instances(reader);
  	  			} catch (IOException e) {
  	  				e.printStackTrace();
  	  			}
  	  			try {
  	  				reader.close();	
  	  			} catch (IOException e) {
  	  				e.printStackTrace();
  	  			}
  	  		}
 
  	  	} catch (FileNotFoundException e) {
  	  		e.printStackTrace();
  	  	}
		
  	  	// setting class attribute
  	  	data.setClassIndex(data.numAttributes() - 1);
		//System.out.println(data);
  	  	new_data = FileHandler.numericToNominal(data);
  	  
      }
	

	public  Instances getData() {
		return new_data;
	}
	
}

