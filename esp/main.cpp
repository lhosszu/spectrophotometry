#include <Arduino.h>
#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLECharacteristic.h>
#include <BLE2902.h>
#include <BLE2904.h>
#include "callbacks.h"

#define DEVICE_NAME "SPECTRO"
#define TRIGGER_SIGNAL "1"

#define BATTERY_INFO_SERVICE_UUID 0x180f
#define BATTERY_INFO_CHARACTERISTIC_UUID 0x2a19
#define SPECTRUM_SERVICE_UUID "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define SPECTRUM_CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"
#define TRIGGER_SERVICE_UUID "62fb0057-e0ed-4a22-b906-7b2e848cdc0b"
#define TRIGGER_CHARACTERISTIC_UUID "1556197c-4e2c-4840-9736-6c452a0e041e"

#define START 25
#define CLOCK 26
#define VIDEO 34

#define LED 35
#define RELAY 33
#define BATTERY 32

#define PIXEL_NUMBER 288

uint16_t spectrum[PIXEL_NUMBER];

BLEServer *bleServer;

BLEService *batteryService;
BLEService *spectrumService;
BLEService *triggerService;

BLECharacteristic *batteryCharacteristic;
BLECharacteristic *spectrumCharacteristic;
BLECharacteristic *triggerCharacteristic;

bool deviceConnected = false;
bool triggered = false;

void initBLEServer();
void initBatteryLevelService();
void initSpectrumService();
void initTriggerService();
void initAdvertising();
void checkBatteryLevel();
int batteryPinValueToLevel(int pinValue);

int batteryLevel = 5;

void readSpectrometer(int accumulationCycles)
{
  int delayTime = 1;
  for (int j = 0; j < accumulationCycles; j++)
  {
    digitalWrite(CLOCK, LOW);
    delayMicroseconds(delayTime);
    digitalWrite(CLOCK, HIGH);
    delayMicroseconds(delayTime);
    digitalWrite(CLOCK, LOW);
    digitalWrite(START, HIGH);
    delayMicroseconds(delayTime);
    for (int i = 0; i < 15; i++)
    {
      digitalWrite(CLOCK, HIGH);
      delayMicroseconds(delayTime);
      digitalWrite(CLOCK, LOW);
      delayMicroseconds(delayTime);
    }
    digitalWrite(START, LOW);
    for (int i = 0; i < 85; i++)
    {
      digitalWrite(CLOCK, HIGH);
      delayMicroseconds(delayTime);
      digitalWrite(CLOCK, LOW);
      delayMicroseconds(delayTime);
    }
    digitalWrite(CLOCK, HIGH);
    delayMicroseconds(delayTime);
    digitalWrite(CLOCK, LOW);
    delayMicroseconds(delayTime);
    for (int i = 0; i < PIXEL_NUMBER; i++)
    {
      spectrum[i] = (spectrum[i] * (j) + analogRead(VIDEO) * 10) / (j + 1);
      digitalWrite(CLOCK, HIGH);
      delayMicroseconds(delayTime);
      digitalWrite(CLOCK, LOW);
      delayMicroseconds(delayTime);
    }
    digitalWrite(START, HIGH);
    for (int i = 0; i < 7; i++)
    {
      digitalWrite(CLOCK, HIGH);
      delayMicroseconds(delayTime);
      digitalWrite(CLOCK, LOW);
      delayMicroseconds(delayTime);
    }
    digitalWrite(CLOCK, HIGH);
    delayMicroseconds(delayTime);
    delay(10);
  }
}

void setup()
{
  Serial.begin(115200);

  initBLEServer();
  initBatteryLevelService();
  initSpectrumService();
  initTriggerService();
  initAdvertising();

  pinMode(LED, OUTPUT);
  pinMode(START, OUTPUT);
  pinMode(CLOCK, OUTPUT);
  pinMode(RELAY, OUTPUT);

  digitalWrite(LED, HIGH);

  digitalWrite(CLOCK, HIGH);
  digitalWrite(START, LOW);
  digitalWrite(RELAY, LOW);
}

void loop()
{
  if (triggered)
  {
    digitalWrite(RELAY, HIGH);
    readSpectrometer(15);
    digitalWrite(RELAY, LOW);

    triggered = false;
    Serial.println("Forwarding spectrum");
    for (int i = 0; i < PIXEL_NUMBER; i++)
    {
      spectrumCharacteristic->setValue(String(spectrum[i]).c_str());
      spectrumCharacteristic->notify();
    }
    memset(spectrum, 0, sizeof(spectrum));
    checkBatteryLevel();
  }
}

