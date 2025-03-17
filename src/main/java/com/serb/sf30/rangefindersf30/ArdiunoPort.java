package com.serb.sf30.rangefindersf30;

import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;


public class ArdiunoPort {
    public static String devicePortName = "COM14";
    public static SerialPort arduinoPort = null;
    public static InputStream arduinoStream = null;

    private static ArdiunoPort single_instance = null;
    static String logText = "";
    boolean status = false;

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setRunning(boolean running) {
        isRunning = running;
        if (!running) {
            if (loogger != null) {
                loogger.stop();
            }
        }
    }

    public static ArdiunoPort getInstance() {
        if (single_instance == null)
            single_instance = new ArdiunoPort();
        return single_instance;
    }

    private static boolean isRunning = false;
    static DataLogger loogger;

    boolean connectPort(String port) {
        int len = SerialPort.getCommPorts().length;
        SerialPort serialPorts[] = SerialPort.getCommPorts();
        for (int i = 0; i < len; i++) {
            String portName = serialPorts[i].getDescriptivePortName();
            System.out.println(serialPorts[i].getSystemPortName() + ": " + portName + ": " + i);
            if (portName.contains(port)) {
                try {
                    arduinoPort = serialPorts[i];
                    arduinoPort.setBaudRate(115200);
                    arduinoPort.openPort();
                    getInstance().setRunning(true);
                    System.out.println("connected to: " + portName + "[" + i + "]");
                    logText = "Connected to: " + portName;
                    // Create HexData Object
                    Params.getInstance();
                    loogger = new DataLogger();
                    //sendData(BtnHexData.getInstance().getSendingPack(),isRunning());
                    loogger.start();
                    status = true;
                    //break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return status;


        // (new Thread(new SerialReader(BtnHexData.getInstance().getReceivingPack()))).start();
    }

    String[] getComPorts() {
        int len = SerialPort.getCommPorts().length;
        String[] ports = new String[len];
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (int i = 0; i < len; i++) {
            ports[i] = serialPorts[i].getSystemPortName();
        }
        return ports;
    }

    public static void connectPort2(String port) {
        devicePortName = port;

        int len = SerialPort.getCommPorts().length;
        SerialPort serialPorts[] = new SerialPort[len];
        serialPorts = SerialPort.getCommPorts();

        for (int i = 0; i < len; i++) {

            String portName = serialPorts[i].getDescriptivePortName();
            System.out.println(serialPorts[i].getSystemPortName() + ": " + portName + ": " + i);
            loogger = new DataLogger();
            if (portName.contains(devicePortName)) {
                try {
                    arduinoPort = serialPorts[i];
                    arduinoPort.setBaudRate(115200);
                    arduinoPort.openPort();
                    setRunning(true);
                    System.out.println("connected to: " + portName + "[" + i + "]");
                    logText = "Connected to: " + portName;

                    loogger.start();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    // System.out.println("Not connected to: " + portName + "[" + i + "]");
                    loogger.stop();
                    arduinoPort.closePort();
                }
            }

        }

        //  (new Thread(new SerialReader(Params.getInstance().getReceivingPackSMD()))).start();

//--------give it a rest--------------------------------

        //arduinoPort.removeDataListener();
        // arduinoPort.closePort();
//        arduinoPort.addDataListener(new SerialPortDataListener() {
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//
//            @Override
//            public void serialEvent(SerialPortEvent event) {
//if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
// char[] buffer=new char[36];
//
//                BufferedReader portreader=new BufferedReader(new InputStreamReader(arduinoPort.getInputStream()));
//                try{
//                  //  portreader.read(buffer,0,36);
//                    String line=portreader.readLine();
//                    System.out.println(line);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                arduinoPort.readBytes(Params.getInstance().getReceivingPack(),36,0);
//                // if((Params.getInstance().getReceivingPack()[35] & 0xff)==13 && (Params.getInstance().getReceivingPack()[36] &0xff)==10)
//                Params.getInstance().extractDownlink(Params.getInstance().getReceivingPack());
//               // System.out.println(bytesToHexString(Params.getInstance().getReceivingPack()));
//                try {
//                    Thread.sleep(142);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }

    void startFeedbackThread() {
        (new Thread(new SerialReader(Params.getInstance().getReceivingPack()))).start();
    }

    void startFeedbackThrea() {
        (new Thread(new SerialReader(Params.getInstance().getReceivingPack()))).start();
    }

    void startPingThread() {
        new Thread(new SerialWriter()).start();
    }

    public static class SerialReader implements Runnable {
        byte[] buffer;

        public SerialReader(byte[] buffer) {
            this.buffer = buffer;
            System.out.println("Reader");

        }

        public void run() {

            readRangeData(buffer);
        }
    }

    public static class SerialWriter implements Runnable {
        byte[] array;

        public SerialWriter() {
            array = new byte[]{0x61};

        }

        public void run() {

            pingDLAEFI(array);

        }
    }




//    public static void readData(byte[] buffer,boolean loopStatus){
//        while (isRunning()){
//
//            arduinoPort.readBytes(Params.getInstance().getReceivingPack(),38,0);
//           // System.out.println(arduinoPort.getReadTimeout()+"  "+arduinoPort.getDeviceReadBufferSize());
//           // System.out.println(Params.getInstance().getReceivingPack()[35] & 0xff);
//            if((Params.getInstance().getReceivingPack()[0] & 0xff)==144 && (Params.getInstance().getReceivingPack()[1]& 0xff)==159 && (Params.getInstance().getReceivingPack()[2]& 0xff)==250 ) {
//                if ((Params.getInstance().getReceivingPack()[37] & 0xff) == 187) {
//                        Params.getInstance().extractDownlink(Params.getInstance().getReceivingPack());
//
//                    String bufferD = bytesToHexString(Params.getInstance().getReceivingPack());
//                 //   System.out.println(bufferD+"CRC>>"+getCRC(Params.getInstance().getReceivingPack())+"Buffer CRC>>"+Params.getInstance().getCrc());
//                    Params.getInstance().setDownlinkBuffer(bufferD);
//                    loogger.onEvent();
//                }
//            }
//            try {
//                Thread.sleep(200);   //from 20 to 200
//                pingDLAEFI(new byte[]{0x61});
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

    public static void readDataSMD(byte[] buffer,boolean loopStatus){
        while (isRunning()){



          //  System.out.println(arduinoPort.getReadTimeout()+"  "+arduinoPort.getDeviceReadBufferSize());
            // System.out.println(Params.getInstance().getReceivingPack()[35] & 0xff);
          //  arduinoPort.readBytes(Params.getInstance().getReceivingPackSMD(),1);
            // if(Params.getInstance().getReceivingPackSMD()[0]==(byte)0x2d) {
            //if((Params.getInstance().getReceivingPackSMD()[0] & 0xff)==45 && (Params.getInstance().getReceivingPackSMD()[12]& 0xff)==106 ) {
                arduinoPort.readBytes(Params.getInstance().getReceivingPackSMD(),10,0);
                String bufferD = bytesToHexString(Params.getInstance().getReceivingPackSMD());
                System.out.println(bufferD);
                if(Params.getInstance().getReceivingPackSMD()[0]==0x2d && Params.getInstance().getReceivingPackSMD()[9]==0x6a) {
                    Params.getInstance().extractDownlink(Params.getInstance().getReceivingPackSMD());
                }

                // String bufferD = bytesToHexString(Params.getInstance().getReceivingPackSMD());
                //   System.out.println(bufferD+"CRC>>"+getCRC(Params.getInstance().getReceivingPack())+"Buffer CRC>>"+Params.getInstance().getCrc());
                //Params.getInstance().setDownlinkBuffer(bufferD);
                //loogger.onEvent();
           // }
            //}
            loogger.onEvent();
            try {
                Thread.sleep(200);      //from 50 to 200
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void pingDLAEFI(byte[] buffer) {
        while (isRunning())
            arduinoPort.writeBytes(buffer, 1, 0);

        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void readRangeData(byte[] buffer){
        while (isRunning()) {
            arduinoPort.readBytes(Params.getInstance().getReceivingPack(), 11, 0);
            String bufferD = bytesToHexString(Params.getInstance().getReceivingPack());
            System.out.println("Rec "+bufferD);
           if (Params.getInstance().getReceivingPack()[0] == 0x6A)
               Params.getInstance().extractDownlink(Params.getInstance().getReceivingPack());
        try {
            Thread.sleep(30);
        }catch (Exception e){
            e.printStackTrace();
        }
        }
    }
    public static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for      (byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }
}
