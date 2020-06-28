/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Point2D;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.event.GraphListener;
import org.jgrapht.graph.*;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

/**
 *
 * @author pbpras
 */
class BasicConnector {

    String shapeId;
    String begFromShapeId;
    String endsToShapeId;
    Point2D PinPoint2D;
    Point2D begPoint2D;
    Point2D endPoint2D;

    public BasicConnector(String shapeId, Point2D begPoint2D, Point2D endPoint2D) {
        this.shapeId = shapeId;
        this.PinPoint2D = PinPoint2D;
        this.begPoint2D = begPoint2D;
        this.endPoint2D = endPoint2D;
    }

    public BasicConnector(String shapeId, String begShapeId, String endShapeId, Point2D begPoint2D, Point2D endPoint2D) {
        this.shapeId = shapeId;
        this.begFromShapeId = begShapeId;
        this.endsToShapeId = endShapeId;
        this.begPoint2D = begPoint2D;
        this.endPoint2D = endPoint2D;
    }

    public BasicConnector(String shapeId, String begShapeId, String endShapeId) {
        this.shapeId = shapeId;
        this.begFromShapeId = begShapeId;
        this.endsToShapeId = endShapeId;

    }
}

public class UnifyConnectors {

    private static LinkedHashMap<String, Point2D> shapesNotConnectorPointLHM = new LinkedHashMap<>();
    private static LinkedHashSet<MyRectangle> shapesNotConnectorRectangleHS = new LinkedHashSet<>();
    private static LinkedHashMap<String, Point2D> shapesConnectorPointLHM = new LinkedHashMap<>();
    private static LinkedHashMap<String, BasicConnector> fixedConnectorsLHM = new LinkedHashMap<>();
    private static LinkedList<String> connectorMidPoint = new LinkedList<>();
    private static LinkedHashMap<String, BasicConnector> finalUnifiedConnectorLHM = new LinkedHashMap<>();

    public static void fixedEndConnectors(Set<String> uniqueConnectionIDSet, HashMap<String, VisioShape> idAndVisioShapeHM, HashMap<String, XDGFShape> idAndXDGFShapesHM) {

        for (Map.Entry<String, XDGFShape> entry : idAndXDGFShapesHM.entrySet()) {
            String key = entry.getKey();
            XDGFShape val = entry.getValue();
            Point2D tempPoint2D = new Point2D(val.getPinX(), val.getPinY());
            if (uniqueConnectionIDSet.contains(key)) {
                shapesConnectorPointLHM.put(key, tempPoint2D);

            } else {
                shapesNotConnectorPointLHM.put(key, tempPoint2D);
                shapesNotConnectorRectangleHS.add(new MyRectangle(key, val.getPinX(), val.getPinY(), val.getWidth(), val.getHeight()));
            }
            if (val.getName().toLowerCase().contains("jako")) {
//                System.out.println("connector midpoint:" + key);
                connectorMidPoint.add(key);
            }

        }

        LinkedHashMap<String, BasicConnector> idPerfectConnector = new LinkedHashMap<>();

        for (String s : uniqueConnectionIDSet) {
            XDGFShape tempConnectorShape = idAndXDGFShapesHM.get(s);
//            Double pinX = tempConnectorShape.getPinX();
//            Double pinY = tempConnectorShape.getPinY();
            Double begPinX = tempConnectorShape.getBeginX();
            Double begPinY = tempConnectorShape.getBeginY();
            Double endPinX = tempConnectorShape.getEndX();
            Double endPinY = tempConnectorShape.getEndY();

//            Point2D currPinPoint2D = new Point2D(pinX, pinY);
            Point2D currBegPinPoint2D = new Point2D(begPinX, begPinY);
            Point2D currEndPinPoint2D = new Point2D(endPinX, endPinY);

//            System.out.println(GlobalVariable.ANSI_BLUE + "Connector's Pin details: ");
//            System.out.println("Pin of:" + s + " : " + " begPin: " + currBegPinPoint2D + "endPin: " + currEndPinPoint2D);

            HashMap<String, String> tempConnectionTriggerHM = idAndVisioShapeHM.get(s).connectionTriggerHM;

            String beginShape = tempConnectionTriggerHM.get("BegTrigger");
            String endShape = tempConnectionTriggerHM.get("EndTrigger");
//            System.out.println("connector ID: " + s + " has BegTrigger:" + beginShape + " EndTrigger: " + endShape);

            BasicConnector tempBasicConnector = new BasicConnector(s, currBegPinPoint2D, currEndPinPoint2D);

//            System.out.println("BegTrigger:" + beginShape + " EndTrigger: " + endShape);
            if ((endShape != null && !endShape.equalsIgnoreCase(s)) && (beginShape != null && !beginShape.equalsIgnoreCase(s))) {

                tempBasicConnector.begFromShapeId = beginShape;
                tempBasicConnector.endsToShapeId = endShape;
                idPerfectConnector.put(s, tempBasicConnector);
                fixedConnectorsLHM.put(s, tempBasicConnector);

            }
            if (endShape == null || endShape.equalsIgnoreCase(s)) {
//                System.out.println(ANSI_RED+"Wrong end ShapeID");
                tempBasicConnector.begFromShapeId = beginShape;
//                endShape = MyCustomFunctions.findNearestPinXYPoint(currEndPinPoint2D, shapesNotConnectorPointLHM);
                endShape = MyCustomFunctions.getNearestRectangleFromPoint(currEndPinPoint2D, shapesNotConnectorRectangleHS).shapeID;
                tempBasicConnector.endsToShapeId = endShape;
                fixedConnectorsLHM.put(s, tempBasicConnector);

            }
            if (beginShape == null || beginShape.equalsIgnoreCase(s)) {
                tempBasicConnector.begFromShapeId = MyCustomFunctions.findNearestPinXYPoint(currBegPinPoint2D, shapesNotConnectorPointLHM);
                tempBasicConnector.endsToShapeId = endShape;
                fixedConnectorsLHM.put(s, tempBasicConnector);
            }

        }

    }

