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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.poi.xdgf.usermodel.XDGFShape;

public class IdentificationFunctions {

    private static HashMap<String, VisioShape> localIdAndVisioShapeHM = new LinkedHashMap<>();
    private static LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM = new LinkedHashMap<>();

    public IdentificationFunctions() {

    }

    public IdentificationFunctions(HashMap<String, VisioShape> idAndVisioShapeHM, LinkedHashMap<String, XDGFShape> idAndXDGFShapesHM) {
        this.localIdAndVisioShapeHM = idAndVisioShapeHM;
        this.idAndXDGFShapesHM = idAndXDGFShapesHM;
//        System.out.println(GlobalVariable.ANSI_BLUE + " loaded idAndVisioShapeHM and idAndXDGFShapesHM");

    }

    public static boolean isItBooleanOrAnd(VisioShape tempShape) {

        if (tempShape.textContent.equalsIgnoreCase("and") || tempShape.textContent.equalsIgnoreCase("or")) {
            return true;
        } else {

            return false;
        }

    }

    public static boolean isItMINchecker(VisioShape tempShape) {

        if (tempShape.textContent.equalsIgnoreCase("min")) {
            return true;
        }
        return false;

    }

    public static boolean isItValueOfDelay(VisioShape tempShape) {

        String delayTimerValue = "([=][nx]*[\\d]+[\\w]+)";
        Pattern curPattern = Pattern.compile(delayTimerValue);
        Matcher curMatcher = curPattern.matcher(tempShape.textContent);
        if (curMatcher.matches()) {
            return true;
        }

        return false;
    }

    public static boolean isItLimitChecker(VisioShape tempShape) {

        String limitChecker = "([<>]([-]?)[\\d]*[,°]?([\\d]+?[\\w]+)?[\\w%/]+)";
        Pattern curPattern = Pattern.compile(limitChecker);
        Matcher curMatcher = curPattern.matcher(tempShape.textContent);
        if (curMatcher.matches()) {
            return true;
        }

        return false;

    }

    public static boolean isItVotingLogic(VisioShape tempShape) {

        String votingLogic = "([\\d][/][\\d])";
        Pattern curPattern = Pattern.compile(votingLogic);
        Matcher curMatcher = curPattern.matcher(tempShape.textContent);

        if (curMatcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isItSignalProcessing(VisioShape tempShape) {

        String referenceText = "signalprocessing";
        if (referenceText.equalsIgnoreCase(tempShape.textContent)) {
//            System.out.println("found signal processing ");
            return true;
        }
        return false;
    }
    public static boolean isItManualAcknowledgement(VisioShape tempShape){
        String referenceText = "mcracknow.";
        if(referenceText.equalsIgnoreCase(tempShape.textContent)){
        return true;
        }
    
    return false;
    }

    public static boolean isItSlideSLV(VisioShape tempShape) {

        return false;
    }

    public static boolean isItVariableVSP(VisioShape tempShape) {

        return false;
    }

    public static boolean isItReactorTrip(VisioShape tempShape) {
        String referenceText = "tripsignal";
        if(referenceText.equalsIgnoreCase(tempShape.textContent)){
        return true;
        }

        return false;
    }

    public static boolean isItSignalProcessingALGO(VisioShape tempShape) {

        return false;
    }

    public static boolean isItSummation(VisioShape tempShape) {
        String shapeName = idAndXDGFShapesHM.get(tempShape.ShapeID).getName().toLowerCase();
        if (shapeName.contains("sum")) {
//            System.out.println("found signal summation ");
            return true;
        }
        return false;
    }

    public static boolean isItInversion(VisioShape tempShape) {
        String shapeName = idAndXDGFShapesHM.get(tempShape.ShapeID).getName().toLowerCase();
        if (shapeName.contains("no")) {
//            System.out.println("found signal summation ");
            return true;
        }
        return false;
    }

    public static boolean isItCircle(VisioShape tempShape) {
        String shapeName = idAndXDGFShapesHM.get(tempShape.ShapeID).getName().toLowerCase();
        if (shapeName.contains("circle")) {
//            System.out.println("found signal summation ");
            return true;
        }
        return false;
    }

    public static boolean isItSign(VisioShape tempShape) {
        if (tempShape.textContent.equalsIgnoreCase("-") || tempShape.textContent.equalsIgnoreCase("+")) {
            return true;
        }
        return false;

    }
//        *********************************************************************************************
// functions below from this point operates only on group elements and the functions*******************
    // above calculates only single unit elements******************************************************
//        *********************************************************************************************

    public static boolean isItFlipFlop(BasicShape ipBasicShape) {

        String referenceStr = "rs1";
        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isItFlipFlop()");
                }

            }

        }
        if (tempString.isEmpty()) {
            return false;
        } else {

            if (MyCustomFunctions.areStringsAnagram(referenceStr, tempString) && ipBasicShape.childShapeIdList.size() == 4) {
//                System.out.println(GlobalVariable.ANSI_PURPLE + "flipflop RS has: " + tempString);
//                System.out.println(GlobalVariable.ANSI_RED + "found a flipflop");
                return true;
            }
            return false;
        }

    }

    public static boolean isIfDelayTimer(BasicShape ipBasicShape) {
        String referenceStr = "t0";
        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isIfDelayTimer()");
                }

            }

        }
        if (tempString.isEmpty()) {
            return false;
        } else {
            if (MyCustomFunctions.areStringsAnagram(referenceStr, tempString)) {
                System.out.println(GlobalVariable.ANSI_PURPLE + "delay timer has: " + tempString);
//                System.out.println("found a delay timer");
                return true;
            }
            return false;
        }

    }

    public static boolean isItParSetPointWithoutVal(BasicShape ipBasicShape) {

        String referenceStr = "par";
        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isItParSetPoint()");
                }

            }
