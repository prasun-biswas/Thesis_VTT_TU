/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xdgf.usermodel.XDGFShape;

/**
 *
 * @author pbpras
 */
public class BuilderVisioShape {

    public static VisioShape buildVisioShape(XDGFShape shape, String ID, boolean ipIsDynamicConector, boolean ipIsItAGroup, boolean ipIsMemberOfAGroup) {

        XDGFShape currentShape = shape;
        String ShapeID = ID;
        VisioShape tempVisioShape = new VisioShape(currentShape, ShapeID);

        boolean isDynamicConector = false;
        boolean isItAGroup = false;
        List<String> shapesInsideGroup = new ArrayList<String>();
        List<String> textInGroupMember = new ArrayList<>();
        boolean isMemberOfAGroup = false;
        String idOfParentGroup;
        String textContent = "";
        double beginX = 0;
        double beginY = 0;
        double endX = 0;
        double endY = 0;
        double pinX = 0;
        double pinY = 0;

        ShapeID = shape + "";

//        System.out.println(ShapeID+", connector?:"+ipIsDynamicConector+", group?:"+ipIsItAGroup+", groupMember?:"+ipIsMemberOfAGroup);
        //try to find nearest shape
        currentShape = shape;
        isDynamicConector = ipIsDynamicConector;
        tempVisioShape.isDynamicConector = isDynamicConector;
        if (isDynamicConector == true) {

            // if it is a dynamic connector then this block adds the connected shape id
            tempVisioShape.connectionTriggerHM = XMLShapeParser.processXMLDynamicConnector(currentShape.getXmlObject().toString());
//            System.out.println("connected to shapes: "+tempVisioShape.connectionTriggerHM.get("BegTrigger")+":"+tempVisioShape.connectionTriggerHM.get("EndTrigger"));

        }

        isItAGroup = ipIsItAGroup;
        tempVisioShape.isItAGroup = isItAGroup;
        if (isItAGroup == true) {
            for (int i = 0; i < currentShape.getShapes().size(); i++) {
                String tempShapeID = "" + currentShape.getShapes().get(i);
                String tempTextInMemberShape = currentShape.getShapes().get(i).getTextAsString().replaceAll("\\s", "").toLowerCase();
                shapesInsideGroup.add(tempShapeID);
                textInGroupMember.add(tempTextInMemberShape);

            }
//            System.out.println("Group contains: "+shapesInsideGroup);
            tempVisioShape.ShapesInsideGroup = shapesInsideGroup;
            tempVisioShape.textInGroupMember = textInGroupMember;
        }

        isMemberOfAGroup = ipIsMemberOfAGroup;
        tempVisioShape.isMemberOfAGroup = isMemberOfAGroup;
        if (isMemberOfAGroup == true) {
            idOfParentGroup = currentShape.getParentShape() + "";
        }
//       
        if (currentShape.getTextAsString() != null && currentShape.getTextAsString() != "") {

            String tempTextContent = currentShape.getTextAsString().replaceAll("\\s", "").toLowerCase();
            tempVisioShape.textContent = tempTextContent;
//            System.out.println("text content:"+tempVisioShape.textContent);

        }
        tempVisioShape.pinX = currentShape.getPinX();
        tempVisioShape.pinY = currentShape.getPinY();
        tempVisioShape.width = currentShape.getWidth();
        tempVisioShape.height = currentShape.getHeight();

//        System.out.println(GlobalVariable.ANSI_RED + ShapeID+"visio shape has dimention:" + " X:" + tempVisioShape.pinX + " Y:" + tempVisioShape.pinY + " W:" + tempVisioShape.width + " H:" + tempVisioShape.height);
//        System.out.println(currentShape.getXmlObject());

        return tempVisioShape;

    }

}
