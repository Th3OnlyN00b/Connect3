#define TRIG0 4
#define ECHO0 5
#define TRIG1 6
#define ECHO1 7
#define TRIG2 8
#define ECHO2 9
#define TRIG3 10
#define ECHO3 11
#define TRIG4 12
#define ECHO4 13
#define threshold 2.0

float distance0[5];
float distance1[5];
float distance2[5];
float distance3[5];
float distance4[5];
byte count = 0;
byte b = -1;
bool playerTurn = false;

void setup() {
  Serial.begin(9600);

  pinMode(A0, OUTPUT);
  pinMode(A1, OUTPUT);
  pinMode(A2, OUTPUT);
  pinMode(A3, OUTPUT);
  pinMode(A4, OUTPUT);
  
  pinMode(TRIG0, OUTPUT);
  pinMode(ECHO0, INPUT);
  pinMode(TRIG1, OUTPUT);
  pinMode(ECHO1, INPUT);
  pinMode(TRIG2, OUTPUT);
  pinMode(ECHO2, INPUT);
  pinMode(TRIG3, OUTPUT);
  pinMode(ECHO3, INPUT);
  pinMode(TRIG4, OUTPUT);
  pinMode(ECHO4, INPUT);
}

float readDistance(byte TRIG, byte ECHO) { //Code lovingly borrowed and altered from Rui Wang
  digitalWrite(TRIG, LOW); delayMicroseconds(2);
  digitalWrite(TRIG, HIGH); delayMicroseconds(10);
  digitalWrite(TRIG, LOW);
  unsigned long timeout=micros()+26233L;
  while((digitalRead(ECHO)==LOW)&&(micros()<timeout));
  unsigned long start_time = micros();
  timeout=start_time+26233L;
  while((digitalRead(ECHO)==HIGH)&&(micros()<timeout));
  unsigned long lapse = micros() - start_time;
  return lapse*0.01716f;
}

void testDistanceSensors() {
  Serial.print("0: ");
  Serial.println(readDistance(TRIG0, ECHO0));
  delay(50);
  Serial.print("1: ");
  Serial.println(readDistance(TRIG1, ECHO1));
  delay(50);
  Serial.print("2: ");
  Serial.println(readDistance(TRIG2, ECHO2));
  delay(50);
  Serial.print("3: ");
  Serial.println(readDistance(TRIG3, ECHO3));
  delay(50);
  Serial.print("4: ");
  Serial.println(readDistance(TRIG4, ECHO4));
  delay(1250);
}

int columns[5]  = {0,0,0,0,0};

void loop(){
  /*while(true){
    testDistanceSensors();
  }*/
  if(Serial.available()){
    b = Serial.read();
    playerTurn = false;
    if(b == '0') {digitalWrite(A0, HIGH); columns[0]++;}
    if(b == '1') {digitalWrite(A1, HIGH); columns[1]++;}
    if(b == '2') {digitalWrite(A2, HIGH); columns[2]++;}
    if(b == '3') {digitalWrite(A3, HIGH); columns[3]++;}
    if(b == '4') {digitalWrite(A4, HIGH); columns[4]++;}
  }
  if(millis() % 250 == 0){
    distance0[count] = readDistance(TRIG0, ECHO0);
    delay(50);
    distance1[count] = readDistance(TRIG1, ECHO1);
    delay(50);
    distance2[count] = readDistance(TRIG2, ECHO2);
    delay(50);
    distance3[count] = readDistance(TRIG3, ECHO3);
    delay(50);
    distance4[count] = readDistance(TRIG4, ECHO4);
    int count2 = (count + 1) % 5;
    if(distance0[count2] == 0);
    else if(columns[0] < 4 && distance0[count] - distance0[count2] >= threshold){
      if(playerTurn){
        Serial.write('0');
        columns[0]++;
        playerTurn = false;
      }
      else if(b == '0'){
        playerTurn = true;
        digitalWrite(A0, LOW);
        b = -1;
        delay(2000);
      }
    }
    else if(columns[1] < 4 && distance1[count] - distance1[count2] >= threshold){
      if(playerTurn){
        Serial.write('1');
        columns[1]++;
        playerTurn = false;
      }
      else if(b == '1'){
        playerTurn = true;
        digitalWrite(A1, LOW);
        b = -1;
        delay(2000);
      }
    }
    else if(columns[2] < 4 && distance2[count] - distance2[count2] >= threshold || (distance2[count] > 50 && distance2[count] - distance2[count2] >= 100)){
      if(playerTurn){
        Serial.write('2');
        playerTurn = false;
        columns[2]++;
      }
      else if(b == '2'){
        playerTurn = true;
        digitalWrite(A2, LOW);
        b = -1;
        delay(2000);
      }
    }
    else if(columns[3] < 4 && distance3[count] - distance3[count2] >= threshold){
      if(playerTurn){
        Serial.write('3');
        playerTurn = false;
        columns[3]++;
      }
      else if (b == '3'){
        playerTurn = true;
        digitalWrite(A3, LOW);
        b = -1;
        delay(2000);
      }
    }
    else if(columns[4] < 4 && distance4[count] - distance4[count2] >= threshold){
      if(playerTurn){
        Serial.write('4');
        playerTurn = false;
        columns[4]++;
      }
      else if(b == '4'){
        playerTurn = true;
        digitalWrite(A4, LOW);
        b = -1;
        delay(2000);
      }
    }
    count = count2;
  }
}

