
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.poi.xdgf.usermodel.XDGFShape;
//to find beg/end triggerID
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author pbpras
 */
public class VisioShape {

    XDGFShape currentShape;
    String ShapeID;
    boolean isDynamicConector = false;
    HashMap<String, String> connectionTriggerHM = new HashMap<>();
    boolean isItAGroup = false;
    boolean isItUnifiedGroup = false;
    List<String> ShapesInsideGroup;
    List<String> textInGroupMember;
    boolean isMemberOfAGroup = false;
    String idOfParentGroup;
    String textContent = "";
    private LinkedHashSet<String> previousShapeIdLHS = new LinkedHashSet<>();
    private LinkedHashSet<String> nextShapeIdLHS = new LinkedHashSet<>();
    boolean isItInversion=false;
    String nearestShape="";
    double beginX = 0;
    double beginY = 0;
    double endX = 0;
    double endY = 0;
    double pinX = 0;
    double pinY = 0;
    double width = 0;
    double height = 0;

    public VisioShape(XDGFShape shape, String ShapeID) {
        currentShape = shape;
        this.ShapeID = ShapeID;
    }

    public LinkedHashSet<String> getPreviousShapeIdLHS() {

        return (LinkedHashSet<String>) previousShapeIdLHS;
    }

    public void setPreviousShapeIdLSH(String connectorID) {

        if (isDynamicConector == false) {
            previousShapeIdLHS.add(connectorID);
//            System.out.println("incoming connector added to shape");

        }

    }

    public LinkedHashSet<String> getNextShapeIdLHS() {

        return (LinkedHashSet<String>) nextShapeIdLHS;
    }

    public void setNextShapeIdLHS(String connectorID) {

        if (isDynamicConector == false) {

            nextShapeIdLHS.add(connectorID);
//            System.out.println("outgoing connector added to shape");
        }
    }
    
    
    public List<String> getTextInGroupMember(){
    
    return textInGroupMember;
    }

}
