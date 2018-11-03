////////////////////////////////////////////////////////////////////////////
// Example application for data publishing to an MQTT broker from Arduino
////////////////////////////////////////////////////////////////////////////
// Used in course "Computer networks and network programming", NTNU, Aalesund

#include <WiFiNINA.h>
#include <PubSubClient.h>

char ssid[]     = "your-wifi-SSID";
char password[] = "your-wifi-password";

// IP address of the MQTT broker (server)
IPAddress serverIP(18, 197, 2, 56);
unsigned int serverPort = 1883;

WiFiClient wificlient;
PubSubClient mqttclient(wificlient);

void setup() {
  Serial.begin(9600);
  Serial.print("Connecting");
  while (WiFi.begin(ssid, password) != WL_CONNECTED){
    Serial.print(".");
    delay(100);
  }
  Serial.println("\nConnected");
  mqttclient.setServer(serverIP, serverPort);
  Serial.println("Connecting to MQTT server");
  while (!mqttclient.connected()){
    mqttclient.connect("TestNode1762"); //name MUST BE unique
    delay(100);
  }
  Serial.println("Connected to MQTT server");

  // You don't need to subscribe to anything in this exercise. But in theory, 
  // this Arduino node can listen for data published by others
  //mqttclient.subscribe("/sensors/light");
}

unsigned long lastPublish = 0;

void loop() {
  mqttclient.loop();

  if (millis() - lastPublish > 5000){
    lastPublish = millis();
	// Publish a static value. Put your sensor data here
    mqttclient.publish("/ntnu/datakomm/sensors/dummy13", "13");
  }
}

void callback(char* topic, byte* payload, unsigned int length){
  //do things with the data received
  //topic is the "room" the message was posted in (example: /sensors/light)
  //payload is a string of bytes with length length, and can for example be text, a number/numbers or entire datastructures.
}
