/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visiotoplcopen;

import javafx.geometry.Point2D;

/**
 *
 * @author pbpras
 */
public class MyRectangle {
        String shapeID;
    double pinX, pinY, reachWidth, reachHeight;
    Point2D centerPoint2D;
    Point2D topLeftPoint2D;
    Point2D toprightPoint2D;
    Point2D bottomLeftPoint2D;
    Point2D bottomRightPoint2D;

    public MyRectangle(String shapeID, double pinX, double pinY, double width, double height) {
        this.shapeID = shapeID;
        this.pinX = pinX;
        this.pinY = pinY;
        this.reachHeight = height / 2;
        this.reachWidth = width / 2;
        centerPoint2D = new Point2D(pinX, pinY);
        topLeftPoint2D = new Point2D(pinX - reachWidth, pinY + reachHeight);
        toprightPoint2D = new Point2D(pinX + reachWidth, pinY + reachHeight);
        bottomLeftPoint2D = new Point2D(pinX - reachWidth, pinY - reachHeight);
        bottomRightPoint2D = new Point2D(pinX + reachWidth, pinY - reachHeight);

//    System.out.println("center:"+centerPoint2D+"\n topleft:"+topLeftPoint2D+"\n topright:"+toprightPoint2D+
//                        "\n bottomLeft: "+bottomLeftPoint2D+"\n bottomRight:"+bottomRightPoint2D);
//        System.out.println(shapeID + " center:" + this.pinX + "," + this.pinY + " Rw & Rh: " + this.reachWidth + "," + this.reachHeight);
    }

    public boolean areShapesFriend(MyRectangle ipRectangle) {

//        System.out.println("center:"+ipRectangle.pinX+ipRectangle.pinY+"\n h&W: "+ipRectangle.reachHeight+","+ipRectangle.reachWidth);
        if (((this.reachWidth + ipRectangle.reachWidth) >= Math.abs(this.pinX - ipRectangle.pinX))
                && ((this.reachHeight + ipRectangle.reachHeight) >= Math.abs(this.pinY - ipRectangle.pinY))) {
//            System.out.println(this.shapeID +" friend shape of "+ipRectangle.shapeID);
            return true;
        } else if ((((this.pinX + this.reachWidth) == (ipRectangle.pinX - ipRectangle.reachWidth))
                || ((ipRectangle.pinX + ipRectangle.reachWidth) == (this.pinX - this.reachWidth)))
                && ((this.reachHeight + ipRectangle.reachHeight) >= Math.abs(this.pinY - ipRectangle.pinY))) {
//            System.out.println(this.shapeID +" friend shape of width "+ipRectangle.shapeID);
            return true;
        } else if ((((this.pinY + this.reachHeight) == (ipRectangle.pinY - ipRectangle.reachHeight))
                || ((ipRectangle.pinY + ipRectangle.reachHeight) == (this.pinY - this.reachHeight)))
                && ((this.reachWidth + ipRectangle.reachWidth) >= Math.abs(this.pinX - ipRectangle.pinX))) {
//            System.out.println(this.shapeID +" friend shape of height "+ipRectangle.shapeID);
            return true;
        } else {
//            System.out.println("distant shapes");
            return false;

        }

    }

    
}
