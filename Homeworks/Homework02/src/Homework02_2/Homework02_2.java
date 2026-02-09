package Homework02_2;

public class Homework02_2 {
    public static void main(String[] args) {
        int height, width, gcd, aspectWidth, aspectHeight;

        height = InputHandler.inputInteger("height");
        width = InputHandler.inputInteger("width");

        gcd = getGCD(height, width);

        aspectHeight = height/gcd;
        aspectWidth = width/gcd;

        System.out.printf("Your aspect ratio is: %d:%d%n", aspectWidth, aspectHeight);

    }

    public static int getGCD(int a, int b){
        while (b != 0) {
            int temporal = b;
            b = a % b;
            a = temporal;
        }
        return a;
    }
}