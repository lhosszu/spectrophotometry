#include <Arduino.h>
#include <BLEServer.h>

class ServerCallbacks : public BLEServerCallbacks
{
public:
    void onConnect(BLEServer *pServer);
    void onDisconnect(BLEServer *pServer);
};

class BatteryCharacteristicCallbacks : public BLECharacteristicCallbacks
{
public:
    void onRead(BLECharacteristic *pCharacteristic)
    {
        Serial.print("BATTERY characteristic READ by client, value ");
        Serial.println(pCharacteristic->getValue().c_str());
    };
    void onNotify(BLECharacteristic *pCharacteristic)
    {
        Serial.println("BATTERY characteristic NOTIFY client");
    };
};

class SpectrumCharacteristicCallbacks : public BLECharacteristicCallbacks
{
public:
    void onRead(BLECharacteristic *pCharacteristic){
        Serial.print(pCharacteristic->getValue().c_str());
    };
    void onNotify(BLECharacteristic *pCharacteristic)
    {
        Serial.println("SPECTRUM characteristic NOTIFY client");
    };
};

class TriggerCharacteristicCallbacks : public BLECharacteristicCallbacks
{
public:
    void onWrite(BLECharacteristic *pCharacteristic);
};