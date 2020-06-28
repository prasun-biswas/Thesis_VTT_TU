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
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import java.util.concurrent.*;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.event.GraphListener;
import org.jgrapht.graph.*;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

class BasicShape {

    String parentShapeId;
    String shapeId;
    String shapeType;
    List<String> childShapeIdList = new ArrayList<>();
    List<String> specificsOfConnectionList = new ArrayList<>();
    public BasicShape(String shapeId, String parentShapeId, List<String> childShapeIdList) {
        this.shapeId = shapeId;
        this.parentShapeId = parentShapeId;
        this.childShapeIdList = childShapeIdList;
    }

}

public class UnifyGroups {

    private static LinkedHashMap<String, Set<String>> rootParentAllChildShapes = new LinkedHashMap<>();
    private static ArrayList<BasicShape> unifiedGroupShapes = new ArrayList<>();
    private static Set<String> uniqueNullShapeSet = new LinkedHashSet<>();

    public static ArrayList<BasicShape> findParentAndChildGroup(LinkedHashSet<BasicShape> ipShapeSet, Set<String> uniqueNullShapeSet) {

        rootParentAllChildShapes.clear();
        unifiedGroupShapes.clear();
        Graph<String, DefaultEdge> directedGraph = new DefaultUndirectedGraph<>(DefaultEdge.class);
//                Graph<String,DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

//        Graph<String,DefaultEdge> directedGraph = new DirectedAcyclicGraph<>(DefaultEdge.class);
        System.out.println(GlobalVariable.ANSI_CYAN + "null shape ID:" + uniqueNullShapeSet);
        for (BasicShape currShape : ipShapeSet) {
            if (uniqueNullShapeSet.contains(currShape.shapeId)) {
                System.out.println(GlobalVariable.ANSI_CYAN + "null shape found" + currShape.shapeId);

            }

            if (!directedGraph.containsVertex(currShape.shapeId)) {
                directedGraph.addVertex(currShape.shapeId);
                if (!currShape.childShapeIdList.isEmpty()) {
//                    System.out.println(currShape.shapeId + " : " + currShape.childShapeIdList);
                    List<String> currChildList = currShape.childShapeIdList;
                    for (String sChildS : currChildList) {
                        directedGraph.addVertex(sChildS);
                        directedGraph.addEdge(currShape.shapeId, sChildS);
                    }

                }
            } else {
                if (!currShape.childShapeIdList.isEmpty()) {
//                    System.out.println(currShape.shapeId + " : " + currShape.childShapeIdList);
                    List<String> currChildList = currShape.childShapeIdList;
                    for (String sChildS : currChildList) {
                        directedGraph.addVertex(sChildS);
                        directedGraph.addEdge(currShape.shapeId, sChildS);
                    }

                }

            }

        }
//        StrongConnectivityAlgorithm<String, DefaultEdge> sConAlg = new KosarajuStrongConnectivityInspector<>(directedGraph);
//        List<Graph<String,DefaultEdge>> stronglyConnectedSubgraphs = sConAlg.getStronglyConnectedComponents();
//        List<Set<String>> stronglyConnectedSets = sConAlg.stronglyConnectedSets();

        ConnectivityInspector<String, DefaultEdge> conAlg = new ConnectivityInspector<>(directedGraph);
        List<Set<String>> connectedGroupHS = conAlg.connectedSets();
        Set<String> parentNode = new LinkedHashSet<>();
//        LinkedHashMap<String, Set<String>> rootParentAllChildShapes = new LinkedHashMap<>();
//        System.out.println("connected set:");
//        System.out.println(connectedGroupHS);

//        BreadthFirstIterator<String,DefaultEdge> bfItrAlg = new BreadthFirstIterator<>(directedGraph);
        BreadthFirstIterator graphItr = new BreadthFirstIterator<>(directedGraph);
        for (String curVertex : directedGraph.vertexSet()) {

            while (graphItr.hasNext()) {
                Object aObject = graphItr.next();
                try {
                    if (graphItr.getParent(aObject) != null) {
                        aObject = graphItr.getParent(aObject);

                    } else {
                        parentNode.add(aObject.toString());

                    }
                } catch (Exception e) {
                }

            }

        }
//        System.out.println("parent nodes: " + parentNode);

        for (String s : parentNode) {
            for (Set<String> set : connectedGroupHS) {
                if (set.contains(s)) {
//                    set.remove(s);

                    if (set.size() > 1) {

                        rootParentAllChildShapes.put("rootParent_" + s, set);
                        BasicShape tempGrouShape = new BasicShape(s, s, new ArrayList<>(set));
                        unifiedGroupShapes.add(tempGrouShape);
                    }

                }

            }

        }
//        for (Map.Entry<String, Set<String>> entry : rootParentAllChildShapes.entrySet()) {
//            String key = entry.getKey();
//            Set<String> value = entry.getValue();
//            List<String> listOfShapes = new ArrayList<>(value);
//            System.out.println(key + ": " + listOfShapes);
//        }

        return unifiedGroupShapes;

    }

    public static void printParentAndChildGroup(ArrayList<BasicShape> unifiedGroupShapes) {

        try {
            System.out.println("printing all unified groups ");
            for (BasicShape tempShape : unifiedGroupShapes) {
                System.out.println(GlobalVariable.ANSI_RED + tempShape.shapeId + ": has childs>>" + tempShape.childShapeIdList);
            }

        } catch (Exception e) {

            System.err.println(" fault in the input map of root parent and child");

        }

    }

    public static String getParentIdIfMemberFound(String shapeId) {
        if (unifiedGroupShapes.isEmpty()) {
            System.out.println("unifiedGroupShapes list is empty: ");
            return null;
        } else {
            Boolean found = false;
            String ParentID = "";
            for (BasicShape temShape : unifiedGroupShapes) {
                if (temShape.childShapeIdList.contains(shapeId)) {
                    found = true;
                    ParentID = temShape.shapeId;
                }
            }
            if (found) {
//                System.out.println(shapeId+ " is a member of: "+ ParentID);
                return ParentID;
            }
            return null;

        }

    }

}
