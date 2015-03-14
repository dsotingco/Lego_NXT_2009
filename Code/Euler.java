import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class Euler implements TimerListener
{
	public static int theta = 0;
	public static GyroSensor gyro = new GyroSensor(SensorPort.S3);
	public static int delay = 10; //ms
	public static int t =0;
	public static Timer timer = new Timer(delay, new Euler());
	
	public static void main(String[] args)
	{
		LCD.setAutoRefresh(1);
		timer.start();
		while(true)
		{
			LCD.drawInt(theta/1000,4,0,1);
			if(Button.ESCAPE.isPressed())
			{
				break;
			}
		}
	}
	
	public void timedOut()
	{
		theta = theta + (gyro.readValue() - 2)*delay;
	}
}