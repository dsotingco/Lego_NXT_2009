import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;
import lejos.navigation.Pilot;

public class FilterInt implements TimerListener
{
	public static int theta = 0;
	public static GyroSensor gyro = new GyroSensor(SensorPort.S3);
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static int delay = 50; //ms
	public static int t =0;
	public static Timer timer = new Timer(delay, new FilterInt());
	public static double[] coefficients = new double[5];
	public static int[] speeds = new int[3];
	public static int[] angles = new int[3];
	public static int oneTimestep = delay;
	public static int twoTimesteps = 2*delay;
	public static int threeTimesteps = 3*delay;
	public static int newAngle = 0;
	public static int K = 150;
	public static int theSpeed;
	
	public static void main(String[] args)
	{
		//populate coefficients
		coefficients[0] = 1.6;
		coefficients[1] = -1.6;
		coefficients[2] = 1;
		coefficients[3] = 1.2;
		coefficients[4] = 0.36;
		LCD.setAutoRefresh(1);
		timer.start();
		while(true)
		{
			LCD.drawInt(theta/1000,4,0,1);
			LCD.drawInt(t,4,0,2);
			LCD.drawInt(gyro.readValue(),4,0,3);
			theSpeed = pilot.getSpeed();
			if (theSpeed>0)
			{
				pilot.forward();
			}
			else if(theSpeed<0)
			{
				pilot.backward();
			}
			else
			{
				//do nothing
			}
			if(Button.ESCAPE.isPressed())
			{
				break;
			}
		}
	}
	
	public void timedOut()
	{
		int f = gyro.readValue();
		if(t>threeTimesteps)
		{
			speeds[0] = speeds [1];
			speeds[1] = speeds[2];
			speeds[2] = f;
			angles[0] = angles[1];
			angles[1] = angles[2];
			newAngle= integrate(coefficients, speeds, angles);
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
			newAngle= integrate(coefficients, speeds, angles);
			angles[2] = newAngle;
		}
		theta = newAngle;
		pilot.setSpeed(getNewSpeed(speeds,angles,K));
		t = t + delay;
	}
	
	//coeff has five items, indexed 0 through 4
	//speeds has three items, indexed 0 through 2
	//angles has three items, indexed 0 through 2
	public static int integrate(double[] coeff, int[] speeds, int[] angles)
	{
		double A = coeff[0];
		double B = coeff[1];
		double C = coeff[2];
		double D = coeff[3];
		double E = coeff[4];
		double theta = (D/C)*angles[2] - (E/C)*angles[0] + (A/C)*speeds[2] - (B/C)*speeds[0];
		return (int)theta;
	}
	
	//oldSpeeds should have speed(k-1), speed(k), and speed(k+1) in that order
	//tiltAngles should have angle(k-1), angle(k), and angle(k+1) in that order
	public static int getNewSpeed(int[] speeds, int[] angles, int K)
	{
		//for  theDelay = 10ms:
		//double newSpeed = 2.051*speeds[2] - 1.051*speeds[1] + K*0.005179*angles[2] + K*0.0001026*angles[1] - K*0.005077*angles[0];
		
		//for the Delay = 50ms: this one worked sorta well
		//double newSpeed = 2.285*speeds[2] - 1.285*speeds[1] + K*0.02999*angles[2] + K*0.002856*angles[1] - K*0.02714*angles[0];
		
		double newSpeed = 2.285*speeds[2] - 1.285*speeds[1] + K*0.02999*angles[2] + K*0.006*angles[1] - K*0.04*angles[0];
				
		//for theDelay = 100ms:
		//double newSpeed = 2.666*speeds[2] - 1.665*speeds[1] + K*0.0733*angles[2] + K*0.01333*angles[1] - K*0.05997*angles[0];
		
		//for 50ms and more phase margin:
		//double newSpeed = 2.237*oldSpeeds[2] - 1.223*oldSpeeds[1] + K*0.02927*tiltAngles[2] + K*0.002787*tiltAngles[1] - K*0.02648*tiltAngles[0];
		
		//50ms and lead-lag:
		//double newSpeed = oldSpeeds[2] + 2.051*tiltAngles[2] - 4*tiltAngles[1] + 1.95*tiltAngles[0];
		
		//100ms and lead-lag:
		//double newSpeed = oldSpeeds[2] + 211.2*tiltAngles[2] - 399.9*tiltAngles[1] + 189*tiltAngles[0];
		
		//just proportional control!
		//double newSpeed = K*tiltAngles[2];
		
		//PD control
		//double newSpeed = speeds[2] + K*angles[2] - K*angles[1];
		
		//K*s^2 / (s^2 - 1) at 50ms sampling time
		//double newSpeed = 2.003*oldSpeeds[2] - 1*oldSpeeds[1] + K*1.001*tiltAngles[2] - K*2.001*tiltAngles[1] + K*1.001*tiltAngles[0];
		return (int)newSpeed;
	}
}