package es.bsc.inb.corpus.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser.Feature;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;

/**
 * 
 * Generation of gate Corpus from  ADEs corpus
 */
public class App2 {
	
	static final Logger log = Logger.getLogger("log");
	
	
	/**
	 * 
	 * @param args
	 */
    public static void main( String[] args ) {
    	String inputFile = "corpus/corpus_2/ADE-Corpus-V2/DRUG-AE.rel";
    	String outputDirectoryPath = "corpus/corpus_2/corpus_gate/";
    	
    	if (!java.nio.file.Files.isRegularFile(Paths.get(inputFile))) {
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
    	
    	System.out.println("Wrapper::processTagger :: processing file : " + inputFile);
		File annotatedFile = new File (inputFile);
		File outputGATEFile = new File (outputDirectoryPath +  File.separator + annotatedFile.getName().replaceAll(".rel", ".xml"));
		process(annotatedFile, outputDirectoryPath, outputGATEFile); 
		
    	System.out.println("Wrapper::main :: END ");
    }
    
    /**
     * 
     * @param file
     * @param outputGATEFile
     */
	private static void process(File annotated, String outputPath, File outputGATEFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(annotated));
		    String line;
		    Integer i = 0;
		    while ((line = br.readLine()) != null) {
		    	String[] data = line.split(Pattern.quote("|"));
		    	String pmid = data[0];
		    	String sentence = pmid + "|" +data[1];
		    	gate.Document gateDocument = Factory.newDocument(sentence);
		    	String ade = data[2];
		    	String start_ade = data[3];
		    	String end_ade= data[4];
		    	String disease = data[5];
		    	String start_disease = data[6];
		    	String end_disease = data[7];
		    	FeatureMap features = gate.Factory.newFeatureMap();
		    	features.put("text",ade);
		    	FeatureMap features2 = gate.Factory.newFeatureMap();
		    	features2.put("text",disease);
		    	i++;
		    	try {
		    		gateDocument.getAnnotations("EVALUATION").add(new Long(start_ade), new Long(end_ade), "FINDING", features);
		    		gateDocument.getAnnotations("EVALUATION").add(new Long(start_disease), new Long(end_disease), "DISEASE", features2);
		    	} catch (Exception e) {
		    		System.out.println(sentence);
		    		e.printStackTrace();
		    	}
		    	java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputPath +  File.separator + pmid + "_" +i, false)));
				out.write(gateDocument.toXml());
				out.close();
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
