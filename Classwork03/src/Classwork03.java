import java.io.IOException;
import java.io.FileWriter;

public class Classwork03 {
    public static void main(String args[]){
        //Diagonal flag exercise
        String firstLine = "<svg height=\"300\" width=\"400\" xmlns=\"http://www.w3.org/2000/svg\">";
        String secondLine = "\n\t<polygon points=\"0,0 400,0 400,300\" style=\"fill:red\"/>";
        String thirdLine = "\n\t<polygon points=\"0,0 400,300 0,300\" style=\"fill:blue\"/>";
        String fourthLine = "\n</svg>";


        try{
            FileWriter flag = new FileWriter("flag.svg");
            flag.write(firstLine);
            flag.write(secondLine);
            flag.write(thirdLine);
            flag.write(fourthLine);
            flag.close();
        } catch (IOException e){
            System.out.println("Error occurred");
            e.printStackTrace();
        }
    }
}
