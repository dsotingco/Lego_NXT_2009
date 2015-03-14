import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.navigation.Pilot;


public class NXTEuler implements TimerListener
{
	public static int theta = 0;
	public static GyroSensor gyro = new GyroSensor(SensorPort.S3);
	public static LightSensor light = new LightSensor(SensorPort.S2);
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static int delay = 50; //ms
	public static int t =0;
	public static Timer timer = new Timer(delay, new NXTEuler());	
	public static float motorInput = 0;
	public static int K = 50;
	public static int lightCenter = 460; //this is the nominal light reading when tilt angle is zero
	public static int lightRange = 2; //this is the range around the light reading to accept as zero tilt angle
	
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
		if (Math.abs(gyro.readValue())>=2)
		{
			theta = theta + (gyro.readValue() - 2)*delay;
		}
		else if((lightCenter - lightRange/2<=light.readNormalizedValue()) && (light.readNormalizedValue()<=lightCenter + lightRange/2))
		{
			theta = 0; //gyro drift compensation using the light sensor
		}
		else
		{
			//do nothing; theta has not changed
		}
		pilot.setSpeed(controlSignal(theta));
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