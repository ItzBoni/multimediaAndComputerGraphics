import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Triangle {
    //Creating a record to define the coordinates of each point
    private record Point(int x, int y) {}

    private final Point pointA, pointB, pointC;
    private double lambda1, lambda2, lambda3;

    public Triangle(int xA, int yA, int xB, int yB, int xC, int yC){
        this.pointA = new Point(xA, yA);
        this.pointB = new Point(xB, yB);
        this.pointC = new Point(xC, yC);
    }

    private void calculateLambda(Point p, Point a, Point b, Point c){
        double denominator = (((b.y - c.y) * (a.x - c.x)) + ((c.x - b.x) * (a.y - c.y)));

        this.lambda1 = (double) (((b.y - c.y) * (p.x - c.x)) + ((c.x - b.x) * (p.y - c.y))) / denominator;
        this.lambda2 = (double) (((c.y - a.y) * (p.x - c.x)) + ((a.x - c.x) * (p.y - c.y))) / denominator;
        this.lambda3 = 1 - this.lambda1 - this.lambda2;
    }

    //Function to calculate if the points are in the triangle and add to pointArray
    public void paintTriangle(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Point p = new Point(x,y);
                calculateLambda(p, pointA, pointB, pointC);

                if (this.lambda1 >= 0 && this.lambda2 >= 0 && this.lambda3 >=0){
                    int red = (int)((lambda1*Color.red.getRed()) + (lambda2*Color.green.getRed()) + (lambda3*Color.blue.getRed()));
                    int green = (int)((lambda1*Color.red.getGreen()) + (lambda2*Color.green.getGreen()) + (lambda3*Color.blue.getGreen()));
                    int blue = (int)((lambda1*Color.red.getBlue()) + (lambda2*Color.green.getBlue()) + (lambda3*Color.blue.getBlue()));
                    Color blendedColor = new Color(red, green, blue);
                    image.setRGB(x, y, blendedColor.getRGB());
                }
            }
        }
    }

}
