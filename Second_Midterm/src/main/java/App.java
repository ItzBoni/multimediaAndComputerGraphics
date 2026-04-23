import models.MediaItem;
import models.MediaItemFactory;
import services.MediaPipeline;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point. Responsible only for collecting user input and handing off to MediaPipeline.
 */
public class App {
    public static void main(String[] args) {
        ArrayList<MediaItem> elements = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String directory;

        System.out.println("Welcome to the AI video generator :)");

        System.out.println("Please insert the directory where you want to store all project files");
        String projectDirectory = sc.nextLine();

        System.out.println("Please input the directories of the files you want to add");

        do {
            directory = sc.nextLine();
            if (!directory.equals("exit")) {
                try {
                    elements.add(MediaItemFactory.create(directory));
                    System.out.println("added new element");
                } catch (RuntimeException e) {
                    System.out.println("came into an error");
                    System.out.println("Failed to load file: " + directory);
                    System.out.println("Reason: " + e.getMessage());
                }
            }
            System.out.println(directory);
        } while (!directory.equals("exit"));

        new MediaPipeline().run(elements, projectDirectory);
    }
}
