/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javafx.util.Pair;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import sun.security.krb5.internal.KDCOptions;

/**
 *
 * @author pbpras
 */
public class SearchTask_1_1 {

    public static Pair<String, Double> getElementByIndex(LinkedHashMap map, int index) {
        Object Key = map.keySet().toArray()[index - 1];
        Object value = map.get(Key);
        Pair keyValPair = new Pair(Key, value);
        return keyValPair;
    }

    public static Pair<String, Double> getTopRectangleInOrder(LinkedHashSet<MyRectangle> ipRectanglesHS, int index) {
        LinkedHashMap<String, Double> RectIDAndPinY = new LinkedHashMap<>();

        for (MyRectangle tempRect : ipRectanglesHS) {
            RectIDAndPinY.put(tempRect.shapeID, tempRect.pinY);
        }

//        Stream<Map.Entry<String, Double>> sortedMap = RectIDAndPinY.entrySet().stream().sorted(Map.Entry.comparingByValue());
        Stream<Map.Entry<String, Double>> sortedMapReverse = RectIDAndPinY.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
//        LinkedHashMap<String, Double> sortedMapLHM = new LinkedHashMap<>();
        LinkedHashMap<String, Double> sortedMapReverseLHM = new LinkedHashMap<>();

//        System.out.println(sortedMap);
        System.out.println("Sorted map: \n");
//        sortedMap.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
//        sortedMap.forEach(entry -> sortedMapLHM.put(entry.getKey(), entry.getValue()));
        sortedMapReverse.forEach(entry -> sortedMapReverseLHM.put(entry.getKey(), entry.getValue()));

//        sortedMapReverse.forEach(entry -> System.out.println(entry.getKey()+": "+ entry.getValue()));
//        System.out.println("Sorted LinkedHashMap: \n");
//        sortedMapLHM.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        });
        System.out.println("SortedReverse LinkedHashMap: \n");

        sortedMapReverseLHM.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });

        System.out.println("First Element:" + getElementByIndex(sortedMapReverseLHM, 0));

