#include <ESP8266WiFi.h>
#include <WiFiUdp.h>
#include <WiFiClient.h>

#define PIN_FORWARD     15            // specify the pins as PWN output
#define PIN_BACKWARD    13
#define PIN_LEFT        14
#define PIN_RIGHT       12

const char *ssid = "Raymond's 86"; // The name of the Wi-Fi network that will be created
const char *password = "raymond86";   // The password required to connect to it, leave blank for an open network

WiFiClient client;
WiFiServer server(8888);
byte databuf[5] = {0};

void reset() {
  memset(databuf, 0, sizeof(databuf));
  runWithNewData();
}

void runWithNewData() {
  analogWrite(PIN_FORWARD, databuf[0]);
  analogWrite(PIN_BACKWARD, databuf[1]);
  analogWrite(PIN_LEFT, databuf[2]);
  analogWrite(PIN_RIGHT, databuf[3]);
}

void startWiFi() { // Start a Wi-Fi access point, and try to connect to some given access points. Then wait for either an AP or STA connection
  WiFi.softAP(ssid, password);             // Start the access point
  Serial.print("Access Point \"");
  Serial.print(ssid);
  Serial.println("\" started\r\n");
}

void setupIO() {
  analogWriteRange(255);
  pinMode(PIN_FORWARD, OUTPUT);    // the pins with LEDs connected are outputs
  pinMode(PIN_BACKWARD, OUTPUT);
  pinMode(PIN_LEFT, OUTPUT);
  pinMode(PIN_RIGHT, OUTPUT);
  
  runWithNewData();
}

void startTCPServer() {
  // Start the TCP server
  server.begin();
}

void setup() {
  Serial.begin(115200);        // Start the Serial communication to send messages to the computer
  delay(10);
  Serial.println("\r\n");

  setupIO();

  startWiFi();                 // Start a Wi-Fi access point, and try to connect to some given access points. Then wait for either an AP or STA connection
  
  startTCPServer();
}

void loop() {
  client = server.available();
  if (client) {
    Serial.println("Client connected");
    while (client.connected()) {
      if (client.available()) {
        int readCount = client.readBytesUntil(0xff, databuf, 5);
        if (readCount  > 0) {
           Serial.println(databuf[0], HEX);
           Serial.println(databuf[1], HEX);
           Serial.println(databuf[2], HEX);
           Serial.println(databuf[3], HEX);
           runWithNewData();
        }
      }
    }
    Serial.println("Client disconnected");
    reset();
  }
}