//            System.out.println(GlobalVariable.ANSI_PURPLE + "set point With Val has: " + tempString);

        }

        if (tempString.isEmpty()) {
            return false;
        } else {
            if (MyCustomFunctions.areStringsAnagram(referenceStr, tempString.toLowerCase()) && ipBasicShape.childShapeIdList.size() == 6) {
//                System.out.println(GlobalVariable.ANSI_PURPLE + "Parcent set point has: " + tempString);
//                System.out.println(GlobalVariable.ANSI_PURPLE + "found a Par Set Point Val");
                return true;
            }
            return false;
        }
    }

    public static boolean isItParSetPointWithVal(BasicShape ipBasicShape) {

        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isItParSetPoint()");
                }

            }
//            System.out.println(GlobalVariable.ANSI_PURPLE + "set point WITHOUT val has: " + tempString);

        }

        if (tempString.isEmpty()) {
            return false;
        } else {
            if (tempString.contains("par=") && ipBasicShape.childShapeIdList.size() == 6) {
//                System.out.println(GlobalVariable.ANSI_PURPLE + "Parcent set point has: " + tempString);
//                System.out.println(GlobalVariable.ANSI_PURPLE + "found a Par Set Point Val");
                return true;
            }
            return false;
        }

    }

    public static boolean isItSignalSwitching(BasicShape ipBasicShape) {

        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isItParSetPoint()");
                }

            }
