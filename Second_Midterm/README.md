# AI Video Generator

A Java-based application that transforms a collection of multimedia files (images, videos, audio) into a polished narrative video with AI-generated narration, metadata extraction, and geospatial visualization.

## Overview

The AI Video Generator pipeline automates the creation of multimedia presentations by:
1. **Metadata Extraction** - Extracts EXIF data, GPS coordinates, and creation dates from media files
2. **AI Analysis** - Uses OpenAI's GPT-4V to generate descriptions for each media item
3. **Content Generation** - Creates unified narration via text-to-speech and generates representative images
4. **Geospatial Mapping** - Generates a map visualization of the geographic journey
5. **Video Assembly** - Stitches all elements together into a final video product

## Features

- **Multi-format Support**: Images (JPEG, PNG, HEIC), videos (MP4, MOV, AVI, MKV, etc.), and audio files
- **Automatic HEIC Conversion**: Converts Apple's HEIC/HEIF format to JPEG for compatibility
- **Video Frame Extraction**: Automatically extracts representative frames from video files
- **GPS-aware**: Extracts and visualizes geographic coordinates from media metadata
- **AI-powered Narration**: Uses OpenAI GPT-4 mini for intelligent image descriptions
- **Text-to-Speech**: Generates natural-sounding audio narration
- **Image Generation**: Creates visual representations based on content analysis
- **Chronological Sorting**: Organizes media by creation date for logical narrative flow

## System Requirements

- **Java 17+** (Maven project targets Java 17)
- **FFmpeg** (required for video operations and HEIC conversion)
  - Must be installed and accessible in your system PATH
  - Install via: `choco install ffmpeg` (Windows) or `brew install ffmpeg` (macOS)
- **ExifTool** (required for metadata extraction)
  - Install via: `choco install exiftool` (Windows) or `brew install exiftool` (macOS)
- **OpenAI API Key** (for AI-powered features)
  - Sign up at https://platform.openai.com
  - Create an API key from your account settings

## Installation

### Prerequisites Setup

1. **Install FFmpeg**
   ```bash
   # Windows (using Chocolatey)
   choco install ffmpeg
   
   # macOS
   brew install ffmpeg
   ```

2. **Install ExifTool**
   ```bash
   # Windows (using Chocolatey)
   choco install exiftool
   
   # macOS
   brew install exiftool
   ```

3. **Set Up Environment Variables**
   Set system environment variables for API keys:
   
   **Windows (Command Prompt):**
   ```cmd
   set OpenAIToken=your-api-key-here
   set GeoapifyToken=your-map-api-key-here
   java -cp target/classes App
   ```
   
   **Windows (PowerShell):**
   ```powershell
   $env:OpenAIToken="your-api-key-here"
   $env:GeoapifyToken="your-map-api-key-here"
   java -cp target/classes App
   ```
   
   **macOS/Linux:**
   ```bash
   export OpenAIToken=your-api-key-here
   export GeoapifyToken=your-map-api-key-here
   java -cp target/classes App
   ```

### Build & Compile

```bash
# Compile the project
mvn clean compile

# Package the application
mvn clean package

# Set environment variables and run (macOS/Linux)
export OpenAIToken=your-api-key-here
export GeoapifyToken=your-map-api-key-here
java -cp target/classes App

# Or for Windows Command Prompt:
# set OpenAIToken=your-api-key-here
# set GeoapifyToken=your-map-api-key-here
# java -cp target/classes App
```

## Usage Guide

### Basic Workflow

1. **Start the Application**
   ```bash
   java -cp target/classes App
   ```

2. **Specify Output Directory**
   - Enter the directory path where all generated files will be stored
   - The application will create the directory if it doesn't exist

3. **Add Media Files**
   - Enter file paths one at a time (absolute or relative paths)
   - Type `done` when finished adding files
   - Supported formats:
     - **Images**: JPG, PNG, GIF, BMP, WebP, HEIC
     - **Videos**: MP4, MOV, AVI, MKV, FLV, WebM
     - **Audio**: MP3, WAV, AAC, FLAC, OGG, M4A

4. **Review Summary**
   - The application displays a summary of selected files and output directory
   - Review before processing begins

5. **Processing**
   The pipeline automatically:
   - Extracts metadata from each file
   - Generates AI descriptions
   - Creates narration and visuals
   - Builds the final video

### Example

```
Enter the directory where project files will be stored.
▶ Output directory: /path/to/output/

Enter paths to media files (images, videos, audio).
▶ File path [1]: /path/to/photo1.jpg
✓ Added: photo1.jpg (1)
▶ File path [2]: /path/to/video.mp4
✓ Added: video.mp4 (2)
▶ File path [3]: done

Output Directory: /path/to/output/
Media Files: 2
```

## Project Structure

```
Second_Midterm/
├── src/main/java/
│   ├── App.java                          # Entry point, user input handling
│   ├── api/
│   │   ├── AICaller.java                 # OpenAI API wrapper
│   │   ├── MapCaller.java                # Map generation API
│   │   ├── Connectable.java              # Base class for API callers
│   │   └── RequestHandler.java           # HTTP utilities
│   ├── handlers/
│   │   ├── ConversionHandler.java        # Base64/binary encoding utilities
│   │   └── RequestHandler.java           # HTTP request/response handling
│   ├── models/
│   │   ├── MediaItem.java                # Data model for media files
│   │   ├── MediaItemFactory.java         # Factory for MediaItem creation
│   │   └── MetadataParser.java           # EXIF/metadata parsing utilities
│   ├── services/
│   │   └── MediaPipeline.java            # Main orchestration logic
│   ├── utils/
│   │   └── GeoUtils.java                 # Geographic utilities
│   └── wrappers/
│       ├── ExifTool.java                 # ExifTool CLI wrapper
│       ├── FFmpeg.java                   # FFmpeg CLI wrapper
│       └── VideoStitcher.java            # Video assembly logic
├── pom.xml                               # Maven configuration
└── README.md                             # This file
```

