import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileHandler {
    //Method to input the textFile
    public static String textFileInput(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input the ABSOLUTE path to your .txt file");
        String txt = sc.nextLine();
        Path txtPath = Path.of(txt);
        StringBuilder textBuilder = new StringBuilder();
        String result;

        try (BufferedReader reader = Files.newBufferedReader(txtPath)) {

            String line;
            // Read line by line until we reach the end of the file (null)
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            System.err.println("Could not read the file: " + e.getMessage());
        }

        result = textBuilder.toString();
        return result;
    }

    //Method to create another text file
    public static void textFileOutput(String text){
        System.out.println(text);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("translated.txt"))) {
            writer.write(text);
            System.out.println("El nuevo archivo es: translated.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
