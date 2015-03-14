//Working as of Apr. 25, 2009.
import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;
import lejos.navigation.Pilot;
import lejos.nxt.Motor;
import java.lang.Math;

public class NXTWayPilot implements TimerListener
{
	public static int tiltAngle = 0; //degrees
	public static GyroSensor theGyro = new GyroSensor(SensorPort.S3);
	public static LightSensor light = new LightSensor(SensorPort.S2);
	public static int theDelay = 50;  //in milliseconds
	public static int t = 0; //initialize time
	public static Timer timer = new Timer(theDelay,new NXTWayPilot()); //this line needs to be changed if the name of this file changes (e.g. with saved copies)
	public static int[] speeds = new int[3];
	public static int[] angles = new int[3];
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static float motorInput = 0;
	public static float k = 200;
	public static int gain = 9; //gain of controller
	public static int Ki = 8;
	public static int Kd = 50;
	public static int lightCenter = 460; //this is the nominal light reading when tilt angle is zero
	public static int lightRange = 2; //this is the range around the light reading to accept as zero tilt angle
	public static double iAngle = 0;
	
	public static void main(String[] args) throws InterruptedException
	{
		LCD.setAutoRefresh(1);
		calibrateLight();
		Button.ENTER.waitForPress();
		timer.start();
		while(true)
		{
			LCD.drawInt(getTiltAngle()/1000,4,0,1);
			LCD.drawInt(getTime(),4,0,2);
			LCD.drawInt(theGyro.readValue(),4,0,3);
			LCD.drawInt(getGyroValue(),4,0,4);
			LCD.drawInt((int)getMotorInput(),4,0,5);
			LCD.drawInt(getNewSpeed(speeds,angles,gain),4,0,6);
			LCD.drawInt(pilot.getLeftActualSpeed(),4,0,7);
			LCD.drawInt(pilot.getRightActualSpeed(),4,0,8);
			
			if (getMotorInput()>0)
			{
				pilot.forward();
			}
			else if(getMotorInput()<0)
			{
				pilot.backward();
			}
			else
			{
				//do nothing
			}
			
			if (Button.ESCAPE.isPressed()) 
			{
				timer.stop();
				break;
			}
		}
	}
	
	public void timedOut()
	{
		pilot.stop();
		//Sound.beep();
		int newAngle = 0;
		setTime(t + theDelay);
		int f = getGyroValue(); 
		int oneTimestep = theDelay;
		int twoTimesteps = 2*theDelay;
		int threeTimesteps = 3*theDelay;
		if (getTime() == oneTimestep)
		{
			speeds[0] = f;
			angles[0] = 0; //not enough data to integrate yet
		}
		else if(getTime() == twoTimesteps)
		{
			speeds[1] = f;
			angles[1] = 0; //not enough data to integrate yet
		}
		else if(getTime() == threeTimesteps)
		{
			speeds[2] = f;
			newAngle = RK4(tiltAngle,theDelay, speeds);
			angles[2] = newAngle;
		}
		else
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
				newAngle = RK4(tiltAngle, theDelay, speeds);
			}
			angles[0] = angles[1];
			angles[1] = angles[2];
			angles[2] = newAngle;
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
		//setMotorInput(getK()*getTiltAngle()/1000);
		//pilot.travel(getMotorInput());
		setMotorInput(getNewSpeed(speeds,angles,gain));
		pilot.setSpeed((int)Math.abs(motorInput)); //remember that this doesn't make the motor go; it just sets the default speed
		//LCD.drawInt(getTiltAngle(),4,0,1);
		//LCD.refresh();
	}
	
	public int getDelay()
	{
		return theDelay;
	}
	
	public static int getGyroValue()
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
		//return theSpeed - 1;
	}
	
	public static int getTime()
	{
		return t;
	}
	
	public static int getTiltAngle()
	{
		return tiltAngle;
	}
	
	public static float getK()
	{
		return k;
	}
	
	public static float getMotorInput()
	{
		return motorInput;
	}
	
	public static void setTime(int newTime)
	{
		t = newTime;
	}
	
	public void setTiltAngle(int theAngle)
	{
		tiltAngle = theAngle;
	}
	
	public void setK(float newK)
	{
		k = newK;
	}
	
	public void setMotorInput(float newInput)
	{
		motorInput = newInput;
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
	
	//oldSpeeds should have speed(k-1), speed(k), and speed(k+1) in that order
	//tiltAngles should have angle(k-1), angle(k), and angle(k+1) in that order
	public static int getNewSpeed(int[] oldSpeeds, int[] tiltAngles, int K)
	{
		//for  theDelay = 10ms:
		//double newSpeed = 2.051*oldSpeeds[2] - 1.051*oldSpeeds[1] + K*0.005179*tiltAngles[2] + K*0.0001026*tiltAngles[1] - K*0.005077*tiltAngles[0];
		
		//for the Delay = 50ms:
		//double newSpeed = 2.285*oldSpeeds[2] - 1.285*oldSpeeds[1] + K*0.02999*tiltAngles[2] + K*0.002856*tiltAngles[1] - K*0.02714*tiltAngles[0];
		
		//for theDelay = 100ms:
		//double newSpeed = 2.666*oldSpeeds[2] - 1.665*oldSpeeds[1] + K*0.0733*tiltAngles[2] + K*0.01333*tiltAngles[1] - K*0.05997*tiltAngles[0];
		
		//for 50ms and more phase margin:
		//double newSpeed = 2.237*oldSpeeds[2] - 1.223*oldSpeeds[1] + K*0.02927*tiltAngles[2] + K*0.002787*tiltAngles[1] - K*0.02648*tiltAngles[0];
		
		//50ms and lead-lag:
		//double newSpeed = oldSpeeds[2] + 2.051*tiltAngles[2] - 4*tiltAngles[1] + 1.95*tiltAngles[0];
		
		//100ms and lead-lag:
		//double newSpeed = oldSpeeds[2] + 211.2*tiltAngles[2] - 399.9*tiltAngles[1] + 189*tiltAngles[0];
		
		//just proportional control!
		//double newSpeed = K*tiltAngles[2];
		
		//PD control
		//double newSpeed = oldSpeeds[2] + 41*K*tiltAngles[2] - 39*K*tiltAngles[1];
		
		//K*s^2 / (s^2 - 1) at 50ms sampling time
		//double newSpeed = 2.003*oldSpeeds[2] - 1*oldSpeeds[1] + K*1.001*tiltAngles[2] - K*2.001*tiltAngles[1] + K*1.001*tiltAngles[0];
		
		//PID control:
		iAngle = iAngle + tiltAngle;
		double newSpeed = K/10*tiltAngle + Ki/10*(iAngle)+ Kd*speeds[2];
		return (int)newSpeed;
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