    public static void printFixedConnector() {

        System.out.println("\n After fixing endshape:");
        for (Map.Entry<String, BasicConnector> entry : fixedConnectorsLHM.entrySet()) {

            String key = entry.getKey();
            BasicConnector val = entry.getValue();
            System.out.println(GlobalVariable.ANSI_BLUE + "Connector ID: " + key + " has BegTrigger:" + val.begFromShapeId + " EndTrigger: " + val.endsToShapeId);
//            System.out.println("BegTrigger:" + val.begFromShapeId + " EndTrigger: " + val.endsToShapeId);

        }
    }

    public static void unifyFixedEndConnector() {

        Graph<String, DefaultEdge> connectorUDGraph = new DefaultUndirectedGraph<>(DefaultEdge.class);

        LinkedHashMap<String, BasicConnector> tempfixedConnectorsLHM = fixedConnectorsLHM;

        for (Map.Entry<String, BasicConnector> entry : fixedConnectorsLHM.entrySet()) {
            String curConnectorID = entry.getKey();
            BasicConnector curBasicConnector = entry.getValue();

            if (!connectorUDGraph.containsVertex(curConnectorID)) {
                connectorUDGraph.addVertex(curConnectorID);
            }
            for (BasicConnector tempConnector : tempfixedConnectorsLHM.values()) {

                if (!curConnectorID.equalsIgnoreCase(tempConnector.shapeId)) {
                    if (!connectorUDGraph.containsVertex(tempConnector.shapeId)) {
                        connectorUDGraph.addVertex(tempConnector.shapeId);

                    }

                    if (curBasicConnector.begFromShapeId.equalsIgnoreCase(tempConnector.endsToShapeId) && connectorMidPoint.contains(curBasicConnector.begFromShapeId) && connectorMidPoint.contains(tempConnector.endsToShapeId)) {
                        connectorUDGraph.addEdge(curConnectorID, tempConnector.shapeId);
                    } else if (curBasicConnector.begFromShapeId.equalsIgnoreCase(tempConnector.begFromShapeId) && connectorMidPoint.contains(curBasicConnector.begFromShapeId) && connectorMidPoint.contains(tempConnector.begFromShapeId)) {
                        connectorUDGraph.addEdge(curConnectorID, tempConnector.shapeId);
                    } else if (curBasicConnector.endsToShapeId.equalsIgnoreCase(tempConnector.begFromShapeId) && connectorMidPoint.contains(curBasicConnector.endsToShapeId) && connectorMidPoint.contains(tempConnector.begFromShapeId)) {
                        connectorUDGraph.addEdge(curConnectorID, tempConnector.shapeId);
                    }

                }

            }

        }

        ConnectivityInspector<String, DefaultEdge> conAlg = new ConnectivityInspector<>(connectorUDGraph);
        List<Set<String>> connectedGroupHS = conAlg.connectedSets();
        Set<String> parentNode = new LinkedHashSet<>();
        LinkedHashMap<String, Set<String>> rootParentAllChildShapes = new LinkedHashMap<>();
//        System.out.println(GlobalVariable.ANSI_RED + " \nconnector group set:" + GlobalVariable.ANSI_RESET);
//        connectedGroupHS.forEach(item -> System.out.println(item));

        for (Set<String> conGrpSet : connectedGroupHS) {

            if (conGrpSet.size() > 1) {
                String headConShape = "";
                Set<String> tailConShape = new HashSet<>();
                for (String s : conGrpSet) {
                    BasicConnector tempConn = fixedConnectorsLHM.get(s);
                    if (!connectorMidPoint.contains(tempConn.begFromShapeId)) {
                        headConShape = s;
                    }
                    if (!connectorMidPoint.contains(tempConn.endsToShapeId)) {

                        tailConShape.add(s);
                    }
                }
                for (String tail : tailConShape) {

                    String shapeId = headConShape + "+" + tail;
                    String begShapeId = fixedConnectorsLHM.get(headConShape).begFromShapeId;
                    String endShapeId = fixedConnectorsLHM.get(tail).endsToShapeId;

                    Point2D begPoint2D = fixedConnectorsLHM.get(headConShape).begPoint2D;
                    Point2D endPoint2D = fixedConnectorsLHM.get(tail).endPoint2D;

                    BasicConnector finalConnector = new BasicConnector(shapeId, begShapeId, endShapeId, begPoint2D, endPoint2D);
                    finalUnifiedConnectorLHM.put(shapeId, finalConnector);

                }

            } else {
                String connShapeId = conGrpSet.iterator().next();

                BasicConnector tempConnector = fixedConnectorsLHM.get(connShapeId);
                BasicConnector finalConnector = new BasicConnector(tempConnector.shapeId, tempConnector.begFromShapeId, 
                        tempConnector.endsToShapeId, tempConnector.begPoint2D, tempConnector.endPoint2D);
                finalUnifiedConnectorLHM.put(connShapeId, finalConnector);

            }

        }

    }

