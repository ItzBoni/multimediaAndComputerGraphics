package Homework02_2;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandler {

    public static int inputInteger(String name){
        //get input from the user
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;
        int num = 0;

        while (!validInput) {
            try {
                System.out.println("Input the "+name);
                num = sc.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, try again with an integer");
                sc.nextLine();
            }

        }

        return num;
    }

}
