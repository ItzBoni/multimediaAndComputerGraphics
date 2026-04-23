package models;

import wrappers.ExifTool;
import wrappers.FFmpeg;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Factory for creating fully-initialized MediaItem instances.
 *
 * Orchestrates the complete initialization process:
 * - Extracting representative frames from videos
 * - Converting HEIC format to JPEG
 * - Extracting EXIF metadata (GPS, date)
 * - Populating all MediaItem fields
 *
 * Keeps complex initialization logic and external tool calls
 * out of the MediaItem data model itself.
 */
public class MediaItemFactory {

    /**
     * Creates a fully-initialized MediaItem from a file path.
     *
     * Performs the following operations:
     * 1. Creates base MediaItem with file reference
     * 2. For videos: extracts representative frame at 00:00:01
     * 3. For HEIC images: converts to JPEG using FFmpeg
     * 4. Extracts EXIF metadata (GPS, creation date) using ExifTool
     * 5. Parses extracted metadata and populates MediaItem
     *
     * @param fileLocation absolute or relative path to the media file
     * @return fully-initialized MediaItem with all metadata extracted
     * @throws RuntimeException if file processing or tool execution fails
     */
    public static MediaItem create(String fileLocation) {
        File file = new File(fileLocation);
        MediaItem item = new MediaItem(file);

        if (item.isVideo()) {
            System.out.println("I'm getting into the video");
            String fileName      = file.getName();
            String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
            String frameDirectory = fileLocation.substring(0, fileLocation.length() - fileName.length());
            String framePath      = frameDirectory + nameWithoutExt + "_repFrame.jpg";

            FFmpeg.saveRepresentativeFrame(file, framePath);
            item.setRepresentativeFrame(new File(framePath));
        }

        if (MediaItem.isHeicFile(file)) {
            String fileName  = file.getName();
            String nameNoExt = fileName.substring(0, fileName.lastIndexOf('.'));
            String dir       = fileLocation.substring(0, fileLocation.length() - fileName.length());
            File converted   = FFmpeg.convertHeicToJpeg(file, dir + nameNoExt + "_converted.jpg");
            item.setFile(converted);
        }

        String raw = ExifTool.extractMetadata(fileLocation);
        item.setGpsLat(MetadataParser.parseGPS(raw, "GPS Latitude"));
        item.setGpsLon(MetadataParser.parseGPS(raw, "GPS Longitude"));

        LocalDateTime dt = MetadataParser.parseDate(raw);
        item.setCreatedAt(dt != null ? dt : MetadataParser.fallbackTime(new File(fileLocation)));

        return item;
    }
}
