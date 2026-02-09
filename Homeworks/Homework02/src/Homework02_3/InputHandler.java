package Homework02_3;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandler {
    public static int inputInteger(String text){
        //get input from the user
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;
        int num = 0;

        while (!validInput) {
            try {
                System.out.println(text);
                num = sc.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, try again with an integer");
                sc.nextLine();
            }

        }

        return num;
    }

    public static float inputFloat(String text){
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;
        float num = 0;

        while (!validInput) {
            try {
                System.out.println(text);
                num = sc.nextFloat();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, try again with an float");
                sc.nextLine();
            }

        }

        return num;
    }
}
