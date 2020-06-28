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
import java.math.*;
import javafx.geometry.Point2D;

/**
 *
 * @author pbpras
 */
public class VisioDataParserICA {

    String inputFile;
    XmlVisioDocument xmlVisioDoc;
    Collection<XDGFPage> diagrams;
    String currentXDGFPageName;
    XDGFPage currentXDGFPage;
    
    
//    LinkedList<XDGFShape> diagramsXDGFShapesLL;
    LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM = new LinkedHashMap<>();
    LinkedList<XDGFConnection> diagramsXDGFConnectionsLL;
    LinkedHashSet<String> uniqueConnectionIDSet = new LinkedHashSet<>();
    LinkedHashMap<String, BasicConnector> finalUnifiedConnectorLHM;// final connector after fixing loose ends and broken lines
    LinkedHashSet<String> uniqueNullShapeSet = new LinkedHashSet<>();
    LinkedHashSet<String> shapeAsGroupLHS = new LinkedHashSet<>();
    LinkedHashSet<String> shapeAsGroupMemberLHS = new LinkedHashSet<>();
    LinkedHashSet<String> shapeNotGroupOrMember = new LinkedHashSet<>();
    LinkedHashSet<String> shapeAsInversionLHS = new LinkedHashSet<>();// stores shapeID of the shape that are inversion
    LinkedHashSet<String> shapesAsNullSet = new LinkedHashSet<>();// stores shapes that are of type "null"(there are very few of null shapes but those might crash the program)
    LinkedList<VisioShape> visioShapeLL = new LinkedList<>();
    HashMap<String, VisioShape> idAndVisioShapeHM = new HashMap<String, VisioShape>();
    LinkedHashMap<String, Point2D> pinXYPointofShapesLHM = new LinkedHashMap<>();// stores shapeID + pinX & pinY as point2D of the shapes that are not connector
    LinkedHashSet<BasicShape> basicShapeSet = new LinkedHashSet<>();// stores shapes as instance of BasicShape Class to use later for unifying group.

//    LinkedList<VisioShape> logicalAndLL = new LinkedList<>();
//    LinkedList<VisioShape> votingOperationLL = new LinkedList<>();
//    public VisioDataParserICA(String inputFileName) {
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
    
        public VisioDataParserICA(XDGFPage IpCurrentXDGFPage) {

        XDGFPage currentXDGFPage = IpCurrentXDGFPage;
        
            System.out.println(GlobalVariable.ANSI_BLUE + "Currently processing page:"+ currentXDGFPage.getName()+" : "+currentXDGFPage);
            System.out.println(currentXDGFPage.getContent());
//        System.out.println("name of the page inside diagram: "+diagrams+", size of diagram: "+diagrams.size());
            populateICAFromVisio(currentXDGFPage);
    }
        

