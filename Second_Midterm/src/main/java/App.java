import models.MediaItem;
import models.MediaItemFactory;
import services.MediaPipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point. Responsible only for collecting user input and handing off to MediaPipeline.
 */
public class App {
    private static final String SEPARATOR = "═".repeat(70);
    private static final String LIGHT_SEPARATOR = "─".repeat(70);

    public static void main(String[] args) {
        ArrayList<MediaItem> elements = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        printWelcome();

        String projectDirectory = getProjectDirectory(sc);
        getMediaFiles(sc, elements);

        printSummary(elements, projectDirectory);

        if (!elements.isEmpty()) {
            new MediaPipeline().run(elements, projectDirectory);
        } else {
            System.out.println("\n⚠️  No media files added. Exiting...");
        }
    }

    private static void printWelcome() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("   🎬 Welcome to the AI Video Generator! 🎬");
        System.out.println(SEPARATOR);
    }

    private static String getProjectDirectory(Scanner sc) {
        System.out.println("\n📁 PROJECT OUTPUT DIRECTORY");
        System.out.println(LIGHT_SEPARATOR);
        System.out.println("Enter the directory where project files will be stored.");
        System.out.println("(Will create if it doesn't exist)\n");

        String projectDirectory;
        while (true) {
            System.out.print("▶ Output directory: ");
            projectDirectory = sc.nextLine().trim();

            if (projectDirectory.isEmpty()) {
                System.out.println("❌ Directory cannot be empty. Please try again.");
                continue;
            }

            if (!projectDirectory.endsWith(File.separator)) {
                projectDirectory += File.separator;
            }

            File projDir = new File(projectDirectory);
            if (!projDir.exists()) {
                if (projDir.mkdirs()) {
                    System.out.println("✓ Created directory: " + projectDirectory);
                } else {
                    System.out.println("❌ Failed to create directory. Please check the path and try again.");
                    continue;
                }
            } else {
                System.out.println("✓ Using directory: " + projectDirectory);
            }

            return projectDirectory;
        }
    }

    private static void getMediaFiles(Scanner sc, ArrayList<MediaItem> elements) {
        System.out.println("\n📸 ADD MEDIA FILES");
        System.out.println(LIGHT_SEPARATOR);
        System.out.println("Enter paths to media files (images, videos, audio).");
        System.out.println("Type 'done' when finished.\n");

        int fileCount = 0;
        String input;

        do {
            System.out.print("▶ File path [" + (fileCount + 1) + "]: ");
            input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            if (input.isEmpty()) {
                System.out.println("⚠️  Path cannot be empty. Please try again.");
                continue;
            }

            File file = new File(input);
            if (!file.exists()) {
                System.out.println("❌ File not found: " + input);
                continue;
            }

            try {
                MediaItem item = MediaItemFactory.create(input);
                elements.add(item);
                fileCount++;
                System.out.println("✓ Added: " + new File(input).getName() + " (" + fileCount + ")");
            } catch (RuntimeException e) {
                System.out.println("❌ Error loading file: " + input);
                System.out.println("   Reason: " + e.getMessage());
            }
        } while (true);
    }

    private static void printSummary(ArrayList<MediaItem> elements, String projectDirectory) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("   📋 SUMMARY");
        System.out.println(SEPARATOR);
        System.out.println("Output Directory: " + projectDirectory);
        System.out.println("Media Files: " + elements.size());

        if (!elements.isEmpty()) {
            System.out.println("\nFiles to Process:");
            for (int i = 0; i < elements.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + elements.get(i).getFilePath());
            }
        }

        System.out.println("\n" + SEPARATOR);
    }
}
