//Angle measurement is sorta working as of 04/23/2009.  We are seeing some integration error.  
import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;
import java.lang.Math;

public class NXTWayRK4b implements TimerListener
{
	public static int tiltAngle = 0; //degrees
	public static GyroSensor theGyro = new GyroSensor(SensorPort.S3);
	public static int theDelay = 5;  //in milliseconds
	public static int t = 0; //initialize time
	public static Timer timer = new Timer(theDelay,new NXTWay());
	
	public static void main(String[] args) throws InterruptedException
	{
		LCD.setAutoRefresh(1);
		timer.start();
		while(true)
		{
			LCD.drawInt(getTiltAngle()/1000,4,0,1);
			LCD.drawInt(getTime(),4,0,2);
			LCD.drawInt(theGyro.readValue(),4,0,3);
			if (Button.ESCAPE.isPressed()) 
			{
				timer.stop();
				break;
			}
		}
	}
	
	public void timedOut()
	{
		//Sound.beep();
		setTime(t + theDelay);
		int f = getGyroValue(); //The -2 is there because the sensor seems to think +1 is zero.  
		int newAngle = euler(tiltAngle, theDelay, f);
		setTiltAngle(newAngle);
		//LCD.drawInt(getTiltAngle(),4,0,1);
		//LCD.refresh();
	}
	
	public int getDelay()
	{
		return theDelay;
	}
	
	public int getGyroValue()
	{
		int theSpeed = theGyro.readValue();
		if (Math.abs(theSpeed) <= 2)
		{
			return 0;
		}
		else
		{
			return theSpeed;
		}
	}
	
	public static int getTime()
	{
		return t;
	}
	
	public static int getTiltAngle()
	{
		return tiltAngle;
	}
	
	public static void setTime(int newTime)
	{
		t = newTime;
	}
	
	public void setTiltAngle(int theAngle)
	{
		tiltAngle = theAngle;
	}
	
	//Enter timestep in milliseconds.
	public int euler(int angle, int timestep, int speed)
	{
		return angle + speed*timestep;
	}	
	
	//Enter timestep in milliseconds.
	/*public int RK4(int angle, int timestep, int speed)
	{
		k1 = speed;
		
	}*/
}