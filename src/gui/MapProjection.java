package gui;

import java.awt.Dimension;
import java.awt.Point;


public class MapProjection {
    private MapProjection() {} //stops the user from creating an instance of this static class

    public static Point mapToCanvas(int x, int y, Dimension d) {
        double nx = (x + 90) / 180.0; // converts it in range [0,1]
        double ny = (y + 90) / 180.0; // same
        int cx = (int) Math.round(nx * (d.width - 40)) + 20;
        int cy = (int) Math.round((1 - ny) * (d.height - 40)) + 20;
        return new Point(cx, cy);
    }
}