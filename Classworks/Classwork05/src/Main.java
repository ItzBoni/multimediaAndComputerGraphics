import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        String file = FileHandler.textFileInput();
        System.out.println("Input your desired language");
        String lang = sc.nextLine();

        AICommunicator translator = new AICommunicator();

        FileHandler.textFileOutput(translator.getTranslation(file, lang));
    }
}
