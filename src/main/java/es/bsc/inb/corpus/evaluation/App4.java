package es.bsc.inb.corpus.evaluation;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;

/**
 * 
 * Generate gate corpus from the TAC challenge 2017 corpus.
 * 
 */
public class App4 {
	
	static final Logger log = Logger.getLogger("log");
	
	
	/**
	 * 
	 * @param args
	 */
    public static void main( String[] args ) {
    	String inputDirectoryPath = "corpus/corpus_4/tac_2017_corpus/train_xml/";
    	String outputDirectoryPath = "corpus/corpus_4/tac_2017_corpus/corpus_gate/";
    	
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
				if(file.getName().endsWith(".xml")){
					System.out.println("Wrapper::processTagger :: processing file : " + file.getAbsolutePath());
					File outputGATEFile = new File (outputDirectoryPath +  File.separator + file.getName());
					process(file, outputGATEFile); 
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
	private static void process(File file, File outputGATEFile) {
		try {
			
			
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			//doc.getDocumentElement().normalize();
			NodeList sections = doc.getElementsByTagName("Section");
			//System.out.println("-------------SECTIONS---------------");
			
			String section_text = "";
			for (int temp_s = 0; temp_s < sections.getLength(); temp_s++) {
				Node section = sections.item(temp_s);
				section_text = section.getTextContent();
				Element eElement = (Element) section;
				gate.Document gateDocument = Factory.newDocument(section_text);
				String section_id = eElement.getAttribute("id");
				NodeList mentions = doc.getElementsByTagName("Mention");
				//System.out.println("-------------MENTIONS---------------");
				for (int temp = 0; temp < mentions.getLength(); temp++) {
					Node mentionNode = mentions.item(temp);
					//System.out.println("\nCurrent Element :" + mentionNode.getNodeName());
					if (mentionNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eMention = (Element) mentionNode;
						String section_attr = eMention.getAttribute("section");
						if(section_attr.equals(section_id)) {
							String id = "";
							try {
								id = eMention.getAttribute("id");
								String type = eMention.getAttribute("type");
								String start = eMention.getAttribute("start");
								String len = eMention.getAttribute("len");
								String str = eMention.getAttribute("str");
								FeatureMap features = Factory.newFeatureMap(); 
								features.put("value", str);
								if(type.equals("AdverseReaction")) {
									type="FINDING";
								}else if(type.equals("Animal")) {
									type="SPECIES";
								}else if(type.equals("Severity")) {
									type="SEVERITY";
								}else if(type.equals("Negation")) {
									System.out.println(str+"\tNEGATION_WORD\t10.0");
								}else {
									//System.out.println("No se mapea " + type + " text " + str);
								}
								gateDocument.getAnnotations("EVALUATION").add(new Long(start), new Long(start) + new Long(len), type, features);
							}catch(Exception e) {
								//System.out.println("ERROR annotated id : " + id);
								//System.out.println(e);
							}
						}
						
							
					}
				}
			
				
				java.io.Writer out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new FileOutputStream(outputGATEFile + "_" + eElement.getAttribute("id")+".xml", false)));
			    out.write(gateDocument.toXml());
			    out.close();
				
				
			}
//			NodeList relations = doc.getElementsByTagName("Relation");
//			System.out.println("--------------RELATIONS--------------");
//			for (int temp = 0; temp < relations.getLength(); temp++) {
//				Node nNode = relations.item(temp);
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						Element eElement = (Element) nNode;
//						System.out.println("id: " + eElement.getAttribute("id"));
//						System.out.println("type: " + eElement.getAttribute("type"));
//						System.out.println("arg1: " + eElement.getAttribute("arg1"));
//						System.out.println("arg2: " + eElement.getAttribute("arg2"));
//						
//				}
//			}
//			
//			
//			NodeList reactions = doc.getElementsByTagName("Reaction");
//			System.out.println("--------------RELATIONS--------------");
//			for (int temp = 0; temp < reactions.getLength(); temp++) {
//				Node nNode = reactions.item(temp);
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//						Element eElement = (Element) nNode;
//						System.out.println("id: " + eElement.getAttribute("id"));
//						System.out.println("type: " + eElement.getAttribute("type"));
//						System.out.println("arg1: " + eElement.getAttribute("arg1"));
//						System.out.println("arg2: " + eElement.getAttribute("arg2"));
//				}
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
