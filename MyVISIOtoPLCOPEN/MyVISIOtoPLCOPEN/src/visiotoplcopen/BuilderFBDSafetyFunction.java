/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/**
 *
 * @author pbpras
 */
public class BuilderFBDSafetyFunction {

    private static LinkedHashSet<String> uniqueConnectionIDSet = new LinkedHashSet<>();
    private static LinkedHashMap<String, BasicConnector> finalUnifiedConnectorLHM = new LinkedHashMap<>();
    private static HashMap<String, VisioShape> idAndVisioShapeHM = new LinkedHashMap<>();
    private static LinkedHashSet<String> shapeNotGroupOrMemberHS = new LinkedHashSet<>();
    private static LinkedHashSet<String> shapeAsGroupLHS = new LinkedHashSet<>();
    private static LinkedHashSet<BasicShape> basicShapeSet = new LinkedHashSet<>();
    private static LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM = new LinkedHashMap<>();
    private static LinkedHashSet<String> uniqueNullShapeSet = new LinkedHashSet<>();
    private static LinkedHashSet<String> shapeAsBooleanOrAndHS = new LinkedHashSet<>();
    private static LinkedHashSet<String> shapeAsInversionHS = new LinkedHashSet<>();
    private static LinkedHashSet<String> shapeAsSummationHS = new LinkedHashSet<>();
    private static LinkedHashSet<String> shapeAsSignHS = new LinkedHashSet<>();
    private static LinkedHashSet<String> shapeAsCircleHS = new LinkedHashSet<>();
    private static LinkedHashMap<String, String> shapeIdAndTypeLHM = new LinkedHashMap<>(); // store all id and type of initially identified shape before building FBD elements
    private static LinkedHashMap<String, FBDElement> finalFbdElementLHM = new LinkedHashMap<>(); // stores all identified final FBD elements to display in a graph 
    private static ArrayList<BasicShape> unifiedShapes = new ArrayList<>(); // stores the unified groups as basic shape(contains parent shapes and all nested child shapes)
    private static LinkedHashMap<String, BasicShape> unifiedShapesLHM = new LinkedHashMap<>();// stores from "unifiedShapes" in Linked hash map
    private static HashSet<MyRectangle> rectHS = new LinkedHashSet<>(); // stores all rectangle to find nearest shapes
    private static LinkedHashMap<String, BasicShape> externalSafetyFunctionLHM = new LinkedHashMap<>();// stores all external safety functions to use for Antti's SEARCH task1.1
    private static LinkedHashMap<String, BasicShape> ab_ipSigProcessLHM = new LinkedHashMap<>(); // stores all redundant input
    public BuilderFBDSafetyFunction(
            HashMap<String, VisioShape> ipIdAndVisioShapeHM,
            LinkedHashMap<String, BasicConnector> finalUnifiedConnector,
            LinkedHashSet<String> ipUniqueConnectionIDSet,
            LinkedHashSet<String> ipShapeNotGroupOrMember,
            LinkedHashSet<String> ipShapeAsGroupLHS,
            LinkedHashSet<BasicShape> ipBasicShapeSet,
            LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM,
            LinkedHashSet<String> uniqueNullShapeSet
    ) {
        this.finalUnifiedConnectorLHM = finalUnifiedConnector;
        this.uniqueConnectionIDSet = ipUniqueConnectionIDSet;
        this.idAndVisioShapeHM = ipIdAndVisioShapeHM;
        this.shapeNotGroupOrMemberHS = ipShapeNotGroupOrMember;
        this.shapeAsGroupLHS = ipShapeAsGroupLHS;
        this.basicShapeSet = ipBasicShapeSet;
        this.idAndXDGFShapesHM = idAndXDGFShapesHM;
        this.uniqueNullShapeSet = uniqueNullShapeSet;

    }

    public static HashMap<String, VisioShape> getIdAndVisioShapeHM() {

        return idAndVisioShapeHM;
    }

