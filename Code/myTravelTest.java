import lejos.nxt.*;
import lejos.navigation.Pilot;

public class myTravelTest 
{
	public static void main(String[] args)
	{
		//units 
		Pilot pilot = new Pilot(2.25f, 2.75f, Motor.B, Motor.C);
		pilot.travel(50f);
	}
}