//            System.out.println(GlobalVariable.ANSI_PURPLE + "set point WITHOUT val has: " + tempString);

        }

        if (tempString.isEmpty()) {
            return false;
        } else {
            if (tempString.equalsIgnoreCase("#") && ipBasicShape.childShapeIdList.size() == 3) {
//                System.out.println(GlobalVariable.ANSI_PURPLE + "Parcent set point has: " + tempString);
//                System.out.println(GlobalVariable.ANSI_PURPLE + "found a Par Set Point Val");
                return true;
            }
            return false;
        }

    }

    public static boolean isItPulseWithNoReset(BasicShape ipBasicShape) {
        int countConnector = 0;
        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {

                if (idAndXDGFShapesHM.get(s).getName().contains("Dynamic connector")) {
                    countConnector += 1;
                }
            }
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isItPulseWithNoReset()");
                }

            }

        }
        if (tempString.isEmpty()) {
            return false;
        } else {
            if (tempString.equalsIgnoreCase("1") && ipBasicShape.childShapeIdList.size() == 7 && countConnector == 4) {
//                System.out.println(GlobalVariable.ANSI_PURPLE + "Parcent set point has: " + tempString);
//                System.out.println(GlobalVariable.ANSI_PURPLE + "found a Par Set Point Val");
                System.out.println("Connector in group" + countConnector);
                return true;
            }
            return false;
        }
    }

    public static boolean isItPulseWithReset(BasicShape ipBasicShape) {

        int pito = 0;
        int countConnector = 0;
        String tempString = "";
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {
//                System.out.println("shape name: "+idAndXDGFShapesHM.get(s).getName());

                if (idAndXDGFShapesHM.get(s).getName().contains("Dynamic connector")) {
                    countConnector += 1;
//                    System.out.println(" Dynamic connector");

                }
                if (idAndXDGFShapesHM.get(s).getName().toLowerCase().contains("pito")) {

                    pito += 1;
//                    System.out.println(" found shape name pito");
                }
            }
            for (String s : ipBasicShape.childShapeIdList) {

                try {
                    VisioShape tempshape = localIdAndVisioShapeHM.get(s);
                    if (tempshape.isItAGroup) {
                        for (String memberText : tempshape.getTextInGroupMember()) {
                            tempString += memberText;

                        }
                    } else {
                        String sText = tempshape.textContent;
                        if (sText != null && sText.equalsIgnoreCase("")) {
                            tempString += sText;
                        }
                    }

                } catch (Exception e) {
                    System.err.println("error inside try IdentificationFunction isItPulseWithNoReset()");
                }

            }

        }

        if (((tempString.equalsIgnoreCase("") || tempString.equalsIgnoreCase("r"))) && ipBasicShape.childShapeIdList.size() == 7 && countConnector == 4 && pito > 0) {
//                System.out.println(GlobalVariable.ANSI_PURPLE + "Parcent set point has: " + tempString);
//                System.out.println(GlobalVariable.ANSI_PURPLE + "found a Par Set Point Val");
//                System.out.println("Connector in group" + countConnector);
            return true;
        }
        return false;

    }

    public static boolean isItExternalSaftyFunction(BasicShape ipBasicShape) {
        Boolean found_Pattern = false;
        Boolean found_Text = false;
        Boolean numOfMember = false;
        Pattern currPattern = Pattern.compile("([0-9a-zA-Z]*[_]{1}[0-9a-zA-Z.]*)");

        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            if (ipBasicShape.childShapeIdList.size() == 3) {
                numOfMember = true;
            }
            try {
                for (String s : ipBasicShape.childShapeIdList) {
                    VisioShape tempShape = localIdAndVisioShapeHM.get(s);
                    if (currPattern.matcher(tempShape.textContent).matches()) {
                        found_Pattern = true;
                    } else if (!tempShape.textContent.isEmpty() && !currPattern.matcher(tempShape.textContent).matches()) {
                        found_Text = true;
                    }
                }
            } catch (Exception e) {
                System.err.print("error in IdentificationFunctions.isItExternalSaftyFunction()" + e);
            }

        }
        if (found_Pattern && found_Text && numOfMember) {
            return true;
        }
        return false;
    }
    
        public static boolean isItExternalSaftyFuncMultiDirection(BasicShape ipBasicShape) {
            // identify external safety function countaining multiple function name
        Boolean found_Pattern = false;
        Boolean found_Text = false;
        Boolean numOfMember = false;
        Pattern currPattern = Pattern.compile("([0-9a-zA-Z]*[_]{1}[0-9a-zA-Z.]*)");

        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            if (ipBasicShape.childShapeIdList.size() == 3) {
                numOfMember = true;
            }
            try {
                for (String s : ipBasicShape.childShapeIdList) {
                    VisioShape tempShape = localIdAndVisioShapeHM.get(s);
                    String[] tempTextContent = tempShape.textContent.split(",");
                    for(String partialText: tempTextContent){
                        if(currPattern.matcher(partialText).matches()){
                            
                            found_Pattern=true; 
                        }
                      
                    }
                    
                    if (currPattern.matcher(tempShape.textContent).matches()) {
                        found_Pattern = true;
                    } else if (!tempShape.textContent.isEmpty() && !currPattern.matcher(tempShape.textContent).matches()) {
                        found_Text = true;
                    }
                }
            } catch (Exception e) {
                System.err.print("error in IdentificationFunctions.isItExternalSaftyFunction()" + e);
            }

        }
        if (found_Pattern && found_Text && numOfMember) {
            return true;
        }
        return false;
    }

    public static boolean isItBinOrAnlgInProcessParam(BasicShape ipBasicShape) {
        Boolean found_aORb = false;
        Boolean found_Pattern = false;
        Boolean found_signal_processing = false;

        Pattern curPattern = Pattern.compile("([|][\\d]{1}[|][\\d]*[|][\\d]*[|][\\d]*[|])");

        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {
                VisioShape tempShape = localIdAndVisioShapeHM.get(s);

//                System.out.println("text inside basic shape:" + tempShape.textContent + " : " + tempShape.textInGroupMember);
                if (tempShape.isItAGroup) {
                    List<String> textInGroupMember = tempShape.textInGroupMember;
//                    System.out.println(" text inside GROUP: " + textInGroupMember);
                    XDGFShape tempXDGFShape = idAndXDGFShapesHM.get(s);
                    for (String text : textInGroupMember) {
                        if (text.equalsIgnoreCase("a") || text.equalsIgnoreCase("b")) {
//                            System.out.println(GlobalVariable.ANSI_BRIGHT_CYAN + "found a or b");
                            found_aORb = true;
                        } else if (curPattern.matcher(text).matches()) {
//                            System.out.println(GlobalVariable.ANSI_BRIGHT_CYAN + "found pattern |1|2|3|4|");
                            found_Pattern = true;
                        } else if (text.equalsIgnoreCase("signalprocessing")) {
                            found_signal_processing=true;
                        }

                    }
                } else {
                    XDGFShape tempXDGFShape = idAndXDGFShapesHM.get(s);
//                    System.out.println("text inside group member:" + tempShape.textContent);
                    if (tempShape.textContent.equalsIgnoreCase("a") || tempShape.textContent.equalsIgnoreCase("b")) {
                        found_aORb = true;
                    } else if (curPattern.matcher(tempShape.textContent).matches()) {
                        found_Pattern = true;
                    }

                }

            }

        }
        if (found_aORb && found_Pattern && !found_signal_processing) {
//            System.out.println(GlobalVariable.ANSI_CYAN + " found a analog/binary process ip");
            return true;
        }

        return false;
    }

    public static boolean isItBinOrAnlgInProcessParamWithSP(BasicShape ipBasicShape) {
        Boolean found_aORb = false;
        Boolean found_Pattern = false;
        Boolean found_signal_processing = false;
        Pattern curPattern = Pattern.compile("([|][\\d]{1}[|][\\d]*[|][\\d]*[|][\\d]*[|])");

        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {
                VisioShape tempShape = localIdAndVisioShapeHM.get(s);

//                System.out.println("text inside basic shape:" + tempShape.textContent +" : "+ tempShape.textInGroupMember );
                if (tempShape.isItAGroup) {
                    List<String> textInGroupMember = tempShape.textInGroupMember;
//                    System.out.println(" text inside GROUP: "+ textInGroupMember);
                    XDGFShape tempXDGFShape = idAndXDGFShapesHM.get(s);
                    for (String text : textInGroupMember) {
                        if (text.equalsIgnoreCase("a") || text.equalsIgnoreCase("b")) {
//                            System.out.println(GlobalVariable.ANSI_BRIGHT_CYAN + "found a or b");
                            found_aORb = true;
                        } else if (curPattern.matcher(text).matches()) {
//                            System.out.println(GlobalVariable.ANSI_BRIGHT_CYAN + "found pattern |1|2|3|4|");
                            found_Pattern = true;
                        } else if (text.equalsIgnoreCase("signalprocessing")) {
//                            System.out.println(GlobalVariable.ANSI_BRIGHT_CYAN + "found signal processing");
                            found_signal_processing = true;
                        }

                    }
                } else {
                    XDGFShape tempXDGFShape = idAndXDGFShapesHM.get(s);
//                    System.out.println("text inside group member:"+tempShape.textContent);
                    if (tempShape.textContent.equalsIgnoreCase("a") || tempShape.textContent.equalsIgnoreCase("b")) {
                        found_aORb = true;
                    } else if (curPattern.matcher(tempShape.textContent).matches()) {
                        found_Pattern = true;
                    }

                }

            }

        }
        if (found_aORb && found_Pattern && found_signal_processing) {
//            System.out.println(GlobalVariable.ANSI_CYAN + " found a analog/binary process ip with signal processing");
            return true;
        }

        return false;
    }

    public static boolean isItPDController(BasicShape ipBasicShape) {

        return false;
    }

    public static boolean isItSpecialFuncToCoolingGradiant(BasicShape ipBasicShape) {

        String delayTimerValue = "([0-9]*[°][с][/][h])";
        Pattern curPattern = Pattern.compile(delayTimerValue);

        Boolean found_p = false;
        Boolean found_t = false;
        Boolean found_pattern = false;

        if (!ipBasicShape.childShapeIdList.isEmpty()) {

            for (String s : ipBasicShape.childShapeIdList) {
                VisioShape tempShape = localIdAndVisioShapeHM.get(s);
                if (tempShape.isItAGroup) {

                    List<String> textInGroupMember = tempShape.textInGroupMember;
//                    System.out.println(" text inside GROUP: "+ textInGroupMember);
                    for (String text : textInGroupMember) {
                        if (text.equalsIgnoreCase("p")) {
                            found_p = true;
                        } else if (text.equalsIgnoreCase("t")) {
                            found_t = true;
                        } else if (curPattern.matcher(text).matches()) {
                            found_pattern = true;
                        }

                    }

                }

            }
            if (found_p && found_t && found_pattern) {
                return true;
//            System.out.println(GlobalVariable.ANSI_CYAN+"found a cooling gradiant");
            }

            return false;
        }

        Matcher curMatcher = curPattern.matcher("");
        if (curMatcher.matches()) {
            return true;
        }

        return false;
    }

    public static boolean isItOnOffWithStep(BasicShape ipBasicShape) {

        Boolean foundOnOrOff = false;
        Boolean foundStep = false;
        if (!ipBasicShape.childShapeIdList.isEmpty()) {
            for (String s : ipBasicShape.childShapeIdList) {
                VisioShape tempShape = localIdAndVisioShapeHM.get(s);
                if (tempShape.textContent.equalsIgnoreCase("on") || tempShape.textContent.equalsIgnoreCase("off")) {
                    foundOnOrOff = true;
                } else if (tempShape.textContent.contains("step")) {
                    foundStep = true;
                }

            }

        }
        if (foundOnOrOff && foundStep) {
            return true;
        }
        return false;
    }
}
