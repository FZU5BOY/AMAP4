package com.example.amap;

interface IGetLocationService 
{
double getLongitude();
double getLatitude();
double getDistance();
double getAverageSpeed();
int countSteps();
double getDegree();
void startDeadReckoningService();
boolean getGPSStatus();
boolean getSensorStatus();
void changeGPSStatus();
void changeSensorStatus();
void unRegisterSensors();
void unRegisterGPS();

	
}