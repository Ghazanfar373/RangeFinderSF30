
# RangeFinderSF30

## Description
RangeFinderSF30 is a Java application that interfaces with the SF30 LiDAR sensor to provide real-time range finding capabilities. It features a graphical user interface built with JavaFX, offering visual representations of distance measurements and alarm notifications.

## Features
- Real-time distance plotting using a spark line graph
- High/Low alarm indicator
- Serial port connection for communication with the SF30 LiDAR sensor
- Responsive UI that adapts to window resizing
- Logging functionality for distance measurements and alarm triggers

## Requirements
- Java 11 or higher
- JavaFX 17.0.6
- Gradle (for building and managing dependencies)

## Dependencies
- eu.hansolo:tilesfx:11.32
- com.fazecast:jSerialComm:[2.5.3,2.6.12)
- org.openjfx:javafx-controls:17.0.6
- org.openjfx:javafx-fxml:17.0.6

## Building the Project
This project uses Gradle as its build system. To build the project.


## Usage
1. Launch the application
2. Click the "Connect" button to select a COM port for the SF30 LiDAR sensor
3. Once connected, the application will start displaying real-time distance measurements
4. The spark line graph shows the distance trend over time
5. The LED indicator will light up when an alarm condition is met (distance ≥ 0.50m)
6. The text area at the bottom logs alarm events with timestamps

## Project Structure
- `src/main/java/com/serb/sf30/rangefindersf30/`: Contains the main Java source files
- `src/main/resources/`: Contains any resource files (if applicable)
- `src/arduino/`: Contains the Arduino code for the SF30 LiDAR sensor interface

## Contributing
Contributions to the RangeFinderSF30 project are welcome. Please feel free to submit pull requests or create issues for bugs and feature requests.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

