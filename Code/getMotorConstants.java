import lejos.nxt.*;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.navigation.Pilot;
import lejos.nxt.Motor;

public class getMotorConstants
{
	public static int theDelay = 10; //in milliseconds
	public static int inputSpeed = 150; //in degrees/s
	//public static Timer timer = new Timer(theDelay, new getMotorConstants());
	public static Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B,Motor.A);
	public static int[] speeds = new int[5]; 
	
	public static void main(String[] args) throws InterruptedException
	{
		pilot.setSpeed(inputSpeed);
		pilot.forward();
		for (int index = 0; index<5; index++)
		{
			speeds[index] = pilot.getLeftActualSpeed(); 
		}
		for (int index = 0; index<5; index++)
		{
			LCD.drawInt(speeds[index],4,0,index+1);
		}
	}
	
}