    public static void printFinalUnifiedConnector() {
        System.out.println(GlobalVariable.ANSI_RED + "Printing all final Connector: \n");

        for (Map.Entry<String, BasicConnector> entry : finalUnifiedConnectorLHM.entrySet()) {
            String key = entry.getKey();
            BasicConnector val = entry.getValue();

            System.out.println(GlobalVariable.ANSI_RED + "final Connector:" + key + " begin from:" + val.begFromShapeId + " end to: " + val.endsToShapeId
            +" begPinXY:"+val.begPoint2D+" endPinXY:"+val.endPoint2D);

        }

    }

    public static LinkedHashMap<String, BasicConnector> getFinalUnifiedConnector() {

        if (finalUnifiedConnectorLHM.isEmpty()) {
            System.out.println(GlobalVariable.ANSI_BRIGHT_RED + " finalUnifiedConnectorLHM is empty");
            return null;
        } else {
            return finalUnifiedConnectorLHM;
        }

    }

    public static void assignVirtialConnector(LinkedHashSet<String> inputShapeId, LinkedHashSet<String> shapeNotGroupOrMember,
            HashMap<String, XDGFShape> idAndXDGFShapesHM) {
//        System.out.println(GlobalVariable.ANSI_RED+" inside UnifyConnector.assignVirtualConnector()");
        LinkedHashMap<String, Point2D> pinPointofShapeLHM = new LinkedHashMap<>();

        for (String s : shapeNotGroupOrMember) {

            Double PinX = idAndXDGFShapesHM.get(s).getPinX();
            Double PinY = idAndXDGFShapesHM.get(s).getPinY();
            Point2D tempPoint2D = new Point2D(PinX, PinY);
            pinPointofShapeLHM.put(s, tempPoint2D);

        }

        for (String s : inputShapeId) {
//            System.out.println(GlobalVariable.ANSI_RED + " inside UnifyConnector.assignVirtualConnector()");

            Double pinX = idAndXDGFShapesHM.get(s).getPinX();
            Double pinY = idAndXDGFShapesHM.get(s).getPinY();

            Point2D currPoint = new Point2D(pinX, pinY);
            String nearestShapeID = MyCustomFunctions.findNearestPinXYPoint(currPoint, pinPointofShapeLHM);
            System.out.println(s + " has Nearest shape:" + nearestShapeID);

        }

    }

}
