/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;


import java.util.ArrayList;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
/**
 *
 * @author pbpras
 */
public class XMLShapeParser {
    
    public static HashMap<String,String> processXMLDynamicConnector(String inputXMLDocString){
        
        /*
        this method can access properties of xml object of type: shape and name Dynamic connector
        ex: begTrigger, endTrigger and any other xml element by modifiying this code block     
        
        */
    HashMap<String,String> connectionTriggerHashMap=new HashMap<String,String>();
//        System.out.println("inside XMLShapeParser.processXMLDynamicConnector: \n");
//        File inputFile= (File)inputXMLDocString;
        try {
            
            
            Document doc = convertStringToXMLDocument(inputXMLDocString);
            doc.getDocumentElement().normalize();
//            System.out.println("Root element : "+doc.getDocumentElement().getNodeName());                
                            
        NodeList nList = doc.getElementsByTagName("main:Cell");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                
//                System.out.println("current element:"+ temp);
                Node node = nList.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

//                    System.out.println("has a node in element ");

                    if (node.hasAttributes()) {
                        
                        NamedNodeMap nodeMap = node.getAttributes();
                        String nodeName = nodeMap.getNamedItem("N").getNodeValue();
//                        System.out.println("Node name: "+nodeName);

//                            System.out.println("node with: " + nodeMap.getNamedItem("N") + " , "  + nodeMap.getNamedItem("F") +"\n");

                        if (nodeName.equalsIgnoreCase("BegTrigger") || nodeName.equalsIgnoreCase("EndTrigger")) {
//                            System.out.println("from method printNodeDataWithAttrName");
//                            System.out.println("node data:" + nodeMap.getNamedItem("N") + " , " + nodeMap.getNamedItem("F")+"\n");
                            if(nodeName.equalsIgnoreCase("BegTrigger")){
                                String begin="BegTrigger";
                                String bTrigger=""+nodeMap.getNamedItem("F");
                                String bShapeID=findDigit(bTrigger);
                                connectionTriggerHashMap.put(begin, bShapeID);
                            } else if(nodeName.equalsIgnoreCase("EndTrigger")){
                                String begin="EndTrigger";
                                String bTrigger=""+nodeMap.getNamedItem("F");
                                String bShapeID=findDigit(bTrigger);
                                connectionTriggerHashMap.put(begin, bShapeID);
                            }

                        }

                    }

                }

            }
            
            if(!connectionTriggerHashMap.isEmpty()){
//                System.out.println("BegTrigger:-> "+ connectionTriggerHashMap.get("BegTrigger"));
//                System.out.println("EndTrigger:-> "+ connectionTriggerHashMap.get("EndTrigger"));
                return connectionTriggerHashMap;
            
            }
                   
        } catch (Exception e) {
            
            System.out.println("error in XMLShapeParser>processDynamicConnector: "+e);
        }
        
        
          return connectionTriggerHashMap;


    }
    
//    public static void ProcessXMLGroup(String inputXMLDocString){
    
//    System.out.println("inside XMLShapeParser.ProcessXMLGroup: \n");
////        File inputFile= (File)inputXMLDocString;
//        try {
//            
//            Document doc = convertStringToXMLDocument(inputXMLDocString);
//            doc.getDocumentElement().normalize();
//            System.out.println("Root element : "+doc.getDocumentElement().getNodeName());                
//                            
//        NodeList nList = doc.getElementsByTagName("main:Cell");
//
//            for (int temp = 0; temp < nList.getLength(); temp++) {
//                
////                System.out.println("current element:"+ temp);
//                Node node = nList.item(temp);
//
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//
////                    System.out.println("has a node in element ");
//
//                    if (node.hasAttributes()) {
//                        
//                        NamedNodeMap nodeMap = node.getAttributes();
//                        String nodeName = nodeMap.getNamedItem("N").getNodeValue();
////                        System.out.println("Node name: "+nodeName);
//
////                            System.out.println("node with: " + nodeMap.getNamedItem("N") + " , "  + nodeMap.getNamedItem("F") +"\n");
//
//                        if (nodeName.equalsIgnoreCase("BegTrigger") || nodeName.equalsIgnoreCase("EndTrigger")) {
////                            System.out.println("from method printNodeDataWithAttrName");
//                            System.out.println("node data:" + nodeMap.getNamedItem("N") + " , " + nodeMap.getNamedItem("F")+"\n");
//
//                        }
//
//                    }
//
//                }
//
//            }
//                   
//        } catch (Exception e) {
//            
//            System.out.println("error in XMLShapeParser>processDynamicConnector: "+e);
//        }
        
        
//    }
    
//    public static void Process(){}
    
    
    private static String findDigit(String inputStr){
//        System.out.println("myTestcheck.practice1.findDigit()");
//        this finds the ID digits from the string retrieved from beg/end trigger Dynamic connector
        
       try {
           Pattern intsOnly = Pattern.compile("\\d+");
           Matcher makeMatch = intsOnly.matcher(inputStr);
           makeMatch.find();
           String inputInt = makeMatch.group();
           
           return "<Shape ID=\""+inputInt+"\">";
       } catch (Exception e) {
           return null;
       }

   }
    
    private static Document convertStringToXMLDocument(String xmlString) 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
}


    