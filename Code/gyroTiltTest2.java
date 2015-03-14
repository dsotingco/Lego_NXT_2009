//Apr. 21, 2009
//test stuff for the gyroTilt class
//PLUG THE GYRO SENSOR INTO PORT #3

import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;

public class gyroTiltTest2
{
	public static void main(String[] args) throws InterruptedException
	{
		GyroTilt myGyro = new GyroTilt();
		System.out.println("gyroTiltTest2 is going.");
		Button.waitForPress();
			//~ while(true)
			//~ {
				//~ LCD.drawInt(myGyro.getGyroValue(),4,0,1);
				//~ LCD.drawInt(myGyro.getSensorRawValue(), 4, 0, 2);
				//~ LCD.drawInt(myGyro.getSensorValue(), 4, 0, 3);
				//~ //LCD.drawInt(myGyro.getTiltAngle(),4,0,4);
				//~ if (Button.ENTER.isPressed()) break;
			//~ }
		
		
	}
}