void initBLEServer()
{
  BLEDevice::init(DEVICE_NAME);
  bleServer = BLEDevice::createServer();
  bleServer->setCallbacks(new ServerCallbacks());
  Serial.println("BLE server started");
}

void initBatteryLevelService()
{
  batteryService = bleServer->createService((uint16_t)BATTERY_INFO_SERVICE_UUID);

  BLE2904 *batteryLevelDescriptor = new BLE2904();
  batteryLevelDescriptor->setFormat(BLE2904::FORMAT_UINT8);
  batteryLevelDescriptor->setNamespace(1);
  batteryLevelDescriptor->setUnit(0x27ad);

  batteryCharacteristic = batteryService->createCharacteristic((uint16_t)BATTERY_INFO_CHARACTERISTIC_UUID, BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_NOTIFY);
  batteryCharacteristic->addDescriptor(batteryLevelDescriptor);
  batteryCharacteristic->addDescriptor(new BLE2902());
  batteryCharacteristic->setCallbacks(new BatteryCharacteristicCallbacks());
  batteryCharacteristic->setValue(String(batteryLevel).c_str());
  batteryService->start();
  Serial.println("Battery service initialized");
}

void initSpectrumService()
{
  spectrumService = bleServer->createService(SPECTRUM_SERVICE_UUID);
  spectrumCharacteristic = spectrumService->createCharacteristic(SPECTRUM_CHARACTERISTIC_UUID, BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_NOTIFY);
  spectrumCharacteristic->setCallbacks(new SpectrumCharacteristicCallbacks());
  spectrumCharacteristic->addDescriptor(new BLE2902());
  spectrumService->start();
  Serial.println("Spectrum service initialized");
}

void initTriggerService()
{
  triggerService = bleServer->createService(TRIGGER_SERVICE_UUID);
  triggerCharacteristic = triggerService->createCharacteristic(TRIGGER_CHARACTERISTIC_UUID, BLECharacteristic::PROPERTY_WRITE);
  triggerCharacteristic->setCallbacks(new TriggerCharacteristicCallbacks());
  triggerService->start();
  Serial.println("Trigger service initialized");
}

void initAdvertising()
{
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SPECTRUM_SERVICE_UUID);
  pAdvertising->addServiceUUID((uint16_t)BATTERY_INFO_SERVICE_UUID);
  pAdvertising->addServiceUUID(TRIGGER_SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  BLEDevice::startAdvertising();
  Serial.println("Advertisement started");
}

void checkBatteryLevel()
{
  int batteryPinValue = analogRead(BATTERY);
  int newBatteryLevel = batteryPinValueToLevel(batteryPinValue);

  if (newBatteryLevel != batteryLevel)
  {
    batteryLevel = newBatteryLevel;
    batteryCharacteristic->setValue(String(batteryLevel).c_str());
    batteryCharacteristic->notify();
  }
};

// ADC resolution 12 bit
// 3300 (mV) / 4096 (step) = 0.8056 mV per step
// voltage divider divides by 2 = 1.6113 mV/step
int batteryPinValueToLevel(int value)
{
  double voltage = value * 1.6113;
  if (voltage < 0.5)
    return 0;
  else if (voltage < 1)
    return 1;
  else if (voltage < 1.5)
    return 2;
  else if (voltage < 2)
    return 3;
  else if (voltage < 2.5)
    return 4;
  else
    return 5;
};

void ServerCallbacks::onConnect(BLEServer *pServer)
{
  Serial.println("APP connected");
  deviceConnected = true;
};

void ServerCallbacks::onDisconnect(BLEServer *pServer)
{
  Serial.println("APP disconnected");
  deviceConnected = false;
};

void TriggerCharacteristicCallbacks::onWrite(BLECharacteristic *pCharacteristic)
{
  std::string value = pCharacteristic->getValue();
  Serial.print("TRIGGER characteristic READ by server, written value: ");
  Serial.println(value.c_str());

  if (value == TRIGGER_SIGNAL)
  {
    triggered = true;
    Serial.println("Data collection triggered");
  }
};