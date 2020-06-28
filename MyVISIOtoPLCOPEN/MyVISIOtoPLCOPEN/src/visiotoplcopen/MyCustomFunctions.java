/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Point2D;

/**
 *
 * @author pbpras
 */
public class MyCustomFunctions {

    public static String findNearestPinXYPoint(Point2D centerPoint, LinkedHashMap<String, Point2D> inputPointsLHM) {

        //this finds the nearest point from a input point and linkedhashmap<shapeID, Point2D> and returns 
        //the shapeId of the nearest point within the input collection. the collection can be different.
        String nearestShapeID = "";
        Double lowDistance = Double.POSITIVE_INFINITY;

        Iterator<Map.Entry<String, Point2D>> pIt = inputPointsLHM.entrySet().iterator();

        while (pIt.hasNext()) {

            Map.Entry<String, Point2D> next = pIt.next();
            String id = next.getKey();
            Point2D point = next.getValue();

//            System.out.println(id+":"+point);
            Double tempdist = point.distance(centerPoint);
            if (tempdist < lowDistance && tempdist != 0) {
                lowDistance = tempdist;
                nearestShapeID = id;
            }

        }

        return nearestShapeID;
    }

    public static boolean areAllMatchedBetweenSets(Set<String> first, Set<String> second) {

        if (first.containsAll(second)) {
            return true;
        } else if (second.containsAll(first)) {
            return true;
        }
        return false;
    }

    public static boolean areStringsAnagram(String a, String b) {
        // Complete the function
      if (a.length() != b.length()) {
        return false;
    }
    char[] ca = a.toCharArray();
    char[] cb = b.toCharArray();
    Arrays.sort(ca);
    Arrays.sort(cb);
    return Arrays.equals(ca, cb);
    }

    public static void canItbeGroup(HashSet<MyRectangle> ipRectangles) {

//        HashSet<RECTANGLE> copyRectanglesHS = ipRectangles;
//        HashSet<String> alreadyPartOfGroupHS = new HashSet<>(); 
        Deque<MyRectangle> ipDeque = new ArrayDeque<>(ipRectangles);
        List<Set<String>> potentialGrpSet = new ArrayList<>();

        for (MyRectangle currRectangle : ipDeque) {
            Set<String> tempGrpMbrShapeID = new HashSet<>();
            MyRectangle tempRect = ipDeque.pop();
            for (MyRectangle r : ipDeque) {
                if (tempRect.areShapesFriend(r)) {
                    tempGrpMbrShapeID.add(r.shapeID);
                    tempGrpMbrShapeID.add(tempRect.shapeID);

                }

            }
            if (tempGrpMbrShapeID.size() > 1) {
//                System.out.println(tempGrpMbrShapeID);
                for (Set<String> grpSet : potentialGrpSet) {
                    for (String s : tempGrpMbrShapeID) {
                        if (grpSet.contains(s)) {
                            grpSet.addAll(tempGrpMbrShapeID);
                            break;
                        }

                    }
                }
                potentialGrpSet.add(tempGrpMbrShapeID);

            }

//            System.out.println(potentialGrpSet);
            for (int i = 0; i < potentialGrpSet.size(); i++) {
                Set<String> tempSet_i = potentialGrpSet.get(i);
                for (int j = i + 1; j < potentialGrpSet.size(); j++) {
                    Set<String> tempSet_j = potentialGrpSet.get(j);
                    if (areAllMatchedBetweenSets(tempSet_i, tempSet_j)) {
                        potentialGrpSet.remove(j);
                    }
//                    if (tempSet_i.containsAll(tempSet_j)) {
//                        potentialGrpSet.remove(j);
//                    }

                }

            }

        }
        System.out.println("final: " + potentialGrpSet);

    }

