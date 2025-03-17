package com.serb.sf30.rangefindersf30;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataLogger {
    private FileWriter realTimeWriter;// =new FileWriter("DataLog-"+formattedDate+".txt");
    private boolean mRunning = false;
    private boolean realRunning=false;
    private Date date;
    public DataLogger() {
        //init stuff
        try {
            date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd-HHmm").format(ts);
            String myDocumentPath = System.getProperty("user.home") + "\\Documents\\";
            System.out.println("DataLogging Started... ");
            System.out.println(myDocumentPath+"###########################");
            realTimeWriter=new FileWriter(myDocumentPath+ "RangeFinder-" + formattedDate + ".csv");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //......................................Your Portion
    //Display your name here
    //..............................................
    public synchronized void onEvent() {
        //...
        try {
            if (realRunning) {
                Calendar now = Calendar.getInstance();
                String ts = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
                String dataLog =ts+","+Params.getInstance().getMillis() + "," + Params.getInstance().getDistance()/100.0 + "," + Params.getInstance().getAlarmFlag()+ "\r\n";// + "," + Params.getInstance().getFuelBufferAvg() + "," + Params.getInstance().getFuelperHour();
                realTimeWriter.write(dataLog);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public synchronized void start() {
        //startdata logging
        realRunning = true;
        initialize();
    }
public synchronized void stopRealTime(){
        realRunning=false;
        try {
            realTimeWriter.close();
        }catch (IOException x){
            x.printStackTrace();
        }
}
    public synchronized void stop()  {
        //stop data logging
        try {
            realRunning = false;
            //closing File writer etc.
            realTimeWriter.close();
           // myWriter.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public  synchronized  void initialize(){
        try {
            String data = "Timestamp" + ","+"Millis"+ "," +"Distance" + "," + "Alarm" +"\r\n";//+ "," + "CHT Left" + "," + "fuel (ml/sec)" + "," + "fuel lit/Hr"+"\r\n";//,"+"FuelCons"+","+"FuelLvlT1"+","+"FuelLiters"+","+"VoltSrc1"+","+"VoltSrc2"+","+"VoltSrc3"+","+"VoltSrc4"+","+"VoltSrc5"+" \r\n";
            realTimeWriter.write(data);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public  synchronized  void logRealTime(double winspeed,int angle){
        try {
            if(realRunning) {
                Date date=new Date();
                Timestamp ts=new Timestamp(date.getTime());
                String formattedDate = new SimpleDateFormat("HHmmss").format(ts);
                String data = formattedDate+","+winspeed + "," + angle + "\r\n";
                realTimeWriter.write(data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
//   public void updateLogger(int payloadNumber) {
//        try {
//
//           // Timestamp ts=new Timestamp(date.getTime());
//           // String formattedDate = new SimpleDateFormat("yyyy-MM-dd-HHmm").format(ts);
//         //   System.out.println(formattedDate);
//            // myWriter = new FileWriter("DataLogAllinOne-"+formattedDate+".txt");
//            String data=payloadNumber+","+BtnHexData.getInstance().getWindSpeed()+","+BtnHexData.getInstance().getWindAngle()+","+BtnHexData.getInstance().getLatitude()+","+BtnHexData.getInstance().getLongitude();
//            myWriter.write(data);
//            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//    }
}
