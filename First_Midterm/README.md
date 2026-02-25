# Image Editor PRO

A JavaFX-based image editor application that allows users to perform region-based transformations on images including rotation, cropping, and color inversion.

## Features

### Core Image Operations
- **Rotate**: Rotate a rectangular region of an image by angles that are multiples of 90°
- **Cut (Crop)**: Extract and keep only a specified rectangular region of an image
- **Invert Colors**: Invert the colors of a rectangular region (RGB values become inverted)
- **Save**: Export edited images to disk in supported formats

### User Interface
- Clean and intuitive GUI built with JavaFX
- Real-time image preview with dimensions displayed
- Region selection using coordinate inputs (x1, y1, x2, y2)
- Error handling with user-friendly messages
- Menu navigation system for file selection and operation management

## Requirements

### System Requirements
- **JDK**: Oracle OpenJDK 23 or compatible
- **Java Version**: Java 23+
- **Build Tool**: Maven 3.6+

### Dependencies
- JavaFX 17.0.6 (UI framework)
- BootstrapFX 0.4.0 (UI styling)
- JUnit 5 (testing)

## Building and Running

### Build the Project
```bash
mvn clean compile
```

### Run the Application
```bash
mvn clean javafx:run
```

This will compile the project and launch the Image Editor GUI.

## Project Structure

```
First_Midterm/
├── src/main/java/com/javafxdemo/first_midterm/
│   ├── HelloApplication.java           # Application entry point
│   ├── controllers/
│   │   ├── EditorController.java        # Main editor UI logic
│   │   ├── MainController.java          # View management and navigation
│   │   └── InitialMenuController.java   # Menu interface controller
│   └── utils/
│       ├── ImageTransformer.java        # Interface for image operations
│       ├── ImageTransformerTool.java    # Image transformation implementation
│       ├── FileHandler.java             # File I/O operations
│       ├── Handlers.java                # Event handling utilities
│       └── InvalidImageDimensionsException.java  # Custom exception
├── src/main/resources/                 # FXML UI layouts
└── pom.xml                              # Maven configuration
```

## Usage

### Basic Workflow
1. **Launch** the application using `mvn clean javafx:run`
2. **Load** an image from the menu
3. **Select** a rectangular region by entering coordinates:
   - `initial_x`, `initial_y`: Top-left corner (x, y)
   - `final_x`, `final_y`: Bottom-right corner (x, y)
4. **Apply** transformations using the available buttons:
   - Click **Rotate** and enter an angle (must be 0°, 90°, 180°, or 270°)
   - Click **Cut** to crop the image to the selected region
   - Click **Invert Colors** to invert colors in the selected region
5. **Save** the edited image using the Save button
6. **Return** to menu to load another image

### Input Constraints
- **Coordinates**: Must be valid integers within image bounds
- **Rotation Angle**: Must be a multiple of 90 (90, 180, 270, etc.)
- **Region**: Must form a valid rectangle (x1 < x2 and y1 < y2)

## Technical Details

### Image Processing
- Images are processed using `BufferedImage` from Java's AWT library
- Real-time display conversion from AWT BufferedImage to JavaFX Image
- Operations work on specified rectangular regions rather than entire images

### Architecture
- **MVC Pattern**: Controllers manage UI logic separate from image transformations
- **Interface-based Design**: `ImageTransformer` interface allows flexible implementation
- **Exception Handling**: Custom exceptions for invalid image dimensions and operation errors

## Development Notes

- The project uses Maven for dependency management and building
- JavaFX requires module configuration (see javafx-maven-plugin in pom.xml)
- UI layouts are defined in FXML files for separation of concerns
- Error messages are displayed in-app for user feedback

## Known Limitations

- Rotation angles are limited to multiples of 90 degrees
- Operations are performed on rectangular regions only (no freeform selections)
- Single image editing per session (must close and reopen for new images)