    public static boolean areShapesFriend(MyRectangle ipRect1, MyRectangle ipRect2) {

//        System.out.println("center:"+ipRect2.pinX+ipRect2.pinY+"\n h&W: "+ipRect2.reachHeight+","+ipRect2.reachWidth);
        if (((ipRect1.reachWidth + ipRect2.reachWidth) >= Math.abs(ipRect1.pinX - ipRect2.pinX))
                && ((ipRect1.reachHeight + ipRect2.reachHeight) >= Math.abs(ipRect1.pinY - ipRect2.pinY))) {
//            System.out.println(this.shapeID +" friend shape of "+ipRect2.shapeID);
            return true;
        } else if ((((ipRect1.pinX + ipRect1.reachWidth) == (ipRect2.pinX - ipRect2.reachWidth))
                || ((ipRect2.pinX + ipRect2.reachWidth) == (ipRect1.pinX - ipRect1.reachWidth)))
                && ((ipRect1.reachHeight + ipRect2.reachHeight) >= Math.abs(ipRect1.pinY - ipRect2.pinY))) {
//            System.out.println(this.shapeID +" friend shape of width "+ipRect2.shapeID);
            return true;
        } else if ((((ipRect1.pinY + ipRect1.reachHeight) == (ipRect2.pinY - ipRect2.reachHeight))
                || ((ipRect2.pinY + ipRect2.reachHeight) == (ipRect1.pinY - ipRect1.reachHeight)))
                && ((ipRect1.reachWidth + ipRect2.reachWidth) >= Math.abs(ipRect1.pinX - ipRect2.pinX))) {
//            System.out.println(this.shapeID +" friend shape of height "+ipRect2.shapeID);
            return true;
        } else {
//            System.out.println("distant shapes");
            return false;

        }

    }

    public static double minDistPointToLineSegment(Point2D p, Point2D end1, Point2D end2) {
        // need to clarify this concept more  

        Double distSquared = Math.pow(end1.getX() - end2.getX(), 2) + Math.pow(end1.getY() - end2.getY(), 2);
        if (distSquared == 0) {
            return Math.sqrt(Math.pow(end1.getX() - p.getX(), 2) + Math.pow(end1.getY() - p.getY(), 2));
        }

        Double tan = ((p.getX() - end1.getX()) * ((end2.getX() - end1.getX())) + (p.getY() - end1.getY()) * ((end2.getY() - end1.getY()))) / distSquared;

        if (tan < 0) {
            return Math.sqrt(Math.pow(end1.getX() - p.getX(), 2) + Math.pow(end1.getY() - p.getY(), 2));
        } else if (tan > 1.0) {
            return Math.sqrt(Math.pow(end2.getX() - p.getX(), 2) + Math.pow(end2.getY() - p.getY(), 2));
        } else {

            Point2D projectionPoint = new Point2D(end1.getX() + tan * (end2.getX() - end1.getX()), end1.getY() + tan * (end2.getY() - end1.getY()));
            return Math.sqrt(Math.pow(projectionPoint.getX() - p.getX(), 2) + Math.pow(projectionPoint.getY() - p.getY(), 2));
        }

    }

    public static double minDistPointToRectangle(Point2D ipPoint, MyRectangle ipRect) {

        ArrayList<Double> distances = new ArrayList<>();

        try {
            distances.add(minDistPointToLineSegment(ipPoint, ipRect.bottomLeftPoint2D, ipRect.bottomRightPoint2D));
            distances.add(minDistPointToLineSegment(ipPoint, ipRect.topLeftPoint2D, ipRect.toprightPoint2D));
            distances.add(minDistPointToLineSegment(ipPoint, ipRect.bottomLeftPoint2D, ipRect.topLeftPoint2D));
            distances.add(minDistPointToLineSegment(ipPoint, ipRect.bottomRightPoint2D, ipRect.toprightPoint2D));

        } catch (Exception e) {
            System.out.println("error in minDistPointToRectangle()");
        }

        return Collections.min(distances);
    }

    public static boolean doesLineSegmentsIntersect(Point2D begLine1, Point2D endLine1, Point2D begLine2, Point2D endLine2) {

//      logic found in following link (need more understanding of this logic but seems to work flawless)
//      "math.stackexchange.com/questions/162728/how-to-determine-if-2-points-are-on-opposite-sides-of-a-line"
//      Explicitly, they are on opposite sides if --> ((y1−y2)(ax−x1)+(x2−x1)(ay−y1))((y1−y2)(bx−x1)+(x2−x1)(by−y1))<0.
        Double ax = begLine1.getX();
        Double ay = begLine1.getY();
        Double bx = endLine1.getX();
        Double by = endLine1.getY();
        Double x1 = begLine2.getX();
        Double y1 = begLine2.getY();
        Double x2 = endLine2.getX();
        Double y2 = endLine2.getY();

        if ((((y1 - y2) * (ax - x1) + (x2 - x1) * (ay - y1)) * ((y1 - y2) * (bx - x1) + (x2 - x1) * (by - y1)) < 0)
                && (((ay - by) * (x1 - ax) + (ax - bx) * (y1 - ay)) * ((ay - by) * (x2 - ax) + (ax - bx) * (y2 - ay)) < 0)) {

            return true;
        }

        return false;
    }

