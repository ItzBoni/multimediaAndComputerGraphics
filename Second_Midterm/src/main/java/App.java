import api.AICaller;
import api.MapCaller;
import handlers.ConversionHandler;
import models.MediaItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ArrayList<MediaItem> elements = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        AICaller ai = new AICaller();
        String directory;
        String fullDescription = "";
        String projectDirectory;

        System.out.println("Welcome to the AI video generator :)");

        System.out.println("Please insert the directory where you want to store all project files");
        projectDirectory = sc.nextLine();

        System.out.println("Please input the directories of the files you want to add");

        do {
            directory = sc.nextLine();
            if (!directory.equals("exit")) {
                try {
                    elements.add(new MediaItem(directory));
                    System.out.println("added new element");
                } catch (RuntimeException e) {
                    System.out.println("came into an error");
                    System.out.println("Failed to load file: " + directory);
                    System.out.println("Reason: " + e.getMessage());
                }
            }
            System.out.println(directory);
        } while (!directory.equals("exit"));

        sortByDate(elements);

        for(MediaItem item : elements){
            File fileToEncode = item.isVideo() ? item.getRepresentativeFrame() : item.getFile();
            String base64 = ConversionHandler.encodeToBase64(fileToEncode);
            System.out.println(base64);

            String description = ai.descriptionRequest(base64);
            item.setDescription(description);
            fullDescription = fullDescription.concat(" "+description);
        }

        fullDescription = ai.mergeDescriptions(fullDescription);
        byte[] tts = ai.audioRequest(fullDescription);
        String image = ai.imageRequest(fullDescription);

        File audioFile = new File(projectDirectory+"output.mp3");
        audioFile.getParentFile().mkdirs(); // Creates "Audio" folder if missing

        File imageFile = new File(projectDirectory+"output.jpg");
        imageFile.getParentFile().mkdirs(); // Creates "Images" folder if missing

        File decodedFile = ConversionHandler.decodeByteResponse(tts, audioFile);
        File decodedImage = ConversionHandler.decodeByteResponse(image, imageFile);

        MediaItem img1 = elements.get(0);
        MediaItem img2 = elements.get(1);

        MapCaller map = new MapCaller();

        System.out.println("Image 1 Lat: "+img1.getGps()[0] + " Lon:" + img1.getGps()[1]);

        System.out.println("Image 2 Lat: "+img2.getGps()[0] + " Lon:" + img2.getGps()[1]);

        byte[] mapBytes = map.mapRequest(img1.getGps(), img2.getGps());
        File mapFile = new File("map.png");
        ConversionHandler.decodeByteResponse(mapBytes, mapFile);
    }

    private static void sortByDate(ArrayList<MediaItem> items) {
        System.out.println("Found where I got stuck");
        items.sort(Comparator.comparing(MediaItem::getCreatedAt));
    }
}
