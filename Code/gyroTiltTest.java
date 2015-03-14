//Apr. 21, 2009
//test stuff for the gyroTilt class
//PLUG THE GYRO SENSOR INTO PORT #3

import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;

public class gyroTiltTest
{
	public static void main(String[] args) throws Exception
	{
		GyroTilt myGyro = new GyroTilt();
		if(Button.ENTER.isPressed())
		{
			//myGyro.goGyro();
			while(true)
			{
				LCD.drawInt(myGyro.getGyroValue(),4,0,1);
				LCD.drawInt(myGyro.getSensorRawValue(), 4, 0, 2);
				LCD.drawInt(myGyro.getSensorValue(), 4, 0, 3);
				//LCD.drawInt(myGyro.getTiltAngle(),4,0,4);
				if (Button.ENTER.isPressed()) break;
			}
		}
		else
		{
			LCD.drawInt(myGyro.getGyroValue(),4,0,1);
			//Do nothing.  The user is still trying to hold the robot still.  
		}
	}
}