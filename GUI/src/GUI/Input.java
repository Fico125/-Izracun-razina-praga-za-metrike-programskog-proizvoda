package GUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class Input {
	
	private static Instances data;
	private static Instances new_data;

	
	public static void readFile() throws Exception {
		
		JFileChooser chooser=new JFileChooser();
		chooser.showSaveDialog(null);

  	  	String filepath = chooser.getSelectedFile().getAbsolutePath();
  	  	String filename = chooser.getSelectedFile().toString();
  	  
  	  	BufferedReader reader = null;
  	  	try {
  	  		
  	  		if(filename.endsWith(".csv")) 
  	  		{
  	  			filepath = changeCSV_to_ARFF(filename);
  	  		}
  	  		
  	  		reader = new BufferedReader(new FileReader(filepath));
  	  		
  	  	} catch (FileNotFoundException e) {
  	  		e.printStackTrace();
  	  	}
		
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
  	  	
  	  	// setting class attribute
  	  	data.setClassIndex(data.numAttributes() - 1);
		//System.out.println(data);
  	  	new_data = FileHandler.numericToNominal(data);
  	  
  	  
      }
	

	public static Instances getData() {
		return new_data;
	}
	
	public static String changeCSV_to_ARFF(String path) throws IOException{
		 
		//AKO ZAVRSAVA NA ARFF IZAÄ�I
		if(path.endsWith(".arff")){
		    return path;
		}
	            
		//OVO JE PRETVARANJE CSV U ARFF
		// load CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(path));
		Instances data = loader.getDataSet();//get instances object
		
		//promjeni ime stinga u arff
		String newPath = path.replace(".csv", ".arff");
		
		// save ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);//set the dataset we want to convert
		//and save as ARFF
		saver.setFile(new File(newPath));
		saver.writeBatch();
		
		return newPath;
	}
	
}

