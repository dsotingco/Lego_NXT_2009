import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.navigation.Pilot;

public class NXTLite implements TimerListener
{
	public static int theta = 0;
	public static GyroSensor gyro = new GyroSensor(SensorPort.S3);
	public static LightSensor light = new LightSensor(SensorPort.S2);
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static int delay = 1000; //ms
	public static int oneTimestep = delay;
	public static int twoTimesteps = 2*delay;
	public static int threeTimesteps = 3*delay;
	public static int t =0;
	public static Timer timer = new Timer(delay, new NXTLite());	
	public static float motorInput = 0;
	public static int K = 50;
	public static int lightCenter = 460; //this is the nominal light reading when tilt angle is zero
	public static int lightRange = 2; //this is the range around the light reading to accept as zero tilt angle
	public static int[] speeds = new int[3];
	public static int[] angles = new int[3];
	public static int newAngle = 0;
	
	public static void main(String[] args)
	{
		LCD.setAutoRefresh(1);
		calibrateLight();
		Button.ENTER.waitForPress();
		timer.start();
		while(true)
		{
			LCD.drawInt(theta/1000,4,0,1);
			if (pilot.getSpeed()>0)
			{
				pilot.forward();
			}
			else if(pilot.getSpeed()<0)
			{
				pilot.backward();
			}
			if(Button.ESCAPE.isPressed())
			{
				timer.stop();
				break;
			}
		}
	}
	
	public void timedOut()
	{
		pilot.stop();
		int f = gyro.readValue(); 
		if(t>threeTimesteps)
		{
			speeds[0] = speeds [1];
			speeds[1] = speeds[2];
			speeds[2] = f;
			if((lightCenter - lightRange/2<=light.readNormalizedValue()) && (light.readNormalizedValue()<=lightCenter + lightRange/2))
			{
				newAngle = 0; //gyro drift compensation using the light sensor
			}
			else
			{
				newAngle = RK4(theta, delay, speeds);
			}
			angles[0] = angles[1];
			angles[1] = angles[2];
			angles[2] = newAngle;
		}
		else if (t == oneTimestep)
		{
			speeds[0] = f;
			angles[0] = 0; //not enough data to integrate yet
		}
		else if(t == twoTimesteps)
		{
			speeds[1] = f;
			angles[1] = 0; //not enough data to integrate yet
		}
		else if(t == threeTimesteps)
		{
			speeds[2] = f;
			angles[2] = RK4(theta,delay, speeds);
		}
		pilot.setSpeed(controlSignal(theta));
		t = t + delay;
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
	
	public static int controlSignal(int angle)
	{
		return K*angle;
	}
	
	//Find lightCenter
	//Let the user hit the orange button to say that it's OK.  
	public static void calibrateLight()
	{
		while(true)
		{
			LCD.drawInt(light.readNormalizedValue(),4,0,1);
			if(Button.ENTER.isPressed())
			{
				lightCenter = light.readNormalizedValue();
				break;
			}
			if(Button.ESCAPE.isPressed())
			{
				break;
			}
		}
	}
}