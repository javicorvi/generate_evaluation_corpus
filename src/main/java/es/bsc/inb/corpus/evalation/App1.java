package es.bsc.inb.corpus.evalation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;

/**
 * 
 * Generation of gate Corpus from  ADEs corpus. Corpus 1.
 * 
 */
public class App1 {
	
	static final Logger log = Logger.getLogger("log");
	
	
	/**
	 * 
	 * @param args
	 */
    public static void main( String[] args ) {
    	String inputDirectoryPath = "corpus/corpus_1/Disease-ae-corpus.iob";
    	String outputDirectoryPath = "corpus/corpus_1/corpus_gate/";
    	
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
    	if (java.nio.file.Files.isRegularFile(Paths.get(inputDirectoryPath))) {
			File inputFile = new File(inputDirectoryPath);
			process(inputFile, outputDirectoryPath); 
		}else {
			System.out.println("No File :  " + inputDirectoryPath);
			System.exit(1);
		}
    	System.out.println("App1::main :: END ");
    }
    
    /**
     * 
     * @param file
     * @param outputGATEFile
     */
	private static void process(File file,String outputDirectoryPath) {
		try {
			//Read the input file, and get sentences 
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String pmid="";
			StringBuffer abstract_txt= null;
			Map<Integer, AnnotatedText> labels = null;
		    while ((line = br.readLine()) != null) {
		    	if(line.startsWith("###")) {
		    		pmid=line.substring(4);
		    		abstract_txt= new StringBuffer();
		    		labels = new HashMap<Integer, AnnotatedText>();
		    	}else if(line.equals("")){
		    		gate.Document gateDocument = Factory.newDocument(abstract_txt.toString());
		    		for (Integer i : labels.keySet()) {
		    			AnnotatedText annotatedText = labels.get(i);
		    			FeatureMap features = gate.Factory.newFeatureMap();
				    	features.put("text",annotatedText.getText());
				    	features.put("original_label", annotatedText.getLabel());
				    	String label = "";
				    	if(annotatedText.getLabel().equals("DISEASE")) {
				    		label="FINDING";
				    	}else if(annotatedText.getLabel().equals("ADVERSE")) {
				    		label="FINDING";
				    	}
		    			gateDocument.getAnnotations("EVALUATION").add(new Long(i), new Long(i)+annotatedText.getText().length(), label, features);
		    		}
		    		java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputDirectoryPath +  File.separator + pmid + ".xml" , false)));
					out.write(gateDocument.toXml());
					out.close();
		    	}else{
		    		String[] data = line.split("\t");
		    		if(data.length!=5) {
		    			System.out.println(data);
		    		}
		    		Integer offset_start = new Integer(data[1]);
		    		if(abstract_txt.length()==offset_start) {
		    			abstract_txt.append(data[0]);
		    		}else if(abstract_txt.length()<offset_start) {
		    			int spaces_to_add = offset_start - abstract_txt.length();
		    			String filled = Strings.repeat(" ", spaces_to_add);
		    			abstract_txt.append(filled+data[0]);
		    			if(!data[4].equals("|O")) {
		    				if(data[4].equals("|B-DISEASE") || data[4].equals("|B-ADVERSE")) {
		    					String label = data[4].replace("|B-","");
		    					labels.put(offset_start, new AnnotatedText(data[3], label));
		    				}
		    			}
		    		}
		    	}
		    }
		} catch (Exception e) {
			System.out.println("Error reading file : " + file );
			e.printStackTrace();
		}
		
	}
}
