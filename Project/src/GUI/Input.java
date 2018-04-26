package GUI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import weka.core.Instances;

public class Input {
	
	private static Instances data = null;
	
	public static void readFile() {
		JFileChooser chooser=new JFileChooser();
  	  
  	  chooser.showSaveDialog(null);

  	  String filepath = chooser.getSelectedFile().getAbsolutePath();
  	  String filename = chooser.getSelectedFile().toString();
  	  BufferedReader reader = null;
		try {
			reader = new BufferedReader(
			          new FileReader(filepath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  
		
		try {
			data = new Instances(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  // setting class attribute
  	  data.setClassIndex(data.numAttributes() - 1);
		//System.out.println(data);

      }
	

	public static Instances getData() {
		return data;
	}
}

