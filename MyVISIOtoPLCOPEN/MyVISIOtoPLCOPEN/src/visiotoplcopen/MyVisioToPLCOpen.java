/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.io.FileInputStream;
import java.util.Collection;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;

/**
 *
 * @author pbpras
 */
public class MyVisioToPLCOpen {
//    static FBDICArchitecture fBDICArchitecture;
    /**
     * @param args the command line arguments
     */
//    XDGFPage abc;
//    SafetyFunction tempSafetyFunction = new SafetyFunction("afsd",abc );
     
 
    public static void main(String[] args) {
        
        XmlVisioDocument inputXMLVisioDoc = null;
        
//        String inputFileName ="FNPPHANHI-00044_App04_v7.0_RPS-SF.vsdx";//FNPPHANHI-00044_App04_v7.0_RPS-SF.vsdx//Diagram.vsdx
        String inputFileName ="Diagram.vsdx";//FNPPHANHI-00044_App04_v7.0_RPS-SF.vsdx//Diagram.vsdx

        Collection<XDGFPage> inputDiagramPages;
        
//        VisioDataParserICA visioDataParser=new VisioDataParserICA("Diagram.vsdx");
//        visioDataParser.populateICAFromVisio();
        
//            public VisioDataParserICA(String inputFileName) {
//
//        this.inputFile = inputFileName;
//        try {
//            xmlVisioDoc = new XmlVisioDocument(new FileInputStream(this.inputFile));
////            System.out.println("found file named:"+this.inputFile);         
//        } catch (Exception ex) {
//            Logger.getLogger(VisioDataParserICA.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        diagrams = xmlVisioDoc.getPages();
////        System.out.println("name of the page inside diagram: "+diagrams+", size of diagram: "+diagrams.size());
//    }

        try {
            
            inputXMLVisioDoc = new XmlVisioDocument(new FileInputStream(inputFileName));
            System.out.println("found file named:" +inputFileName);
            
        } catch (Exception ex) {
            Logger.getLogger(VisioDataParserICA.class.getName()).log(Level.SEVERE, null, ex);
        }
        inputDiagramPages=inputXMLVisioDoc.getPages();
//        System.out.println("name of the pages inside inputVisioFile:"+ inputDiagramPages);
        
        for(XDGFPage page:inputDiagramPages){
            System.out.println("\n"+"name of the SafetyFunction:"+ page.getName());
            VisioDataParserICA currentVisioDataParserICA = new VisioDataParserICA(page);
//            currentVisioDataParserICA.populateICAFromVisio();
            
        }
        
        System.out.println("visiotoplcopen.MyVisioToPLCOpen.main()");
    }
    
}

