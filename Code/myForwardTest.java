import lejos.nxt.*;
import lejos.navigation.Pilot;
import lejos.nxt.LCD;

public class myForwardTest 
{
	public static void main(String[] args)
	{
		//units 
		Pilot pilot = new Pilot(2.25f, 2.75f, Motor.A, Motor.B);
		pilot.forward();
		LCD.setAutoRefresh(1);
		pilot.setSpeed(500);
		while(true)
		{
			LCD.drawInt(pilot.getLeftActualSpeed(),4,0,1);
			LCD.drawInt(pilot.getRightActualSpeed(),4,0,2);
			if (Button.ENTER.isPressed()) break;
		}
	}
}