// www.Lightware.co.za          July 2020
// Lightware example code using the lw_sf30.h library
// This library enables the use of the I2C and Serial Interface

// This library is designed to use 2 Serial ports on the Arduino

#include <lw_sf30d.h>
#include <Wire.h>
//#include <Nextion.h> 

LW_SF30 sf30(Serial1, Serial);
const int alarmPin = 7;
uint8_t alarmState =0; 
uint16_t distance=0;
volatile bool triggerFlag = false; // Flag to indicate the event
void setup() {

//________________________________________________________
//   pinMode(Green, OUTPUT); // Declare the LED as an output
//   pinMode(Blue, OUTPUT); // Declare the LED as an output
//   pinMode(Red, OUTPUT); // Declare the LED as an output

//__________________________________________________________

// Alarm Pin Mode
pinMode(alarmPin, INPUT);
//attachInterrupt(digitalPinToInterrupt(alarmPin), eventTriggered, RISING); // Attach interrupt  
  //this is the serial port for the terminal window
  Serial.begin(115200);

  // Setup the Serial port for the sf30 interface
  Serial1.begin(115200);

  // Startup Nextion Serial
    // Serial2.begin(9600); 

  // Request the sf30 Hardware Name
  sf30.readRequestHardwareName();
  delay(100);
  // Read the data sent from the sf30
  sf30.ProcessSerialInput(1);
  delay(100);

  // Disable any possible streaming data
  sf30.writeDataStreamType(0);
  delay(100);
  // Read the data sent from the sf30
  sf30.ProcessSerialInput(1);
  delay(100);

  
  // Request the sf30 Hardware Name
  sf30.readRequestHardwareName();
  delay(100);
  // Read the data sent from the sf30
  sf30.ProcessSerialInput(1);
  delay(100);

  // Request the sf30 Firmware Version
  sf30.readRequestFirmwareVersion();
  delay(100);
  // Read the data sent from the sf30
  sf30.ProcessSerialInput(1);
  delay(100);

  // Request the streaming output distance data selection
  sf30.writeDistOutConfig(255);
  //sf30.readRequestDistOutConfig();
  delay(100);
  // Read the data sent from the sf30
  sf30.ProcessSerialInput(1);
  delay(100);
  
  // Set the streaming to distance in cm
  sf30.writeDataStreamType(5);
  delay(100);
  // Read the data sent from the sf30
  sf30.ProcessSerialInput(1);
  delay(100);
  
}

void loop() {
  uint8_t new_data = 0;
  // Every cycle check the Serial recieve buffer for data and then process it
  new_data = sf30.ProcessSerialInput(0);

  // if new data was recieved, then display the following first and last distance
 /* if (new_data == 1){
    Serial.print(sf30.firstRaw_cm,DEC);
    Serial.print(" ");
    Serial.print(sf30.firstFiltered_cm,DEC);
    Serial.print(" ");
    Serial.print(sf30.firstStrength_cm,DEC);
    Serial.print(" ");
    Serial.print(sf30.lastRaw_cm,DEC);
    Serial.print(" ");
    Serial.print(sf30.lastFiltered_cm,DEC);
    Serial.print(" ");
    Serial.print(sf30.lastStrength_cm,DEC);
    Serial.print(" ");
    Serial.print(sf30.backgroundNoise,DEC);
    Serial.print(" ");
    Serial.println(sf30.APDTemperature,DEC);
    new_data = 0;
  }*/

  unsigned long timestamp = millis();
  //if(triggerFlag) alarmState=1; else alarmState=0;
  alarmState = digitalRead(alarmPin);
  //distance = sf30.lastRaw_cm;
  distance = sf30.lastFiltered_cm;
//  Serial.print(timestamp);
//  Serial.print(" ");
//  Serial.print(alarmState);
//  Serial.print(" ");
//  Serial.println(distance);



 SendPacketFormation(timestamp,alarmState, distance);
 delay(20); 

}
void eventTriggered() {
       triggerFlag = true; // Set the flag in the ISR
}
void SendPacketFormation(unsigned long timestamp,uint8_t state,uint16_t distance ){
  
  // Construct the 4-byte packet
  uint8_t header = 0x6A;           // Packet header (1 byte)
  byte crc = header ^ 0x2d;    // Simple CRC calculation (XOR)
  //uint16_t crc = header ^ state ^ (distance & 0xFF) ^ ((distance >> 8) & 0xFF); // CRC
  
//distance = 945;
// Send the packet over serial
  Serial.write(header);           // Send header
  Serial.write((byte*)&timestamp, sizeof(timestamp));
  

 // Serial.write((distance >> 8) & 0xff);
 // Serial.write((distance & 0xff));
              // Send button state
  //Serial.write((byte*)&distance, sizeof(distance));
  Serial.write(0x2d);
  Serial.write(lowByte(distance)); // Send lower byte of distance
  Serial.write(highByte(distance)); // Send higher byte of distance
  Serial.write(0x2d);
  Serial.write(state);
  Serial.write(crc); 

  delay(25);
  
  }
