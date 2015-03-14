import lejos.nxt.*;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.LCD;

public class ultraTest
{
	public static void main(String[] args) throws Exception
	{
		UltrasonicSensor ultra = new UltrasonicSensor(SensorPort.S2);
		
		while(true)
		{
			LCD.drawInt(ultra.getDistance(),4,0,1);
			LCD.drawInt(SensorPort.S2.readRawValue(), 4, 0, 2);
			LCD.drawInt(SensorPort.S2.readValue(), 4, 0, 3);
			if (Button.ESCAPE.isPressed()) break;
		}
	}
}