    public static FBDSafetyFunction getFinalFBDSafetyFunction() {
        // temporary variables are declared below for building FBDSafetyFunction

        String tempFBDSafetyFunctionPageID = "Diagram";
        FBDSafetyFunction tempFBDSafetyFunction = new FBDSafetyFunction(tempFBDSafetyFunctionPageID);

        LinkedList<String> idRecognizedShapeNotGrpOrMbrLL = new LinkedList<>();// stores identified singular shape from "shapeNotGroupOrMember" which have text inside
        LinkedList<String> idNotRecognizedShapeNotGrpOrMbrLL = new LinkedList<>();// stores unidentified singular shapes from "shapeNotGroupOrMember" to use later
        LinkedList<String> idRecognizedDelayTimerValueLL = new LinkedList<>(); // stores identified delay timer value blocks the value will later be assigned to nearest timer shape
        LinkedList<String> idRecognizedLimitCheckerLL = new LinkedList<>(); // stores identified limit checker to use later
        LinkedList<String> idRecognizedMINCheckerLL = new LinkedList<>(); // stores identified "MIN" checker to use later

        //variables
        try {
            //try block 1
            //builds FBDConnection visioShape and connectionID

//            System.out.println(" accepted connections inside BuilderFBDSafetyFunction: ");
            for (String s : uniqueConnectionIDSet) {

//                System.out.println(s);
                VisioShape tempVisioShape = idAndVisioShapeHM.get(s);
                FBDConnection tempFBDConnection = new FBDConnection();
                tempFBDConnection.shapeID = s;
                tempFBDConnection.begTriggerID = tempVisioShape.connectionTriggerHM.get("BegTrigger");
                tempFBDConnection.endTriggerID = tempVisioShape.connectionTriggerHM.get("EndTrigger");

                tempFBDSafetyFunction.fBDConnectionsLHM.put(s, tempFBDConnection);

            }

            System.out.println("number of FBDConnection: " + tempFBDSafetyFunction.fBDConnectionsLHM.size() + "\n");

        } catch (Exception e) {
            System.out.println("Error in FBDSafetyFunctionBuilder on ");
            System.out.println(" \n error in: try block:-> 1" + e);

        }

        try {
            // finds out logical "AND"
            // will be modified to find logical "OR"
            // delay timer block's value (next to actual delay block)
            // limit checker blocks(checks if a input is greater/smaller that set limit), 
            // MIN checker (is a comparator that sends the minimum value to next input
            // 
//            System.out.println(" \n try block:-> 2");

            IdentificationFunctions tempIdFunc = new IdentificationFunctions(idAndVisioShapeHM, idAndXDGFShapesHM);

            for (String s : shapeNotGroupOrMemberHS) {
//                System.out.println(s);

                VisioShape tempShape = idAndVisioShapeHM.get(s);

//          System.out.println("id: "+tempVisioShape.ShapeID+" : "+tempVisioShape.textContent+":"+tempVisioShape.currentShape.getSymbolName());
                if (IdentificationFunctions.isItBooleanOrAnd(tempShape)) {//finds boolean logic AND
//                    System.out.println("found a boolean AND/OR");
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "Boolean");
                    idRecognizedShapeNotGrpOrMbrLL.add(tempShape.ShapeID);
                    shapeAsBooleanOrAndHS.add(s);
                    FBDBooleanLogic tempBooleanLogic = new FBDBooleanLogic();
                    tempBooleanLogic.shapeID = tempShape.ShapeID;
                    tempBooleanLogic.isAND = true;

//          logicalAndLL.add(tempVisioShape);
                    tempFBDSafetyFunction.fBDBooleanLogicsLHM.put(s, tempBooleanLogic);

//                    String strDescription = tempShape.textContent
//                            + "\n Pin(x,y):" + tempShape.currentShape.getPinX() + "," + tempShape.currentShape.getPinY()
//                            + "\n h:" + tempShape.currentShape.getHeight() + ", w:" + tempShape.currentShape.getWidth();
////                    System.out.println(""+strDescription);
//                    tempFBDSafetyFunction.idFbdElementNotConnection.put(s, strDescription);
                } else if (IdentificationFunctions.isItValueOfDelay(tempShape)) {
                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    idRecognizedDelayTimerValueLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "value_of_delay");

                } else if (IdentificationFunctions.isItLimitChecker(tempShape)) {
                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    idRecognizedLimitCheckerLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "limit_checker");

                } else if (IdentificationFunctions.isItMINchecker(tempShape)) {
                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    idRecognizedMINCheckerLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "min_checker");

                } else if (IdentificationFunctions.isItVotingLogic(tempShape)) {
                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "voting_logic");
                    FBDVotingLogic tempVotingLogic = new FBDVotingLogic();
                    tempVotingLogic.shapeID = s;
                    tempVotingLogic.votingLogic = tempShape.textContent;

