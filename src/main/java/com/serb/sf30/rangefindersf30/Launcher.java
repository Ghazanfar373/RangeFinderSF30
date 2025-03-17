package com.serb.sf30.rangefindersf30;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

public class Launcher extends Application {
    BorderPane borderPane;
    //ToolBar toolbar;
    Tile tileDistancePlot, tileHighLow;
    private long lastTimerCall;
    private AnimationTimer timer;
    private static final Random RND = new Random(100);
    @Override
    public void init() throws Exception {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets: 0;"
                + "-fx-border-radius: 3;" + "-fx-border-color: gray;" + "-fx-background-color: #303030;");
        tileDistancePlot  = TileBuilder.create().skinType(Tile.SkinType.SPARK_LINE).minSize(400, 200).title("Range").unit("m").middleText("fwf").strokeWithGradient(true).tickLabelDecimals(2).decimals(2).smoothing(true).gradientStops(new Stop(0, Color.web("#1CAF4D")),
                new Stop(0.0075,  Color.web("#1CAF4D")),
                new Stop(0.00751, Color.web("#91CA40")),
                new Stop(0.01166, Color.web("#91CA40")),
                new Stop(0.01167, Color.web("#F8C610")),
                new Stop(0.01666, Color.web("#F8C610")),
                new Stop(0.01667, Color.web("#F29222")),
                new Stop(0.025,   Color.web("#F29222")),
                new Stop(0.02501, Color.web("#EC1D24")),
                new Stop(1.0,     Color.web("#EC1D24"))).build();
        tileHighLow = TileBuilder.create().skinType(Tile.SkinType.LED).prefSize(100,100).description("Alarm").build();


        // Create the VBox
        HBox logbox = new HBox(10); // 10 is the spacing between elements
        logbox.setPadding(new Insets(1)); // Add padding around the edges
        //HBox loggerBox = new VBox(logbox,tileHighLow);
        // Create the button
        Button button = new Button("Connect");
        button.setPrefSize(30,10); // Set preferred width

        // Create the TextArea
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 1;" + "-fx-border-insets: 0;"
                + "-fx-border-radius: 3;" + "-fx-border-color: gray;" + "-fx-background-color: #303030;");
        textArea.setPrefRowCount(10); // Set preferred number of rows
        textArea.setPrefColumnCount(48); // Set preferred number of columns
        //textArea.setWrapText(true); // Enable text wrapping

        // Add event handler to the button
        button.setOnAction(e -> {
            //textArea.appendText("Button clicked!\n");
            readSerial();
        });
        logbox.getChildren().addAll(button, textArea);
        //logbox.setMinSize(500,200);

        // Add components to the VBox

        borderPane.setTop(tileHighLow);
        borderPane.setCenter(tileDistancePlot);
        borderPane.setBottom(logbox);
        lastTimerCall = System.nanoTime();
        //gaugeFuel1.setValue(Params.getInstance().pref.getInt(Params.getInstance().FuelTank,0 ));
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 100_000_000L) { //10_000_000_00L

                tileDistancePlot.setValue(Params.getInstance().getDistance()/100.0);
                tileDistancePlot.setTitle(Params.getInstance().getDistance()+"");
                //tileDistancePlot.setValue(RND.nextDouble());

                    if(Params.getInstance().getAlarmFlag()==0){

                        tileHighLow.setActive(true);
                    if(Params.getInstance().getDistance()>=0.50)    textArea.appendText(String.format("Alarm Triggered at Range: %.2f         RTC: %d         \t\n", Params.getInstance().getDistance() / 100.0, Params.getInstance().getMillis()));
                    }else {

                        tileHighLow.setActive(false);
                    }



                    lastTimerCall = now;
                }
                }
            };

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(borderPane, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lidar (SF30) Debugger 1.0");
        //Image icon = new Image(new File("appicon.ico").toURI().toURL().toString());

        //primaryStage.getIcons().add(new Image(new File(System.getProperty("user.dir")+"\\src\\main\\resources\\baseline_security_black_24dp.png").toURI().toURL().toString()));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
//            if(stageSetting!=null && stageSetting.isShowing()){
//                stageSetting.close();
//            }
//            if(stageAbout!=null && stageAbout.isShowing()){
//                stageAbout.close();
//            }
        });
        timer.start();
    }
    private void readSerial(){
        String[] ports = ArdiunoPort.getInstance().getComPorts();
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("USB Connection",ports);
        choiceDialog.getDialogPane().setPrefSize(100,50);
        try {
            //Image iconimage = new Image(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\baseline_security_black_24dp.png").toURI().toURL().toString());
           // ((Stage)choiceDialog.getDialogPane().getScene().getWindow()).getIcons().add(iconimage);
            choiceDialog.showingProperty().addListener((ov, b, b1)->{
                if(b1) {
                    Node comboBox = choiceDialog.getDialogPane().lookup(".combo-box");
                    comboBox.requestFocus();
                }
            });
            choiceDialog.showAndWait();
            String portName = choiceDialog.getSelectedItem();
            System.out.println(choiceDialog.getSelectedItem());
            boolean status = ArdiunoPort.getInstance().connectPort(portName);
            if(!status){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error in Connection!");
                //Image img = new Image(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\baseline_security_black_24dp.png").toURI().toURL().toString());
                //((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(img);
                alert.getDialogPane().setMaxWidth(230);
                alert.getDialogPane().setMaxHeight(70);
                alert.show();
            }else {
                //ArdiunoPort.getInstance().startPingThread();
                ArdiunoPort.getInstance().startFeedbackThread();
            }
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public void stop() throws Exception {
        timer.stop();
        try{
            if(ArdiunoPort.getInstance().isRunning()){
                ArdiunoPort.getInstance().arduinoPort.closePort();
                ArdiunoPort.getInstance().setRunning(false);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
        super.stop();
    }
    public static void main(String[] args) {
        launch();
    }
}