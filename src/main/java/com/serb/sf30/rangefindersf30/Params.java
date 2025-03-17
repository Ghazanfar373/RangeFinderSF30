package com.serb.sf30.rangefindersf30;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.prefs.Preferences;

public class Params {
    public static Params param;
    private boolean isRunning = false;


    int crc;

    public static Params getInstance() {
        if (param == null) {
            param = new Params();
        }
        return param;
    }

    public int getCrc() {
        return crc;
    }

    public void setCrc(int crc) {
        this.crc = crc;
    }

    byte[] receivingPack;
    int bufferSize = 11;
    static Preferences pref;

    long millis;
    int distance;
    int alarmFlag;


    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(int alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    Params() {



        receivingPack = new byte[bufferSize];
        pref = Preferences.userRoot().node(getClass().getName());
    }

    public byte[] getReceivingPack() {
        return receivingPack;
    }

    public byte[] getReceivingPackSMD() {
        return receivingPack;
    }

    public void extractDownlink(byte[] receivingPack) {






        crc = (receivingPack[10]);
        if (calcXOR(receivingPack) == crc) {
            millis = ((long) (receivingPack[4] & 0xFF) << 24) |
                    ((receivingPack[3] & 0xFF) << 16) |
                    ((receivingPack[2] & 0xFF) << 8) |
                    (receivingPack[1] & 0xFF);

            int lowByte=receivingPack[7]&0xFF;
           int highByte = receivingPack[6]&0xFF;
            distance = ((lowByte << 8) | highByte);
            alarmFlag = receivingPack[9];
           // if(alarmFlag>1) alarmFlag=1;
            Params.getInstance().setMillis(millis);
            Params.getInstance().setDistance(distance);  //cm to m
            Params.getInstance().setAlarmFlag(alarmFlag);
            System.out.println("Millis- " + millis + "Distance- " + distance + "AlarmFlag- " + alarmFlag );// + "IAT- " + IAT + "BARO- " + BARO + "FuelConsm- " + fuelConsumption+"Battery- "+vol3+"Fuel1- "+fuelLevel1+"Fuel2- "+fuelLevel2+"Consm:  "+fuelConsumption);
            ArdiunoPort.loogger.onEvent();
        }
    }



//    public void extractDLAPack(byte buffer[]){
//        seconds = (buffer[1]>>8 & 0xff00) | (buffer[2] & 0x00ff);
//        pw1 =     (buffer[3]>>8 & 0xff00) | (buffer[4] & 0x00ff);
//        pw2 =     (buffer[5]>>8 & 0xff00) | (buffer[6] & 0x00ff);
//        RPM =     (buffer[7]>>8 & 0xff00) | (buffer[8] & 0x00ff);
//        adv_deg = (buffer[9]>>8 & 0xff00) | (buffer[10] & 0x00ff);
//        squirt =  (buffer[11] & 0xff);
//        engineState= (buffer[12] & 0xff);
//        afrtgt1 = (buffer[13] & 0xff);
//        afrtgt2 = (buffer[14] & 0xff);
//        wbo2_en1 = (buffer[15] & 0xff);
//        wbo2_en2 = (buffer[16] & 0xff);
//        BARO     = (buffer[17] & 0xff00) | (buffer[18] & 0x00ff);
//        map      = (buffer[19] & 0xff00) | (buffer[20] & 0x00ff);
//        IAT      = (buffer[21] & 0xff00) | (buffer[22] & 0x00ff);
//        ECT      = (buffer[23] & 0xff00) | (buffer[24] & 0x00ff);
//        TPS      = (buffer[25] & 0xff00) | (buffer[26] & 0x00ff);
//        batt     = (buffer[27] & 0xff00) | (buffer[28] & 0x00ff);
//
//        System.out.println("TimeStamp:"+ seconds+ " RPM-"+ RPM + "TPS- " + TPS + "ECT- " + ECT + "IAT- " + IAT + "BARO- " + BARO + "FuelConsm- " + fuelConsumption+"Battery- "+vol3+"Fuel1- "+fuelLevel1+"Fuel2- "+fuelLevel2+"Consm:  "+fuelConsumption);
//        Params.getInstance().setRPM(RPM );                          if((Params.getInstance().getMaxRPM())<(Params.getInstance().getRPM())){ Params.getInstance().setMaxRPM(Params.getInstance().getRPM());}
//        Params.getInstance().setTPS(TPS*10) ;                         if(Params.getInstance().getMaxTPS()<Params.getInstance().getTPS()){Params.getInstance().setMaxTPS(Params.getInstance().getTPS());}
//        Params.getInstance().setECT(ECT* 10);                     if(Params.getInstance().getMaxECT()<Params.getInstance().getECT()){Params.getInstance().setMaxECT(Params.getInstance().getECT());}
//        Params.getInstance().setIAT(IAT* 10);                     if(Params.getInstance().getMaxIAT()<Params.getInstance().getIAT()){Params.getInstance().setMaxIAT(Params.getInstance().getIAT());}
//        Params.getInstance().setBARO(BARO* 10);                   if(Params.getInstance().getMaxPres()<Params.getInstance().getBARO()){Params.getInstance().setMaxPres(Params.getInstance().getBARO());}
//    }
//
//    public void extractDLAPack2(byte buffer[]){
//        seconds = (buffer[0]>>8 & 0xff00) | (buffer[1] & 0x00ff);
//        pw1 =     (buffer[2]>>8 & 0xff00) | (buffer[3] & 0x00ff);
//        pw2 =     (buffer[4]>>8 & 0xff00) | (buffer[5] & 0x00ff);
//        RPM =     (buffer[6]>>8 & 0xff00) | (buffer[7] & 0x00ff);
//        adv_deg = (buffer[8]>>8 & 0xff00) | (buffer[9] & 0x00ff);
//        squirt =  (buffer[10] & 0xff);
//        engineState= (buffer[11] & 0xff);
//        afrtgt1 = (buffer[12] & 0xff);
//        afrtgt2 = (buffer[13] & 0xff);
//        wbo2_en1 = (buffer[14] & 0xff);
//        wbo2_en2 = (buffer[15] & 0xff);
//        BARO     = (buffer[16] & 0xff00) | (buffer[17] & 0x00ff);
//        map      = (buffer[18] & 0xff00) | (buffer[19] & 0x00ff);
//        mat      = (buffer[20] & 0xff00) | (buffer[21] & 0x00ff);
//        ECT      = (buffer[22] & 0xff00) | (buffer[23] & 0x00ff);
//        TPS      = (buffer[24] & 0xff00) | (buffer[25] & 0x00ff);
//        batt     = (buffer[26] & 0xff00) | (buffer[27] & 0x00ff);
//
//        System.out.println("TimeStamp:"+ seconds+ " RPM-"+ RPM + "TPS- " + TPS + "ECT- " + ECT + "IAT- " + IAT + "BARO- " + BARO + "FuelConsm- " + fuelConsumption+"Battery- "+vol3+"Fuel1- "+fuelLevel1+"Fuel2- "+fuelLevel2+"Consm:  "+fuelConsumption);
//
//    }
//    public void extractDownlinkSMD(byte[] buffer ) {
//        if((buffer[0] & 0xff)==45 && (buffer[9]& 0xff)==106 ) {
//            byte[] array = {buffer[4],buffer[5],buffer[6],buffer[7]};
//            fuelConsumption = (buffer[8] & 0xff);
//            RPM = (buffer[1]<<8 & 0xff00 ) | (buffer[2]& 0x00ff);
//            //System.out.println("RPM = "+RPM*10);
//            //System.out.println("FuelCons = "+fuelConsumption);
//            fuelConsumption = fuelConsumption/100;
//            if(fuelConsumption > 0.0) {
//                Params.getInstance().setFuelConsumption(fuelConsumption);
//                if ((Params.getInstance().getMaxFuelCons()) < (Params.getInstance().getFuelConsumption())) {
//                    Params.getInstance().setMaxFuelCons(Params.getInstance().getFuelConsumption());
//                }
//                Params.getInstance().setFuelperHour(getFuelBufferAvg());
//            }
//            Params.getInstance().setRPM(RPM); if((Params.getInstance().getMaxRPM())<(Params.getInstance().getRPM())){ Params.getInstance().setMaxRPM(Params.getInstance().getRPM());}
//            if(buffer[3]== (byte)0x2a){
//                //System.out.println("C1 = "+getfloat(array));
//                Params.getInstance().setCHTRight(getfloat(array));
//                if((Params.getInstance().getMaxCHTRight())<(Params.getInstance().getCHTRight())){Params.getInstance().setMaxCHTRight(Params.getInstance().getCHTRight());}
//            }
//            if(buffer[3]== (byte)0x4a){
//                //System.out.println("C2 = "+getfloat(array));
//                Params.getInstance().setCHTLeft(getfloat(array));
//                System.out.println("Temp :"+getfloat(array));
//                if((Params.getInstance().getMaxCHTLeft())<(Params.getInstance().getCHTLeft())){Params.getInstance().setMaxCHTLeft(Params.getInstance().getCHTLeft());}
//            }
//
//        }
//    }
    float getfloat(byte[] bytes){
        float f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return f;
    }
    int calcCRC(byte[] buffer) {
        int crc = 0;
        for (int i = 3; i <= 34; i++) {
            crc = crc ^ buffer[i];
        }
        return crc;
    }
    int calcXOR(byte[] buffer){
        int crc = buffer[0]^0x2d;
//                ^buffer[5]^buffer[6]^buffer[7]^buffer[8]^buffer[9]^buffer[10]^buffer[11]^buffer[12]
//                ^buffer[13]^buffer[14]^buffer[15]^buffer[16]^buffer[17]^buffer[18]^buffer[19]^buffer[20]^buffer[21]^buffer[22]
//                ^buffer[23]^buffer[24]^buffer[25]^buffer[26]^buffer[27]^buffer[28]^buffer[29]^buffer[30]^buffer[31]^buffer[32]
//                ^buffer[33]^buffer[34]^buffer[35];
        return crc;
    }
}