//        return getElementByIndex(sortedMapReverseLHM, 0);
        return null;

    }

    private static boolean doesPatternMatch(String ipPattern, String text) {
        Pattern currPattern = Pattern.compile(ipPattern);
        String[] tempTextContent = text.split(",");
        for (String partialText : tempTextContent) {
            if (currPattern.matcher(partialText).matches()) {
//                System.out.println(" pattern matched: "+ text);
                return true;
            }

        }

        return false;
    }

    public static void arrangeExternalFunction(LinkedHashMap<String, BasicShape> externalSafetyFunction, HashMap<String, VisioShape> idAndVisioShapeHM,
            LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM,
            LinkedHashMap<String, FBDElement> finalFbdElementLHM,
            LinkedHashMap<String, String> shapeIdAndTypeLHM) {

        LinkedHashMap<String, BasicShape> externalSafetyFunctionLHM = externalSafetyFunction;
        LinkedHashMap<String, FBDElement> storeLocalExternalFunctionLHM = new LinkedHashMap<>();

        Pattern currPattern = Pattern.compile("([0-9a-zA-Z]*[_]{1}[0-9a-zA-Z.]*)");
        String currPatternStr = "([0-9a-zA-Z]*[_]{1}[0-9a-zA-Z.]*)";

        System.out.println("inside SearchTask_1_1: arrangeExternalFunction()" + "\n");
        System.out.println("size of externalSafetyFunction: " + externalSafetyFunction.size());
        System.out.println("size of finalFbdElementLHM: " + finalFbdElementLHM.size() + "\n");

//        for (Map.Entry<String, FBDElement> entry : finalFbdElementLHM.entrySet()) {
//            String key = entry.getKey();
//            FBDElement val = entry.getValue();
//            System.out.println(key+", "+val.shapeType+", "+ val.textContent);
//            
//        }
//        System.out.println("FBDElements: "+ finalFbdElementLHM.keySet());
        try {
            for (Map.Entry<String, BasicShape> entry : externalSafetyFunction.entrySet()) {
                String CurrShapeId = entry.getKey();
                BasicShape CurrBasicShape = entry.getValue();
//                System.out.println("current shapeID: " + CurrBasicShape.parentShapeId + ", " + CurrBasicShape.shapeId);

//                LinkedHashSet<MyRectangle> ipRectanglesHS = new LinkedHashSet<>();
                FBDElement tempExternalFuncFBD = new FBDElement(); // to store the values of external functions

                for (String S : CurrBasicShape.childShapeIdList) {
//                    System.out.println(CurrBasicShape.shapeId + " has ChildShape:" + S + "\n");
                    VisioShape tempVisioShape = idAndVisioShapeHM.get(S);
                    XDGFShape tempXDGFShape = idAndXDGFShapesHM.get(S);
                    if (!tempVisioShape.textContent.isEmpty()) {
//                        System.out.println("text content:" + tempVisioShape.textContent);
//                    MyRectangle tempRectangle = new MyRectangle(S, tempXDGFShape.getPinX(), tempXDGFShape.getPinY(), tempXDGFShape.getWidth(), tempXDGFShape.getHeight());
//                    ipRectanglesHS.add(tempRectangle);
//                    System.out.println(S+" has dimentions:"+"X:"+tempRectangle.pinX+",Y:"+ tempRectangle.pinY+",W:"+ tempRectangle.reachWidth+",H:"+ tempRectangle.reachHeight);
//                    System.out.println("text inside top rectangle: " + getTopRectangleInOrder(ipRectanglesHS, 0));
                        if (doesPatternMatch(currPatternStr, tempVisioShape.textContent)) {
//                            System.out.println(GlobalVariable.ANSI_CYAN + " pattern matched: " + tempVisioShape.textContent);
                            tempExternalFuncFBD.shapeId = idAndXDGFShapesHM.get(S).getTextAsString().replace("\n", "");
//                            tempExternalFuncFBD.shapeId = tempVisioShape.textContent;

                        } else {
//                            System.out.println(GlobalVariable.ANSI_CYAN + " Shape details: " + tempVisioShape.textContent);
                            tempExternalFuncFBD.textContent = idAndXDGFShapesHM.get(S).getTextAsString().replace("\n", "");

                        }
                    }
                }

                FBDElement tempFBDElement = finalFbdElementLHM.get(CurrShapeId);
                if (tempFBDElement.prevElementLHS.isEmpty() && !tempFBDElement.nextElementLHS.isEmpty()) {
                    tempExternalFuncFBD.shapeType = "input";
                    String Connected_to = "";
                    System.out.println("next shape: " + tempFBDElement.nextElementLHS);
                    for (String s : tempFBDElement.nextElementLHS) {
//                        System.out.println("Connected shape ID:" + s + " type: " + shapeIdAndTypeLHM.get(s));
//                        System.out.println(finalFbdElementLHM.get(s).shapeType);
                        if (finalFbdElementLHM.get(s) != null) {
                            Connected_to += finalFbdElementLHM.get(s).shapeType;
                            Connected_to += " : Text:" + finalFbdElementLHM.get(s).textContent.replace("\n", "");
                        }

                    }
                    tempExternalFuncFBD.moreDetails = Connected_to;

                } else if (!tempFBDElement.prevElementLHS.isEmpty() && tempFBDElement.nextElementLHS.isEmpty()) {
                    tempExternalFuncFBD.shapeType = "output";
                    String Connected_to = "";
                    System.out.println("previous shape: " + tempFBDElement.prevElementLHS);
                    for (String s : tempFBDElement.prevElementLHS) {
//                        System.out.println("Connected shape ID:" + s + " type: " + shapeIdAndTypeLHM.get(s));
//                        System.out.println(finalFbdElementLHM.get(s).shapeType);
                        if (finalFbdElementLHM.get(s) != null) {
                            Connected_to += finalFbdElementLHM.get(s).shapeType;
                            Connected_to += " : Text:" + finalFbdElementLHM.get(s).textContent.replace("\n", "");

                        }

//
                    }
                    tempExternalFuncFBD.moreDetails = Connected_to;

                } else {
                    System.err.println("something wrong, maybe not connected with any shapes");
                }

                storeLocalExternalFunctionLHM.put(CurrShapeId, tempExternalFuncFBD);
            }

        } catch (Exception e) {
            System.err.println("error in SearchTask_1_1 arrangeExternalFunction() classification" + e);
        }

        try {
            System.out.println("\n");
            System.out.println("Located inside safety function: ");
            System.out.println("number of external safety function: " + storeLocalExternalFunctionLHM.size() + "\n");

            for (Map.Entry<String, FBDElement> entry : storeLocalExternalFunctionLHM.entrySet()) {
                String key = entry.getKey();
                FBDElement externalFunction = entry.getValue();
                System.out.println("\t" + "Function: " + externalFunction.shapeId + ",  DIRECTION:>  " + externalFunction.shapeType
                        + ",  NAME: " + "[" + externalFunction.textContent + "]" + ",  Connected to > " + externalFunction.moreDetails);

            }

            System.out.println("\n");
            System.out.println("\n");

        } catch (Exception e) {
            System.err.println("error in SearchTask_1_1 arrangeExternalFunction() printing" + e);

        }

    }

    public static void arrangeRedundantInput(LinkedHashMap<String, BasicShape> ab_ipSigProcessLHM, HashMap<String, VisioShape> idAndVisioShapeHM, LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM, LinkedHashMap<String, FBDElement> finalFbdElementLHM, LinkedHashMap<String, String> shapeIdAndTypeLHM) {
        
        HashMap<String,VisioShape> local_idAndVisioShapeHM = idAndVisioShapeHM;
        
        for(Map.Entry<String, BasicShape> entry: ab_ipSigProcessLHM.entrySet()){
            String currShapeID = entry.getKey();
            BasicShape currShape = entry.getValue();
        
        }
        
    }
    
  

}
