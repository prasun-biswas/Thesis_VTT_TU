/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XDGFConnection;

/**
 *
 * @author pbpras
 */
public class VisioDataParserICA {

    
    String inputFile;
    XmlVisioDocument xmlVisioDoc;
    Collection<XDGFPage> diagrams;
    LinkedList<XDGFShape> diagramsXDGFShapesLL;
    LinkedList<XDGFConnection> diagramsXDGFConnectionsLL;
    Set<String> uniqueConnectionIDSet = new LinkedHashSet<>();
    LinkedHashSet<String> shapeAsGroupLHS = new LinkedHashSet<>();
    LinkedHashSet<String> shapeAsGroupMemberLHS = new LinkedHashSet<>();
    LinkedHashSet<String> shapeNotGroupOrMember = new LinkedHashSet<>();
    LinkedList<VisioShape> visioShapeLL= new LinkedList<>();
    HashMap<String, VisioShape> idAndVisioShapeHM=new HashMap<String,VisioShape>();
    LinkedList<VisioShape> logicalAndLL = new LinkedList<>();

    public VisioDataParserICA(String inputFileName) {

        this.inputFile = inputFileName;
        try {
            xmlVisioDoc = new XmlVisioDocument(new FileInputStream(this.inputFile));
//            System.out.println("found file named:"+this.inputFile);         
        } catch (Exception ex) {
            Logger.getLogger(VisioDataParserICA.class.getName()).log(Level.SEVERE, null, ex);
        }
        diagrams = xmlVisioDoc.getPages();
//        System.out.println("name of the page inside diagram: "+diagrams+", size of diagram: "+diagrams.size());
    }

