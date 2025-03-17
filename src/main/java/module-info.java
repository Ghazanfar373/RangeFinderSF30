module com.serb.sf30.rangefindersf30 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires eu.hansolo.tilesfx;
    requires com.fazecast.jSerialComm;
    requires java.sql;
    requires java.prefs;

    //requires org.controlsfx.controls;
    //requires eu.hansolo.tilesfx;

    opens com.serb.sf30.rangefindersf30 to javafx.fxml;
    exports com.serb.sf30.rangefindersf30;
}