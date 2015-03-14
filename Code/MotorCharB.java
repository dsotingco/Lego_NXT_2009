import lejos.nxt.*;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.navigation.Pilot;
import lejos.nxt.Motor;
import lejos.nxt.Sound;

public class MotorCharB implements TimerListener
{
	public static int theDelay = 50; //in milliseconds
	public static int inputSpeed = 500; //in degrees/s
	public static Timer timer = new Timer(theDelay, new MotorCharB());
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static int[] speeds = new int[5]; 
	public static int index = 0;
	public static int tempSpeed = -1;
	public static int time = 0; //in milliseconds
	public static int endTime = 0; //in milliseconds
	
	public static void main(String[] args) throws InterruptedException
	{
		LCD.setAutoRefresh(1);
		timer.start();
		pilot.setSpeed(inputSpeed);
		pilot.forward();
		Button.waitForPress();
		LCD.drawInt(endTime,4,0,1);
		LCD.drawInt(inputSpeed,4,0,2);
		LCD.drawInt(inputSpeed*64/100,4,0,3);
		Button.waitForPress();
	}
	
	public void timedOut()
	{
		time = time + theDelay;
		if(pilot.getLeftActualSpeed() >= 0.64*inputSpeed)
		{
			endTime = time;
			timer.stop();
		}
		else
		{
			//do nothing and keep going
		}
	}
	
}