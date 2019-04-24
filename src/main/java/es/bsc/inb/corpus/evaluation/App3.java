package es.bsc.inb.corpus.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;

/**
 * 
 * Generation of gate Corpus from  ADEs corpus
 */
public class App3 {
	
	static final Logger log = Logger.getLogger("log");
	
	
	/**
	 * 
	 * @param args
	 */
    public static void main( String[] args ) {
    	String inputDirectoryPath = "corpus/corpus_3/finding_dose_corpus/original_corpus/";
    	String outputDirectoryPath = "corpus/corpus_3/finding_dose_corpus/corpus_gate/";
    	
    	if (!java.nio.file.Files.isDirectory(Paths.get(inputDirectoryPath))) {
    		log.error("Please set the inputDirectoryPath ");
			System.exit(1);
    	}
    	
    	File outputDirectory = new File(outputDirectoryPath);
	    if(!outputDirectory.exists())
	    	outputDirectory.mkdirs();
    	
    	try {
			Gate.init();
		} catch (GateException e) {
			log.error("Wrapper::generatePlainText :: Gate Exception  ", e);
			System.exit(1);
		}
    	
    	System.out.println("Wrapper::main :: INIT ");
    	if (java.nio.file.Files.isDirectory(Paths.get(inputDirectoryPath))) {
			File inputDirectory = new File(inputDirectoryPath);
			File[] files =  inputDirectory.listFiles();
			for (File file : files) {
				if(file.getName().endsWith(".txt")){
					System.out.println("Wrapper::processTagger :: processing file : " + file.getAbsolutePath());
					File outputGATEFile = new File (outputDirectoryPath +  File.separator + file.getName().replaceAll(".txt", ".xml"));
					File annotatedFile = new File (file.getAbsolutePath().replaceAll(".txt", ".ann"));
					process(file, annotatedFile , outputGATEFile); 
				}
			}
		}else {
			System.out.println("No directory :  " + inputDirectoryPath);
		}
    	System.out.println("Wrapper::main :: END ");
    }
    
    /**
     * 
     * @param file
     * @param outputGATEFile
     */
	private static void process(File file,File annotated , File outputGATEFile) {
		try {
			gate.Document gateDocument = Factory.newDocument(file.toURI().toURL(), "UTF-8");
			BufferedReader br = new BufferedReader(new FileReader(annotated));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] data = line.split("\t");
		    	String id_annotation = data[0];
		    	if(id_annotation.startsWith("T")) {
		    		FeatureMap features = gate.Factory.newFeatureMap();
		    		String label = data[1];
		    		String[] label_data = label.split(" ");
		    		String type = label_data[0];
		    		features.put("original_label", type);
		    		if(type.equals("Duration")) {
		    			type="DOSE_DURATION";
		    		}else if(type.equals("Dose") | type.equals("Strength")) {
		    			type="DOSE_QUANTITY";
		    		}else if(type.equals("Frequency")) {
		    			type="DOSE_FREQUENCY";
		    		}else if(type.equals("Route")) {
		    			type="ROUTE_OF_ADMINISTRATION";
		    		}else if(type.equals("Reason")) {
		    			type="FINDING";
		    		}
		    		String text = data[2];
		    		features.put("text", text);
		    		try {
		    			gateDocument.getAnnotations("EVALUATION").add(new Long(label_data[1]), new Long(label_data[2]), type, features);
		    		} catch (NumberFormatException e) {
		    			//e.printStackTrace();
		    		}
		    	}
		    }
			java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputGATEFile, false)));
			out.write(gateDocument.toXml());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
