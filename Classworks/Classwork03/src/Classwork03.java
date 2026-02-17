import java.io.IOException;
import java.io.FileWriter;

public class Classwork03 {
    public static void main(String args[]){
        //Diagonal flag exercise
        String firstLine = "<svg height=\"300\" width=\"400\" xmlns=\"http://www.w3.org/2000/svg\">";
        String secondLine = "\n\t<polygon points=\"0,0 400,0 400,300\" style=\"fill:red\"/>";
        String thirdLine = "\n\t<polygon points=\"0,0 400,300 0,300\" style=\"fill:blue\"/>";
        String fourthLine = "\n</svg>";

        //Writing the lines into the file to be created.
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

        //Landscape exercise
        String background = "\n\t<rect width=\"400\" height=\"300\" x=\"0\" y=\"0\" fill=\"white\"/>";
        String rays = """
                <g stroke="red" stroke-width="2">
                <line x1="67" y1="0" x2="67" y2="15" />
                <line x1="67" y1="85" x2="67" y2="100" />
                <line x1="17" y1="50" x2="32" y2="50" />
                <line x1="102" y1="50" x2="117" y2="50" />
                <line x1="32" y1="15" x2="42" y2="25" />
                <line x1="102" y1="15" x2="92" y2="25" />
                <line x1="32" y1="85" x2="42" y2="75" />
                <line x1="102" y1="85" x2="92" y2="75" /> </g>""";
        String sun = "\n\t<circle r=\"40\" cx=\"67\" cy=\"50\" fill=\"yellow\"/>";
        String grass = "\n\t <path d=\"M 0 225 Q 25 205 50 225 T 100 225 T 150 225 T 200 225 T 250 225 T 300 225 T 350 225 T 400 225 V 300 H 0 Z\" fill=\"green\" />";
        String end = "\n</svg>";

        try{
            FileWriter landscape = new FileWriter("landscape.svg");
            landscape.write(firstLine);
            landscape.write(background);
            landscape.write(rays);
            landscape.write(sun);
            landscape.write(grass);
            landscape.write(end);
            landscape.close();
        }catch(IOException e){
            System.out.println("Error creating landscape");
            e.printStackTrace();
        }
    }
}