## Key Components

### App.java
**Entry point** - Manages user input and initializes the pipeline. Handles:
- Directory selection and creation
- Media file input collection
- Validation and error handling

### MediaPipeline.java
**Orchestrator** - Coordinates the entire workflow:
1. Sorts media files chronologically
2. Generates AI descriptions for each item
3. Creates unified narration
4. Generates representative visuals
5. Creates geographic visualization
6. Stitches final video

### MediaItem.java & MediaItemFactory.java
**Data Models** - Represent media files with extracted metadata:
- File path and type information
- GPS coordinates (latitude/longitude)
- Creation date and time
- AI-generated descriptions
- Representative frames (for videos)

### API Wrappers (AICaller, MapCaller, RequestHandler)
**External Integrations** - Handle API communication:
- OpenAI GPT-4V for image analysis
- OpenAI TTS for audio generation
- OpenAI DALL-E for image creation
- Custom map API for geospatial visualization

### Tool Wrappers (FFmpeg, ExifTool)
**CLI Interfaces** - Manage external tools:
- **FFmpeg** - Extracts video frames, converts HEIC to JPEG
- **ExifTool** - Extracts EXIF metadata from images and videos

## Configuration

### Environment Variables
The application requires two system environment variables to be set:

- **OpenAIToken** - Your OpenAI API key (required)
  - Get it from: https://platform.openai.com/account/api-keys
  
- **GeoapifyToken** - Your Geoapify map API key (required)
  - Get it from: https://apidocs.geoapify.com/

### Maven Dependencies
- **JUnit 5** - Testing framework
- **Java 17** - HTTP client (built-in)

## Output Files

After processing, the output directory contains:
- `narration.mp3` - Unified text-to-speech narration
- `representation.jpg` - AI-generated representative image
- `map.png` - Geographic route visualization
- `final_video.mp4` - Assembled video with all elements
- `*_repFrame.jpg` - Representative frames from videos (intermediates)
- `*_converted.jpg` - Converted HEIC files (intermediates)

## Troubleshooting

### FFmpeg Not Found
**Error**: "ffmpeg: command not found"
**Solution**: Ensure FFmpeg is installed and in your system PATH
```bash
ffmpeg -version  # Test if installed correctly
```

### ExifTool Not Found
**Error**: "exiftool: command not found"
**Solution**: Ensure ExifTool is installed and in your system PATH
```bash
exiftool -ver  # Test if installed correctly
```

### API Key Errors
**Error**: "Invalid API key", "401 Unauthorized", or "environment variable is not set"
**Solution**: 
- Verify `OpenAIToken` and `GeoapifyToken` environment variables are set
- Ensure the keys are valid and have available API credits
- Check that environment variables are properly exported before running the application
- **Windows**: Use `set` command or set in System Properties
- **macOS/Linux**: Use `export` command or add to shell profile (~/.bashrc, ~/.zshrc)

### Out of Memory
**Error**: "Exception in thread 'main' java.lang.OutOfMemoryError"
**Solution**: Increase JVM heap size
```bash
java -Xmx4g -cp target/classes App
```

### File Not Found
**Error**: "File not found: [path]"
**Solution**: 
- Use absolute paths or verify relative paths from the working directory
- Ensure the file actually exists and you have read permissions

## Development

### Architecture Principles
- **Separation of Concerns**: Each class has a single responsibility
- **Factory Pattern**: MediaItemFactory handles complex object creation
- **Wrapper Pattern**: Tool wrappers abstract CLI interactions
- **Pipeline Pattern**: MediaPipeline orchestrates the workflow
- **Immutable Configuration**: Environment variables loaded once at startup

### Adding New Media Types
1. Update `MediaItem.VIDEO_EXTS` or add new extension set
2. Create corresponding handler in `MediaItemFactory`
3. Implement tool wrapper if external processing needed

### Testing
Run unit tests with Maven:
```bash
mvn test
```

## API Documentation

### AICaller Methods
- `descriptionRequest(base64Image)` - Generate description for an image
- `mergeDescriptions(descriptions)` - Unify multiple descriptions
- `audioRequest(text)` - Generate audio from text
- `imageRequest(prompt)` - Generate image from text

### MediaItemFactory
- `create(fileLocation)` - Build fully initialized MediaItem with all metadata

### ConversionHandler
- `encodeToBase64(file)` - Encode file to base64 string
- `decodeByteResponse(bytes, outputFile)` - Decode bytes and write to file

### FFmpeg Wrapper
- `saveRepresentativeFrame(videoFile, outputPath)` - Extract video frame at 00:00:01
- `convertHeicToJpeg(inputFile, outputPath)` - Convert HEIC to JPEG

## Performance Considerations

- **Large Videos**: Processing time increases with video duration; consider splitting long videos
- **API Rate Limiting**: OpenAI API has rate limits; processing many files may take time
- **Memory Usage**: Each image is converted to base64; large images consume more memory
- **Parallel Processing**: Currently sequential; future optimization could parallelize API calls

## License

This project is part of a university multimedia graphics course assignment.

## Support

For issues or questions:
1. Check the Troubleshooting section
2. Verify all system dependencies are installed
3. Review console output for specific error messages
4. Ensure API keys and permissions are correct

---

**Last Updated**: April 2026  
**Java Version**: 17+  
**Maven Version**: 3.6.0+
