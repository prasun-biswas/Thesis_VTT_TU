/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

/**
 *
 * @author pbpras
 */
import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class GraphRepresentation {

    public static void displayElementIDInGraph(LinkedHashMap<String, FBDElement> finalFbdElementLHM, HashMap<String, VisioShape> idAndVisioShapeHM, LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        SingleGraph graph = new SingleGraph("Use");
        String styleSheet = "node { stroke-mode: plain; fill-color: white;shape: rounded-box;size-mode:fit ; text-size:8; padding: 1px, 1px;text-alignment:center;text-color: blue;text-style:bold;}"
                + "edge {arrow-shape: arrow; shape: cubic-curve;}";

        graph.addAttribute("ui.stylesheet", styleSheet);
        System.out.println("visiotoplcopen.GraphRepresentation.displayFbdElementsInGraph()");
        graph.setStrict(false);
        System.out.println("FBDelements in graph:"+ finalFbdElementLHM.keySet());
        for (Map.Entry<String, FBDElement> entry : finalFbdElementLHM.entrySet()) {
            String key = entry.getKey();
            FBDElement element = entry.getValue();
//            System.out.println(GlobalVariable.ANSI_BLUE + key + " has pin: " + element.pinX + "," + element.pinY);
//            System.out.println(element.shapeId + " has next elements: " + element.nextElementLHS + " has prev elements: " + element.prevElementLHS);

            org.graphstream.graph.Node tempNode = graph.addNode(key);
            tempNode.addAttribute("ui.label", key);
            tempNode.addAttribute("xyz", idAndXDGFShapesHM.get(key).getPinX(), idAndXDGFShapesHM.get(key).getPinY(), 0);

            for (String s : element.nextElementLHS) {
//                    System.out.println("           next of: "+key+" is s: "+s );
                try {
                    if (s != null) {

//                                System.out.println("already existing key: " + s);
                        org.graphstream.graph.Node tempNextNode = graph.addNode(s);
                        tempNextNode.addAttribute("ui.label", s);
                        tempNextNode.addAttribute("xyz", idAndXDGFShapesHM.get(s).getPinX(), idAndXDGFShapesHM.get(s).getPinY(), 0);
//                                tempNextNode.addAttribute("ui.style", "shape:circle;fill-color: red;size: 10px;");
//                                System.out.println(GlobalVariable.ANSI_BRIGHT_PURPLE + "added next node: " + key);
                        graph.addEdge(key + ":" + s, key, s, true);
//                        System.out.println("added next edge from: " + key + " to: " + s);

                    }
                } catch (Exception e) {
                    System.err.println("error in graphRepresentation.displayFBDElementsInGraph() ");
                    System.err.println("error in try block:-> 1 " + e);
                }

            }
            for (String s : element.prevElementLHS) {
                try {
                    if (s != null) {

                        org.graphstream.graph.Node tempNextNode = graph.addNode(s);
                        tempNextNode.addAttribute("ui.label", s);
                        tempNextNode.addAttribute("xyz", idAndXDGFShapesHM.get(s).getPinX(), idAndXDGFShapesHM.get(s).getPinY(), 0);
//                                tempNextNode.addAttribute("ui.style", "shape:circle;fill-color: red;size: 10px;");
//                                System.out.println(GlobalVariable.ANSI_BRIGHT_PURPLE + "added prev node: " + key);
                        graph.addEdge(key + ":" + s, s, key, true);
//                        System.out.println("added prev edge to: " + key + " from: " + s);

                    }
                } catch (Exception e) {
                    System.err.println("error in graphRepresentation.displayFBDElementsInGraph() ");
                    System.err.println("error in try block:-> 1 " + e);
                }

            }

        }

        graph.display(false);
        


    }
    
    // display identified function blocks with shape type
    // if shape is unknown then show the ID and display the block in red color
    
        public static void displayElementTypeInGraph(LinkedHashMap<String, FBDElement> finalFbdElementLHM,
                HashMap<String, VisioShape> idAndVisioShapeHM,
                LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM,
                LinkedHashMap<String, String> shapeIdAndTypeLHM){
            
            // predefine node type
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        SingleGraph graph = new SingleGraph("Use");
        String styleSheet = "node { stroke-mode: plain; fill-color: white;shape: rounded-box;size-mode:fit ; text-size:12; padding: 1px, 1px;text-alignment:center;text-color: blue;text-style:bold;}"
                + "edge {arrow-shape: arrow; shape: cubic-curve;}" 
                +"node.unknown { stroke-mode: plain; fill-color: white;shape: rounded-box;size-mode:fit ; text-size:12; padding: 1px, 1px;text-alignment:center;text-color: red;text-style:bold;}"
                + "edge {arrow-shape: arrow; shape: cubic-curve;}";

        graph.addAttribute("ui.stylesheet", styleSheet);
        System.out.println("visiotoplcopen.GraphRepresentation.displayFbdElementsInGraph()");
        graph.setStrict(false);
        for (Map.Entry<String, FBDElement> entry : finalFbdElementLHM.entrySet()) {
            String key = entry.getKey();
            FBDElement element = entry.getValue();
//            System.out.println(GlobalVariable.ANSI_BLUE + key + " has pin: " + element.pinX + "," + element.pinY);
//            System.out.println(element.shapeId + " has next elements: " + element.nextElementLHS + " has prev elements: " + element.prevElementLHS);

            org.graphstream.graph.Node tempNode = graph.addNode(key);
            String label = shapeIdAndTypeLHM.get(key);
            if(label.equalsIgnoreCase("unknown")){
                tempNode.setAttribute("ui.class", "unknown");
            }
            tempNode.addAttribute("ui.label", label);
            tempNode.addAttribute("xyz", idAndXDGFShapesHM.get(key).getPinX(), idAndXDGFShapesHM.get(key).getPinY(), 0);

            for (String s : element.nextElementLHS) {
//                    System.out.println("           next of: "+key+" is s: "+s );
                try {
                    if (s != null) {
                        
//                                System.out.println("already existing key: " + s);
                        org.graphstream.graph.Node tempNextNode = graph.addNode(s);
                        String inlabel = shapeIdAndTypeLHM.get(s);
                        if(inlabel.equalsIgnoreCase("unknown")){
                            tempNextNode.setAttribute("ui.class", "unknown");
                        }
                        tempNextNode.addAttribute("ui.label", inlabel);
                        tempNextNode.addAttribute("xyz", idAndXDGFShapesHM.get(s).getPinX(), idAndXDGFShapesHM.get(s).getPinY(), 0);
//                                tempNextNode.addAttribute("ui.style", "shape:circle;fill-color: red;size: 10px;");
//                                System.out.println(GlobalVariable.ANSI_BRIGHT_PURPLE + "added next node: " + key);
                        graph.addEdge(key + ":" + s, key, s, true);
//                        System.out.println("added next edge from: " + key + " to: " + s);

                    }
                } catch (Exception e) {
                    System.err.println("error in graphRepresentation.displayFBDElementsInGraph() ");
                    System.err.println("error in try block:-> 1 " + e);
                }

            }
            for (String s : element.prevElementLHS) {
                try {
                    if (s != null) {
                        org.graphstream.graph.Node tempNextNode = graph.addNode(s);
                        String inlabel = shapeIdAndTypeLHM.get(s);
                        if(inlabel.equalsIgnoreCase("unknown")){
                            tempNextNode.setAttribute("ui.class", "unknown");
                        }
                        tempNextNode.addAttribute("ui.label", inlabel);
                        tempNextNode.addAttribute("xyz", idAndXDGFShapesHM.get(s).getPinX(), idAndXDGFShapesHM.get(s).getPinY(), 0);
//                                tempNextNode.addAttribute("ui.style", "shape:circle;fill-color: red;size: 10px;");
//                                System.out.println(GlobalVariable.ANSI_BRIGHT_PURPLE + "added prev node: " + key);
                        graph.addEdge(key + ":" + s, s, key, true);
//                        System.out.println("added prev edge to: " + key + " from: " + s);

                    }
                } catch (Exception e) {
                    System.err.println("error in graphRepresentation.displayFBDElementsInGraph() ");
                    System.err.println("error in try block:-> 1 " + e);
                }

            }

        }

        graph.display(false);
        


    }
    

}