    public void populateICAFromVisio() {

        try {

//            this stores all shapes that are connection in a set to find unique shapeID, which can 
//            be used to truly identify connectors that don't have the string "Dynamic connector" in it
            System.out.println(" \n try block:-> 1");
            Iterator diagramIterator = diagrams.iterator();
            while (diagramIterator.hasNext()) {
                XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();

//                System.out.println("Shape In currentXDGFPage as connections  : \n " + currentXDGFPage.getContent().getXmlObject().getConnects());// very useful for the connections
                Iterator xdgfConnectioninXDGFPage = currentXDGFPage.getContent().getConnections().iterator();
                System.out.println("\n number of connections in currentXDGFPage: " + currentXDGFPage.getContent().getConnections().size());
                for (int i = 0; i < currentXDGFPage.getContent().getConnections().size(); i++) {
                    System.out.println("connection data:" + i + "\n"
//                            + currentXDGFPage.getContent().getConnections().get(i).getFromCellName() + "\n"
                            //               +currentXDGFPage.getContent().getConnections().get(i).getToCellName()+"\n"
                            + currentXDGFPage.getContent().getConnections().get(i).getFromShape() + "\n"
                            //               +currentXDGFPage.getContent().getConnections().get(i).getToShape()+"\n"
                            //               +currentXDGFPage.getContent().getConnections().get(i).getFromPart()+"\n"
                            //               +currentXDGFPage.getContent().getConnections().get(i).getToPart()+"\n"

//                            + "\n"
                    );

                    uniqueConnectionIDSet.add("" + currentXDGFPage.getContent().getConnections().get(i).getFromShape());
                }

            }
            System.out.println("number of unique connection ID: " + uniqueConnectionIDSet.size());
//            System.out.println("all the unique connection ID: "+ uniqueConnectionID);
            for (String i : uniqueConnectionIDSet) {
                System.out.println(i);
            }
        } catch (Exception e) {
            System.out.println("Error in VisioDataParser.populateICAFromVisio()");
            System.out.println(" \n error in: try block:-> 1" + e);
        }

        try {

//            this stores all shapes that are type:"Group" in a set to find unique shapeID, which can 
//            be used to truly identify group and will later store List of member shape
            System.out.println("\n try block:-> 2");
            Iterator diagramIterator = diagrams.iterator();
            while (diagramIterator.hasNext()) {
                XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();// access the current XDGFPage

                System.out.println("number of shapes in currentXDGFPage: " + currentXDGFPage.getContent().getShapes().size());
//                System.out.println("In currentXDGFPage TopLevelShapes  : \n " + currentXDGFPage.getContent().getTopLevelShapes());

//                iterate through each shape inside page 
                Iterator xdgfShapeInXDGFPage = currentXDGFPage.getContent().getShapes().iterator();
                while (xdgfShapeInXDGFPage.hasNext()) {
                    XDGFShape currentShape = (XDGFShape) xdgfShapeInXDGFPage.next();

//                    System.out.println("CurrentShape data:\n "
//                    +currentShape+"\n"
//                    +currentShape.getType()
//                    );
                    if (currentShape.getType().equalsIgnoreCase("Group")) {
                        // store in if it is a "Group"
                        shapeAsGroupLHS.add("" + currentShape);
                    }
                    if(currentShape.getParentShape()!=null){
                        shapeAsGroupMemberLHS.add(""+currentShape);
                    } 
                    if(currentShape.getType().equalsIgnoreCase("Shape")&&currentShape.getParentShape()==null
                            &&uniqueConnectionIDSet.contains(""+currentShape)){
                        shapeNotGroupOrMember.add(""+currentShape);
                    }

                }

            }
            System.out.println("\n number of shapes as group:" + shapeAsGroupLHS.size());
            for (String i : shapeAsGroupLHS) {
                System.out.println(i);
            }
             System.out.println("\n number of shape group member:" + shapeAsGroupMemberLHS.size());
            for (String i : shapeAsGroupMemberLHS) {
                System.out.println(i);
            }
            
            System.out.println("\n number of shape notGrpOrMbr:"+ shapeNotGroupOrMember.size());
            for(String i: shapeNotGroupOrMember){
            System.out.println(i);
            }

        } catch (Exception e) {
            System.out.println("Error in VisioDataParser.populateICAFromVisio()");
            System.out.println(" \n error in: try block:-> 2" + e);
        }

        try {

            System.out.println(" \n try block:-> 3");
            Iterator diagramIterator = diagrams.iterator();
            while (diagramIterator.hasNext()) {
                XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();// access the current XDGFPage

                System.out.println("number of shapes in currentXDGFPage: " + currentXDGFPage.getContent().getShapes().size());
//                System.out.println("Shape In currentXDGFPage as connections  : \n "+currentXDGFPage.getContent().getXmlObject() );//.getconnect() very useful for the connections
//                System.out.println("In currentXDGFPage TopLevelShapes  : \n " + currentXDGFPage.getContent().getTopLevelShapes());
//                System.out.println("currentXDGFPage Shape getShapeById : \n "+currentXDGFPage.getContent().getShapeById(36).getName() );

//                iterate through each shape inside page 
                Iterator xdgfShapeInXDGFPage = currentXDGFPage.getContent().getShapes().iterator();
                while (xdgfShapeInXDGFPage.hasNext()) {
                    XDGFShape currentShape = (XDGFShape) xdgfShapeInXDGFPage.next();

                    System.out.println("CurrentShape data: "
                            + currentShape + "\n"
                            + currentShape.getShapeType()//+": has text: "
                                        +" : "+currentShape.getName()+"\n"
                    //                    +currentShape.getTextAsString()+"\n"
                    //                    +currentShape.getID()+"\n"
                    //                    +currentShape.getType()+"\n"
                    //                    +currentShape.getShapes()+"\n"
//                                        +currentShape.getText()+"\n"
                    //                    +currentShape.getParentShape()+"\n"
                    //                    +currentShape.getXmlObject()+"\n"
                    );
                    
//                    VisioShape tempVisioShape = new VisioShape(currentShape, currentShape + "");

                    
                    String currentShapeID=""+currentShape;
                    boolean connector=uniqueConnectionIDSet.contains(currentShapeID);
                    boolean group=shapeAsGroupLHS.contains(currentShapeID);
                    boolean groupMember = shapeAsGroupMemberLHS.contains(currentShapeID);
//                    System.out.println(currentShapeID+", connector?:"+uniqueConnectionIDSet.contains(currentShapeID)+", Group?: "+shapeAsGroupLHS.contains(currentShapeID)+", Group member?: "+shapeAsGroupMemberLHS.contains(currentShapeID));
//                    System.out.println(currentShapeID+", Connector?:"+connector+", Group?: "+group+", Group member?: "+groupMember);

//                    VisioShapeBuilder.buildVisioShape(currentShape,currentShapeID, connector, group, groupMember);
                   VisioShape tempVisioShape = VisioShapeBuilder.buildVisioShape(currentShape,currentShapeID, connector, group, groupMember);
                    System.out.println("text Content: "+tempVisioShape.textContent);
                   visioShapeLL.add(tempVisioShape);
                   idAndVisioShapeHM.put(currentShapeID, tempVisioShape);// easy to get visioShape searching with shapeID
                    System.out.println("initialized instance: " + tempVisioShape.ShapeID+"\n");
                }
                System.out.println("number of Visio shapes: "+ visioShapeLL.size()+", "+idAndVisioShapeHM.size());
                /*
 *Iterate through individual XDGFShape from XDGFPage contents
                 */
            }
           
            
//           Iterator<Entry<String,VisioShape>> it = idAndVisioShapeHM.entrySet().iterator();
//           
//           while(it.hasNext()){
//           
//               HashMap.Entry<String,VisioShape> visioPair=(Map.Entry<String,VisioShape>)it.next();
//               String id=visioPair.getKey();
//               System.out.println("found key: "+id);
//               VisioShape tempShape=visioPair.getValue();
//               System.out.println(tempShape.ShapeID+":"+tempShape.isDynamicConector+":"+tempShape.isItAGroup+":"+tempShape.isMemberOfAGroup+":"+tempShape.textContent);
//     
//           }
//            
            
        } catch (Exception e) {
            System.out.println("Error in VisioDataParser.populateICAFromVisio()");
            System.out.println(" \n error in: try block:-> 3" + e);
        }
//        


    
    }
    
  

}
