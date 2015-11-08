// Sketch to upload pictures to Dropbox when motion is detected
#include <Bridge.h>
#include <Process.h>
#include <Servo.h> 
#include "pitches.h"

Servo myservo;  
Process picture;

unsigned long time = 0;
const int calibrationTime = 10;
const int pirPin = 3;
const int motorPin = 9;
int pos = 0;  
const int blue = 1;
const int red = 0;

int melodyUp[] = {
  NOTE_C3, NOTE_D3, NOTE_E3, NOTE_F3, NOTE_G3
};

int melodyDown[] = {
  NOTE_G3, NOTE_F3, NOTE_E3, NOTE_D3, NOTE_C3
};

int noteDurations[] = {
  16, 16, 16, 16, 16
};

int melodyComing[] = {
  NOTE_G3
};

int noteDurationsComing[] = {
  16
};

void move_cloth(int color)
{
  if(color == blue) {
    myservo.write(160);  
  }
  else if (color == red) {
    myservo.write(20);  
  }  
}

void setup() {  
  // Bridge
  Bridge.begin();

  pinMode(pirPin, INPUT);
  pinMode(13, OUTPUT);
  digitalWrite(pirPin, LOW);

  Serial.print("Starting servo...");
  myservo.attach(motorPin);  // attaches the servo on pin 9 to the servo object 
  myservo.write(90); 
    
  //give the sensor some time to calibrate
  Serial.print("calibrating sensor ");
  for(int i = 0; i < calibrationTime; i++){
    Serial.print(".");
    delay(1000);
  }
  Serial.println(" done");
  Serial.println("SENSOR ACTIVE");
  delay(50); 
}

void playUp() {
  // iterate over the notes of the melody:
  for (int thisNote = 0; thisNote < 5; thisNote++) {

    // to calculate the note duration, take one second
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1500 / noteDurations[thisNote];
    tone(8, melodyUp[thisNote], noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(8);
  }
}

void playDown() {
  // iterate over the notes of the melody:
  for (int thisNote = 0; thisNote < 5; thisNote++) {

    // to calculate the note duration, take one second
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1500 / noteDurations[thisNote];
    tone(8, melodyDown[thisNote], noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(8);
  }
}

void playComing() {
  // iterate over the notes of the melody:
  for (int thisNote = 0; thisNote < 1; thisNote++) {

    // to calculate the note duration, take one second
    // divided by the note type.
    //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
    int noteDuration = 1500 / noteDurationsComing[thisNote];
    tone(8, melodyComing[thisNote], noteDuration);

    // to distinguish the notes, set a minimum time between them.
    // the note's duration + 30% seems to work well:
    int pauseBetweenNotes = noteDuration * 1.30;
    delay(pauseBetweenNotes);
    // stop the tone playing:
    noTone(8);
  }
}

void wifiCheck() {
  Process wifiCheck;  // initialize a new process

  Serial.println();

  wifiCheck.runShellCommand("/usr/bin/pretty-wifi-info.lua");  // command you want to run

  // while there's any characters coming back from the
  // process, print them to the serial monitor:
  while (wifiCheck.available() > 0) {
    char c = wifiCheck.read();
    Serial.print(c);
  }

  Serial.println();
}

int getColor()
{
    String returnString;

    Serial.println("Starting caputring");    
    picture.runShellCommand("fswebcam /www/nice295/capture.jpg -r 100x100");
    while(picture.running());
    Serial.println("End caputring"); 

    Serial.println("Starting analysising"); 
    picture.runShellCommand("python /www/nice295/CaptureAndColor.py");
    Serial.println("picture end");
    while (picture.available() > 0) {
      char c = picture.read();
      returnString += c;     
    }
    Serial.println("End analysising");

    Serial.println(returnString);
   
    if (returnString.substring(0,3) == "red")
      return 0;
    else if (returnString.substring(0,4) == "blue")
      return 1;
    else
      return 1;
}

void loop(void) 
{
  int color;

  if (digitalRead(pirPin) == HIGH){
    Serial.println("Clothe coming...");
    playComing();
    
    delay(1000);
    
    color = getColor();
    Serial.print("Color: ");
    Serial.println(color); 
 
    move_cloth(color);
    delay(1000);
    myservo.write(90);              // tell servo to go to position in variable 'pos'    
  }
  else {
    Serial.print(".");
  }
  
  delay(100);
}

