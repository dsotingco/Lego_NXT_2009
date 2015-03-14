//Apr. 21, 2009
//class that makes the gyro sensor return a tilt angle
//PLUG THE GYRO SENSOR INTO PORT #3

import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;

public class GyroTilt implements TimerListener
{
	public int tiltAngle = 0; //degrees
	public GyroSensor theGyro = new GyroSensor(SensorPort.S3);
	public int theDelay = 1500;  //in milliseconds
	public int t = 0; //initialize time
	public Timer myTimer = new Timer(theDelay,new GyroTilt());
	
	public static void main(String[] args) throws InterruptedException
	{
		int delay = 100;
		//It appears that the timer does not have to be started for the thing to be crapping out.  The bot goes crazy upon instantiating the timer.  
		//Timer theTimer = new Timer(delay,new GyroTilt());
		//theTimer.start();
		System.out.println("GyroTilt is going.");
		Button.waitForPress();
	}
	
	public int getDelay()
	{
		return theDelay;
	}
	
	public int getGyroValue()
	{
		return theGyro.readValue();
	}
	
	//probably won't use this, but it's here just in case
	public int getSensorRawValue()
	{
		return SensorPort.S3.readRawValue();
	}
	
	//probably won't use this, but it's here just in case
	public int getSensorValue()
	{
		return SensorPort.S3.readValue();
	}
	
	public int getTiltAngle()
	{
		return tiltAngle;
	}
	
	public void setTiltAngle(int theAngle)
	{
		tiltAngle = theAngle;
	}
	
	public int euler(int angle, int timestep, int speed)
	{
		return angle + speed*timestep;
	}	
	
	public void timedOut()
	{
		Sound.beep();
		//update the tiltAngle continuously
		t+=theDelay;
		int f = getGyroValue();
		int newAngle = euler(tiltAngle, theDelay, f);
		setTiltAngle(newAngle);
	}
	
	//The method below starts the timer going, which starts the angle measurement going.  The bot should be held still at this point.  
	public void goGyro()
	{
		myTimer.start();
	}
	
}