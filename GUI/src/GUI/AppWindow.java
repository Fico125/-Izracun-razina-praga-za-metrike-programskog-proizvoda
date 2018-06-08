package GUI;

/*
 * Importi �e vam najvjerovatnije davati errore, kliknite link
 * i skinite ovo �ta �e vam do�
 * http://www.eclipse.org/downloads/download.php?file=/eclipse/downloads/drops4/R-4.3-201306052000/swt-4.3-win32-win32-x86_64.zip
 * 
 * onda slijedite ove upute
 * i samo kad vas pita da u�itate fajl odabarite onaj jar iz tog zipa �ta ste skinuli
 * http://www.mkyong.com/swt/how-to-import-swt-library-into-eclipse-workspace/
 * 
 * */

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.io.IOException;
import org.eclipse.swt.widgets.Text;


import GUI.GenerateROC;
import GUI.org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.Point; 

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import java.awt.Frame;
import javax.swing.JRootPane;

public class AppWindow {

	private Ksustav ks;
	protected Shell shlZdravapp;
	private Text textCalculation;
	private Button btnClose;

	/**
	 * Launch the application.
	 * @param args
	 */
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
		shlZdravapp.setSize(1181, 605);
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
		textCalculation.setBounds(177, 10, 430, 540);
		
		btnClose = new Button(shlZdravapp, SWT.NONE);
		btnClose.setBounds(0, 101, 134, 28);
		btnClose.setText("Zatvori");
		
		btnGenerateROC = new Button(shlZdravapp, SWT.NONE);
		btnGenerateROC.setBounds(0, 135, 134, 28);
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
					DecisionTree decisionTree = new DecisionTree( learningEngine.getTestDataSet(), learningEngine.getRegressionResults());
					decisionTree.computeDecisionTree();
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
		
		Composite composite = new Composite(shlZdravapp, SWT.EMBEDDED);
		composite.setBounds(613, 9, 558, 541);
		
		Frame frame = SWT_AWT.new_Frame(composite);
		ks = new Ksustav(40, 450);
		frame.add(ks);
		
		JRootPane rootPane = new JRootPane();
		ks.add(rootPane);

	    
	}
}