package net.ichenglin.kbext.util;

import java.awt.*;
import java.awt.geom.Point2D;

public class Geometry {
    public static boolean point_within_rectangle(Rectangle rectangle, Point2D point) {
        double  offset_x = (point.getX() - rectangle.getX());
        double  offset_y = (point.getY() - rectangle.getY());
        boolean within_x = (0 <= offset_x) && (offset_x < rectangle.getWidth());
        boolean within_y = (0 <= offset_y) && (offset_y < rectangle.getHeight());
        return (within_x && within_y);
    }

    public static Rectangle rectangle_scale_origin(Rectangle rectangle, double scale) {
        return new Rectangle(
            (int) (rectangle.getX()      * scale),
            (int) (rectangle.getY()      * scale),
            (int) (rectangle.getWidth()  * scale),
            (int) (rectangle.getHeight() * scale)
        );
    }

    public static Rectangle rectangle_scale_size(Rectangle rectangle, double scale_x, double scale_y) {
        return new Rectangle(
                (int) rectangle.getX(),
                (int) rectangle.getY(),
                (int) (rectangle.getWidth()  * scale_x),
                (int) (rectangle.getHeight() * scale_y)
        );
    }

    public static Point2D rectangle_center(Rectangle rectangle) {
        return new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY());
    }
}
