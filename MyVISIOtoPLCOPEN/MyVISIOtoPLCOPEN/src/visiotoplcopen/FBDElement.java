/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import java.util.LinkedHashSet;

/**
 *
 * @author pbpras
 */
public class FBDElement {
    String shapeId="";
    String shapeType ="";
    String textContent = "";
    String moreDetails = "";
    Double pinX;
    Double pinY;
    LinkedHashSet<String> nextElementLHS = new LinkedHashSet<>();
    LinkedHashSet<String> prevElementLHS = new LinkedHashSet<>();
    
}