//          logicalAndLL.add(tempVisioShape);
                    tempFBDSafetyFunction.fBDVotingLogicsLHM.put(s, tempVotingLogic);

                } else if (IdentificationFunctions.isItSignalProcessing(tempShape)) {

                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "signal_processing");

                } else if (IdentificationFunctions.isItReactorTrip(tempShape)) {

//                    System.out.println("found a reactor trip signal");
                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "trip_signal");

                } else if (IdentificationFunctions.isItManualAcknowledgement(tempShape)) {
//                    System.out.println("found a manual acknowledgement");
                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "manual_acknowledgement");

                } else if (IdentificationFunctions.isItSummation(tempShape)) {

                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeAsSummationHS.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "summation");
//                    System.out.println("Found a summation ");
                } else if (IdentificationFunctions.isItSign(tempShape)) {

                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeAsSignHS.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "sign" + tempShape.textContent);
//                    System.out.println("Found a sign +/- ");

                } else if (IdentificationFunctions.isItInversion(tempShape)) {

                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeAsInversionHS.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "inversion");
//                    System.out.println("Found an inversion 'o' " + tempShape.ShapeID);

                } else if (IdentificationFunctions.isItCircle(tempShape)) {

                    idRecognizedShapeNotGrpOrMbrLL.add(s);
                    shapeAsCircleHS.add(s);
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "circle");
//                    System.out.println("Found a CIRCLE O ");
                } else {
                    shapeIdAndTypeLHM.put(tempShape.ShapeID, "unknown");
                    idNotRecognizedShapeNotGrpOrMbrLL.add(s);
                }

            }

        } catch (Exception e) {

            System.out.println("Error in FBDSafetyFunctionBuilder");
            System.out.println(" \n error in: try block:-> 2" + e);
        }

        try {
            // this block operates on shapes inside final groups to identify those shapes as FBD element
            // these functions check text or shape type to identify the type of element is the group

            System.out.println("try block:-> 2");
            try {
                // prints number of unified shapes (all shape's id if needed)

                System.out.println("try block:-> 2");

                unifiedShapes = UnifyGroups.findParentAndChildGroup(basicShapeSet, uniqueNullShapeSet);
                System.out.println("number of shapes to Unify :" + basicShapeSet.size());
                System.out.println("number of unified group :" + unifiedShapes.size());
//                UnifyGroups.printParentAndChildGroup(unifiedShapes);
                IdentificationFunctions tempIdFunc = new IdentificationFunctions(idAndVisioShapeHM, idAndXDGFShapesHM);

                for (int i = 0; i < unifiedShapes.size(); i++) {
//                    System.out.println(i);
                    BasicShape tempBasicShape = (BasicShape) unifiedShapes.get(i);
//                    System.out.println(tempBasicShape.shapeId);
                    unifiedShapesLHM.put(tempBasicShape.shapeId, tempBasicShape);
//                    System.out.println(tempBasicShape.childShapeIdList);
                    if (IdentificationFunctions.isItFlipFlop(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "flip_flop";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "flip_flop");
//                        System.out.println("found a flipflop: ");

                    } else if (IdentificationFunctions.isIfDelayTimer(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "delay_timer";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "delay_timer");
//                        System.out.println("found a delay timer: ");

                    } else if (IdentificationFunctions.isItParSetPointWithoutVal(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "par_set_point_wo_val";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "par_set_point_wo_val");
//                        System.out.println("found a Par Set Point WITHOUT Val: ");

                    } else if (IdentificationFunctions.isItParSetPointWithVal(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "par_set_point_w_val";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "par_set_point_w_val");

//                        System.out.println("found a Par Set Point with value: ");
                    } else if (IdentificationFunctions.isItSignalSwitching(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "signal_switching";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "signal_switching");
//                        System.out.println("found a signal switching : ");

                    } else if (IdentificationFunctions.isItPulseWithNoReset(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "pulse_wo_rst";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "pulse_wo_rst");
//                        System.out.println("found a Pulse without reset: ");

                    } else if (IdentificationFunctions.isItPulseWithReset(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "pulse_w_rst";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "pulse_w_rst");
//                        System.out.println("found a Pulse with RESET: ");

                    } else if (IdentificationFunctions.isItSpecialFuncToCoolingGradiant(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "SF_cool_gradient";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "sf_cool_gradient");
//                        System.out.println("found special function cooling gradiant: ");

                    } else if (IdentificationFunctions.isItBinOrAnlgInProcessParam(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "a/b_ip_wo_sig_process";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "a/b_ip_wo_sig_process");
//                        System.out.println("found analog/binary processIp: ");

                    } else if (IdentificationFunctions.isItBinOrAnlgInProcessParamWithSP(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "ab_ip_w_sig_process";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "ab_ip_w_sig_process");
//                        System.out.println("found analog/binary processIp with Signal processing: ");

                    } else if (IdentificationFunctions.isItOnOffWithStep(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "on/off_w_step";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "on/off_w_step");
//                        System.out.println("found On/Off with step: ");

                    } else if (IdentificationFunctions.isItExternalSaftyFunction(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "external_Safety_fun";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "external_safety_fun");
                        externalSafetyFunctionLHM.put(tempBasicShape.shapeId, tempBasicShape);
                        System.out.println(GlobalVariable.ANSI_RED + "found external Safety Function block");

                    } else if (IdentificationFunctions.isItExternalSaftyFuncMultiDirection(tempBasicShape)) {
                        unifiedShapes.get(i).shapeType = "external_Safety_fun";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "external_safety_fun");
                        externalSafetyFunctionLHM.put(tempBasicShape.shapeId, tempBasicShape);
                        System.out.println(GlobalVariable.ANSI_RED + "found external Safety Function(multi direction)");

                    } else {
                        unifiedShapes.get(i).shapeType = "unknown";
                        shapeIdAndTypeLHM.put(tempBasicShape.parentShapeId, "?unknown");
                        System.out.println("found unknown function");

                    }
//                    

                }

            } catch (Exception e) {
                System.err.println("Error in BuilderFBDSafetyFunction group identification block:");
                System.err.println(" \n error in: try block:-> 8 " + e);
            }

        } catch (Exception e) {
            System.err.println("Error in BuilderFBDSafetyFunction");
        }

        try {
            // this block try to build connections from inversions to logical AND/OR if the nearest shape is AND/OR
            // some of the circle's are Summation node and some are inversion node. this block check through all the circles to 
            // determine wheather it is supposed to be an summation or inversion element and if inversion that it assign connections
            System.out.println("try block:-> 3");

//            HashSet<MyRectangle> rectHS = new LinkedHashSet<>();
            for (String s : idAndXDGFShapesHM.keySet()) {
                // build hashset of rectangle to iterate later
                XDGFShape tempShape = idAndXDGFShapesHM.get(s);
                if (!uniqueConnectionIDSet.contains(s)) {
                    MyRectangle tempRect = new MyRectangle(s, tempShape.getPinX(), tempShape.getPinY(), tempShape.getWidth(), tempShape.getHeight());
                    rectHS.add(tempRect);
                }
            }

            for (String s : shapeAsCircleHS) {
                // "circle" can be summation or inversion. this block identify that by finding nearest sign/boolean element and stores with the already found sum/inv
                XDGFShape tempShape = idAndXDGFShapesHM.get(s);

                MyRectangle ipRectangle = new MyRectangle(s, tempShape.getPinX(), tempShape.getPinY(), tempShape.getWidth(), tempShape.getHeight());
                MyRectangle nearestRect = MyCustomFunctions.getNearestRectangleFromRectangle(ipRectangle, rectHS);
                if (shapeAsBooleanOrAndHS.contains(nearestRect.shapeID)) {
                    shapeAsInversionHS.add(s);
                    shapeIdAndTypeLHM.replace(s, "inversion");
//                    System.out.println(GlobalVariable.ANSI_GREEN + "circle " + s + " has nearest shape AND/OR: " + nearestRect.shapeID);
                } else if (shapeAsSignHS.contains(nearestRect.shapeID)) {
                    shapeAsSummationHS.add(s);
                    shapeIdAndTypeLHM.replace(s, "summation");

//                    System.out.println(GlobalVariable.ANSI_GREEN + "circle " + s + " has nearest shape sign-/+: " + nearestRect.shapeID);
                }

            }
            for (String s : shapeAsInversionHS) {
                // if inversion has AND/OR as nearest element than assign virtual connection to both of the shape as next/previous ShapeID
                XDGFShape tempShape = idAndXDGFShapesHM.get(s);

                MyRectangle ipRectangle = new MyRectangle(s, tempShape.getPinX(), tempShape.getPinY(), tempShape.getWidth(), tempShape.getHeight());
                MyRectangle nearestRect = MyCustomFunctions.getNearestRectangleFromRectangle(ipRectangle, rectHS);
                if (shapeAsBooleanOrAndHS.contains(nearestRect.shapeID)) {
//                    System.out.println(GlobalVariable.ANSI_GREEN + " AND/OR is the nearest shape of: " + s);
                    idAndVisioShapeHM.get(s).setNextShapeIdLHS(nearestRect.shapeID);
                    idAndVisioShapeHM.get(nearestRect.shapeID).setPreviousShapeIdLSH(s);

                } else if (shapeIdAndTypeLHM.get(nearestRect.shapeID).equalsIgnoreCase("flip_flop")) {

//                    System.out.println(GlobalVariable.ANSI_RED + "nearest shape is a flip_flop" + GlobalVariable.ANSI_RESET);
                    idAndVisioShapeHM.get(s).setNextShapeIdLHS(nearestRect.shapeID);
                    idAndVisioShapeHM.get(nearestRect.shapeID).setPreviousShapeIdLSH(s);

                }

            }

//            MyRectangle nearestRect = MyCustomFunctions.getNearestRectangleFromRectangle(ipRectangle, rectHS);
        } catch (Exception e) {
            System.err.println("error in BuilderFBDSafetyFunction ");
            System.err.println("error in try block:-> 3 " + e);
        }

        try {
            System.out.println("try block:-> 3(flip_flop)");
            // this block try to assign the incoming connection to flip-flop in specific S/R(regular connection/connection through inversion)
            // determine the connection assignment based on position of the incoming connector or inversion
            for (Map.Entry<String, String> entry : shapeIdAndTypeLHM.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();

                if (val.equalsIgnoreCase("flip_flop")) {
                    List<String> tempChildShapeList = unifiedShapesLHM.get(key).childShapeIdList;

//                    System.out.println(GlobalVariable.ANSI_BRIGHT_GREEN + "flip_flop to be examined");
                    for (Map.Entry<String, BasicConnector> entryCon : finalUnifiedConnectorLHM.entrySet()) {
                        String keyCon = entryCon.getKey();
                        BasicConnector valCon = entryCon.getValue();
//                        System.out.println(GlobalVariable.ANSI_BRIGHT_GREEN + valCon.endsToShapeId +"=="+ key);
                        // find match with "endsToShape" of a connector
                        if (tempChildShapeList.contains(valCon.endsToShapeId)) {
//                            System.out.println(GlobalVariable.ANSI_BRIGHT_GREEN + keyCon + "ends to a flip-flop" + key);

                        }

                    }
                    for (String s : shapeAsInversionHS) {
                        XDGFShape tempShape = idAndXDGFShapesHM.get(s);
                        MyRectangle ipRectangle = new MyRectangle(s, tempShape.getPinX(), tempShape.getPinY(), tempShape.getWidth(), tempShape.getHeight());
                        MyRectangle nearestRect = MyCustomFunctions.getNearestRectangleFromRectangle(ipRectangle, rectHS);

                        if (tempChildShapeList.contains(nearestRect.shapeID)) {
                            // find the closest shape within the child shapes of RS Flip-flop

                        }

                    }

                }

            }

        } catch (Exception e) {
            System.err.println("error in BuilderFBDSafetyFunction ");
            System.err.println("error in try block:-> 3(flip_flop) " + e);
        }

        try {
            // create FBDelementLHM to later be used to display GRAPH
            // iterates through different container to build FBD elements for presentation in GRAPH 
            System.out.println("try block:-> 4");
            ArrayList<BasicShape> tempUnifiedShapesAL = unifiedShapes; // temporary copy of the unified groups as basic shape

            for (String s : idRecognizedShapeNotGrpOrMbrLL) {

                VisioShape tempVisioShape = idAndVisioShapeHM.get(s);
                FBDElement tempElement = new FBDElement();
                tempElement.shapeId = s;
                tempElement.shapeType = shapeIdAndTypeLHM.get(s);// add attribute "shapeType"
                tempElement.pinX = tempVisioShape.pinX;
                tempElement.pinY = tempVisioShape.pinY;
                tempElement.textContent = "(" + idAndXDGFShapesHM.get(s).getTextAsString() + ")";

                for (String next : tempVisioShape.getNextShapeIdLHS()) {
                    String foundParent = UnifyGroups.getParentIdIfMemberFound(next);
                    if (foundParent != null) {
                        if (tempElement.shapeId.equalsIgnoreCase(foundParent) == false) {
                            tempElement.nextElementLHS.add(foundParent);
                        }
                    } else {
                        if (tempElement.shapeId.equalsIgnoreCase(next) == false) {
                            tempElement.nextElementLHS.add(next);
                        }
                    }

                }

                for (String prev : tempVisioShape.getPreviousShapeIdLHS()) {
                    String foundParent = UnifyGroups.getParentIdIfMemberFound(prev);
                    if (foundParent != null) {
                        if (tempElement.shapeId.equalsIgnoreCase(foundParent) == false) {
                            tempElement.prevElementLHS.add(foundParent);
                        }
                    } else {
                        if (tempElement.shapeId.equalsIgnoreCase(prev)) {
                            tempElement.prevElementLHS.add(prev);
                        }
                    }

                }
                finalFbdElementLHM.put(s, tempElement);
//                System.out.println("try FBD 1:" + s + " has next:" + tempElement.nextElementLHS + " and prev: " + tempElement.prevElementLHS);
//                System.out.println(GlobalVariable.ANSI_RED + s + " has next:" + tempElement.nextElementLHS + " and prev: " + tempElement.prevElementLHS);

            }

            // added later for search task 1.1 to process IDnotRecognizedNotGrpNotMbrLL
            for (String s : idNotRecognizedShapeNotGrpOrMbrLL) {

                VisioShape tempVisioShape = idAndVisioShapeHM.get(s);
                FBDElement tempElement = new FBDElement();
                tempElement.shapeId = s;
                tempElement.shapeType = shapeIdAndTypeLHM.get(s);// add attribute "shapeType"
                tempElement.pinX = tempVisioShape.pinX;
                tempElement.pinY = tempVisioShape.pinY;
                tempElement.textContent = "(" + idAndXDGFShapesHM.get(s).getTextAsString() + ")";

                for (String next : tempVisioShape.getNextShapeIdLHS()) {
                    String foundParent = UnifyGroups.getParentIdIfMemberFound(next);
                    if (foundParent != null) {
                        if (tempElement.shapeId.equalsIgnoreCase(foundParent) == false) {
                            tempElement.nextElementLHS.add(foundParent);
                        }
                    } else {
                        if (tempElement.shapeId.equalsIgnoreCase(next) == false) {
                            tempElement.nextElementLHS.add(next);
                        }
                    }

                }

                for (String prev : tempVisioShape.getPreviousShapeIdLHS()) {
                    String foundParent = UnifyGroups.getParentIdIfMemberFound(prev);
                    if (foundParent != null) {
                        if (tempElement.shapeId.equalsIgnoreCase(foundParent) == false) {
                            tempElement.prevElementLHS.add(foundParent);
                        }
                    } else {
                        if (tempElement.shapeId.equalsIgnoreCase(prev)) {
                            tempElement.prevElementLHS.add(prev);
                        }
                    }

                }
                finalFbdElementLHM.put(s, tempElement);
//                System.out.println("try FBD 2:" + s + " has next:" + tempElement.nextElementLHS + " and prev: " + tempElement.prevElementLHS);

//                System.out.println(GlobalVariable.ANSI_RED + s + " has next:" + tempElement.nextElementLHS + " and prev: " + tempElement.prevElementLHS);
            }

            // IDnotRecognizedNotGrpNotMbrLL
            for (int i = 0; i < unifiedShapes.size(); i++) {

                // find if the shapeID belong to a unified group than. only the parent ID will be added 
                FBDElement tempFBDElement = new FBDElement();
                BasicShape tempShape = unifiedShapes.get(i);
                tempFBDElement.shapeId = tempShape.shapeId;
                tempFBDElement.shapeType = shapeIdAndTypeLHM.get(tempShape.shapeId);// add attribute "shapeType"
                tempFBDElement.pinX = idAndVisioShapeHM.get(tempShape.shapeId).pinX;
                tempFBDElement.pinY = idAndVisioShapeHM.get(tempShape.shapeId).pinY;
                tempFBDElement.textContent = "";
                String tempTextStore = "";
                for (String tempChildShape : tempShape.childShapeIdList) {
                    tempTextStore += "(" + idAndXDGFShapesHM.get(tempChildShape).getTextAsString() + ")";
                }

                for (String s : tempShape.childShapeIdList) {
                    VisioShape tempVisioShape = idAndVisioShapeHM.get(s);

                    for (String next : tempVisioShape.getNextShapeIdLHS()) {
                        // find if the shapeID belong to a unified group than. only the parent ID will be added 
                        String foundParent = UnifyGroups.getParentIdIfMemberFound(next);
//                        System.out.println("next is:"+next+", " + idAndXDGFShapesHM.get(next).getTextAsString());
                        if (foundParent != null) {
                            if (tempFBDElement.shapeId.equalsIgnoreCase(foundParent) == false) {
                                tempFBDElement.nextElementLHS.add(foundParent);
                            }
                        } else {
                            if (tempFBDElement.shapeId.equalsIgnoreCase(next) == false) {
                                tempFBDElement.nextElementLHS.add(next);
                            }
                        }

                    }
                    for (String prev : tempVisioShape.getPreviousShapeIdLHS()) {
                        // find if the shapeID belong to a unified group than. only the parent ID will be added 
                        String foundParent = UnifyGroups.getParentIdIfMemberFound(prev);
//                        System.out.println("prev is:"+prev + ", " + idAndXDGFShapesHM.get(prev).getTextAsString());

                        if (foundParent != null) {
                            if (tempFBDElement.shapeId.equalsIgnoreCase(foundParent) == false) {
                                tempFBDElement.prevElementLHS.add(foundParent);
                            }
                        } else {
                            if (tempFBDElement.shapeId.equalsIgnoreCase(prev) == false) {
                                tempFBDElement.prevElementLHS.add(prev);
                            }
                        }

                    }

                }

                finalFbdElementLHM.put(tempShape.shapeId, tempFBDElement);
//                System.out.println("try FBD 3: " + tempFBDElement.shapeId + " has next: " + tempFBDElement.nextElementLHS + " has prev: " + tempFBDElement.prevElementLHS);

//                System.out.println(GlobalVariable.ANSI_RED + tempFBDElement.shapeId + " has next:" + tempFBDElement.nextElementLHS + " and prev: " + tempFBDElement.prevElementLHS);
            }

// graphical representation with graph stream
//            GraphRepresentation.displayElementIDInGraph(finalFbdElementLHM, idAndVisioShapeHM, idAndXDGFShapesHM);
//            GraphRepresentation.displayElementTypeInGraph(finalFbdElementLHM, idAndVisioShapeHM, idAndXDGFShapesHM,shapeIdAndTypeLHM );
//        System.out.println("FBDelements in graph:"+ finalFbdElementLHM.keySet());
        } catch (Exception e) {
            System.err.println("error in BuilderFBDSafetyFunction ");
            System.err.println("error in try block:-> 4 " + e);
        }

        try {
            // this block works on Search task_1.1
            System.out.println("try block:-> 4(BuilderFBDSafetyFunction.java)");
            SearchTask_1_1.arrangeExternalFunction(externalSafetyFunctionLHM, idAndVisioShapeHM, idAndXDGFShapesHM, finalFbdElementLHM, shapeIdAndTypeLHM);

        } catch (Exception e) {
            System.err.println("error in try block:-> 4(BuilderFBDSafetyFUnction.java)" + e);
        }

        System.out.println("BilderFBDSafetyFunction.java \n");
        System.out.println("FBD elements: \n");
        int count = 1;

//        for (Map.Entry<String, String> entry : tempFBDSafetyFunction.idFbdElementNotConnection.entrySet()) {
//
//            System.out.println("\n" + "Element:-> " + count + " ID:" + entry.getKey());
//            System.out.println(entry.getValue());
//            count += 1;
//        }
//
//        System.out.println("\n number of FBDBooleanLogic: " + tempFBDSafetyFunction.fBDBooleanLogicsLHM.size());
//        System.out.println("\n number of FBDVotingLogic: " + tempFBDSafetyFunction.fBDVotingLogicsLHM.size());
//
//        System.out.println("\n Number of delay timer's VALUE: " + idRecognizedDelayTimerValueLL.size());
//        System.out.println("\n Number of limit checker block: " + idRecognizedLimitCheckerLL.size());
//    System.out.println("FBDConnection size: "+tempFBDSafetyFunction.fBDConnectionsLL.size());
        return null;
    }

}
