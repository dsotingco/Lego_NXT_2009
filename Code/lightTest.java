import lejos.nxt.*;
import lejos.nxt.LightSensor;
import lejos.nxt.LCD;

public class lightTest
{
	public static void main(String[] args) throws Exception
	{
		LightSensor light = new LightSensor(SensorPort.S2);
		
		while(true)
		{
			LCD.drawInt(light.readNormalizedValue(),4,0,1);
			LCD.drawInt(light.readValue(),4,0,2);
			if (Button.ESCAPE.isPressed()) break;
		}
	}
}