    public static double minDistOfTwoLineSegments(Point2D begLine1, Point2D endLine1, Point2D begLine2, Point2D endLine2) {

        ArrayList<Double> distances = new ArrayList<>();
        if (doesLineSegmentsIntersect(begLine1, endLine1, begLine2, endLine2)) {
            return 0;
        }
        try {
            distances.add(minDistPointToLineSegment(begLine1, begLine2, endLine2));
            distances.add(minDistPointToLineSegment(endLine1, begLine2, endLine2));
            distances.add(minDistPointToLineSegment(begLine2, begLine1, endLine1));
            distances.add(minDistPointToLineSegment(endLine2, begLine1, endLine1));
        } catch (Exception e) {
            System.out.println("error in minDistOfTwoLineSegment()");
        }

        return Collections.min(distances);
    }

    public static double minDistOfTwoRectangles(MyRectangle rect1, MyRectangle rect2) {

        ArrayList<Double> distances = new ArrayList<>();
        try {
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.bottomRightPoint2D, rect2.bottomLeftPoint2D, rect2.bottomRightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.bottomRightPoint2D, rect2.topLeftPoint2D, rect2.toprightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.bottomRightPoint2D, rect2.bottomLeftPoint2D, rect2.topLeftPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.bottomRightPoint2D, rect2.bottomRightPoint2D, rect2.toprightPoint2D));

            distances.add(minDistOfTwoLineSegments(rect1.topLeftPoint2D, rect1.toprightPoint2D, rect2.bottomLeftPoint2D, rect2.bottomRightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.topLeftPoint2D, rect1.toprightPoint2D, rect2.topLeftPoint2D, rect2.toprightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.topLeftPoint2D, rect1.toprightPoint2D, rect2.bottomLeftPoint2D, rect2.topLeftPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.topLeftPoint2D, rect1.toprightPoint2D, rect2.bottomRightPoint2D, rect2.toprightPoint2D));

            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.topLeftPoint2D, rect2.bottomLeftPoint2D, rect2.bottomRightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.topLeftPoint2D, rect2.topLeftPoint2D, rect2.toprightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.topLeftPoint2D, rect2.bottomLeftPoint2D, rect2.topLeftPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomLeftPoint2D, rect1.topLeftPoint2D, rect2.bottomRightPoint2D, rect2.toprightPoint2D));

            distances.add(minDistOfTwoLineSegments(rect1.bottomRightPoint2D, rect1.toprightPoint2D, rect2.bottomLeftPoint2D, rect2.bottomRightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomRightPoint2D, rect1.toprightPoint2D, rect2.topLeftPoint2D, rect2.toprightPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomRightPoint2D, rect1.toprightPoint2D, rect2.bottomLeftPoint2D, rect2.topLeftPoint2D));
            distances.add(minDistOfTwoLineSegments(rect1.bottomRightPoint2D, rect1.toprightPoint2D, rect2.bottomRightPoint2D, rect2.toprightPoint2D));

        } catch (Exception e) {
            System.err.println("error in minDistOfTwoRectangles()");
        }

        return Collections.min(distances);
    }

    public static MyRectangle getNearestRectangleFromPoint(Point2D ipPoint2D, HashSet<MyRectangle> rectHS) {

        MyRectangle currentNearest = null;
        Double minDistance = Double.POSITIVE_INFINITY;
        try {
            for (MyRectangle tempRect : rectHS) {
                double tempDist = minDistPointToRectangle(ipPoint2D, tempRect);
                if (tempDist < minDistance) {
                    minDistance = tempDist;
                    currentNearest = tempRect;
                }
            }
        } catch (Exception e) {
            System.err.println("error in getNearestRectangleFromPoint()" + e);
        }

        return currentNearest;

    }

    public static MyRectangle getNearestRectangleFromRectangle(MyRectangle ipRectangle, HashSet<MyRectangle> rectHS) {
        MyRectangle currentNearest = null;
        Double minDiatance = Double.POSITIVE_INFINITY;
        try {
            for (MyRectangle tempRect : rectHS) {
                if (!ipRectangle.shapeID.equalsIgnoreCase(tempRect.shapeID)) {
                    double tempDist = minDistOfTwoRectangles(ipRectangle, tempRect);
                    if (tempDist < minDiatance) {
                        minDiatance = tempDist;
                        currentNearest = tempRect;
                    }
                }

            }

        } catch (Exception e) {
            System.err.println("error in getNearestRectangleFromRectangle()" + e);
        }
        return currentNearest;
    }

}
