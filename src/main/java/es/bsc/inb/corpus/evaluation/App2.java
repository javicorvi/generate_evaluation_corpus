package es.bsc.inb.corpus.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

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
    	String inputFilePath = "corpus/corpus_2/ADE-Corpus-V2/DRUG-AE.rel";
    	String inputFilePath2 = "corpus/corpus_2/ADE-Corpus-V2/DRUG-DOSE.rel";
    	
    	String abstractsPath = "corpus/corpus_2/ADE-Corpus-V2/abstracts/";
    	
    	String outputDirectoryPath = "corpus/corpus_2/corpus_gate/";
    	
    	File outputDirectory = new File(outputDirectoryPath);
	    if(!outputDirectory.exists())
	    	outputDirectory.mkdirs();
    	
    	try {
			Gate.init();
		} catch (GateException e) {
			log.error("Wrapper::generatePlainText :: Gate Exception  ", e);
			System.exit(1);
		}
    	
    	System.out.println("App1::main :: INIT ");
    	if (java.nio.file.Files.isRegularFile(Paths.get(inputFilePath))) {
			File inputFile = new File(inputFilePath);
			File inputFile2 = new File(inputFilePath2);
			//process(inputFile, outputDirectoryPath, abstractsPath); 
			processDoses(inputFile2, outputDirectoryPath); 
		}else {
			System.out.println("No File :  " + inputFilePath);
			System.exit(1);
		}
    	System.out.println("App1::main :: END ");
    }
    
    
    /**
     * 
     * @param file
     * @param outputGATEFile
     */
	private static void process(File file,String outputDirectoryPath, String abstractsPath) {
		try {
			//Read the input file, and get sentences 
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			gate.Document gateDocument = null;
			String aux_pmid = "";
			while ((line = br.readLine()) != null) {
		    	String[] data = line.split("\\|");
		    	if(!data[0].equals("313865")){
		    		if(!aux_pmid.equals(data[0])) {
			    		if(!aux_pmid.equals("")) {
			    			java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputDirectoryPath +  File.separator + aux_pmid + ".xml" , false)));
							out.write(gateDocument.toXml());
							out.close();
			    		}
			    		File plain = new File(abstractsPath+data[0]+".txt");
			    		gateDocument = Factory.newDocument(plain.toURI().toURL(), "UTF-8");
			    	}
			    	FeatureMap features = gate.Factory.newFeatureMap();
			    	features.put("text",data[2]);
			    	features.put("original_label", "ADVERSE");
			    	gateDocument.getAnnotations("EVALUATION").add(new Long(data[3]), new Long(data[4]), "FINDING", features);
			    	aux_pmid = data[0];
		    	}
		    }
			java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputDirectoryPath +  File.separator + aux_pmid + ".xml" , false)));
			out.write(gateDocument.toXml());
			out.close();
			
		} catch (Exception e) {
			System.out.println("Error reading file : " + file );
			e.printStackTrace();
		}
	}
	
	/**
     * 
     * @param file
     * @param outputGATEFile
     */
	private static void processDoses(File file,String outputDirectoryPath) {
		try {
			//Read the input file, and get sentences 
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			
			gate.Document gateDocument = null;
			String aux_pmid = "";
			while ((line = br.readLine()) != null) {
		    	String[] data = line.split("\\|");
		    	if(!data[0].equals("313865")){
		    		if(!aux_pmid.equals(data[0])) {
			    		if(!aux_pmid.equals("")) {
			    			java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputDirectoryPath +  File.separator + aux_pmid + ".xml" , false)));
							out.write(gateDocument.toXml());
							out.close();
			    		}
			    		File xml = new File(outputDirectoryPath+data[0]+".xml");
			    		gateDocument = Factory.newDocument(xml.toURI().toURL(), "UTF-8");
			    	}
			    	FeatureMap features = gate.Factory.newFeatureMap();
			    	features.put("text",data[2]);
			    	features.put("original_label", "DOSE");
			    	gateDocument.getAnnotations("EVALUATION").add(new Long(data[3]), new Long(data[4]), "DOSE_QUANTITY", features);
			    	aux_pmid = data[0];
		    	}
		    }
			java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputDirectoryPath +  File.separator + aux_pmid + ".xml" , false)));
			out.write(gateDocument.toXml());
			out.close();
			
		} catch (Exception e) {
			System.out.println("Error reading file : " + file );
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param args
	 */
    public static void main_print_pmids( String[] args ) {
    	String inputFilePath = "corpus/corpus_2/ADE-Corpus-V2/DRUG-AE.rel";
    	String inputFilePath2 = "corpus/corpus_2/ADE-Corpus-V2/DRUG-DOSE.rel";
    	String outputDirectoryPath = "corpus/corpus_2/ADE-Corpus-V2/abstracts";
    	
    	if (!java.nio.file.Files.isRegularFile(Paths.get(inputFilePath))) {
    		log.error("Please set the inputDirectoryPath ");
			System.exit(1);
    	}
    	
    	File outputDirectory = new File(outputDirectoryPath);
	    if(!outputDirectory.exists())
	    	outputDirectory.mkdirs();
    	System.out.println("App2::main_print_pmids :: processing file : " + inputFilePath);
    	File inputFile = new File(inputFilePath); 
    	File inputFile2 = new File(inputFilePath2); 
    	print_pmids(inputFile, inputFile2); 
		System.out.println("App2::main_print_pmids :: END ");
    }
    
    /**
     * 
     * @param file
     * @param outputGATEFile
     */
	private static void print_pmids(File annotated, File annotated2) {
		try {
			StringJoiner sj = new StringJoiner(",");
			
			BufferedReader br = new BufferedReader(new FileReader(annotated));
		    String line;
		    Set<String> set = new HashSet<String>();
		    while ((line = br.readLine()) != null) {
		    	String[] data = line.split(Pattern.quote("|"));
		    	String pmid = data[0];
		    	set.add(pmid);
		    }
		    BufferedReader br2 = new BufferedReader(new FileReader(annotated2));
		    String line2;
		    
		    while ((line2 = br2.readLine()) != null) {
		    	String[] data = line2.split(Pattern.quote("|"));
		    	String pmid = data[0];
		    	set.add(pmid);
		    }
		    
		    for (String object : set) {
		    	sj.add(object);
			}
		    System.out.println(sj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
