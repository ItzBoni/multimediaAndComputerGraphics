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
        /*
        System.out.println("Welcome to the AI video generator :)");

        System.out.println("Please insert the directory where you want to store all project files");
        projectDirectory = sc.nextLine();

        System.out.println("Please input the directories of the files you want to add");
        */
        System.out.println("I'm starting here");
        elements.add(new MediaItem("C:/Users/luchy/OneDrive/Pictures/project images/IMG_4457.heic"));
        elements.add(new MediaItem("C:/Users/luchy/OneDrive/Pictures/project images/IMG_2831.heic"));

        /*
        do{
            directory = sc.nextLine();
            if(!directory.equals("exit")){
                elements.add(new MediaItem(directory));
            }
        } while(!directory.equals("exit"));*/

        sortByDate(elements);

        System.out.println("Where the fuck am I stuck");

        for(MediaItem item : elements){
            System.out.println("am I stuck here??");
            File fileToEncode = item.isVideo() ? item.getRepresentativeFrame() : item.getFile();
            String base64 = ConversionHandler.encodeToBase64(fileToEncode);
            System.out.println(base64);

            String description = ai.descriptionRequest(base64);
            item.setDescription(description);
            fullDescription = fullDescription.concat(" "+description);
        }

        System.out.println(fullDescription);

        /*
        MediaItem item = new MediaItem("C:/Users/luchy/Videos/videochistoso.mp4");

        File fileToEncode = item.isVideo() ? item.getRepresentativeFrame() : item.getFile();

        // 3. Encode to Base64
        String base64 = ConversionHandler.encodeToBase64(fileToEncode);

        // 4. Send to ChatGPT
        AICaller ai = new AICaller();
        String description = ai.descriptionRequest(base64);
        item.setDescription(description);

        byte[] tts = ai.audioRequest(description);
        String image = ai.imageRequest(description);

        File audioFile = new File("C:/Users/luchy/Audio/output.mp3");
        audioFile.getParentFile().mkdirs(); // Creates "Audio" folder if missing

        File imageFile = new File("C:/Users/luchy/Images/output.jpg");
        imageFile.getParentFile().mkdirs(); // Creates "Images" folder if missing

        File decodedFile = ConversionHandler.decodeByteResponse(tts, audioFile);
        File decodedImage = ConversionHandler.decodeByteResponse(image, imageFile);
        */
        /*
        MediaItem img1 = new MediaItem("C:/Users/luchy/OneDrive/Pictures/project images/IMG_2831.heic");
        MediaItem img2 = new MediaItem("C:/Users/luchy/OneDrive/Pictures/project images/IMG_4457.heic");

        MapCaller map = new MapCaller();

        System.out.println("Image 1 Lat: "+img1.getGps()[0] + " Lon:" + img1.getGps()[1]);

        System.out.println("Image 2 Lat: "+img2.getGps()[0] + " Lon:" + img2.getGps()[1]);

        byte[] mapBytes = map.mapRequest(img1.getGps(), img2.getGps());
        File mapFile = new File("map.png");
        ConversionHandler.decodeByteResponse(mapBytes, mapFile);
        */
    }

    private static void sortByDate(ArrayList<MediaItem> items) {
        System.out.println("Found where I got stuck");
        items.sort(Comparator.comparing(MediaItem::getCreatedAt));
    }
}
