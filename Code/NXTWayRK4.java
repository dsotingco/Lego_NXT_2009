//Working as of Apr. 25, 2009.
import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;
import java.lang.Math;

public class NXTWayRK4 implements TimerListener
{
	public static int tiltAngle = 0; //degrees
	public static GyroSensor theGyro = new GyroSensor(SensorPort.S3);
	public static LightSensor light = new LightSensor(SensorPort.S2);
	public static int theDelay = 30;  //in milliseconds
	public static int t = 0; //initialize time
	public static Timer timer = new Timer(theDelay,new NXTWayRK4());
	public static int[] speeds = new int[3];
	
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
		int newAngle = 0;
		setTime(t + theDelay);
		int f = getGyroValue(); //
		int oneTimestep = theDelay;
		int twoTimesteps = 2*theDelay;
		int threeTimesteps = 3*theDelay;
		if (getTime() == oneTimestep)
		{
			speeds[0] = f;
		}
		else if(getTime() == twoTimesteps)
		{
			speeds[1] = f;
		}
		else if(getTime() == threeTimesteps)
		{
			speeds[2] = f;
			newAngle = RK4(tiltAngle,theDelay, speeds);
		}
		else
		{
			speeds[0] = speeds [1];
			speeds[1] = speeds[2];
			speeds[2] = f;
			if((460<=light.readNormalizedValue()) && (light.readNormalizedValue()<=465))
			{
				newAngle = 0; //gyro drift compensation using the light sensor
			}
			else
			{
				newAngle = RK4(tiltAngle, theDelay, speeds);
			}
		}
		/*switch(getTime())
		{
			case oneTimestep:
				speeds[0] = f;
				break;
			case twoTimesteps:
				speeds[1] = f;
				break;
			case threeTimesteps:
				speeds[2] = f;
				int newAngle = RK4(tiltAngle, theDelay, speeds);
				break;
			default:
				speeds[0] = speeds [1];
				speeds[1] = speeds[2];
				speeds[2] = f;
				int newAngle = RK4(tiltAngle, theDelay, speeds);
				break;
		}*/
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
			return theSpeed - 2;
		}
		//return theSpeed - 2;
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
	public int RK4(int angle, int timestep, int[] speeds)
	{
		int k1 = speeds[0];
		int k2 = speeds[1];
		int k3 = speeds[1];
		int k4 = speeds[2];
		int newAngle = angle + (timestep/6)*(k1 + 2*k2 + 2*k3 + k4);
		return newAngle;
	}
}