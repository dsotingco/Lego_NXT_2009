import lejos.nxt.*;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.navigation.Pilot;
import lejos.nxt.Motor;
import lejos.nxt.Sound;

public class MotorChar implements TimerListener
{
	public static int theDelay = 50; //in milliseconds
	public static int inputSpeed = 150; //in degrees/s
	public static Timer timer = new Timer(theDelay, new MotorChar());
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static int[] speeds = new int[10]; 
	public static int index = 0;
	
	public static void main(String[] args) throws InterruptedException
	{
		LCD.setAutoRefresh(1);
		pilot.setSpeed(inputSpeed);
		timer.start();
		pilot.forward();
		Button.waitForPress();
		for (int index = 0; index<10; index++)
		{
			LCD.drawInt(speeds[index],4,0,index+1);
		}
		pilot.stop();
		Button.waitForPress();
	}
	
	public void timedOut()
	{
		//Sound.beep();
		if(index<10)
		{
			speeds[index] = pilot.getLeftActualSpeed();
			//LCD.drawInt(pilot.getRightActualSpeed(),4,0,index);
			index++;
		}
		else
		{
			//do nothing
		}
	}
	
}