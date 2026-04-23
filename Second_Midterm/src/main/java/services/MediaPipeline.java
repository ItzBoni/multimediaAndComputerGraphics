package services;

import api.AICaller;
import api.MapCaller;
import handlers.ConversionHandler;
import models.MediaItem;
import models.MediaItemFactory;
import wrappers.VideoStitcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Orchestrates the complete media-to-video generation pipeline.
 *
 * Pipeline steps:
 * 1. Sort media files chronologically
 * 2. Generate AI descriptions for each item
 * 3. Merge descriptions into unified narrative
 * 4. Generate text-to-speech audio narration
 * 5. Generate representative image from descriptions
 * 6. Generate map visualization of geographic route
 * 7. Assemble all elements into final video
 *
 * Coordinates interaction between API clients (AI, Map),
 * conversion handlers, and tool wrappers (FFmpeg, VideoStitcher).
 *
 * Keeps business logic separated from the App entry point.
 */
public class MediaPipeline {

    private final AICaller ai;
    private final MapCaller map;

    /**
     * Initializes the pipeline with API clients.
     */
    public MediaPipeline() {
        this.ai  = new AICaller();
        this.map = new MapCaller();
    }

    /**
     * Executes the complete media-to-video generation pipeline.
     *
     * @param elements list of media items to process (with metadata)
     * @param projectDirectory output directory for generated files
     */
    public void run(ArrayList<MediaItem> elements, String projectDirectory) {
        sortByDate(elements);

        for (MediaItem item : elements) {
            File fileToEncode = item.isVideo() ? item.getRepresentativeFrame() : item.getFile();
            String base64     = ConversionHandler.encodeToBase64(fileToEncode);
            System.out.println(base64);

            String description = ai.descriptionRequest(base64);
            item.setDescription(description);
            System.out.println("Description set for item");
        }

        StringBuilder fullDescription = new StringBuilder();
        for (MediaItem item : elements) {
            fullDescription.append(item.getDescription());
        }

        // Single merged TTS narration
        String mergedNarration = ai.mergeDescriptions(fullDescription.toString());
        byte[] audioBytes      = ai.audioRequest(mergedNarration);
        File audioFile         = new File(projectDirectory + "narration.mp3");
        audioFile.getParentFile().mkdirs();
        ConversionHandler.decodeByteResponse(audioBytes, audioFile);

        // Representative image generation
        String image    = ai.imageRequest(fullDescription.toString());
        File imageFile  = new File(projectDirectory + "representation.jpg");
        imageFile.getParentFile().mkdirs();
        ConversionHandler.decodeByteResponse(image, imageFile);
        elements.add(0, MediaItemFactory.create(imageFile.getAbsolutePath()));

        // Map generation
        byte[] mapBytes = map.mapRequest(elements.get(0).getGps(), elements.get(elements.size() - 1).getGps());
        File mapFile    = new File(projectDirectory + "map.png");
        ConversionHandler.decodeByteResponse(mapBytes, mapFile);
        MediaItem mapItem = new MediaItem(mapFile);
        mapItem.setDescription("Map visualization of route");
        elements.add(mapItem);

        VideoStitcher.stitch(elements, projectDirectory, audioFile);
    }

    /**
     * Sorts media items chronologically by creation date.
     *
     * Ensures the narrative follows a logical temporal sequence.
     *
     * @param items the list of media items to sort in-place
     */
    private static void sortByDate(ArrayList<MediaItem> items) {
        items.sort(Comparator.comparing(MediaItem::getCreatedAt));
    }
}
