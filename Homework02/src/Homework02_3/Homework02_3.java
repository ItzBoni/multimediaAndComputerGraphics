package Homework02_3;

public class Homework02_3 {
    public static void main(String[] args) {
        float x = 0;
        float y = 0;
        float r = 0;
        float theta = 0;
        boolean validOption = false;
        int option;
        Coordinates coords;

        //Check that the user is choosing a valid option in the menu
        do {
            option = InputHandler.inputInteger("1) For cartesian to polar \n2) For polar to cartesian");

            if (option == 1 || option == 2){
                validOption = true;
            } else {
                System.out.println("Insert a valid option");
            }
        } while (!validOption);

        //Conversion from cartesian to polar
        if (option == 1) {
            x = InputHandler.inputFloat("Input x coordinate");
            y = InputHandler.inputFloat("Input y coordinate");
            coords = new Coordinates(x,y,0,0);

            coords.toPolar();
        } else {
            //Conversion from polar to cartesian
            r = InputHandler.inputFloat("Input the radius");
            theta = InputHandler.inputFloat("Input the angle");
            coords = new Coordinates(0,0,r,theta);

            coords.toCartesian();
        }



    }
}
