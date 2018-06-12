package GUI;

/*
 * Importi ce davati errore, kliknite link ispod i skinite 
 * http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops4/R-4.3-201306052000/swt-4.3-win32-win32-x86_64.zip
 * 
 * onda slijedite ove upute
 * i samo kad vas pita da ucitate fajl odabarite onaj jar iz tog zipa koji ste skinuli
 * http://www.mkyong.com/swt/how-to-import-swt-library-into-eclipse-workspace/
 * */

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Text;


import GUI.GenerateROC;
import GUI.org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.Point; 

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import javax.swing.JRootPane;

public class AppWindow {

	protected Shell shlZdravapp;
	private Text textCalculation;
	private Text scoringText;
	private Button btnClose;
	private Table tableModelResults;

	public static void main(String[] args) throws IOException {
		try {
			AppWindow window = new AppWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlZdravapp.open();
		shlZdravapp.layout();
		while (!shlZdravapp.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlZdravapp = new Shell();
		shlZdravapp.setMinimumSize(new Point(90, 22));
		shlZdravapp.setSize(1500, 898);
		shlZdravapp.setText("Zdravapp");
		Input input = new Input();
		Button ucitaj = new Button(shlZdravapp, SWT.NONE);
		Button btnDatotekaSpremna = new Button(shlZdravapp, SWT.CHECK);
		Button btnGenerateROC = new Button(shlZdravapp, SWT.NONE);
		btnDatotekaSpremna.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 9, SWT.NORMAL));
		btnDatotekaSpremna.setBounds(10, 43, 124, 18);
		btnDatotekaSpremna.setText("Datoteka spremna");
		btnDatotekaSpremna.setEnabled(false);
		
		ucitaj.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					
					FileDialog fileDialog = new FileDialog(shlZdravapp, SWT.MULTI);
					String firstFile = fileDialog.open();
					String fileName = fileDialog.getFileName();
					String filePath = fileDialog.getFilterPath();
					input.readFile(fileName, filePath);
					btnDatotekaSpremna.setSelection(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		ucitaj.setBounds(0, 9, 134, 28);
		ucitaj.setText("Dodaj datoteku");
		
		Button nacrtaj = new Button(shlZdravapp, SWT.NONE);
		
		nacrtaj.setBounds(0, 67, 134, 28);
		nacrtaj.setText("Izracunaj");
		
		textCalculation = new Text(shlZdravapp, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		textCalculation.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		textCalculation.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		textCalculation.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		textCalculation.setBounds(177, 10,500, 540);
		
		scoringText = new Text(shlZdravapp, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		scoringText.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scoringText.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		scoringText.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		scoringText.setBounds(677, 10, 900, 820);
		
		
		tableModelResults = new Table(shlZdravapp, SWT.BORDER | SWT.FULL_SELECTION);
		tableModelResults.setBounds(177, 600, 500, 230);
		tableModelResults.setHeaderVisible(true);
		tableModelResults.setLinesVisible(true);
		
		btnClose = new Button(shlZdravapp, SWT.NONE);
		btnClose.setBounds(0, 135, 134, 28);
		btnClose.setText("Zatvori");
		
		btnGenerateROC = new Button(shlZdravapp, SWT.NONE);
		btnGenerateROC.setBounds(0, 101, 134, 28);
		btnGenerateROC.setText("Nacrtaj ROC graf");
		
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					System.exit(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		nacrtaj.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			 if ( input != null ) {
				CrossValidate learningEngine = new CrossValidate(input);
				try {
					learningEngine.crossVal();
					learningEngine.printRegressionResults();
					textCalculation.setText(learningEngine.getResultText());
					
					Map<String, ArrayList<Double>> regressionResultsFromCrossValidation = learningEngine.getRegressionResults();
					
					tableModelResults.setBackground( SWTResourceManager.getColor(SWT.COLOR_WHITE) );
					TableColumn tc1 = new TableColumn(tableModelResults, SWT.CENTER);
				    TableColumn tc2 = new TableColumn(tableModelResults, SWT.CENTER);
				    TableColumn tc3 = new TableColumn(tableModelResults, SWT.CENTER);
				    TableColumn tc4 = new TableColumn(tableModelResults, SWT.CENTER);
				    TableColumn tc5 = new TableColumn(tableModelResults, SWT.CENTER);
				    TableColumn tc6 = new TableColumn(tableModelResults, SWT.CENTER);
				    tc1.setText("Name");
				    tc2.setText("Beta 0");
				    tc3.setText("Beta 1");
				    tc4.setText("p-value");
				    tc5.setText("Error");
				    tc6.setText("VARL");
				    tc1.setWidth(83);
				    tc2.setWidth(83);
				    tc3.setWidth(83);
				    tc4.setWidth(83);
				    tc5.setWidth(83);
				    tc6.setWidth(85);
				    tableModelResults.setHeaderVisible(true);
					
				    int iTableRow = 0;
				    for (Entry<String, ArrayList<Double>> entry : regressionResultsFromCrossValidation.entrySet()) {
				    	TableItem item = new TableItem(tableModelResults, iTableRow);
				    	String[] rowText = new String[6];
				    	rowText[0] = entry.getKey();
				    	for ( int i = 1; i < entry.getValue().size(); i++) {
				    		rowText[i] = entry.getValue().get(i-1).toString();
				    	}
				    	rowText[5] = entry.getValue().get(4).toString();
				    	item.setText(rowText);
				    	iTableRow++;
				    }
				    
					DecisionTree decisionTree = new DecisionTree( learningEngine.getTestDataSet(), learningEngine.getRegressionResults());
					decisionTree.computeDecisionTree();
					scoringText.setText(decisionTree.getResultText());
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			 }
			}
			
		});
		
		btnGenerateROC.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(input != null) {
					GenerateROC plotGraph = new GenerateROC(input);
					try {
						plotGraph.DrawRocGraph();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});   
	}
}