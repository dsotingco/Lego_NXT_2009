import lejos.nxt.*;
import lejos.nxt.addon.GyroSensor;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;

public class Offset implements TimerListener
{
	public static GyroSensor gyro = new GyroSensor(SensorPort.S3);
	public static int delay = 10;
	public static int t = 0;
	public static int endTime = 60000;
	public static Timer timer = new Timer(delay, new Offset());
	public static int total = 0;
	public static int num = 0;
	
	public static void main(String[] args) throws InterruptedException
	{
		LCD.setAutoRefresh(1);
		timer.start();
		while(t<=endTime)
		{
			if(t%5000==0)
			{
				LCD.drawInt(total,4,0,1);
				LCD.drawInt(num,4,0,2);
				LCD.drawInt(t,4,0,3);
			}
		}
		timer.stop();
		Sound.beep();
		LCD.drawInt(total,4,0,1);
		LCD.drawInt(num,4,0,2);
		LCD.drawInt(t,4,0,3);
		Button.ENTER.waitForPress();
	}
	
	public void timedOut()
	{
		total = total + gyro.readValue();
		num++;
		t+=delay;
	}
	
}