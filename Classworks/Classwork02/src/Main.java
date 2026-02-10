import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(1000,1000, BufferedImage.TYPE_INT_RGB);

        int height = image.getHeight();
        int width = image.getWidth();

        Triangle triangle = new Triangle( 0, height, width, height,width/2, 0);

        triangle.paintTriangle(image);

        ImageGenerator.generateImage(image, "Barycentric_Triangle");
    }
}