    public void populateICAFromVisio(XDGFPage currentXDGFPage ) {

//        XDGFPage currentXDGFPage = IpCurrentXDGFPage;

        try {

//            this stores all shapes that are connection in a set to find unique shapeID, which can 
//            be used to truly identify connectors that don't have the string "Dynamic connector" in it
//            System.out.println(" \n try block:-> 1");
//            Iterator diagramIterator = diagrams.iterator();
//            while (diagramIterator.hasNext()) {
//                XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();
//                
//                System.out.println("diagram's name: " + currentXDGFPage.getName() + " : " + currentXDGFPage.getID());
////                System.out.println(GlobalVariable.ANSI_GREEN+"size of the page bound:"+currentXDGFPage.getBoundingBox());
//
////                System.out.println("Shape In currentXDGFPage as connections  : \n " + currentXDGFPage.getContent().getXmlObject().getConnects());// very useful for the connections
//                Iterator xdgfConnectioninXDGFPage = currentXDGFPage.getContent().getConnections().iterator();
//
//                System.out.println("\n number of connections in currentXDGFPage: " + currentXDGFPage.getContent().getConnections().size());
//                for (int i = 0; i < currentXDGFPage.getContent().getConnections().size(); i++) {
//                    XDGFConnection tempConnection = currentXDGFPage.getContent().getConnections().get(i);
//
////                    System.out.println("connection data:" + i + "\n"
////                            + "from and to cell: " + tempConnection.getFromCellName() + "," + tempConnection.getToCellName() + "\n"
////                            + "from and to shape: " + tempConnection.getFromShape() + "," + tempConnection.getToShape() + "\n"
////                            + "\n"
////                    );
//                    uniqueConnectionIDSet.add("" + currentXDGFPage.getContent().getConnections().get(i).getFromShape());
//                }
//
//            }
            System.out.println(" \n try block:-> 1 ");

            // Instead of extracting the pages in this class Now receive it as argument from MyVisioToPLCOpen.java
            System.out.println("diagram's name: " + currentXDGFPage.getName() + " : " + currentXDGFPage.getID());
//                System.out.println(GlobalVariable.ANSI_GREEN+"size of the page bound:"+currentXDGFPage.getBoundingBox());

//                System.out.println("Shape In currentXDGFPage as connections  : \n " + currentXDGFPage.getContent().getXmlObject().getConnects());// very useful for the connections
            Iterator xdgfConnectioninXDGFPage = currentXDGFPage.getContent().getConnections().iterator();

            System.out.println("\n number of connections in currentXDGFPage: " + currentXDGFPage.getContent().getConnections().size());
            
            for (int i = 0; i < currentXDGFPage.getContent().getConnections().size(); i++) {
                XDGFConnection tempConnection = currentXDGFPage.getContent().getConnections().get(i);

//                    System.out.println("connection data:" + i + "\n"
//                            + "from and to cell: " + tempConnection.getFromCellName() + "," + tempConnection.getToCellName() + "\n"
//                            + "from and to shape: " + tempConnection.getFromShape() + "," + tempConnection.getToShape() + "\n"
//                            + "\n"
//                    );
                uniqueConnectionIDSet.add("" + currentXDGFPage.getContent().getConnections().get(i).getFromShape());
            }

        } catch (Exception e) {
            System.out.println("Error in VisioDataParser.populateICAFromVisio()");
            System.out.println("Error in: try block:-> 1" + e);
        }

        try {

//            this stores all shapes that are type:"Group" in a set to find unique shapeID, which can 
//            be used to truly identify group and will later store List of member shape. different shapes
//            are stored in different container to be used later. 
            System.out.println("\n try block:-> 2");
//            Iterator diagramIterator = diagrams.iterator();
//            while (diagramIterator.hasNext()) {
//            XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();// access the current XDGFPage
//            XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();// access the current XDGFPage

//            System.out.println("number of shapes in currentXDGFPage: " + currentXDGFPage.getContent().getShapes().size());
//                System.out.println("In currentXDGFPage TopLevelShapes  : \n " + currentXDGFPage.getContent().getTopLevelShapes());

//                iterate through each shape inside page 
            Iterator xdgfShapeInXDGFPageItr = currentXDGFPage.getContent().getShapes().iterator();
            while (xdgfShapeInXDGFPageItr.hasNext()) {
                XDGFShape currentShape = (XDGFShape) xdgfShapeInXDGFPageItr.next();

                String currentShapeID = "" + currentShape;
//                System.out.println("current shape: "+ currentShapeID+ ": "+ currentShape.getTextAsString());
                idAndXDGFShapesHM.put(currentShapeID, currentShape);
                if (currentShape.getType() == null) {
                    uniqueNullShapeSet.add(currentShapeID);
//                        System.out.println(GlobalVariable.ANSI_CYAN + "null shape found" + currentShapeID);
                }

                if (currentShape.getShapes() != null) {
//              build instances of BasicShape(custome shape different than XDGF shapes) Class to store in basicShapeSet, to be used later to unify groups
                    ArrayList<String> listOfShapes = new ArrayList<>();
                    for (XDGFShape s : currentShape.getShapes()) {
                        String memberShapeId = "" + s;
                        if (!uniqueNullShapeSet.contains(memberShapeId)) {
                            listOfShapes.add(memberShapeId);

                        }
//                            System.err.println("member of group: "+memberShapeId);

                    }
                    BasicShape tempBasicShape = new BasicShape(currentShapeID, "", listOfShapes);
                    basicShapeSet.add(tempBasicShape);
//                        System.err.println("added to basicShapeSet :");

                } else {
                    if (!uniqueNullShapeSet.contains(currentShapeID)) {
                        ArrayList<String> listOfShapes = new ArrayList<>();
                        BasicShape tempBasicShape = new BasicShape(currentShapeID, "", listOfShapes);
                        basicShapeSet.add(tempBasicShape);
                    }

//                        System.err.println("added to basicShapeSet :");
                }

//                    System.out.println("\n" + "CurrentShape data:\n "
//                            + currentShape + "\n"
//                            + currentShape.getType() + " has shape name: " + currentShape.getName() + "\n"
//                            + currentShape.getShapeType() + "\n"
//                            + currentShape.getShapes() + "\n" + currentShape.getTextAsString()
//                    );
                if (currentShape.getType() != null) {
//                        some of the shapes are of type "null" which crash's the program. this approach is to avoid null elements 

                    if (currentShape.getType().equalsIgnoreCase("Group")) {
                        // store in if it is a "Group"
                        shapeAsGroupLHS.add("" + currentShape);
                    }

                    if (currentShape.getName().contains("Dynamic connector")) {
//                            System.out.println("maybe found a connector");
                        if (currentShape.getParentShape() == null) {
                            uniqueConnectionIDSet.add(currentShapeID);
                        } else if (currentShape.getParentShape() != null) {
                            uniqueConnectionIDSet.remove(currentShapeID);
                        }
//                            System.out.println(" maybe not a connector");

                    }
                    if (currentShape.getParentShape() != null) {
                        shapeAsGroupMemberLHS.add("" + currentShape);
                    }
                    if (currentShape.getType().equalsIgnoreCase("Shape") && currentShape.getParentShape() == null
                            && !uniqueConnectionIDSet.contains("" + currentShape)) {
                        shapeNotGroupOrMember.add("" + currentShape);
                    }
                    if (currentShape.getName().contains("No")) {

//                        System.out.println("shape's H and W: "+tempHndWHashMap);
                        shapeAsInversionLHS.add(currentShapeID);

                    }

                } else {

                    shapesAsNullSet.add("" + currentShape);
                }

            }

//            }
//            System.out.println("number of unique connection ID: " + uniqueConnectionIDSet.size());
////            System.out.println("all the unique connection ID: "+ uniqueConnectionID);
//            for (String i : uniqueConnectionIDSet) {
//                System.out.println(i);
//            }
//            System.out.println("\n number of shapes as group:" + shapeAsGroupLHS.size());
//            for (String i : shapeAsGroupLHS) {
//                System.out.println(i);
//            }
//            System.out.println("\n number of shape group member:" + shapeAsGroupMemberLHS.size());
//            for (String i : shapeAsGroupMemberLHS) {
//                System.out.println(i);
//            }
//
//            System.out.println("\n number of shape notGrpOrMbr:" + shapeNotGroupOrMember.size());
//            for (String i : shapeNotGroupOrMember) {
//                System.out.println(i);
//            }
//            System.out.println("\n number of shape inversion :" + shapeAsInversionLHS.size());
//            for (String s : shapeAsInversionLHS.keySet()) {
//
//                System.out.println(s);
//            }
        } catch (Exception e) {
            System.out.println("Error in VisioDataParser.populateICAFromVisio()");
            System.out.println(" \n error in: try block:-> 2" + e);
        }

        try {

            System.out.println(" \n try block:-> 3");
//            Iterator diagramIterator = diagrams.iterator();
//            while (diagramIterator.hasNext()) {
//                XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();// access the current XDGFPage
//                XDGFPage currentXDGFPage = (XDGFPage) diagramIterator.next();// access the current XDGFPage


                System.out.println("number of shapes in currentXDGFPage: " + currentXDGFPage.getContent().getShapes().size());
//                System.out.println("Shape In currentXDGFPage as connections  : \n "+currentXDGFPage.getContent().getXmlObject() );//.getconnect() very useful for the connections
//                System.out.println("In currentXDGFPage TopLevelShapes  : \n " + currentXDGFPage.getContent().getTopLevelShapes());
//                System.out.println("currentXDGFPage Shape getShapeById : \n "+currentXDGFPage.getContent().getShapeById(36).getName() );

//                iterate through each shape inside page 
                Iterator shapeInXDGFPageItr = currentXDGFPage.getContent().getShapes().iterator();
                while (shapeInXDGFPageItr.hasNext()) {
                    XDGFShape currentShape = (XDGFShape) shapeInXDGFPageItr.next();

//                    System.out.println(GlobalVariable.ANSI_PURPLE + "CurrentShape data:-------"
//                            + currentShape + "\n"
//                            + "getShapeType(): " + currentShape.getShapeType()//+": has text: "
//                            + " : " + currentShape.getName() + "\n"
//                    //                            +currentShape.getXmlObject()
//
//                    );
//                    System.out.println(GlobalVariable.ANSI_PURPLE + "PinPoints: " + currentShape.getPinX() + "," + currentShape.getPinY()
//                            + " BeginPin :" + currentShape.getBeginX() + "," + currentShape.getBeginY()
//                            + " EndPin :" + currentShape.getEndX() + "," + currentShape.getEndY()
//                            + ": H&W -> " + currentShape.getHeight() + "," + currentShape.getWidth());
//                    VisioShape tempVisioShape = new VisioShape(currentShape, currentShape + "");
                    String currentShapeID = "" + currentShape;
                    boolean connector = uniqueConnectionIDSet.contains(currentShapeID);
                    boolean group = shapeAsGroupLHS.contains(currentShapeID);
                    boolean groupMember = shapeAsGroupMemberLHS.contains(currentShapeID);
//                    System.out.println(currentShapeID+", connector?:"+uniqueConnectionIDSet.contains(currentShapeID)+", Group?: "+shapeAsGroupLHS.contains(currentShapeID)+", Group member?: "+shapeAsGroupMemberLHS.contains(currentShapeID));
//                    System.out.println(currentShapeID+", Connector?:"+connector+", Group?: "+group+", Group member?: "+groupMember);

//                    BuilderVisioShape.buildVisioShape(currentShape,currentShapeID, connector, group, groupMember);
//// builds Visio(custom) shape to organize the shape data in a more managable way for later use.
                    VisioShape tempVisioShape = BuilderVisioShape.buildVisioShape(currentShape, currentShapeID, connector, group, groupMember);
//                    System.out.println("TEXT CONTENT:>>> " + tempVisioShape.textContent);
                    visioShapeLL.add(tempVisioShape);
                    idAndVisioShapeHM.put(currentShapeID, tempVisioShape);// easy to get visioShape searching with shapeID
                    Point2D tempPoint2D = new Point2D(currentShape.getPinX(), currentShape.getPinY());
                    if (connector == false) {
                        pinXYPointofShapesLHM.put(currentShapeID, tempPoint2D);
                    }

//                    System.out.println("initialized instance:-----" + tempVisioShape.ShapeID + "\n");
                }
                System.out.println("number of Visio shapes: " + visioShapeLL.size() + ", " + idAndVisioShapeHM.size());
                /*
 *Iterate through individual XDGFShape from XDGFPage contents
                 */
//            }

//            
        } catch (Exception e) {
            System.out.println("Error in VisioDataParser.populateICAFromVisio()");
            System.out.println(" \n error in: try block:-> 3" + e);
        }
//        

        try {
            // this group fix loose end of connectors, unifis the broken connector and return unique group of connector 

            System.out.println(" \n try block:-> 5");
            UnifyConnectors.fixedEndConnectors(uniqueConnectionIDSet, idAndVisioShapeHM, idAndXDGFShapesHM);
//            UnifyConnectors.printFixedConnector();
            UnifyConnectors.unifyFixedEndConnector();
//            UnifyConnectors.printFinalUnifiedConnector();
            finalUnifiedConnectorLHM = UnifyConnectors.getFinalUnifiedConnector();

            for (Entry<String, BasicConnector> entry : finalUnifiedConnectorLHM.entrySet()) {
                String key = entry.getKey();
                BasicConnector tempConnector = entry.getValue();

                idAndVisioShapeHM.get(tempConnector.begFromShapeId).setNextShapeIdLHS(tempConnector.endsToShapeId);
                idAndVisioShapeHM.get(tempConnector.endsToShapeId).setPreviousShapeIdLSH(tempConnector.begFromShapeId);

            }

        } catch (Exception e) {
            System.err.println("Error in VisioDataParser.populateICAFromVisio()");
            System.err.println("\n error in: try block:-> 5 failed connector fix attempt");
        }

        // this accepts VisioShape based data processed from XDGFShape and XDGFPages,
        // identify and transfer it to FBD blocks and returns it as FBDSafetyFunction,
        // which contains all the FBD elements on each page
        vigioPageToBDSafetyFunction(); 

    }

    private FBDSafetyFunction vigioPageToBDSafetyFunction() {

        // this accepts VisioShape based data processed from XDGFShape and XDGFPages,
        // identify and transfer it to FBD blocks and returns it as FBDSafetyFunction,
        // which contains all the FBD elements on each page
        Set<String> tempConnectionIDSet = uniqueConnectionIDSet;
        HashMap<String, VisioShape> tempIdAndVisioShapeHM = idAndVisioShapeHM;
        BuilderFBDSafetyFunction tempBDSafetyFunction = new BuilderFBDSafetyFunction(tempIdAndVisioShapeHM, finalUnifiedConnectorLHM, uniqueConnectionIDSet, shapeNotGroupOrMember, shapeAsGroupLHS,
                basicShapeSet, idAndXDGFShapesHM, uniqueNullShapeSet);
        FBDSafetyFunction tempFBDSafetyFunction = tempBDSafetyFunction.getFinalFBDSafetyFunction();

        return tempFBDSafetyFunction;

    }

}
