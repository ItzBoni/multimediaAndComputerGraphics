package models;

import wrappers.ExifTool;
import wrappers.FFmpeg;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Responsible for building fully-initialised MediaItem instances.
 * Keeps orchestration logic (external tool calls) out of the MediaItem data model.
 */
public class MediaItemFactory {

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
            File converted   = ExifTool.convertToJpeg(file, dir + nameNoExt + "_converted.jpg");
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
